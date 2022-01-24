package xyz.atrius.demo.bridge.parsing

import arrow.core.Either
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import xyz.atrius.demo.data.error.ParseError
import xyz.atrius.demo.math.Node

/**
 * @author Atrius
 *
 * Testing suite for expression parsers. This is used to parse a math
 * expression into a [Node] tree which can
 * be evaluated.
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParserTests {

    @Test
    fun `Test simple math parser`() = with(SimpleMathParser()) {
        // Valid expressions
        assertEquals(parse("2 + 2"), 4.0)
        assertEquals(parse("10 - 6 / 2 ^ 5 + 3"), 12.8125)
        assertEquals(parse("3 + 2 / 10 * 5"), 4.0)
        // Result is a repeating decimal
        assertIn(parse("2 + 5 / 3"), 3.6, 3.7)
        assertEquals(parse("2 / 2 / 0"), Double.POSITIVE_INFINITY)
        assertEquals(parse("0"), 0.0)
        // Invalid expressions
        assertFails(parse("+2/"))
        assertFails(parse(""))
        assertFails(parse("4 # 5"))
        assertFails(parse("4 ## 5 *# 3 ! 2"))
        assertFails(parse("4 ++ 5"))
        assertFails(parse("4 + + 5"))
        // Test valid negatives
        assertEquals(parse("-2 + 5"), 3.0)
        assertEquals(parse("2 + -5"), -3.0)
        assertEquals(parse("-2 / -5"), 0.4)
        // No whitespace test
        assertEquals(parse("2+5/4"), 3.25)
        // Whitespace in constants
        assertEquals(parse("2. 000"), 2.0)
        assertEquals(parse("2 .000"), 2.0)
        assertEquals(parse("2 . 000"), 2.0)
        assertEquals(parse("2.0  00"), 2.0)
        assertEquals(parse("223 4.000"), 2234.0)
    }

    @Test
    fun `Test expression parser`() = with(ExpressionParser()) {
        assertEquals(parse("(3 - 5) / 4 + (10 - 2)"), 7.5)
        assertEquals(parse("(4 + 10) / 5 ^ 2"), 0.56)
        // Repeating decimal place
        assertIn(parse("((4 + 9) / 3 * 2) / 10 ^ 2"), 0.086, 0.087)
        assertEquals(parse("(2 + 5 / 4) ^ 3 * 10"), 343.28125)
        // No spaces test
        assertEquals(parse("(2+5/4)^3*10"), 343.28125)
        assertEquals(parse("5/(2-4)*(10+(10-3))*10"), -425.0)
        // Invalid expressions
        assertFails(parse(")))"))
        assertFails(parse("(10+ 2-"))
        assertFails(parse("--10"))
        assertFails(parse("10 + - 5"))
        assertFails(parse("10+- 5"))
    }

    private fun assertEquals(input: Either<ParseError, Node>, expected: Double) =
        assert(input.orNull()?.evaluate() == expected)

    private fun assertIn(input: Either<ParseError, Node>, low: Double, high: Double) {
        require(low < high)
        assert((input.orNull()?.evaluate() ?: (low - 1))  in low..high)
    }

    private fun assertFails(input: Either<ParseError, Node>) =
        assert(input.isLeft())
}