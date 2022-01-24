package xyz.atrius.demo.bridge.parsing

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import xyz.atrius.demo.data.OperationManager
import xyz.atrius.demo.data.error.ParseError
import xyz.atrius.demo.data.error.ParseError.*
import xyz.atrius.demo.math.Constant
import xyz.atrius.demo.math.Node

/**
 * @author Atrius
 *
 * SimpleMathParser is a very basic parser that is meant to only handle parsing
 * flat expressions with no subexpressions (parenthesis groups). This allows for
 * us to focus on the parsing of an expression's order of operations.
 */
class SimpleMathParser : Parser {

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
            value: String = input.replace(" ", ""),
            precedence: Int = 0
        ): Either<ParseError, Node> {
            // Make sure the expression is not empty
            if (value.isEmpty())
                return Left(EmptyExpression)
            // Make sure the expression isn't bordered by operators aside from
            val trimmed = value.trim()
            if (trimmed.firstOrNull()?.let { it in OperationManager && it != '-' } == true
                || trimmed.lastOrNull() in OperationManager
            ) return Left(MalformedExpression)
            // Make sure the expression does not contain any invalid characters
            if (value.any {
                    it !in OperationManager
                    && it != '.'
                    && it !in '0' .. '9'
                    && it != ' '
            }) return Left(InvalidOperation)
            // If the input is a number then we return it as a constant
            value.toDoubleOrNull()?.let {
                return Right(Constant(it))
            }
            // Find the rightmost instance of the next operator with the same precedence
            var index: Int = -1
            for (i in value.lastIndex downTo 0)
                if (OperationManager.getPrecedence(value[i]) == precedence && !value.skipOperator(i)) {
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
                val create = OperationManager[op] ?:
                    return Left(InvalidOperation)
                // Build the left and right nodes, or raise an error if a failure occurs
                val l = build(left, precedence).orNull()
                val r = build(right, precedence).orNull()
                if (l == null || r == null)
                    return Left(MalformedExpression)
                // Return the operator
                return Right(create(l, r))
            }
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
           && lastBefore(index).let { it == null || it in OperationManager }
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
}