package xyz.atrius.demo.bridge.parsing

import arrow.core.Either
import arrow.core.getOrHandle
import xyz.atrius.demo.data.error.ParseError
import xyz.atrius.demo.math.Node

/**
 * @author Atrius
 *
 * Unlike our [SimpleMathParser], our expression parser is not concerned with the
 * order of operations. The primary purpose of this [Parser] implementation is to
 * parse our subexpressions from expressions and flatten our evaluation to a single
 * expression. Once we flatten out our values, we can rely on our math parser to
 * handle the calculations of the result.
 *
 * A general tradeoff of this approach is that the full expression tree is not
 * preserved. Subexpressions that are flattened will simply append their values
 * back into the main function until all subexpressions are removed.
 */
class ExpressionParser : Parser {

    private val math: Parser =
        SimpleMathParser()

    /**
     * Parses a complex expression into a [Node] tree. This structure
     * does not preserve the order of operations on its own, and depends
     * on [SimpleMathParser] to provide this functionality. The primary
     * purpose of this [Parser] is simply parsing our expression scopes.
     *
     * @param input The input expression to parse.
     * @return      The parsed node object, or a [ParseError].
     */
    override fun parse(input: String): Either<ParseError, Node> {
        val subs = arrayListOf<String>()
        val expr = StringBuilder("")
        // Loop over our input to parse out the scopes of our expression
        var i = 0
        while (i < input.length) {
            // Attempt to build a subexpression
            if (input[i] == '(') {
                // Since we called an opening block, we now need to
                // determine where the end of the block is
                val end = input.getSubexpression(i)
                // If the block does not close, we can safely assume the
                // expression is malformed
                if (end == -1)
                    return Either.Left(ParseError.MalformedExpression)
                // Now that we have our bounds, we can extract the expression
                // and add it to our subexpression list
                subs += input.substring(i + 1, end)
                // We should now jump forward to prevent re-processing data
                i = end
                // In place of our subexpression, we will append a placeholder
                // operator. This will be used later to flatten down our
                // expression tree
                expr.append("?${subs.lastIndex} ")
            }
            // Append data as normal
            else {
                expr.append(input[i])
            }
            i++
        }
        // If our expression has a non-empty subexpression list, we need to
        // do a bit of extra processing
        if (subs.isNotEmpty()) {
            // Store the original and clear the builder
            val temp = expr.toString()
            expr.clear()
            // Work over our base expression to populate our placeholders
            var j = 0
            while (j < temp.length) {
                // Attempt to build placeholder
                if (temp[j] == '?') {
                    // Since we detected a placeholder operation, we need to find
                    // the entire number our placeholder uses. We also have to
                    // consider the possibility that an expression could have
                    // 10 or more subexpressions
                    val end = temp.getPlaceHolder(j)
                    // Once our boundary is found, we can convert it to a number
                    val ph  = temp.substring(j + 1, end).toInt()
                    // We should now skip to the end of our boundary to avoid
                    // re-processing previous numbers
                    j       = end
                    // Recursively parse the given subexpression, since subexpressions
                    // may also have nested subexpressions which we will need to resolve.
                    // Any errors we encounter can be passed upwards
                    val res = parse(subs[ph]).getOrHandle {
                        return Either.Left(it)
                    }
                    // Append the flattened evaluation result to our main expression
                    expr.append(res.evaluate())
                }
                // Append data as normal
                else {
                    expr.append(temp[j])
                }
                j++
            }
        }
        // By this point, we should have a flat expression which can be
        // parsed by our simple math parser instead to handle the proper
        // order of operations
        return math.parse(expr.toString())
    }

    private fun String.getSubexpression(index: Int): Int {
        // We must start searching for a subexpression from the
        // opening scope statement
        if (this[index] != '(') return -1
        var depth = 0
        var temp = index
        while (temp < length) {
            // If a '(' is detected, a new scope was opened, however
            // if a ')' is detected, a scope was closed
            if (this[temp] == '(') depth++
            if (this[temp] == ')') depth--
            // If we manage to close our initial scope, that is our end point
            if (depth == 0)
                return temp
            temp++
        }
        // If the scope failed to close, then we return -1
        return -1
    }

    private fun String.getPlaceHolder(index: Int): Int {
        // Placeholders are in the form of "?n " where n is
        // a number referencing a position in a subexpression list
        if (this[index] != '?') return -1
        // Expand right until you reach the number's boundary
        var temp = index + 1
        while (temp < length) {
            if (this[temp] !in '0' .. '9')
                return temp
            temp++
        }
        // If no match is found, then -1 will serve as our error value
        return -1
    }
}