package io.vieira.adventuretime.game;

import io.vieira.adventuretime.game.elements.Adventurer;
import io.vieira.adventuretime.game.elements.Mountain;
import io.vieira.adventuretime.game.elements.WorldElement;
import io.vieira.adventuretime.game.helpers.WorldSize;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;

/**
 * Unit test about map instantiation and routines handling around it.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class AdventureWorldTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testMapInitialization(){
        AdventureWorld toCheck = new AdventureWorld
                .Builder()
                .width(8)
                .height(8)
                .adventurer(new Adventurer(Orientation.NORTH, "John", 1, 1))
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

    @Test
    public void testMapInitializationWithWorldSizeClass(){
        AdventureWorld world = new AdventureWorld.Builder().size(new WorldSize(8, 8)).build();
        Assert.assertEquals(
                "Map must be 8x8",
                world.getSize(),
                new WorldSize(8, 8)
        );
    }

    @Test
    public void testMapInitializationWithNullWorldSize(){
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("A valid size must be supplied");
        new AdventureWorld.Builder().size(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapInitializationWithInvalidRelativeCoordinates(){
        new AdventureWorld
                .Builder()
                .width(8)
                .height(8)
                .adventurer(new Adventurer(Orientation.NORTH, "John", 0, 0))
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
                .adventurer(new Adventurer(Orientation.NORTH, "John", 1, 1))
                .mountain(new Mountain(1, 1))
                .build();
    }

    @Test
    public void testMapInitializationUsingDuplicateAdventurerNames(){
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(new TypeSafeMatcher<String>() {
            @Override
            public void describeTo(Description description) {
                description.appendValue("Adventurer '...' already exists");
            }

            @Override
            protected boolean matchesSafely(String s) {
                return s.matches("Adventurer '[\\w\\s]+' already exists");
            }
        });
        new AdventureWorld.Builder()
                .width(8)
                .height(8)
                .adventurer(new Adventurer(Orientation.NORTH, "John", 1, 1))
                .adventurer(new Adventurer(Orientation.NORTH, "John", 1, 2));
    }

    @Test
    public void testMapInitializationUsingDuplicateAdventurerNamesWithDifferentCasing(){
        AdventureWorld toCheck = new AdventureWorld.Builder()
                .width(8)
                .height(8)
                .adventurer(new Adventurer(Orientation.NORTH, "John", 1, 1))
                .adventurer(new Adventurer(Orientation.NORTH, "john", 1, 2))
                .build();
        Assert.assertEquals(
                "Adventurer at 1,1 must be John",
                1,
                toCheck.at(new Position(1, 1))
                        .filter(worldElement -> worldElement instanceof Adventurer)
                        .filter(worldElement -> ((Adventurer) worldElement).getAdventurerName().equals("John"))
                        .count()
        );
        Assert.assertEquals(
                "Adventurer at 2,1 must be John",
                1,
                toCheck.at(new Position(1, 2))
                        .filter(worldElement -> worldElement instanceof Adventurer)
                        .filter(worldElement -> ((Adventurer) worldElement).getAdventurerName().equals("john"))
                        .count()
        );
    }
}
