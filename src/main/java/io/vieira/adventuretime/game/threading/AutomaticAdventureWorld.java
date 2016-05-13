package io.vieira.adventuretime.game.threading;

import io.vieira.adventuretime.game.AdventureWorld;
import io.vieira.adventuretime.game.Direction;
import io.vieira.adventuretime.game.Position;
import io.vieira.adventuretime.game.elements.Adventurer;
import io.vieira.adventuretime.game.elements.Mountain;
import io.vieira.adventuretime.game.elements.Treasure;
import io.vieira.adventuretime.game.elements.WorldElement;
import io.vieira.adventuretime.game.helpers.MovementTryResult;
import io.vieira.adventuretime.game.routines.AutomaticMovementProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Superclass of {@link AdventureWorld} providing automatic gameplay handling and multi-threading.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@Slf4j
public class AutomaticAdventureWorld extends AdventureWorld {

    public AutomaticAdventureWorld(AdventureWorld world) {
        super(world);
    }

    /**
     * Launches all adventurers into their journey !
     */
    public void launch() {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            executorService.invokeAll(
                    adventurers
                            .entrySet()
                            .stream()
                            .map(adventurerEntry -> new AdventurerMover(adventurerEntry.getKey()))
                            .collect(Collectors.toList())
            );
            this.end();
            //Requesting shutdown to exit right after all Futures exited
            executorService.shutdown();
        }
        catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    @Override
    public void move(String adventurerName, Direction direction) {
        throw new UnsupportedOperationException("Manual move not available using automatic game mode.");
    }

    /**
     * Inner class exposing the underlying {@link AdventureWorld} while providing asynchronous {@link AdventureWorld} handling
     * through {@link Callable} and {@link AutomaticMovementProvider}.
     *
     * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
     */
    private class AdventurerMover implements AutomaticMovementProvider {

        private final String adventurerName;
        private boolean canMove = true;

        private AdventurerMover(String adventurerName) {
            this.adventurerName = adventurerName;
        }

        @Override
        public Void call() throws Exception {
            while (canMove) {
                canMove = move(adventurerName);
            }
            return null;
        }

        @Override
        public boolean move(String adventurerName) throws InterruptedException {
            Adventurer targetAdventurer = getAdventurerInternal(adventurerName);
            Direction nextPlannedDirection = targetAdventurer.getRemainingPath().peek();
            Position currentPosition = targetAdventurer.getPosition();
            log.info("Adventurer {} trying to move {} from {}", adventurerName, nextPlannedDirection, currentPosition);

            MovementTryResult tryResult = tryMoving(adventurerName, nextPlannedDirection);
            if (tryResult.isASuccess()) {
                Position newPosition = tryResult.getNewPosition();
                Adventurer adventurer = getAdventurerInternal(adventurerName);

                adventurer.updatePosition(newPosition);
                adventurer.setCurrentOrientation(tryResult.getNewOrientation());

                Optional<WorldElement> optionalTreasure = at(newPosition).filter(worldElement -> worldElement instanceof Treasure).findFirst();
                if (optionalTreasure.isPresent() && ((Treasure) optionalTreasure.get()).getRemainingLoots() != 0) {
                    adventurer.setPickedUpTreasures(adventurer.getPickedUpTreasures() + 1);
                    ((Treasure) AutomaticAdventureWorld.this.worldElements[newPosition.getAbsoluteNorthing()][newPosition.getAbsoluteEasting()]).removeALoot();
                    log.info("Adventurer {} found a treasure !", adventurerName, nextPlannedDirection);
                    Thread.sleep(1000);
                }

                adventurer.markMove(nextPlannedDirection);
                Thread.sleep(1000);
                adventurers.put(adventurerName, adventurer);
                log.info("Adventurer {} has moved {} to {}", adventurerName, nextPlannedDirection, newPosition);

                //Notify all threads to wake up paused ones
                synchronized (adventurers.get(adventurerName)){
                    log.info("Notifying all possibly awaiting adventurers to take {}'s cell", adventurerName);
                    adventurers.get(adventurerName).notifyAll();
                }
            } else if (tryResult.getElementAtNewPosition() == null || tryResult.getElementAtNewPosition() instanceof Mountain) {
                //Skipping instruction straight away
                targetAdventurer.getRemainingPath().poll();
                log.warn("Adventurer {} can't move, skipping {} instruction", adventurerName, nextPlannedDirection);
                adventurers.put(adventurerName, targetAdventurer);
            } else if (tryResult.getElementAtNewPosition() instanceof Adventurer) {
                String occupyingAdventurerName = ((Adventurer) tryResult.getElementAtNewPosition()).getAdventurerName();
                log.info("Adventurer {} can not move yet as the next position is taken by {}. Waiting...", adventurerName, occupyingAdventurerName);

                //Wait until the cell is freed
                synchronized (adventurers.get(occupyingAdventurerName)){
                    adventurers.get(occupyingAdventurerName).wait();
                }
            }
            return !targetAdventurer.getRemainingPath().isEmpty();
        }
    }
}
