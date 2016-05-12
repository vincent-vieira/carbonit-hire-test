package io.vieira.adventuretime.game.elements;

import io.vieira.adventuretime.game.Direction;
import io.vieira.adventuretime.game.Orientation;
import io.vieira.adventuretime.game.Position;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Adventurer model class.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@ToString(callSuper = true)
public class Adventurer extends WorldElement {

    @Setter
    @Getter
    private int pickedUpTreasures;

    @Getter
    private final String adventurerName;

    @Setter
    @Getter
    private Orientation currentOrientation;

    private final List<Direction> pathHistory = new ArrayList<>();

    @Getter
    private final Deque<Direction> remainingPath;

    public Adventurer(Orientation orientation, String adventurerName, int northing, int easting) {
        this(orientation, adventurerName, northing, easting, Collections.emptyList());
    }

    public Adventurer(Orientation orientation, String adventurerName, int northing, int easting, List<Direction> instructions){
        super(northing, easting);
        this.adventurerName = adventurerName;
        this.currentOrientation = orientation;
        this.remainingPath = new LinkedList<>(instructions);
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

    /**
     * Marks the specified {@link Direction} as the last "moved" direction, removing it from the {@link Adventurer#remainingPath} if present.
     *
     * @param direction the direction to move to
     */
    public void markMove(Direction direction){
        this.pathHistory.add(direction);
        this.remainingPath.poll();
    }
}
