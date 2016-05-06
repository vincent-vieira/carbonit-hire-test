package io.vieira.adventuretime.game.io.exception;

/**
 * {@link RuntimeException} thrown when a game file can't be read.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class UnreadableGameFile extends RuntimeException {

    public UnreadableGameFile(String s, Throwable throwable) {
        super(s, throwable);
    }
}
