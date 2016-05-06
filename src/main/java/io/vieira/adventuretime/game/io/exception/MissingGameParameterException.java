package io.vieira.adventuretime.game.io.exception;

/**
 * Exception thrown when a required game parameter is missing.
 *
 * @see io.vieira.adventuretime.game.AdventureWorld
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class MissingGameParameterException extends RuntimeException {

    public MissingGameParameterException(String s) {
        super(s);
    }
}
