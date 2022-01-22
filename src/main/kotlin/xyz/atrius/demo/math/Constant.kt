package xyz.atrius.demo.math

/**
 * @author Atrius
 *
 * Represents a constant value in a mathematical tree structure. Unlike other [Node]
 * objects, this one does not have children. As such, execution is terminated immediately
 * after this object is evaluated.
 *
 * @param value The constant value stored by our object.
 */
class Constant(
    private val value: Double
) : Node {

    override fun evaluate(): Double = value
}