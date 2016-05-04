package io.vieira.adventuretime.game.elements;

import lombok.Getter;

/**
 * Abstract class aggregating all shared routines for all {@link io.vieira.adventuretime.game.Map} elements.
 *
 * @see Mountain
 * @see Treasure
 * @see Adventurer
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@Getter
public abstract class MapElement {

    public MapElement(int northing, int easting){
        this.declaredEasting = easting;
        this.declaredNorthing = northing;
    }

    protected int declaredEasting;

    protected int declaredNorthing;

    @Override
    public final boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(!(o instanceof MapElement)){
            return false;
        }
        MapElement other = (MapElement) o;
        return (Integer.compare(declaredEasting, other.getDeclaredEasting()) & Integer.compare(declaredNorthing, other.getDeclaredNorthing())) == 0;
    }
}
