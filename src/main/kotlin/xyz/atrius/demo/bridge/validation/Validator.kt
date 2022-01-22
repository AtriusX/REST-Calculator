package xyz.atrius.demo.bridge.validation

import xyz.atrius.demo.data.AppError

/**
 * @author Atrius
 *
 * Validators are used to test an input for validity on a given criteria function.
 * This is primarily useful for ensuring that our provided data does not raise any
 * exceptions or throw any errors when parsed by the application at a later point.
 */
interface Validator {

    /**
     * Validates the input [String] for validity based on a given criteria function.
     * This allows use to determine if certain errors might be raised at a later point.
     * If this function returns null, then it is considered a successful validation.
     *
     * @param input The input to be validated.
     * @return      An [AppError] object if an error is raised, or null if no error is found.
     */
    fun validate(input: String): AppError?
}