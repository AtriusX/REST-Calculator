package xyz.atrius.demo.bridge.validation

import xyz.atrius.demo.data.error.AppError
import xyz.atrius.demo.data.error.ValidationError
import xyz.atrius.demo.data.error.ValidationError.InvalidConstant

/**
 * @author Atrius
 *
 * This validator is used to determine if a given input matches a valid
 * numeric format. If valid, the result is null, otherwise an
 * [InvalidConstant][xyz.atrius.demo.data.error.ValidationError.InvalidConstant]
 * error is raised to the caller.
 */
class NumberValidator : Validator {

    /**
     * Validates that the input is a valid positive or negative
     * floating-point number.
     *
     * @param input The input string to validate.
     * @return      Null if successful, or
     *              [InvalidConstant][xyz.atrius.demo.data.error.ValidationError.InvalidConstant]
     *              if an issue is found.
     */
    override fun validate(input: String): ValidationError? {
        return if (input.isNumber())
            null else InvalidConstant
    }

    // Matches integer and floating point numbers
    private fun String.isNumber(): Boolean =
        toDoubleOrNull() != null
}