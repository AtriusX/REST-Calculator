package xyz.atrius.demo.bridge.validation

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Atrius
 *
 * Testing suite for expression validations. This is used to determine if
 * an expression is syntactically valid or not.
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidatorTests {

    @Test
    fun `Test number validation`() = with(NumberValidator()) {
        // Valid values
        assert(validate("0") == null)
        assert(validate("10") == null)
        assert(validate("-100") == null)
        assert(validate("4.5") == null)
        assert(validate(".03") == null)
        // Invalid values
        assert(validate("") != null)
        assert(validate("undefined") != null)
        assert(validate("null") != null)
        assert(validate("abcdef") != null)
        assert(validate("!!") != null)
    }

    @Test
    fun `Test expression validation`() = with(ExpressionValidator()) {
        // Valid expressions
        assert(validate("0") == null)
        assert(validate("2 + 2") == null)
        assert(validate("2+2") == null)
        assert(validate("(2 / 3) + 6") == null)
        assert(validate("(2/3)+6") == null)
        assert(validate("(2        / 3)              +          6    ") == null)
        assert(validate("(2 / (3 + 10)) + 6 ^ 3") == null)
        assert(validate("(2 / (3+ 10) )+ 6^ 3") == null)
        assert(validate("2 ^ 3 ^ 4 / 5 * (5 - 4 + -3)") == null)
        assert(validate("-3") == null)
        assert(validate("0 * 0 - 0 / 0 + 0") == null)
        assert(validate("2 ^ 2") == null)
        assert(validate("2^2") == null)
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