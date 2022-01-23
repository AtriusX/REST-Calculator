package xyz.atrius.demo.math.op

import xyz.atrius.demo.math.Node
import kotlin.math.pow

/**
 * @author Atrius
 *
 * Performs an exponent operation on a given set of child expressions.
 *
 * @property left  The left-side subexpression for this operation.
 * @property right The right-side subexpression for this operation.
 */
class Exp(
    left: Node,
    right: Node
) : BinaryOperator(left, right) {

    override val operator: Char = '^'

    override fun operation(left: Node, right: Node): Double {
        return left.evaluate() exp right.evaluate()
    }

    // Allows us to define our power function as "x exp y"
    private infix fun Double.exp(other: Double): Double =
        pow(other)
}