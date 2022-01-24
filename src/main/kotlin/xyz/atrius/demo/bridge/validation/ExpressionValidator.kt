package xyz.atrius.demo.bridge.validation

import xyz.atrius.demo.bridge.parsing.Parser
import xyz.atrius.demo.data.OperationManager
import xyz.atrius.demo.data.error.AppError
import xyz.atrius.demo.data.error.ValidationError
import xyz.atrius.demo.data.error.ValidationError.*

/**
 * @author Atrius
 *
 * This validator is used for validating the input as a valid mathematical
 * expression. This can be used ahead of a [Parser] object to determine if
 * the given input is able to be properly used. If the validator detects a
 * formatting issue, then any issues found are raised via an [AppError] object.
 */
class ExpressionValidator : Validator {

    private val number: Validator =
        NumberValidator()

    /**
     * Validate the input to check if the format matches that of a valid
     * math expression.
     *
     * @param input The input string to validate.
     * @return      Any raised [AppError] instances, or null if successful.
     */
    override fun validate(input: String): ValidationError? {
        var depth = 0
        var prev: Char? = null
        val cur = StringBuilder("")
        // Prevent operator issues when no space is present before a minus
        val values = input.replace("-", " -")
        // Loop over input characters
        for (c in values) {
            when (c) {
                // Clears the current item and skips to next character
                ' ' -> {
                    // Skip whitespace
                    if (cur.isEmpty()) continue
                    // Check for errors in constant format
                    val error = number.validate(cur.toString())
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
                        return UnexpectedSubexpression
                    depth++
                }
                // Close the current scope
                ')' -> {
                    // Terminate at the end of a scope (must follow the end
                    // of another scope or constant), or if we cause an
                    // underflow the scope depth
                    if (prev in OperationManager || prev == '.' || prev == '(' || depth - 1 < 0)
                        return UnexpectedSubexpressionTermination
                    depth--
                }
                // Track operators
                in OperationManager -> {
                    // Prevent operators aside from '-' from being places
                    // at the beginning of an expression
                    if (prev == null && c != '-')
                        return UnexpectedOperator
                    // Handle duplicated operators
                    if (prev in OperationManager) {
                        // Fail non-negative operators, duplicated negatives,
                        // and if the current item isn't empty
                        if (cur.isNotEmpty() || c != '-' || prev == '-')
                            return UnexpectedOperator
                        // Append negative prefix
                        cur.append('-')
                    }
                }
                // Build numbers from numeric values
                in '0'..'9', '.' -> cur.append(c)
                // Any other character is considered invalid
                else -> return InvalidSymbol
            }
            prev = c
        }
        // Check if all scopes closed properly and that the final value is valid
        return when {
            cur.isNotEmpty() -> number.validate(cur.toString())
            depth == 0       -> null
            else             -> UnexpectedTermination
        }
    }
}