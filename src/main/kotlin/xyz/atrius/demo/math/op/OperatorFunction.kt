package xyz.atrius.demo.math.op

import xyz.atrius.demo.math.Node
import xyz.atrius.demo.data.OperationManager

/**
 * @author Atrius
 *
 * Describes a constructor function used for building an operator with children
 * from [OperationManager] data. This will allow us to separate areas of concern
 * and leave the creation of nodes to other sources of data.
 *
 * @property Node The left child of the constructed operator.
 * @property Node The right child of the constructed operator.
 * @return        The newly constructed expression tree.
 */
fun interface OperatorFunction : (Node, Node) -> BinaryOperator
