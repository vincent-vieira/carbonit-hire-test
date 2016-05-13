package io.vieira.adventuretime;

import io.vieira.adventuretime.game.io.read.AdventureGameFileLoader;

import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * Application entrypoint.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class Application {

    public static void main(String[] args) throws URISyntaxException {
        new AdventureGameFileLoader
                .Builder()
                .path(Paths.get(Application.class.getClassLoader().getResource("sample_file.txt").toURI()))
                .build()
                .automatic()
                .launch();
    }
}
