package xyz.atrius.demo.bridge.sanitizing

/**
 * @author Atrius
 *
 *
 */
class RequestSanitizer(
    private vararg val mappings: Pair<String, String>
) : InputSanitizer {

    override fun sanitize(input: String): String {
        var out = input
        for ((from, to) in mappings)
            out = out.replace(from, to)
        return out
    }
}