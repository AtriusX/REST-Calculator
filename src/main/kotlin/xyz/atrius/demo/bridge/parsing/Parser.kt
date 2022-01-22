package xyz.atrius.demo.bridge.parsing

import arrow.core.Either
import xyz.atrius.demo.data.AppError
import xyz.atrius.demo.math.Node

/**
 * @author Atrius
 *
 * Expression parsers are used for scanning over an input string and converting it into the
 * proper [Node] representation. This allows us to describe mathematical expressions from
 * natural language instead of through programmatic constructs. This system will then be
 * used as the bridge from natural language to logical constructs.
 */
interface Parser {

    /**
     * Parses an input [String] into a [Node] tree structure. Alternatively, if parsing
     * fails for any reason, an [Error] is instead returned. If parsing succeeds, the
     * resulting node can be evaluated for its result.
     *
     * @param input The string to attempt parsing for.
     * @return      The determined node tree, or an error if parsing fails.
     */
    fun parse(input: String): Either<AppError, Node>
}