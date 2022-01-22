package xyz.atrius.demo.math.op

import xyz.atrius.demo.math.Node

/**
 * @author Atrius
 *
 * A special subtype of nodes meant to describe operations which occur between
 * two child expressions. This can be used to describe mathematical concepts
 * like addition, subtraction, or division, to name a few.
 */
abstract class BinaryOperator(
    private val left: Node,
    private val right: Node
) : Node {

    /**
     * Determines the character used in printing the expression.
     */
    internal abstract val operator: Char

    /**
     * Performs a given operation against the calculated results
     * of the left and right side expressions. Each side can be
     * either a constant or subtree of operations.
     *
     * @param left  The left-hand subexpression.
     * @param right The right-hand subexpression.
     * @return      The total value of the left and right subexpressions
     *              after the given operation is applies.
     */
    internal abstract fun operation(left: Node, right: Node): Double

    /**
     * Each operator should simply rely on the evaluation result of
     * our operation function.
     *
     * @return The total value of both subexpressions after applying the
     *         given operation.
     */
    override fun evaluate(): Double =
        operation(left, right)

    /**
     * Prints all operator expressions in similar form to "a + b".
     *
     * @return The string representation of the given operation.
     */
    override fun toString(): String =
        "$left $operator $right"
}