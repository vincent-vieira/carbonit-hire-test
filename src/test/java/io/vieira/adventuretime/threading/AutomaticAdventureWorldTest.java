package io.vieira.adventuretime.threading;

import io.vieira.adventuretime.game.AdventureWorld;
import io.vieira.adventuretime.game.Direction;
import io.vieira.adventuretime.game.Orientation;
import io.vieira.adventuretime.game.Position;
import io.vieira.adventuretime.game.elements.Adventurer;
import io.vieira.adventuretime.game.io.read.AdventureGameFileLoader;
import io.vieira.adventuretime.game.threading.AutomaticAdventureWorld;
import io.vieira.adventuretime.io.BaseGameFileLoaderTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Unit test checking all automated routines.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class AutomaticAdventureWorldTest extends BaseGameFileLoaderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testAutomaticWorldWithNonCrossingPaths() throws URISyntaxException {
        AutomaticAdventureWorld automaticAdventureWorld = new AdventureGameFileLoader
                .Builder()
                .path(getResource("/noncrossing/gamefile.txt"))
                .build()
                .automatic();

        automaticAdventureWorld.launch();
        await().atMost(10, TimeUnit.SECONDS).until(() ->
                automaticAdventureWorld.at(new Position(6, 4)).filter(worldElement -> worldElement instanceof Adventurer).count() == 1
                && automaticAdventureWorld.at(new Position(6, 3)).filter(worldElement -> worldElement instanceof Adventurer).count() == 1
        );
    }

    @Test
    public void testAutomaticWorldWithCrossingPaths() throws URISyntaxException {
        AutomaticAdventureWorld automaticAdventureWorld = new AdventureGameFileLoader
                .Builder()
                .path(getResource("/crossing/gamefile.txt"))
                .build()
                .automatic();

        automaticAdventureWorld.launch();
        await().atMost(10, TimeUnit.SECONDS).until(() ->
                automaticAdventureWorld.at(new Position(3, 6)).filter(worldElement -> worldElement instanceof Adventurer).count() == 1
                && automaticAdventureWorld.at(new Position(6, 3)).filter(worldElement -> worldElement instanceof Adventurer).count() == 1
        );
    }

    @Test
    public void testAutomaticWorldWithNonCrossingPathsAndTreasuresAndMountains() throws URISyntaxException {
        AutomaticAdventureWorld automaticAdventureWorld = new AdventureGameFileLoader
                .Builder()
                .path(getResource("/noncrossing/full/gamefile.txt"))
                .build()
                .automatic();
        automaticAdventureWorld.launch();
        await().atMost(10, TimeUnit.SECONDS).until(() ->
                automaticAdventureWorld.at(new Position(2, 4)).filter(worldElement -> worldElement instanceof Adventurer).count() == 1
                && automaticAdventureWorld.at(new Position(6, 3)).filter(worldElement -> worldElement instanceof Adventurer).count() == 1
        );
    }

    @Test
    public void testAutomaticWorldWithCrossingPathsAndTreasuresAndMountains() throws URISyntaxException {
        AutomaticAdventureWorld automaticAdventureWorld = new AdventureGameFileLoader
                .Builder()
                .path(getResource("/crossing/full/gamefile.txt"))
                .build()
                .automatic();
        automaticAdventureWorld.launch();
        await().atMost(10, TimeUnit.SECONDS).until(() ->
                automaticAdventureWorld.at(new Position(3, 4)).filter(worldElement -> worldElement instanceof Adventurer && ((Adventurer) worldElement).getPickedUpTreasures() == 1).count() == 1
                && automaticAdventureWorld.at(new Position(6, 3)).filter(worldElement -> worldElement instanceof Adventurer && ((Adventurer) worldElement).getPickedUpTreasures() == 1).count() == 1
        );
    }

    @Test
    public void testManualMoveOnAutomaticWorld(){
        expectedException.expect(UnsupportedOperationException.class);
        expectedException.expectMessage("Manual move not available using automatic game mode.");

        new AdventureWorld
                .Builder()
                .height(6)
                .width(6)
                .adventurer(new Adventurer(Orientation.NORTH, "José", 3, 1))
                .build()
                .automatic()
                .move("José", Direction.FORWARD);
    }


    @Override
    protected String getTestType() {
        return "automatic";
    }
}
