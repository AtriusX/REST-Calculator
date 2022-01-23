package xyz.atrius.demo.bridge.sanitizing

/**
 * @author Atrius
 *
 * Input sanitizers are used primarily for bridging gaps in application contexts.
 * Their primary purpose is to prevent potentially invisible issues that might occur
 * during application runtime, when parts are integrated with others. These issues
 * might not appear during the automated testing process.
 */
interface InputSanitizer {

    /**
     * Sanitizes a given input so that the contents of the string remain
     * within acceptable boundaries.
     *
     * @param input The string to sanitize.
     * @return      The newly sanitized string.
     */
    fun sanitize(input: String): String
}