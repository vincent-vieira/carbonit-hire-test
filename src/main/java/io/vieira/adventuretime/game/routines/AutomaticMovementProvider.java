package io.vieira.adventuretime.game.routines;

import io.vieira.adventuretime.game.elements.Adventurer;

import java.util.concurrent.Callable;

/**
 * Interface defining the behavior of an automated {@link io.vieira.adventuretime.game.AdventureWorld}.
 *
 * @see MovementProvider, the "manual" version.
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public interface AutomaticMovementProvider extends Callable<Void> {

    /**
     * Moves automatically the adventurer passed as a parameter, using its internal configurer path.
     *
     * @see Adventurer#getRemainingPath()
     * @param adventurerName the adventurer name
     * @return a boolean specifying if the adventurer has remaining moves
     * @throws InterruptedException if the underlying {@link Callable} is interrupted.
     */
    boolean move(String adventurerName) throws InterruptedException;
}
