package xyz.atrius.demo.bridge.parsing

import arrow.core.Either
import arrow.core.getOrHandle
import arrow.core.orNull
import xyz.atrius.demo.bridge.validation.NumberValidator
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
            // Make sure the expression isn't bordered by operators
            if (value.first() in ops || value.last() in ops)
                return Either.Left(ParseError.MalformedExpression)
            // Make sure the expression does not contain any invalid characters
            if (value.any { it !in ops && it != '.' && it !in '0' .. '9' && it != ' ' })
                return Either.Left(ParseError.InvalidOperation)
            // If the input is a number then we return it as a constant
            value.toDoubleOrNull()?.let {
                return Either.Right(Constant(it))
            }
            // Find the rightmost instance of the next operator with the same precedence
            val index = value.indexOfLast { getPrecedence(it) == precedence }
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
                val l = build(left, precedence).orNull()!!
                val r = build(right, precedence).orNull()!!
                // Return the operator
                return Either.Right(create(l, r))
            }
            return build(value, precedence + 1)
        }
        return build()
    }

    private fun getPrecedence(op: Char): Int = when (op) {
        '+', '-' -> 0
        '*', '/' -> 1
        '^'      -> 2
        else     -> -1
    }

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