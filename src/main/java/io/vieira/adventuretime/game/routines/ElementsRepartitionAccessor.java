package io.vieira.adventuretime.game.routines;

import io.vieira.adventuretime.game.elements.MapElement;

import java.util.Map;

/**
 * Interface to access the number of each type of elements into the game board.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public interface ElementsRepartitionAccessor {

    /**
     * Fetches the count of the supplied {@link MapElement} inside the current map.
     *
     * @param desiredType the searched {@link MapElement}
     * @return the count
     */
    long getNumberOf(Class<? extends MapElement> desiredType);

    /**
     * Fetches all occupation statistics, grouped by {@link MapElement}.
     *
     * @return a {@link Map} containing the {@link MapElement} as a key, and the count as a value.
     */
    Map<Class<? extends MapElement>, Long> getOccupation();
}
