package xyz.atrius.demo.bridge.parsing

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Atrius
 *
 * Testing suite for expression parsers. This is used to parse a math
 * expression into a [Node][xyz.atrius.demo.math.Node] tree which can
 * be evaluated.
 */
@SpringBootTest
class ParserTests {

    @Test
    fun `Test simple math parser`() = with(SimpleMathParser()) {
        // Valid expressions
        val a = parse("2 + 2")
        assert(a.orNull()?.evaluate() == 4.0)
        val b = parse("10 - 6 / 2 ^ 5 + 3")
        assert(b.orNull()?.evaluate() == 12.8125)
        val c = parse("3 + 2 / 10 * 5")
        assert(c.orNull()?.evaluate() == 4.0)
        val d = parse("2 + 5 / 3")
        // Result is a repeating decimal
        assert((d.orNull()?.evaluate() ?: -1.0) in 3.6 .. 3.7)
        val e = parse("2 / 2 / 0")
        assert(e.orNull()?.evaluate() == Double.POSITIVE_INFINITY)
        val f = parse("0")
        assert(f.orNull()?.evaluate() == 0.0)
        // Invalid expressions
        val g = parse("+2/")
        assert(g.isLeft())
        val h = parse("")
        assert(h.isLeft())
        val i = parse("4 # 5")
        assert(i.isLeft())
        val j = parse("4 ## 5 *# 3 ! 2")
        assert(j.isLeft())
        val k = parse("4 ++ 5")
        assert(k.isLeft())
        val l = parse("4 + + 5")
        assert(l.isLeft())
        // Test valid negatives
        val m = parse("-2 + 5")
        assert(m.orNull()?.evaluate() == 3.0)
        val n = parse("2 + -5")
        assert(n.orNull()?.evaluate() == -3.0)
        val o = parse("-2 / -5")
        assert(o.orNull()?.evaluate() == 0.4)
        // No whitespace test
        val p = parse("2+5/4")
        assert(p.orNull()?.evaluate() == 3.25)
    }

    @Test
    fun `Test expression parser`() = with(ExpressionParser()) {
        val a = parse("(3 - 5) / 4 + (10 - 2)")
        assert(a.orNull()?.evaluate() == 7.5)
        val b = parse("((4 + 9) / 3 * 2) / 10 ^ 2")
        // Repeating decimal place
        assert((b.orNull()?.evaluate() ?: -1.0) in 0.086 .. 0.087)
        val c = parse("(2 + 5 / 4) ^ 3 * 10")
        assert(c.orNull()?.evaluate() == 343.28125)
        // No spaces test
        val d = parse("(2+5/4)^3*10")
        assert(d.orNull()?.evaluate() == 343.28125)
        val e = parse("5/(2-4)*(10+(10-3))*10")
        assert(e.orNull()?.evaluate() == -425.0)
        // Invalid expressions
        val f = parse(")))")
        assert(f.isLeft())
        val g = parse("(10+ 2-")
        assert(g.isLeft())
        val h = parse("--10")
        assert(h.isLeft())
        val i = parse("10 + - 5")
        assert(i.isLeft())
        val j = parse("10+- 5")
        assert(j.isLeft())
    }
}