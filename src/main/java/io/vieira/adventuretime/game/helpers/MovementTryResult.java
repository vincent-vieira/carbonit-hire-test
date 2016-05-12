package io.vieira.adventuretime.game.helpers;

import io.vieira.adventuretime.game.Orientation;
import io.vieira.adventuretime.game.Position;
import io.vieira.adventuretime.game.elements.WorldElement;
import lombok.Getter;
import lombok.ToString;

/**
 * Wrapper class used as a return result when trying {@link io.vieira.adventuretime.game.elements.WorldElement} movements.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@Getter
@ToString
public class MovementTryResult {

    /**
     * Show if the next move is a success, meaning that the cell is free of anything
     */
    private final boolean aSuccess;

    /**
     * Exposes the new computed position
     */
    private final Position newPosition;

    /**
     * Exposes the new computed orientation
     */
    private final Orientation newOrientation;

    /**
     * Exposes the element already present on the new cell, if any.
     */
    private final WorldElement elementAtNewPosition;

    public MovementTryResult(boolean succeeded, Orientation newOrientation, Position newPosition, WorldElement elementAtNewPosition) {
        this.aSuccess = succeeded;
        this.newPosition = newPosition;
        this.newOrientation = newOrientation;
        this.elementAtNewPosition = elementAtNewPosition;
    }
}
