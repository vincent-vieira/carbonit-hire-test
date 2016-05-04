package io.vieira.adventuretime.game;

import io.vieira.adventuretime.game.elements.MapElement;
import io.vieira.adventuretime.game.elements.Adventurer;
import io.vieira.adventuretime.game.elements.Grassland;
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
public class MapTest {

    @Test
    public void testMapInitialization(){
        Map toCheck = new Map
                .Builder()
                .width(8)
                .height(8)
                .adventurer(new Adventurer(0, 0))
                .build();

        Assert.assertTrue("First cell must be an Adventurer", toCheck.at(1, 1) instanceof Adventurer);
        Assert.assertEquals("All other cells must be Grasslands", 63, toCheck.getNumberOf(Grassland.class));
        Assert.assertEquals("The occupation is invalid", new HashMap<Class<? extends MapElement>, Long>(){{
            put(Adventurer.class, 1L);
            put(Grassland.class, 63L);
        }}, toCheck.getOccupation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapInitializationWithInvalidWidth(){
        new Map.Builder()
                .width(-1)
                .height(8)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapInitializationWithInvalidHeight(){
        new Map.Builder()
                .width(8)
                .height(-1)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapInitializationWithoutSetBounds(){
        new Map.Builder().build();
    }

    @Test
    public void testEmptyMapInitialization(){
        Map toCheck = new Map
                .Builder()
                .width(8)
                .height(8)
                .build();
        Assert.assertEquals("All cells must be Grasslands", 64, toCheck.getNumberOf(Grassland.class));
        Assert.assertEquals("The occupation is invalid", new HashMap<Class<? extends MapElement>, Long>(){{
            put(Grassland.class, 64L);
        }}, toCheck.getOccupation());
    }

    @Test(expected = IllegalStateException.class)
    public void testMapInitializationUsingOverlappingElements(){
        new Map.Builder()
                .width(8)
                .height(8)
                .adventurer(new Adventurer(1, 1))
                .mountain(new Mountain(1, 1))
                .build();
    }
}
