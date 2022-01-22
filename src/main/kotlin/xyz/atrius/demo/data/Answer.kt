package xyz.atrius.demo.data

/**
 * @author Atrius
 *
 * Models a simple answer response to the requesting client.
 *
 * @property equation The parsed equation taken from the given input.
 * @property result   The resulting value of the calculation.
 */
data class Answer(
    val equation: String,
    val result: Double
)