package io.vieira.adventuretime.game.elements;

import lombok.Getter;
import lombok.ToString;

/**
 * Treasure model class.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@ToString(callSuper = true)
public class Treasure extends WorldElement {

    @Getter
    private int remainingLoots;

    public Treasure(int northing, int easting, int remainingLoots) {
        super(northing, easting);
        this.remainingLoots = remainingLoots;
    }

    public void removeALoot(){
        this.remainingLoots -= 1;
    }
}
