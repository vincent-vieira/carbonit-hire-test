package io.vieira.adventuretime.game.elements;

import lombok.Getter;

import java.util.UUID;

/**
 * Adventurer model class.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class Adventurer extends MapElement {

    @Getter
    private int pickedUpTreasures;

    private final UUID adventurerID;

    public Adventurer(int northing, int easting) {
        this(UUID.randomUUID(), northing, easting);
    }

    public Adventurer(UUID id, int northing, int easting) {
        super(northing, easting);
        this.adventurerID = id;
    }

    @Override
    public int hashCode() {
        return adventurerID.hashCode();
    }
}
