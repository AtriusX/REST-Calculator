package xyz.atrius.demo.bridge.sanitizing

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

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