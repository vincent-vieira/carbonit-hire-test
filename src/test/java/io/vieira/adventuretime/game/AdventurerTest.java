package io.vieira.adventuretime.game;

import io.vieira.adventuretime.game.elements.Adventurer;
import io.vieira.adventuretime.game.elements.Mountain;
import io.vieira.adventuretime.game.elements.Treasure;
import io.vieira.adventuretime.game.elements.WorldElement;
import io.vieira.adventuretime.game.helpers.MovementTryResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Test of adventurer moving behaviors.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class AdventurerTest {

    private Adventurer targetAdventurer = new Adventurer(Orientation.SOUTH, 1, 1);

    private AdventureWorld currentMap;

    @Before
    public void initializeMap(){
        currentMap = new AdventureWorld.Builder()
                .adventurer(targetAdventurer)
                .mountain(new Mountain(1, 2))
                .treasure(new Treasure(3, 1, 3))
                .height(4)
                .width(4)
                .build();
    }

    @Test
    public void testAdventurerAllocation(){
        Assert.assertEquals(
                "Adventurer can not be found",
                1,
                currentMap.at(targetAdventurer.getPosition()).filter(worldElement -> worldElement instanceof Adventurer).count()
        );
    }

    @Test
    public void testMapElementAccessAtRootIndex(){
        Assert.assertEquals(
                "Element at position 2,0 (array coordinates) must be a treasure element",
                1,
                currentMap.at(new Position(3, 1)).filter(worldElement -> worldElement instanceof Treasure).count()
        );
    }

    @Test
    public void testAdventurerMovementOnMountain(){
        Assert.assertTrue(
                "tryMoving() must return false",
                !currentMap.tryMoving(targetAdventurer.getAdventurerID(), Direction.LEFT).isASuccess()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void testNonExistentAdventurerMovement(){
        currentMap.tryMoving(UUID.randomUUID(), Direction.FORWARD);
    }

    @Test
    public void testOutOfBoundsAdventurerMovement(){
        MovementTryResult result = currentMap.tryMoving(targetAdventurer.getAdventurerID(), Direction.RIGHT);
        Assert.assertTrue(
                "tryMoving() must return false when going out of bounds",
                !result.isASuccess()
        );
        Assert.assertEquals(
                "tryMoving() when going out of bounds must return null",
                result.getNewPosition(),
                null
        );

    }

    @Test
    public void testAdventurerMovementsWithTreasurePickup(){
        currentMap.move(targetAdventurer.getAdventurerID(), Direction.FORWARD);
        currentMap.move(targetAdventurer.getAdventurerID(), Direction.FORWARD);
        currentMap.move(targetAdventurer.getAdventurerID(), Direction.LEFT);
        currentMap.move(targetAdventurer.getAdventurerID(), Direction.FORWARD);
        Assert.assertEquals(
                "Adventurer not found at expected northing and easting",
                1,
                currentMap.at(new Position(3, 3)).filter(worldElement -> worldElement instanceof Adventurer).count()
        );
        Assert.assertEquals(
                "The adventurer must have picked a treasure",
                1,
                ((Adventurer) currentMap.at(new Position(3, 3)).filter(worldElement -> worldElement instanceof Adventurer).findFirst().orElse(null)).getPickedUpTreasures()
        );
        Assert.assertEquals(
                "The treasures number of the cell must have been decremented",
                2,
                ((Treasure) currentMap.at(new Position(3, 1)).filter(worldElement -> worldElement instanceof Treasure).findFirst().orElse(null)).getRemainingLoots()
        );
    }

    @Test
    public void testAdventurerMovementsWithMountain(){
        currentMap.move(targetAdventurer.getAdventurerID(), Direction.LEFT);
        currentMap.move(targetAdventurer.getAdventurerID(), Direction.FORWARD);
        currentMap.move(targetAdventurer.getAdventurerID(), Direction.FORWARD);
        Assert.assertEquals(
                "Adventurer not found at expected northing and easting",
                1,
                currentMap.at(new Position(3, 1)).filter(worldElement -> worldElement instanceof Adventurer).count()
        );
    }

    @Test
    public void testAdventurerMovementsAndFinaleOnMultiElementCell(){
        currentMap.move(targetAdventurer.getAdventurerID(), Direction.LEFT);
        currentMap.move(targetAdventurer.getAdventurerID(), Direction.FORWARD);
        currentMap.move(targetAdventurer.getAdventurerID(), Direction.FORWARD);
        Assert.assertEquals(
                "Two type of elements must be present on 3,1 cell",
                2,
                currentMap.at(new Position(3, 1)).count()
        );

        Map<Class<? extends WorldElement>, Long> cellOccupation = new HashMap<Class<? extends WorldElement>, Long>(){{
            put(Adventurer.class, 1L);
            put(Treasure.class, 1L);
        }};
        Assert.assertEquals(
                "There must be an adventurer and a treasure on 3,1 cell",
                cellOccupation,
                currentMap.at(new Position(3, 1)).collect(Collectors.groupingBy(WorldElement::getClass, Collectors.counting()))
        );
        Assert.assertEquals(
                "The treasures number of the cell must have been decremented",
                2,
                ((Treasure) currentMap.at(new Position(3, 1)).filter(worldElement -> worldElement instanceof Treasure).findFirst().orElse(null)).getRemainingLoots()
        );
    }
}
