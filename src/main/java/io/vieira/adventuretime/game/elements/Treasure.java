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
    private int lootNumber;

    public Treasure(int northing, int easting, int lootNumber) {
        super(northing, easting);
        this.lootNumber = lootNumber;
    }
}
