package io.vieira.adventuretime.game.elements;

import io.vieira.adventuretime.game.Direction;
import io.vieira.adventuretime.game.Orientation;
import io.vieira.adventuretime.game.Position;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Adventurer model class.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@ToString(callSuper = true)
@Getter
public class Adventurer extends WorldElement {

    @Setter
    private int pickedUpTreasures;

    private final UUID adventurerID;

    @Setter
    private Orientation currentOrientation;

    private final List<Direction> pathHistory = new ArrayList<>();

    public Adventurer(int northing, int easting) {
        this(Orientation.EAST, UUID.randomUUID(), northing, easting);
    }

    public Adventurer(Orientation orientation, int northing, int easting) {
        this(orientation, UUID.randomUUID(), northing, easting);
    }

    public Adventurer(Orientation orientation, UUID id, int northing, int easting) {
        super(northing, easting);
        this.adventurerID = id;
        this.currentOrientation = orientation;
    }

    //Exposing the necessary setter just here
    public void updatePosition(Position newPosition){
        this.position = newPosition;
    }
}
