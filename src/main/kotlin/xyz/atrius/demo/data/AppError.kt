package xyz.atrius.demo.data

sealed class AppError(
    val error: String
) {
    object UnexpectedOperator : AppError("Unexpected operator!")

    object InvalidSymbol : AppError("Invalid symbol!")

    object UnexpectedTermination : AppError("Expression ended unexpectedly!")

    object InvalidConstant : AppError("Invalid numeric constant!")

    object UnexpectedSubexpression : AppError("Unexpected subexpression!")

    object UnexpectedSubexpressionTermination : AppError("Subexpression terminated unexpectedly!")

    class Custom(message: String) : AppError(message)
}