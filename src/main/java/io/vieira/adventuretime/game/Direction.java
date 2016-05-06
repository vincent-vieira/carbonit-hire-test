package io.vieira.adventuretime.game;

import lombok.Getter;

import java.util.Arrays;

/**
 * Direction enumeration that serves as descriptor for {@link io.vieira.adventuretime.game.elements.Adventurer} movement.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public enum Direction {
    FORWARD("A"),
    LEFT("G"),
    RIGHT("D");

    @Getter
    private final String directionCode;

    Direction(String directionCode) {
        this.directionCode = directionCode;
    }

    public static Direction fromCode(String input){
        return Arrays
                .stream(values())
                .filter(direction -> direction.getDirectionCode().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format(
                                "'%s' is not a valid direction code.",
                                input
                        )
                ));
    }
}
