package io.vieira.adventuretime.game.io.exception;

import io.vieira.adventuretime.game.io.write.AdventureReporter;

import java.util.stream.Stream;

/**
 * Exception thrown when game reporting fails.
 *
 * @see AdventureReporter#report(Stream)
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class GameReportingFailedException extends RuntimeException {

    public GameReportingFailedException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
