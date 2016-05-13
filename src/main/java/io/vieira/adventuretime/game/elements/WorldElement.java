package io.vieira.adventuretime.game.elements;

import io.vieira.adventuretime.game.AdventureWorld;
import io.vieira.adventuretime.game.Position;
import io.vieira.adventuretime.game.io.write.Savable;
import lombok.Getter;
import lombok.ToString;

/**
 * Abstract class aggregating all shared routines for all {@link AdventureWorld} elements.
 *
 * @see Mountain
 * @see Treasure
 * @see Adventurer
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@ToString
@Getter
public abstract class WorldElement implements Savable {

    public WorldElement(int northing, int easting){
        if(northing <= 0 || easting <= 0){
            throw new IllegalArgumentException("Northing or easting must be entered as relative coordinates, not array-typed ones");
        }
        this.position = new Position(northing, easting);
    }

    protected Position position;

    @Override
    public final boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(!(o instanceof WorldElement)){
            return false;
        }
        WorldElement other = (WorldElement) o;
        return other.getPosition().equals(position);
    }

    @Override
    public String getSavableRepresentation() {
        return this.position.getSavableRepresentation();
    }
}
