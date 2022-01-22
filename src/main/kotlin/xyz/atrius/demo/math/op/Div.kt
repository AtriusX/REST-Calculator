package xyz.atrius.demo.math.op

import xyz.atrius.demo.math.Node

/**
 * @author Atrius
 *
 * Performs a basic division operation on a given set of child expressions.
 *
 * @property left  The left-side subexpression for this operation.
 * @property right The right-side subexpression for this operation.
 */
class Div(
    left: Node,
    right: Node
) : BinaryOperator(left, right) {

    override val operator: Char = '/'

    override fun operation(left: Node, right: Node): Double {
        val r = right.evaluate()
        if (r == 0.0)
            throw ArithmeticException("Cannot divide by 0!")
        return left.evaluate() / r
    }
}