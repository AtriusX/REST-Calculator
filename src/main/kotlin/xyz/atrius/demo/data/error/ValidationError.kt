package xyz.atrius.demo.data.error

sealed class ValidationError(message: String) : AppError(message) {

    object UnexpectedOperator : ValidationError("Unexpected operator!")

    object InvalidSymbol : ValidationError("Invalid symbol!")

    object UnexpectedTermination : ValidationError("Expression ended unexpectedly!")

    object InvalidConstant : ValidationError("Invalid numeric constant!")

    object UnexpectedSubexpression : ValidationError("Unexpected subexpression!")

    object UnexpectedSubexpressionTermination : ValidationError("Subexpression terminated unexpectedly!")
}