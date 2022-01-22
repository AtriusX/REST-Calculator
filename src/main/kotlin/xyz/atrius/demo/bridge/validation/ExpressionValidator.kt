package xyz.atrius.demo.bridge.validation

import xyz.atrius.demo.data.AppError
import java.util.*

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
        fun checkNumber(): AppError? {
            // We are tracking a multi-character item (a constant value)
            // If the current value is not a valid number then an error is raised
            if (cur.isNotEmpty() && !cur.inNumber())
                return AppError.InvalidConstant
            // Flush and continue
            cur.clear()
            return null
        }
        for (c in input) {
            when (c) {
                // Clears the current item and skips to next character
                ' ' -> {
                    val error = checkNumber()
                    if (error != null) return error
                    continue
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
                    if (prev in ops || prev == '.' || prev == '(')
                        return AppError.UnexpectedSubexpressionTermination
                    depth--
                }
                // Track operators
                in ops -> {
                    if (prev == null)
                        return AppError.UnexpectedOperator
                    // Handle duplicated operators
                    if (prev in ops) {
                        if (c != '-')
                            return AppError.UnexpectedOperator
                        if (cur.isEmpty())
                            cur.append('-')
                        else
                            return AppError.UnexpectedOperator
                    }
                }
                // Builds number from input
                in '0' .. '9', '.' -> cur.append(c)
                // Any other character is considered invalid
                else -> return AppError.InvalidSymbol
            }
            prev = c
        }
        val error = checkNumber()
        if (error != null)
            return error
        return if (depth == 0) null
            else AppError.UnexpectedTermination
    }

    private fun StringBuilder.inNumber(): Boolean {
        // Matches integer and floating point numbers
        return toString().toDoubleOrNull() != null
    }
}