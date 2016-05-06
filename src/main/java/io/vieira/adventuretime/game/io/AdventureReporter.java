package io.vieira.adventuretime.game.io;

import io.vieira.adventuretime.game.AdventureWorld;

/**
 * Interface describing the adventure reporter, writing to files the final state of the {@link io.vieira.adventuretime.game.AdventureWorld}.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@FunctionalInterface
public interface AdventureReporter {
    void report(AdventureWorld world);

    class NoOpReporter implements AdventureReporter{
        @Override
        public void report(AdventureWorld world) {
            //Noop
        }
    }
}
