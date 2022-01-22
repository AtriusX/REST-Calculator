package xyz.atrius.demo.bridge.validation

import xyz.atrius.demo.data.AppError

/**
 * @author Atrius
 *
 *
 */
class ExpressionValidator : Validator {

    private val ops: Set<Char> =
        setOf('+', '-', '*', '/', '^')

    override fun validate(input: String): AppError? {
        var depth = 0
        var prev: Char? = null
        val cur = StringBuilder("")
        // Loop over input characters
        for (c in input) {
            when (c) {
                // Clears the current item and skips to next character
                ' '              -> {
                    val error = cur.checkNumber()
                    // Return any raised errors or continue to next character
                    if (error != null)
                        return error else continue
                }
                // Open a new scope
                '('              -> {
                    // Check if the scope starts right after a number
                    if (cur.isNotEmpty())
                        return AppError.UnexpectedSubexpression
                    depth++
                }
                // Close the current scope
                ')'              -> {
                    // Terminate at the end of a scope (must follow the end
                    // of another scope or constant), or if we cause an
                    // underflow the scope depth
                    if (prev in ops || prev == '.' || prev == '(' || depth - 1 < 0)
                        return AppError.UnexpectedSubexpressionTermination
                    depth--
                }
                // Track operators
                in ops           -> {
                    // Prevent operators aside from '-' from being places
                    // at the beginning of an expression
                    if (prev == null && c != '-')
                        return AppError.UnexpectedOperator
                    // Handle duplicated operators
                    if (prev in ops) {
                        // Fail non-negative operators, duplicated negatives,
                        // and if the current item isn't empty
                        if (cur.isNotEmpty() || c != '-' || prev == '-')
                            return AppError.UnexpectedOperator
                        // Append negative prefix
                        cur.append('-')
                    }
                }
                // Build numbers from numeric values
                in '0'..'9', '.' -> cur.append(c)
                // Any other character is considered invalid
                else             -> return AppError.InvalidSymbol
            }
            prev = c
        }
        // Check if all scopes closed properly and the final value is valid
        return if (depth == 0)
            cur.checkNumber() else AppError.UnexpectedTermination
    }

    private fun StringBuilder.isNumber(): Boolean {
        // Matches integer and floating point numbers
        return toString().toDoubleOrNull() != null
    }

    private fun StringBuilder.checkNumber(): AppError? {
        // We are tracking a multi-character item (a constant value)
        // If the current value is not a valid number then an error is raised
        if (isNotEmpty() && !isNumber())
            return AppError.InvalidConstant
        // Flush and continue
        clear()
        return null
    }
}