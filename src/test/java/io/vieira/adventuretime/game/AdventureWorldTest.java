package io.vieira.adventuretime.game;

import io.vieira.adventuretime.game.elements.WorldElement;
import io.vieira.adventuretime.game.elements.Adventurer;
import io.vieira.adventuretime.game.elements.Mountain;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.HashMap;

/**
 * Unit test about map instantiation and routines handling around it.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class AdventureWorldTest {

    @Test
    public void testMapInitialization(){
        AdventureWorld toCheck = new AdventureWorld
                .Builder()
                .width(8)
                .height(8)
                .adventurer(new Adventurer(1, 1))
                .build();

        Assert.assertEquals(
                "First cell must be an Adventurer",
                1,
                toCheck.at(new Position(1, 1)).filter(worldElement -> worldElement instanceof Adventurer).count()
        );
        Assert.assertEquals("The occupation is invalid", new HashMap<Class<? extends WorldElement>, Long>(){{
            put(Adventurer.class, 1L);
        }}, toCheck.getOccupation());
        Assert.assertEquals("The fetched count is invalid", 1, toCheck.getNumberOf(Adventurer.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapInitializationWithInvalidWidth(){
        new AdventureWorld.Builder()
                .width(-1)
                .height(8)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapInitializationWithInvalidHeight(){
        new AdventureWorld.Builder()
                .width(8)
                .height(-1)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapInitializationWithoutSetBounds(){
        new AdventureWorld.Builder().build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapInitializationWithInvalidRelativeCoordinates(){
        new AdventureWorld
                .Builder()
                .width(8)
                .height(8)
                .adventurer(new Adventurer(0, 0))
                .build();
    }

    @Test
    public void testEmptyMapInitialization(){
        AdventureWorld toCheck = new AdventureWorld
                .Builder()
                .width(8)
                .height(8)
                .build();
        Assert.assertEquals("The occupation is invalid", new HashMap<Class<? extends WorldElement>, Long>(), toCheck.getOccupation());
    }

    @Test(expected = IllegalStateException.class)
    public void testMapInitializationUsingOverlappingElements(){
        new AdventureWorld.Builder()
                .width(8)
                .height(8)
                .adventurer(new Adventurer(1, 1))
                .mountain(new Mountain(1, 1))
                .build();
    }
}
