package io.vieira.adventuretime.game.routines;

import io.vieira.adventuretime.game.elements.WorldElement;

import java.util.Map;

/**
 * Interface to access the number of each type of elements into the game board.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public interface ElementsRepartitionAccessor {

    /**
     * Fetches the count of the supplied {@link WorldElement} inside the current map.
     *
     * @param desiredType the searched {@link WorldElement}
     * @return the count
     */
    long getNumberOf(Class<? extends WorldElement> desiredType);

    /**
     * Fetches all occupation statistics, grouped by {@link WorldElement}.
     *
     * @return a {@link Map} containing the {@link WorldElement} as a key, and the count as a value.
     */
    Map<Class<? extends WorldElement>, Long> getOccupation();
}
