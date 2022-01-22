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
@JvmInline
value class Constant(
    private val value: Double
) : Node {

    /**
     * A constant expression simply returns the stored value as its evaluation.
     *
     * @return The stored value of this object.
     */
    override fun evaluate(): Double = value

    /**
     * Converts the constant to a string, rounded to 2 decimal places.
     *
     * @return The rounded representation of this constant.
     */
    override fun toString(): String =
        String.format("%.2f", evaluate())
}