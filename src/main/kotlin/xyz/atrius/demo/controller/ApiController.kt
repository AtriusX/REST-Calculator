package xyz.atrius.demo.controller

import arrow.core.Either
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import xyz.atrius.demo.data.*
import kotlin.random.Random

@RestController
class ApiController {

    @GetMapping("/calc/{equation}")
    fun calc(
        @PathVariable equation: String
    ): Either<AppError, Answer> {
        return Either.Left(AppError.Custom("${Random.nextInt(10000)} Invalid equation: $equation"))
//		return Either.Right(Answer("", 0.0))
    }
}