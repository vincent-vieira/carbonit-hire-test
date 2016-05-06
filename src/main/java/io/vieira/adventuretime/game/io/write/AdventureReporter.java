package io.vieira.adventuretime.game.io.write;

import java.util.stream.Stream;

/**
 * Interface describing the adventure reporter, writing to files the final state of the {@link io.vieira.adventuretime.game.AdventureWorld}.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@FunctionalInterface
public interface AdventureReporter {

    /**
     * Triggers reporting. To anything. File, network...
     *
     * @param savableStream a stream {@link Savable} elements.
     */
    void report(Stream<Savable> savableStream);

    class NoOpReporter implements AdventureReporter{
        @Override
        public void report(Stream<Savable> savableStream) {
            //Noop
        }
    }
}
