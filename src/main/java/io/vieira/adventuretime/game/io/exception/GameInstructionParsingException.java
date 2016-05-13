package io.vieira.adventuretime.game.io.exception;

/**
 * Exception thrown when a game instruction cannot be properly parsed.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class GameInstructionParsingException extends Exception {

    public GameInstructionParsingException(String s) {
        super(s);
    }

    public GameInstructionParsingException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
