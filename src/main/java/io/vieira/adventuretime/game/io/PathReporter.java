package io.vieira.adventuretime.game.io;

import io.vieira.adventuretime.game.AdventureWorld;
import io.vieira.adventuretime.game.io.exception.GameReportingFailedException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * {@link java.nio.file.Path} based implementation of {@link AdventureReporter}.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class PathReporter implements AdventureReporter {

    private final Path outputPath;

    public PathReporter(Path outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public void report(AdventureWorld world) {
        try {
            ///TODO : codify all instructions
            Files.write(outputPath, new ArrayList<>(), Charset.defaultCharset());
        }
        catch (IOException e) {
            throw new GameReportingFailedException("Unable to report current game", e);
        }
    }
}
