package io.vieira.adventuretime.game.io.parse;

import io.vieira.adventuretime.game.Orientation;
import io.vieira.adventuretime.game.Position;
import io.vieira.adventuretime.game.elements.Adventurer;
import io.vieira.adventuretime.game.elements.Mountain;
import io.vieira.adventuretime.game.elements.Treasure;
import io.vieira.adventuretime.game.helpers.WorldSize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Game instruction interface, providing line matching and transformation into any type.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public interface GameInstruction<T> {

    /**
     * Gets the line matching pattern. Any line matching with the returned {@link Pattern} will be processed by {@link GameInstruction#transform(String)}.
     *
     * @return the {@link Pattern}.
     */
    Pattern instructionPattern();

    /**
     * Transforms the supplied line.
     *
     * @param line the line to transform
     * @return an instance of the inferred type supplied
     */
    T transform(String line);

    GameInstruction ADVENTURE_WORLD = new GameInstruction<WorldSize>() {
        //Pre-compiling pattern for performance
        private final Pattern instructionPattern = Pattern.compile("^C\\s(\\d)\\s(\\d)$");

        @Override
        public Pattern instructionPattern() {
            return instructionPattern;
        }

        @Override
        public WorldSize transform(String line) {
            Matcher matcher = instructionPattern().matcher(line);
            matcher.find();
            return new WorldSize(
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(1))
            );
        }
    };

    GameInstruction MOUNTAIN = new GameInstruction<Mountain>() {
        private final Pattern instructionPattern = Pattern.compile("^M\\s(\\d)-(\\d)$");

        @Override
        public Pattern instructionPattern() {
            return instructionPattern;
        }

        @Override
        public Mountain transform(String line) {
            Matcher matcher = instructionPattern().matcher(line);
            matcher.find();
            return new Mountain(
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(1))
            );
        }
    };

    GameInstruction TREASURE = new GameInstruction<Treasure>() {
        private final Pattern instructionPattern = Pattern.compile("^T\\s(\\d)-(\\d)\\s(\\d)$");

        @Override
        public Pattern instructionPattern() {
            return instructionPattern;
        }

        @Override
        public Treasure transform(String line) {
            Matcher matcher = instructionPattern().matcher(line);
            matcher.find();
            return new Treasure(
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(3))
            );
        }
    };

    GameInstruction ADVENTURER = new GameInstruction<Adventurer>() {
        private final Pattern instructionPattern = Pattern.compile("^(\\w+)\\s(\\d-\\d)\\s(N|E|S|O)\\s([A|D|G]+)$");

        @Override
        public Pattern instructionPattern() {
            return instructionPattern;
        }

        @Override
        public Adventurer transform(String line) {
            Matcher matcher = instructionPattern().matcher(line);
            matcher.find();
            Position position = Position.fromString(matcher.group(2));

            //TODO : set adventurer path ?
            return new Adventurer(
                    Orientation.fromString(matcher.group(3)),
                    matcher.group(1),
                    position.getNorthing(),
                    position.getEasting()
            );
        }
    };
}
