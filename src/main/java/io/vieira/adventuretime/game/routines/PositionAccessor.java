package io.vieira.adventuretime.game.routines;

import io.vieira.adventuretime.game.AdventureWorld;
import io.vieira.adventuretime.game.Position;
import io.vieira.adventuretime.game.elements.WorldElement;

import java.util.stream.Stream;

/**
 * Interface used to get {@link WorldElement}s from the game board based on their positions.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public interface PositionAccessor {

    /**
     * Accesses {@link WorldElement} at supplied position, with type-safe access.
     *
     * @param position the x and y-index of the element, starting from 1. Will throw {@link IndexOutOfBoundsException} if one
     * of the indexes is out of the bounds or the underlying {@link AdventureWorld}.
     * @return a {@link Stream} of {@link WorldElement}s at the specified position, which can contain {@link io.vieira.adventuretime.game.elements.Adventurer}s
     * and {@link io.vieira.adventuretime.game.elements.Treasure}s, or nothing if the cell is empty.
     */
    Stream<WorldElement> at(Position position);
}
