package io.vieira.adventuretime.io;

import io.vieira.adventuretime.game.AdventureWorld;
import io.vieira.adventuretime.game.Position;
import io.vieira.adventuretime.game.elements.Mountain;
import io.vieira.adventuretime.game.elements.WorldElement;
import io.vieira.adventuretime.game.io.AdventureGameFileLoader;
import io.vieira.adventuretime.game.io.exception.MissingGameParameterException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

/**
 * Unit test of game loading through multiple files.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class MultipleAdventureGameFileLoaderTest extends BaseGameFileLoaderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testGameInitializationWithoutAdventurers() throws Exception {
        expectedException.expect(MissingGameParameterException.class);
        expectedException.expectMessage("At least one adventurer must be supplied.");

        new AdventureGameFileLoader
                .Builder()
                .paths(getResource("/invalid/noadventurer/gamefile1.txt"), getResource("/invalid/noadventurer/gamefile2.txt"))
                .build();
    }

    @Test
    public void testGameInitializationWithoutGameSize() throws Exception {
        expectedException.expect(MissingGameParameterException.class);
        expectedException.expectMessage("The world size is missing.");

        new AdventureGameFileLoader.Builder()
                .paths(getResource("/invalid/nosize/gamefile1.txt"), getResource("/invalid/nosize/gamefile2.txt"))
                .build();
    }

    @Test
    public void testRegularGameInitialization() throws Exception {
        sharedAssertions(
                new AdventureGameFileLoader.Builder()
                        .paths(getResource("/partial/gamefile1.txt"), getResource("/partial/gamefile2.txt"))
                        .build()
        );
    }

    @Test
    public void testFullGameInitialization() throws Exception {
        AdventureWorld world = new AdventureGameFileLoader.Builder()
                .paths(getResource("/full/gamefile1.txt"), getResource("/full/gamefile2.txt"))
                .build();
        sharedAssertions(world);

        Optional<WorldElement> optionalMountain = world.at(new Position(3, 5)).filter(worldElement -> worldElement instanceof Mountain).findFirst();
        Assert.assertTrue(
                "An item must be present at coordinates 5,3",
                optionalMountain.isPresent()
        );
        Assert.assertTrue(
                "A mountain must be present at coordinates 5,3",
                optionalMountain.get() instanceof Mountain
        );
    }

    @Test
    public void testFileWithBadlyFormattedLines() throws Exception {
        AdventureWorld world = new AdventureGameFileLoader.Builder()
                .paths(getResource("/witherrors/gamefile1.txt"), getResource("/witherrors/gamefile2.txt"))
                .build();
        sharedAssertions(world);

        Assert.assertEquals(
                "There must be nothing at cell 4,1",
                0,
                world.at(new Position(1, 4)).count()
        );
    }


    @Override
    protected String getTestType() {
        return "multiple";
    }
}
