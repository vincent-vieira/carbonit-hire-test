package io.vieira.adventuretime.game.io.write;

import io.vieira.adventuretime.game.io.exception.GameReportingFailedException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public void report(Stream<Savable> savableStream) {
        try {
            Files.write(
                    outputPath,
                    savableStream.
                            map(Savable::getSavableRepresentation)
                            .collect(Collectors.toList()),
                    Charset.defaultCharset()
            );
        }
        catch (IOException e) {
            throw new GameReportingFailedException("Unable to report current game", e);
        }
    }
}
