package xyz.atrius.demo.math

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import xyz.atrius.demo.math.op.*
import java.lang.ArithmeticException

/**
 * @author Atrius
 *
 * Small test suite for testing how nodes behave and interact with each other.
 */
@SpringBootTest
class MathTests {

    @Test
    fun `Test constants`() {
        val const = Constant(10.0)
        // Test const for equality and formatting
        assert(const.evaluate() == 10.0)
        assert(const.toString() == "10.00")
    }

    @Test
    fun `Test addition`() {
        // Simple addition
        val tree = Add(
            Constant(6.0),
            Constant(20.0)
        )
        assert(tree.evaluate() == 26.0)
        assert(tree.toString() == "(6.00 + 20.00)")
        // Inverted addition
        val other = Add(
            Constant(-6.3),
            Constant(6.3)
        )
        assert(other.evaluate() == 0.0)
        assert(other.toString() == "(-6.30 + 6.30)")
        // Both-Negative addition
        val last = Add(
            Constant(-5.0),
            Constant(-5.0)
        )
        assert(last.evaluate() == -10.0)
        assert(last.toString() == "(-5.00 + -5.00)")
    }

    @Test
    fun `Test subtraction`() {
        // Simple subtraction
        val tree = Sub(
            Constant(10.0),
            Constant(2.0)
        )
        assert(tree.evaluate() == 8.0)
        assert(tree.toString() == "(10.00 - 2.00)")
        // Test subtraction on larger right-hand side expressions
        val inverted = Sub(
            Constant(2.0),
            Constant(10.0)
        )
        assert(inverted.evaluate() == -8.0)
        assert(inverted.toString() == "(2.00 - 10.00)")
        // Test negative subtraction
        val negative = Sub(
            Constant(-20.0),
            Constant(-10.0)
        )
        assert(negative.evaluate() == -10.0)
        assert(negative.toString() == "(-20.00 - -10.00)")
    }

    @Test
    fun `Test multiplication`() {
        // Simple multiplication
        val tree = Mul(
            Constant(10.0),
            Constant(5.0)
        )
        assert(tree.evaluate() == 50.0)
        assert(tree.toString() == "(10.00 * 5.00)")
        // Test negative multiplication
        val inverted = Mul(
            Constant(22.0),
            Constant(-10.0)
        )
        assert(inverted.evaluate() == -220.0)
        assert(inverted.toString() == "(22.00 * -10.00)")
        // Test both-negative multiplication
        val negative = Mul(
            Constant(-20.0),
            Constant(-10.0)
        )
        assert(negative.evaluate() == 200.0)
        assert(negative.toString() == "(-20.00 * -10.00)")
        // Test zero multiplication
        val zero = Mul(
            Constant(100.0),
            Constant(0.0)
        )
        assert(zero.evaluate() == 0.0)
        assert(zero.toString() == "(100.00 * 0.00)")
    }

    @Test
    fun `Test division`() {
        // Simple division
        val tree = Div(
            Constant(10.0),
            Constant(5.0)
        )
        assert(tree.evaluate() == 2.0)
        assert(tree.toString() == "(10.00 / 5.00)")
        // Test negative division
        val inverted = Div(
            Constant(20.0),
            Constant(-10.0)
        )
        assert(inverted.evaluate() == -2.0)
        assert(inverted.toString() == "(20.00 / -10.00)")
        // Test both-negative division
        val negative = Div(
            Constant(-20.0),
            Constant(-10.0)
        )
        assert(negative.evaluate() == 2.0)
        assert(negative.toString() == "(-20.00 / -10.00)")
        // Test zero division
        val zero = Div(
            Constant(100.0),
            Constant(0.0)
        )
        assertThrows<ArithmeticException> {
            zero.evaluate()
        }
        assert(zero.toString() == "(100.00 / 0.00)")
    }

    @Test
    fun `Test exponents`() {
        // Simple multiplication
        val tree = Exp(
            Constant(10.0),
            Constant(5.0)
        )
        assert(tree.evaluate() == 100000.0)
        assert(tree.toString() == "(10.00 ^ 5.00)")
        // Test negative multiplication
        val inverted = Exp(
            Constant(20.0),
            Constant(-2.0)
        )
        assert(inverted.evaluate() == 0.0025)
        assert(inverted.toString() == "(20.00 ^ -2.00)")
        // Test both-negative multiplication
        val negative = Exp(
            Constant(-20.0),
            Constant(-10.0)
        )
        assert(negative.evaluate() == 0.00000000000009765625)
        assert(negative.toString() == "(-20.00 ^ -10.00)")
        // Test zero multiplication
        val zero = Exp(
            Constant(100.0),
            Constant(0.0)
        )
        assert(zero.evaluate() == 1.0)
        assert(zero.toString() == "(100.00 ^ 0.00)")
    }

    @Test
    fun `Test complex expressions`() {
        // (2 + 4 / 5) * 3
        val exp =  Mul(
            Add(
                Constant(2.0),
                Div(
                    Constant(4.0),
                    Constant(5.0)
                )
            ),
            Constant(3.0)
        )
        // Small workaround for floating point rounding problems
        assert(exp.evaluate() in 8.3 .. 8.4)
        assert(exp.toString() == "((2.00 + (4.00 / 5.00)) * 3.00)")
    }
}