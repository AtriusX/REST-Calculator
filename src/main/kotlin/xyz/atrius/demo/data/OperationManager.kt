package xyz.atrius.demo.data

import xyz.atrius.demo.math.op.OperatorFunction

/**
 * @author Atrius
 *
 * The operation manager is responsible for the storage of mathematical operators.
 * Other classes within the application will be able to access the data stored within
 * this and create new operation structures on the fly. This will also allow a receiver
 * to check what an operator's precedence is, as well as if it exists within the object.
 */
object OperationManager {

    private val operators =
        hashMapOf<Char, Pair<Int, OperatorFunction>>()

    /**
     * Allows for a new operator to be defined within the manager with a specified
     * operator precedence value. It should be noted that lower values of order should
     * be executed after operators with higher priority are handled.
     *
     * @param operator  The operator character to use for this operation.
     * @param order     The precedence the operator will take in the order of operations.
     *                  Lower values are considered lower-priority.
     * @param construct The construction function used for generating the related object.
     */
    fun register(operator: Char, order: Int, construct: OperatorFunction) {
        if (operator !in operators)
            operators[operator] = order to construct
    }

    /**
     * Checks the precedence of a given operator.
     *
     * @param op The operator to check the precedence of.
     * @return   The operator's precedence value, or -1 if no such operator exists
     */
    fun getPrecedence(op: Char): Int =
        operators[op]?.first ?: -1

    /**
     * Checks if the given operator exists within the object.
     *
     * @param op The operator to check the presence of.
     * @return   True if the operator is present within the object.
     */
    operator fun contains(op: Char?): Boolean =
        operators.containsKey(op)

    /**
     * Retrieves the corresponding [OperatorFunction] for the given operator. If
     * the operator is not present, then no function is returned.
     *
     * @param op The operator to retrieve the function for.
     * @return   The corresponding operator function, or null if it is not present.
     */
    operator fun get(op: Char): OperatorFunction? =
        operators[op]?.second

    /**
     * Converts the manager's data into a human-readable string.
     *
     * @return The manager represented as a string.
     */
    override fun toString(): String =
        operators.toString()
}