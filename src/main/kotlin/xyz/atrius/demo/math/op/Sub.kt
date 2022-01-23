package xyz.atrius.demo.math.op

import xyz.atrius.demo.math.Node

/**
 * @author Atrius
 *
 * Performs a basic subtraction operation on a given set of child expressions.
 *
 * @property left  The left-side subexpression for this operation.
 * @property right The right-side subexpression for this operation.
 */
class Sub(
    left: Node,
    right: Node
) : BinaryOperator(left, right) {

    override val order: Int = 0

    override val operator: Char = '-'

    override fun operation(left: Node, right: Node): Double {
        return left.evaluate() - right.evaluate()
    }
}