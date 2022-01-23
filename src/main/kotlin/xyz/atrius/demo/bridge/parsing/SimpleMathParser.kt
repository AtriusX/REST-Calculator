package xyz.atrius.demo.bridge.parsing

import arrow.core.Either
import xyz.atrius.demo.data.error.ParseError
import xyz.atrius.demo.math.Constant
import xyz.atrius.demo.math.Node
import xyz.atrius.demo.math.op.*

private typealias OperatorFunction =
    (left: Node, right: Node) -> BinaryOperator

/**
 * @author Atrius
 *
 * SimpleMathParser is a very basic parser that is meant to only handle parsing
 * flat expressions with no subexpressions (parenthesis groups). This allows for
 * us to focus on the parsing of an expression's order of operations.
 */
class SimpleMathParser : Parser {

    // TODO: Move this to a dedicated operator repository
    private val ops: Set<Char> =
        setOf('+', '-', '*', '/', '^')

    /**
     * Parses a simple expression into a [Node] tree. This structure
     * does not work with parentheses, and is reserved for all other
     * operations. This means the primary focus is on preserving the
     * order of operations.
     *
     * @param input The input expression to parse.
     * @return      The parsed node object, or a [ParseError].
     */
    override fun parse(input: String): Either<ParseError, Node> {
        // Internal function to add precedence variable, since we
        // do not have the ability to add a parameter to an
        // overridden function.
        fun build(
            value: String = input,
            precedence: Int = 0
        ): Either<ParseError, Node> {
            // Make sure the expression is not empty
            if (value.isEmpty())
                return Either.Left(ParseError.EmptyExpression)
            // Make sure the expression isn't bordered by operators aside from
            val trimmed = value.trim()
            if (trimmed.firstOrNull()?.let { it in ops && it != '-' } == true || trimmed.lastOrNull() in ops)
                return Either.Left(ParseError.MalformedExpression)
            // Make sure the expression does not contain any invalid characters
            if (value.any { it !in ops && it != '.' && it !in '0' .. '9' && it != ' ' })
                return Either.Left(ParseError.InvalidOperation)
            // If the input is a number then we return it as a constant
            value.toDoubleOrNull()?.let {
                return Either.Right(Constant(it))
            }
            // Find the rightmost instance of the next operator with the same precedence
            var index: Int = -1
            for (i in value.lastIndex downTo 0)
                if (getPrecedence(value[i]) == precedence && !value.skipOperator(i)) {
                    index = i
                    break
                }
            // If the index is found
            if (index != -1) {
                // The current operator
                val op    = value[index]
                // Anything to the sides of our operator
                val left  = value.substring(0, index)
                val right = value.substring(index + 1)
                // Get the constructor for this operator or throw an error if invalid
                val create = getOperator(op)
                // Build the left and right nodes, or raise an error if a failure occurs
                val l = build(left, precedence).orNull()
                val r = build(right, precedence).orNull()
                if (l == null || r == null)
                    return Either.Left(ParseError.MalformedExpression)
                // Return the operator
                return Either.Right(create(l, r))
            }
            println(value)
            return build(value, precedence + 1)
        }
        return build()
    }

    private fun String.skipOperator(index: Int): Boolean {
        // In certain cases we will need to skip an operator while evaluating
        // our expressions. The only general case of this currently is the '-'
        // operator, which can also double as a negative constant operator.
        return this[index] == '-'
           // Make sure our '-' does not border the edge of our expression
           && length > index + 1
           // Make sure the next character over is numeric
           && this[index + 1] in '0' .. '9'
           // Make sure there are no other symbols before our given symbol,
           // or there is only a single operator before it.
           && lastBefore(index).let { it == null || it in ops }
    }

    private fun String.lastBefore(index: Int): Char? {
        // If we have an excessive amount of whitespace, we will want to make sure
        // we can find the last non-whitespace character before a given location.
        // Working backwards from our current position, we test our string for a
        // value meeting these criteria
        var temp = index - 1
        while (temp >= 0) {
            // If the current position corresponds to a non-whitespace symbol, we
            // can return it back to the user
            if (!this[temp].isWhitespace())
                return this[temp]
            temp--
        }
        // If no symbol is found, we simply return null
        return null
    }

    // Maps an operator to a given precedence level
    // TODO: Have this be managed by a dedicated repository later
    private fun getPrecedence(op: Char): Int = when (op) {
        '+', '-' -> 0
        '*', '/' -> 1
        '^'      -> 2
        else     -> -1
    }

    // Maps an operation to a constructor function
    private fun getOperator(
        op: Char
    ): OperatorFunction = when(op) {
        '-'  -> { l, r -> Sub(l, r) }
        '*'  -> { l, r -> Mul(l, r) }
        '/'  -> { l, r -> Div(l, r) }
        '^'  -> { l, r -> Exp(l, r) }
        else -> { l, r -> Add(l, r) }
    }
}