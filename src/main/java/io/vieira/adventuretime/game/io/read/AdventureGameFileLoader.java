package io.vieira.adventuretime.game.io.read;

import com.mscharhag.et.ET;
import com.mscharhag.et.ExceptionTranslator;
import io.vieira.adventuretime.game.AdventureWorld;
import io.vieira.adventuretime.game.elements.Adventurer;
import io.vieira.adventuretime.game.elements.Mountain;
import io.vieira.adventuretime.game.elements.Treasure;
import io.vieira.adventuretime.game.helpers.WorldSize;
import io.vieira.adventuretime.game.io.write.AdventureReporter;
import io.vieira.adventuretime.game.io.write.PathReporter;
import io.vieira.adventuretime.game.io.exception.GameInstructionParsingException;
import io.vieira.adventuretime.game.io.exception.MissingGameParameterException;
import io.vieira.adventuretime.game.io.exception.UnreadableGameFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Game file loader. Supports single or multiple file mode.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class AdventureGameFileLoader {

    public static class Builder {
        private final Set<Path> pathsToRead = new HashSet<>();
        private Path outputPath;

        /**
         * Add the supplied {@link Path} to the Builder.
         *
         * @param toRead the {@link Path} to add. Note that if a path is invalid (not a file),
         * an {@link IllegalArgumentException} will be thrown.
         * @return the builder instance
         */
        public Builder path(Path toRead){
            if(Files.isDirectory(toRead)){
                throw new IllegalArgumentException("Supplied Path instance must be a file");
            }
            this.pathsToRead.add(toRead);
            return this;
        }

        /**
         * Add the supplied {@link Path}s to the Builder.
         *
         * @param toRead a vararg containing the different {@link Path}s. Note that if a path is invalid (not a file),
         * it won't be added and silently discarded.
         * @return the builder instance
         */
        public Builder paths(Path... toRead){
            this.pathsToRead.addAll(
                    Arrays.stream(toRead)
                            .filter(path -> !Files.isDirectory(path))
                            .collect(Collectors.toList())
            );
            return this;
        }

        /**
         * Specifies the output file where the game report is gonna be written.
         *
         * @param outputPath the output {@link Path}. Note that if a path is invalid (not a file),
         * an {@link IllegalArgumentException} will be thrown.
         * @return the builder instance
         */
        public Builder writeTo(Path outputPath){
            if(Files.isDirectory(outputPath)){
                throw new IllegalArgumentException("Supplied Path instance must be a file");
            }
            this.outputPath = outputPath;
            return this;
        }

        public AdventureWorld build(){
            return new AdventureGameFileLoader(pathsToRead, outputPath).world;
        }
    }

    private final AdventureWorld world;

    private AdventureGameFileLoader(Set<Path> pathsToRead, Path outputPath) {
        ExceptionTranslator exceptionTranslator = ET
                .newConfiguration()
                .translate(IOException.class)
                .to(UnreadableGameFile.class)
                .translate(GameInstructionParsingException.class)
                .to(UnreadableGameFile.class)
                .done();

        GameInstructionParser parser = new AdventureGameInstructionParser(
                GameInstruction.ADVENTURE_WORLD,
                GameInstruction.MOUNTAIN,
                GameInstruction.TREASURE,
                GameInstruction.ADVENTURER
        );

        List<Object> declaredGameObjects = pathsToRead
                .stream()
                .flatMap(path -> exceptionTranslator.withReturningTranslation(() -> Files.readAllLines(path).stream()))
                .map(line -> exceptionTranslator.withReturningTranslation(() -> parser.fromLine(line)))
                .collect(Collectors.toList());

        Adventurer[] adventurers = declaredGameObjects.stream().filter(o -> o instanceof Adventurer).toArray(Adventurer[]::new);
        if(adventurers.length == 0){
            throw new MissingGameParameterException("At least one adventurer must be supplied.");
        }

        this.world = new AdventureWorld.Builder()
                .size((WorldSize) declaredGameObjects
                        .stream()
                        .filter(o -> o instanceof WorldSize)
                        .findFirst()
                        .orElseThrow(() -> new MissingGameParameterException("The world size is missing."))
                )
                .reporter(outputPath == null ? new AdventureReporter.LoggingReporter() : new PathReporter(outputPath))
                .adventurers(adventurers)
                .mountains(declaredGameObjects.stream().filter(o -> o instanceof Mountain).toArray(Mountain[]::new))
                .treasures(declaredGameObjects.stream().filter(o -> o instanceof Treasure).toArray(Treasure[]::new))
                .build();
    }
}

