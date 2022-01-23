package xyz.atrius.demo.bridge.validation

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Atrius
 *
 * Testing suite for expression validations. This is used to determine if
 * an expression is syntactically valid or not.
 */
@SpringBootTest
class ValidatorTests {

    @Test
    fun `Test expression validation`() {
        with(ExpressionValidator()) {
            // Valid expressions
            assert(validate("0") == null)
            assert(validate("2 + 2") == null)
            assert(validate("(2 / 3) + 6") == null)
            assert(validate("(2        / 3)              +          6    ") == null)
            assert(validate("(2 / (3 + 10)) + 6 ^ 3") == null)
            assert(validate("2 ^ 3 ^ 4 / 5 * (5 - 4 + -3)") == null)
            assert(validate("-3") == null)
            assert(validate("0 * 0 - 0 / 0 + 0") == null)
            // Invalid expressions
            assert(validate("()") != null)
            assert(validate("+") != null)
            assert(validate("2 ++ 10") != null)
            assert(validate("2(100)") != null)
            assert(validate("(((") != null)
            assert(validate("(2 / 3) + 6 + (") != null)
            assert(validate("+3423") != null)
            assert(validate("3.0.0") != null)
            assert(validate("3.0.0 + 56") != null)
            assert(validate("invalid") != null)
            assert(validate("++-*/") != null)
            assert(validate("--0") != null)
            assert(validate("0--") != null)
            assert(validate(")))(((") != null)
        }
    }
}