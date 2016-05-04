package io.vieira.adventuretime.game.elements;

import lombok.Getter;

/**
 * Treasure model class.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class Treasure extends MapElement {

    @Getter
    private int lootNumber;

    public Treasure(int northing, int easting, int lootNumber) {
        super(northing, easting);
        this.lootNumber = lootNumber;
    }
}
