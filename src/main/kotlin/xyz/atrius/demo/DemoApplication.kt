package xyz.atrius.demo

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import xyz.atrius.demo.data.OperationManager
import xyz.atrius.demo.math.op.*

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
