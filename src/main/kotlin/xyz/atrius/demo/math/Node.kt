package xyz.atrius.demo.math

/**
 * @author Atrius
 *
 * Nodes are the basis of our mathematical tree structure. The primary use of
 * this is to enable math expressions to be represented properly within a
 * programmatic environment. Evaluations as a rule can be processed recursively
 * from the root of an expression tree.
 */
interface Node {

    /**
     * Evaluates the current node's value. This is used as the basis for our
     * recursive implementation.
     *
     * @return The evaluation result of the current tree.
     */
    fun evaluate(): Double
}