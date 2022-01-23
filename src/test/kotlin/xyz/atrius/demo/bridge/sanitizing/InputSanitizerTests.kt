package xyz.atrius.demo.bridge.sanitizing

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Atrius
 *
 * Test suite for input sanitizers. This is used to determine if a given input
 * sanitizer is reasonably able to sanitize a given input to something usable.
 */
@SpringBootTest
class InputSanitizerTests {

    @Test
    fun `Test request sanitizer`() = with(RequestSanitizer(
        "%5E" to "^", "%20" to " "
    )) {
        assert(sanitize("2%20%5E%202") == "2 ^ 2")
        assert(sanitize("2%20+%202") == "2 + 2")
        assert(sanitize("2+2") == "2+2")
        assert(sanitize("2%5E2") == "2^2")
    }
}