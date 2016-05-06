package io.vieira.adventuretime.game;

import lombok.Getter;
import lombok.ToString;

/**
 * Position class, wrapping northing/easting of the target {@link io.vieira.adventuretime.game.elements.WorldElement}.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@Getter
@ToString
public class Position {

    private final int northing;
    private final int easting;

    public Position(int northing, int easting) {
        this.northing = northing;
        this.easting = easting;
    }

    public int getAbsoluteNorthing(){
        return northing - 1;
    }

    public int getAbsoluteEasting(){
        return easting - 1;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(!(o instanceof Position)){
            return false;
        }
        Position other = (Position) o;
        return easting == other.getEasting() && northing == other.getNorthing();
    }

    public static Position fromString(String inputString) {
        String[] parts = inputString.split("\\-");
        if(parts.length != 2){
            throw new IllegalArgumentException(
                    String.format(
                            "'%s' is not a valid position string.",
                            inputString
                    )
            );
        }
        return new Position(
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[0])
        );
    }
}
