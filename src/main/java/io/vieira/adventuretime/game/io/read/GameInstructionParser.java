package io.vieira.adventuretime.game.io.read;

import io.vieira.adventuretime.game.io.exception.GameInstructionParsingException;

/**
 * Functional interface providing line parsing to any supplied type.
 *
 * @param <T> the target type to produce
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@FunctionalInterface
public interface GameInstructionParser<T> {

    /**
     * Uses the provided parameter to produce an object.
     *
     * @param line the input line
     * @return the object of type defined
     * @throws GameInstructionParsingException if an error occurs
     */
    T fromLine(String line) throws GameInstructionParsingException;
}
