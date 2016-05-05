package io.vieira.adventuretime.game.helpers;

import io.vieira.adventuretime.game.Direction;
import io.vieira.adventuretime.game.Orientation;
import io.vieira.adventuretime.game.Position;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

/**
 * Wrapper class used as a return result when trying {@link io.vieira.adventuretime.game.elements.WorldElement} movements.
 *
 * @see io.vieira.adventuretime.game.routines.MovementProvider#tryMoving(UUID, Direction)
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@Getter
@ToString
public class MovementTryResult {

    private final boolean aSuccess;

    private final Position newPosition;

    private final Orientation newOrientation;

    public MovementTryResult(boolean succeeded, Orientation newOrientation, Position newPosition) {
        this.aSuccess = succeeded;
        this.newPosition = newPosition;
        this.newOrientation = newOrientation;
    }
}
