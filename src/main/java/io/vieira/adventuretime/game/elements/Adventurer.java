package io.vieira.adventuretime.game.elements;

import io.vieira.adventuretime.game.Direction;
import io.vieira.adventuretime.game.Orientation;
import io.vieira.adventuretime.game.Position;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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

    private final String adventurerName;

    @Setter
    private Orientation currentOrientation;

    private final List<Direction> pathHistory = new ArrayList<>();

    //TODO : Queue for configured path ?

    public Adventurer(Orientation orientation, String adventurerName, int northing, int easting) {
        super(northing, easting);
        this.adventurerName = adventurerName;
        this.currentOrientation = orientation;
    }

    //Exposing the necessary setter just here
    public void updatePosition(Position newPosition){
        this.position = newPosition;
    }

    @Override
    public String getSavableRepresentation() {
        String directionsHistory = pathHistory.stream().map(Direction::getDirectionCode).collect(Collectors.joining());
        StringJoiner joiner = new StringJoiner(" ")
                .add(adventurerName)
                .add(super.getSavableRepresentation())
                .add(currentOrientation.getOrientationCode());
        if(!directionsHistory.equals("")){
            joiner.add(directionsHistory);
        }
        return joiner.toString();
    }
}
