package xyz.atrius.demo.bridge.parsing

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

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
    }
}