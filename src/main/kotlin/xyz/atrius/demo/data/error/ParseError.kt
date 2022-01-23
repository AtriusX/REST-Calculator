package xyz.atrius.demo.data.error

/**
 * @author Atrius
 *
 * Parse errors are errors that occur during the parsing stage of an expression. While
 * [ValidationError] covers the general validation of basic mathematical expressions, it
 * cannot test them for issues that might potentially occur during the process of parsing
 * the expression to a node tree.
 *
 * @param message The message associated with this parse error.
 */
sealed class ParseError(message: String) : AppError(message) {

    object InvalidOperation : ParseError("Unknown operation detected!")

    object MalformedExpression : ParseError("Malformed expression!")

    object EmptyExpression : ParseError("No expression provided!")
}