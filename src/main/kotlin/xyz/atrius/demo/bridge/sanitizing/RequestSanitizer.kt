package xyz.atrius.demo.bridge.sanitizing

/**
 * @author Atrius
 *
 * This sanitizer is primarily intended for sanitizing HTTP request URLs, where
 * a few of the characters we accept in an expression get transformed into url-safe
 * character codes. With this we can revert those transformations and process
 * the expression as it was meant to be.
 */
class RequestSanitizer(
    private vararg val mappings: Pair<String, String>
) : InputSanitizer {

    /**
     * Sanitizes a given set of character transformations our of our input. These
     * mappings are provided by our [mappings] variable. Characters that match the
     * first half of each mapping will be converted to their corresponding second
     * halves.
     *
     * @param input The input string to sanitize.
     * @return      The newly sanitized string.
     */
    override fun sanitize(input: String): String {
        var out = input
        for ((from, to) in mappings)
            out = out.replace(from, to)
        return out
    }
}