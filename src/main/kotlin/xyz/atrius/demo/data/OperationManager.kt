package xyz.atrius.demo.data

import xyz.atrius.demo.math.Node
import xyz.atrius.demo.math.op.BinaryOperator

typealias OperatorFunction =
    (left: Node, right: Node) -> BinaryOperator

object OperationManager {

    private val operators = hashMapOf<Char, Pair<Int, OperatorFunction>>()

    fun register(operator: Char, order: Int, construct: OperatorFunction) {
        if (operator !in operators)
            operators[operator] = order to construct
    }

    fun getPrecedence(op: Char): Int =
        operators[op]?.first ?: -1

    operator fun contains(op: Char?): Boolean =
        operators.containsKey(op)

    operator fun get(op: Char): OperatorFunction? =
        operators[op]?.second

    override fun toString(): String =
        operators.toString()
}