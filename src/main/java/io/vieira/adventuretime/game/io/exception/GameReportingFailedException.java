package io.vieira.adventuretime.game.io.exception;

import io.vieira.adventuretime.game.AdventureWorld;

/**
 * Exception thrown when game reporting fails.
 *
 * @see io.vieira.adventuretime.game.io.AdventureReporter#report(AdventureWorld)
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class GameReportingFailedException extends RuntimeException {

    public GameReportingFailedException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
