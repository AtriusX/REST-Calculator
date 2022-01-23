package xyz.atrius.demo.data.error

/**
 * @author Atrius
 *
 * Validation errors are errors that occur in the context of expression validation. The
 * primary need for this is to provide feedback to REST requests if an issue in the format
 * of an expression is detected.
 *
 * @param message The message raised in our validation error.
 */
sealed class ValidationError(message: String) : AppError(message) {

    /**
     * Occurs when an operator is duplicated, or placed improperly (for example without a
     * prefixing constant).
     */
    object UnexpectedOperator : ValidationError("Unexpected operator!")

    /**
     * Occurs when a non-math symbol is detected. Accepted symbols generally are things such
     * as numbers (0-9), decimal points (.), operators (+, -, *, /, etc.), and parenthesis. All other
     * symbols such as letters are unsupported.
     */
    object InvalidSymbol : ValidationError("Invalid symbol!")

    /**
     * Occurs when an expression has concluded but seems to be incomplete. This could be caused by
     * a value not being supplied after an operator, or a subexpression remaining unclosed.
     */
    object UnexpectedTermination : ValidationError("Expression ended unexpectedly!")

    /**
     * Occurs when a numeric constant does not conform to the proper form. As a general rule, this
     * will occur when a number does not match the form of [-]<value>[.<decimal>].
     */
    object InvalidConstant : ValidationError("Invalid numeric constant!")

    /**
     * Occurs when a subexpression is not anticipated. This generally happens when a constant value
     * is followed by a subexpression block with no connecting operator. While math generally describes
     * this as a valid operation (considering it as multiplication), this remains unsupported for the
     * time being in this application.
     */
    object UnexpectedSubexpression : ValidationError("Unexpected subexpression!")

    /**
     * Occurs when a subexpression is terminated prematurely. This typically happens when a subexpression
     * closer is called following an operator, decimal point, subexpression opener ('('). This can also
     * occur if the subexpression results in a scope depth underflow, where the open scope count becomes
     * less than zero.
     */
    object UnexpectedSubexpressionTermination : ValidationError("Subexpression terminated unexpectedly!")
}