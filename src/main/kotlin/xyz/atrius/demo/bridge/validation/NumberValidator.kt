package xyz.atrius.demo.bridge.validation

import xyz.atrius.demo.data.AppError

class NumberValidator : Validator {

    override fun validate(input: String): AppError? {
        println("Input: '$input': ${input.isNumber()}")
        return if (input.isNumber())
            null else AppError.InvalidConstant
    }

    // Matches integer and floating point numbers
    private fun String.isNumber(): Boolean =
        toDoubleOrNull() != null
}