package io.vieira.adventuretime.game.routines;

import io.vieira.adventuretime.game.Direction;
import io.vieira.adventuretime.game.elements.Adventurer;
import io.vieira.adventuretime.game.elements.WorldElement;
import io.vieira.adventuretime.game.helpers.MovementTryResult;

import java.util.UUID;

/**
 * Interface describing behavior for moving {@link WorldElement}s ({@link Adventurer}s most precisely)
 * on the game board.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public interface MovementProvider {

    /**
     * Tries moving the specified adventurer with the specified position.
     *
     * @param adventurerID the adventurer ID
     * @param direction the {@link Direction} to move to
     * @return the {@link MovementTryResult} object, containing movement results.
     */
    MovementTryResult tryMoving(UUID adventurerID, Direction direction);

    /**
     * Moves the specified adventurer in the specified direction, adjusting automatically the underlying {@link io.vieira.adventuretime.game.Orientation}.
     *
     * @param adventurerID the adventurer ID
     * @param direction the {@link Direction} to move to
     */
    void move(UUID adventurerID, Direction direction);
}
