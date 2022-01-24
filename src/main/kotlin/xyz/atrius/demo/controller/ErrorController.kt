package xyz.atrius.demo.controller

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xyz.atrius.demo.data.error.AppError

/**
 * @author Atrius
 *
 * Error controller for the application. This simply exists to provide a generic
 * error endpoint to clients to let them know the proper endpoint to use.
 */
@RestController
class ErrorController : ErrorController {

    /**
     * Send an error message to the requester letting them know that they are on
     * the wrong REST endpoint.
     */
    @RequestMapping("/error")
    fun error(): AppError =
        AppError.Custom("Invalid request! Use /calc")
}