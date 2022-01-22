package xyz.atrius.demo.bridge.validation

import xyz.atrius.demo.data.AppError

/**
 * @author Atrius
 *
 * This validator is used for validating the input as a valid mathematical
 * expression. This can be used ahead of a [Parser][xyz.atrius.demo.bridge.parsing.Parser]
 * object to determine if the given input is able to be properly used. If the
 * validator detects a formatting issue, then any issues found are raised via
 * an [AppError] object.
 */
class ExpressionValidator : Validator {

    private val ops: Set<Char> =
        setOf('+', '-', '*', '/', '^')

    /**
     * Validate the input to check if the format matches that of a valid
     * math expression.
     *
     * @param input The input string to validate.
     * @return      Any raised [AppError] instances, or null if successful.
     */
    override fun validate(input: String): AppError? {
        var depth = 0
        var prev: Char? = null
        val cur = StringBuilder("")
        // Loop over input characters
        for (c in input) {
            when (c) {
                // Clears the current item and skips to next character
                ' ' -> {
                    val error = cur.checkNumber()
                    // Return any raised errors or continue to next character
                    if (error != null) {
                        return error
                    } else {
                        // Flush and continue
                        cur.clear()
                        continue
                    }
                }
                // Open a new scope
                '(' -> {
                    // Check if the scope starts right after a number
                    if (cur.isNotEmpty())
                        return AppError.UnexpectedSubexpression
                    depth++
                }
                // Close the current scope
                ')' -> {
                    // Terminate at the end of a scope (must follow the end
                    // of another scope or constant), or if we cause an
                    // underflow the scope depth
                    if (prev in ops || prev == '.' || prev == '(' || depth - 1 < 0)
                        return AppError.UnexpectedSubexpressionTermination
                    depth--
                }
                // Track operators
                in ops -> {
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
                else -> return AppError.InvalidSymbol
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
        return if (isNotEmpty() && !isNumber())
            AppError.InvalidConstant else null
    }
}