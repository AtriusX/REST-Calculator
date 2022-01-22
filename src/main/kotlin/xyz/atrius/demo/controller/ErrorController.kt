package xyz.atrius.demo.controller

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ErrorController : ErrorController {

    @RequestMapping("/error")
    fun error(): String = "Invalid request!"
}