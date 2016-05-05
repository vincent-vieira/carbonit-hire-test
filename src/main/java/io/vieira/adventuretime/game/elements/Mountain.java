package io.vieira.adventuretime.game.elements;

import lombok.ToString;

/**
 * Mountain model class.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@ToString(callSuper = true)
public class Mountain extends WorldElement {

    public Mountain(int northing, int easting) {
        super(northing, easting);
    }
}
