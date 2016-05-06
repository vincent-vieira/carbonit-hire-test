package io.vieira.adventuretime.io;

import io.vieira.adventuretime.game.AdventureWorld;
import io.vieira.adventuretime.game.Position;
import io.vieira.adventuretime.game.elements.Treasure;
import io.vieira.adventuretime.game.elements.WorldElement;
import io.vieira.adventuretime.game.helpers.WorldSize;
import org.junit.Assert;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Base unit test routines for game file loading.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
abstract class BaseGameFileLoaderTest {

    protected abstract String getTestType();

    /**
     * Fetches a resource from the classpath, appending automatically the defined test type through {@link BaseGameFileLoaderTest#getTestType()}.
     *
     * @param path the classpath-relative path of the file
     * @return a {@link Path} object representing the file
     * @throws URISyntaxException if a malformatted path is supplied.
     */
    Path getResource(String path) throws URISyntaxException {
        return Paths.get(this.getClass().getClassLoader().getResource(getTestType()+path).toURI());
    }

    /**
     * Shared assertions to do on the supplied {@link AdventureWorld}.
     *
     * @param world the world to test on
     */
    void sharedAssertions(AdventureWorld world){
        Assert.assertEquals(
                "Map must be 6x6",
                world.getSize(),
                new WorldSize(6, 6)
        );

        Optional<WorldElement> optionalTreasure = world.at(new Position(2, 4)).filter(worldElement -> worldElement instanceof Treasure).findFirst();
        Assert.assertTrue(
                "An item must be present at position 4,2",
                optionalTreasure.isPresent()
        );
        Assert.assertTrue(
                "Item at position 4,2 must be a treasure",
                optionalTreasure.get() instanceof Treasure
        );
        Assert.assertEquals(
                "Treasure at position 4,2 must be with one loot remaining",
                1,
                ((Treasure) optionalTreasure.get()).getRemainingLoots()
        );

        optionalTreasure = world.at(new Position(4, 1)).filter(worldElement -> worldElement instanceof Treasure).findFirst();
        Assert.assertTrue(
                "An item must be present at position 1,4",
                optionalTreasure.isPresent()
        );
        Assert.assertTrue(
                "Item at position 1,4 must be a treasure",
                optionalTreasure.get() instanceof Treasure
        );
        Assert.assertEquals(
                "Treasure at position 1,4 must be with three loots remaining",
                3,
                ((Treasure) optionalTreasure.get()).getRemainingLoots()
        );
    }
}
