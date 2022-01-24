package xyz.atrius.demo

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import xyz.atrius.demo.data.OperationManager
import xyz.atrius.demo.math.op.*

/**
 * @author Atrius
 *
 * This application is a simple calculator which takes math expressions in the
 * form of web requests. The application will receive the request, process it,
 * and then return a response back to the client. If an error was found in the
 * format of the expression, an error will be raised instead.
 */
@SpringBootApplication
class DemoApplication {

	private val log = LoggerFactory.getLogger(DemoApplication::class.java)

	init {
		// Register default operations
		with(OperationManager) {
			register('+', 0) { l, r -> Add(l, r) }
			register('-', 0) { l, r -> Sub(l, r) }
			register('*', 1) { l, r -> Mul(l, r) }
			register('/', 1) { l, r -> Div(l, r) }
			register('^', 2) { l, r -> Exp(l, r) }
			log.info("Operation manager initialized with state: $OperationManager")
		}
	}
}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
