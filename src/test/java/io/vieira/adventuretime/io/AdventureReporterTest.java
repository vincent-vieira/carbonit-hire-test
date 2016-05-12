package io.vieira.adventuretime.io;

import io.vieira.adventuretime.game.AdventureWorld;
import io.vieira.adventuretime.game.Direction;
import io.vieira.adventuretime.game.Orientation;
import io.vieira.adventuretime.game.elements.Adventurer;
import io.vieira.adventuretime.game.elements.Mountain;
import io.vieira.adventuretime.game.io.read.GameInstruction;
import io.vieira.adventuretime.game.io.write.AdventureReporter;
import io.vieira.adventuretime.game.io.write.Savable;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Unit test for writing files. Writing is mocked to easily make assertions on output.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class AdventureReporterTest {

    private AdventureWorld currentWorld;

    private AssertingReporter reporter = new AssertingReporter() {
        @Override
        void doAssertions(List<String> collectedSavables) {
            Assert.assertEquals(
                    "Savable elements must be 3",
                    3,
                    collectedSavables.size()
            );
            Assert.assertEquals(
                    "The map size must be present",
                    1,
                    collectedSavables.stream().filter(s -> s.startsWith("C")).count()
            );
            Assert.assertTrue(
                    "The map size must be a valid instruction",
                    GameInstruction.ADVENTURE_WORLD.instructionPattern().matcher(
                            collectedSavables.stream().filter(s -> s.startsWith("C")).findFirst().orElseGet(() -> "")
                    ).matches()
            );
            Assert.assertEquals(
                    "A Mountain must be present",
                    1,
                    collectedSavables.stream().filter(s -> s.startsWith("M")).count()
            );
            Assert.assertTrue(
                    "The Mountain must be a valid instruction",
                    GameInstruction.MOUNTAIN.instructionPattern().matcher(
                            collectedSavables.stream().filter(s -> s.startsWith("M")).findFirst().orElseGet(() -> "")
                    ).matches()
            );
            Assert.assertEquals(
                    "The adventurer must be present",
                    1,
                    collectedSavables.stream().filter(s -> s.startsWith("John")).count()
            );
            Assert.assertTrue(
                    "The adventurer must be a valid instruction",
                    GameInstruction.ADVENTURER.instructionPattern().matcher(
                            collectedSavables.stream().filter(s -> s.startsWith("John")).findFirst().orElseGet(() -> "")
                    ).matches()
            );
        }
    };

    @Getter
    private abstract class AssertingReporter implements AdventureReporter {
        private boolean endOfReporter = false;

        @Override
        public final void report(Stream<Savable> savableStream) {
            doAssertions(savableStream.map(Savable::getSavableRepresentation).collect(Collectors.toList()));
            endOfReporter = true;
        }

        abstract void doAssertions(List<String> collectedSavables);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testAdventureWorldReportWithoutAnyMovement(){
        currentWorld = new AdventureWorld.Builder()
                .width(8)
                .height(6)
                .adventurer(new Adventurer(Orientation.NORTH, "John", 3, 5))
                .mountain(new Mountain(4, 6))
                .reporter(reporter)
                .build();
        currentWorld.end();
        await().atMost(1, TimeUnit.SECONDS).until(() -> reporter.isEndOfReporter());
    }

    @Test
    public void testAdventureWorldReportWithMovement(){
        currentWorld = new AdventureWorld.Builder()
                .width(8)
                .height(6)
                .adventurer(new Adventurer(Orientation.NORTH, "John", 3, 5))
                .mountain(new Mountain(4, 6))
                .reporter(reporter)
                .build();
        currentWorld.move("John", Direction.FORWARD);
        currentWorld.move("John", Direction.FORWARD);
        currentWorld.move("John", Direction.LEFT);
        currentWorld.end();
        await().atMost(1, TimeUnit.SECONDS).until(() -> reporter.isEndOfReporter());
    }

    @Test
    public void testNullAdventurerReport(){
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("A valid reporter must be supplied");
        new AdventureWorld.Builder()
                .height(6)
                .width(6)
                .reporter(null)
                .build();
    }
}
