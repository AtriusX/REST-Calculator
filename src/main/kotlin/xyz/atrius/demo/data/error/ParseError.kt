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

    /**
     * Occurs when an operator or non-standard character is detected in an
     * expression. Accepted characters must be numeric, decimal points, valid
     * operators, or spaces.
     */
    object InvalidOperation : ParseError("Unknown operation detected!")

    /**
     * Occurs when an expression cannot be properly parsed. This generally can
     * occur if an expression has duplicated operators or begins or ends with
     * an operator rather than a constant.
     */
    object MalformedExpression : ParseError("Malformed expression!")

    /**
     * Occurs when an empty expression is passed to the parser.
     */
    object EmptyExpression : ParseError("No expression provided!")
}