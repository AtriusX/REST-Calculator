package xyz.atrius.demo.data.error

sealed class AppError(
    val error: String
) {
    class Custom(message: String) : AppError(message)
}
