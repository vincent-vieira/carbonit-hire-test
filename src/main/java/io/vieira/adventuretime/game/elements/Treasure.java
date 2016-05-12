package io.vieira.adventuretime.game.elements;

import lombok.Getter;
import lombok.ToString;

import java.util.StringJoiner;

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

    @Override
    public String getSavableRepresentation() {
        return new StringJoiner(" ").add("T").add(super.getSavableRepresentation()).add(Integer.toString(remainingLoots)).toString();
    }
}
