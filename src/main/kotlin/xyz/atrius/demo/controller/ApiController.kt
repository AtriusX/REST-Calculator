package xyz.atrius.demo.controller

import arrow.core.Either
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import xyz.atrius.demo.data.*
import javax.servlet.http.HttpServletRequest
import kotlin.random.Random

private const val API_BASE = "/calc"

@RestController
class ApiController {

    @GetMapping("$API_BASE/**")
    fun calc(
        request: HttpServletRequest,
    ): Either<AppError, Answer> {
        return Either.Left(AppError.Custom(
            "${Random.nextInt(10000)} Invalid equation: ${request.requestURI.substringAfter("$API_BASE/")}"
        ))
    }
}