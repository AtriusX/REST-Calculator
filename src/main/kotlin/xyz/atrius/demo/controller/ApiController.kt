package xyz.atrius.demo.controller

import arrow.core.Either
import arrow.core.getOrHandle
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import xyz.atrius.demo.bridge.parsing.ExpressionParser
import xyz.atrius.demo.bridge.parsing.Parser
import xyz.atrius.demo.bridge.sanitizing.InputSanitizer
import xyz.atrius.demo.bridge.sanitizing.RequestSanitizer
import xyz.atrius.demo.bridge.validation.ExpressionValidator
import xyz.atrius.demo.bridge.validation.Validator
import xyz.atrius.demo.data.Answer
import xyz.atrius.demo.data.error.AppError
import javax.servlet.http.HttpServletRequest

private const val API_BASE = "/calc"

/**
 * @author Atrius
 *
 * Primary API controller for the application. This primarily serves the purpose of supporting
 * the /calc/<expression> endpoint.
 */
@RestController
class ApiController {

    private val sanitize: InputSanitizer = RequestSanitizer(
        "%5E" to "^", "%20" to " "
    )
    private val validation: Validator = ExpressionValidator()
    private val parsing: Parser = ExpressionParser()

    /**
     * This represents the /calc/<expression> API endpoint. Math expressions can be fed
     * into it via the expression field, and the server will process the expression and
     * send back the answer. Only basic math expressions are current supported however,
     * so anything beyond subexpressions or binary operations is currently working.
     */
    @GetMapping("$API_BASE/**")
    fun calc(
        request: HttpServletRequest,
    ): Either<AppError, Answer> {
        // Sanitize expression so more exotic characters are
        // translated over properly
        val expression = sanitize.sanitize(
            request.requestURI.substringAfter("$API_BASE/")
        )
        // Check if any validation errors are present
        val error = validation.validate(expression)
        // If an error is found, raise it back to the user
        if (error != null)
            return Either.Left(error)
        // Attempt to parse the expression into a tree, or raise
        // an error if one is present
        val tree = parsing.parse(expression).getOrHandle {
            return Either.Left(it)
        }
        // If our expression parsed correctly, we can now evaluate
        // and return our response back to the user
        return Either.Right(
            Answer(expression.replace(" ", ""), tree.evaluate())
        )
    }
}