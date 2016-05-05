package io.vieira.adventuretime.game;

import io.vieira.adventuretime.game.elements.Adventurer;
import io.vieira.adventuretime.game.elements.Mountain;
import io.vieira.adventuretime.game.elements.Treasure;
import io.vieira.adventuretime.game.elements.WorldElement;
import io.vieira.adventuretime.game.helpers.MovementTryResult;
import io.vieira.adventuretime.game.routines.ElementsRepartitionAccessor;
import io.vieira.adventuretime.game.routines.MovementProvider;
import io.vieira.adventuretime.game.routines.PositionAccessor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * AdventureWorld class, wrapping all game routines.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class AdventureWorld implements PositionAccessor, ElementsRepartitionAccessor, MovementProvider {

    public static class Builder {
        private List<WorldElement> worldElements = new ArrayList<>();
        private int width = -1;
        private int height = -1;

        public Builder width(int width){
            this.width = width;
            return this;
        }

        public Builder height(int height){
            this.height = height;
            return this;
        }

        public Builder adventurer(Adventurer adventurer){
            if(this.worldElements.contains(adventurer)){
                throwCellAlreadyOccupiedInternal(adventurer);
            }
            this.worldElements.add(adventurer);
            return this;
        }

        public Builder treasure(Treasure treasure){
            if(this.worldElements.contains(treasure)){
                throwCellAlreadyOccupiedInternal(treasure);
            }
            this.worldElements.add(treasure);
            return this;
        }

        public Builder mountain(Mountain mountain){
            if(this.worldElements.contains(mountain)){
                throwCellAlreadyOccupiedInternal(mountain);
            }
            this.worldElements.add(mountain);
            return this;
        }

        private void throwCellAlreadyOccupiedInternal(WorldElement element){
            throw new IllegalStateException(
                    String.format(
                            "Unable to add '%s' as the cell is already occupied by '%s'",
                            element,
                            this.worldElements.get(this.worldElements.indexOf(element))
                    )
            );
        }

        public AdventureWorld build(){
            if(height <= 0){
                throw new IllegalArgumentException("AdventureWorld height must be positive");
            }
            if(width <= 0){
                throw new IllegalArgumentException("AdventureWorld width must be positive");
            }
            return new AdventureWorld(width, height, worldElements);
        }
    }

    private final WorldElement[][] worldElements;

    private final int height;

    private final int width;

    private final Map<UUID, Adventurer> adventurers = new ConcurrentHashMap<>();

    private AdventureWorld(int width, int height, List<WorldElement> worldElements){
        this.worldElements = IntStream
                .range(0, height)
                .mapToObj(currentHeight -> IntStream
                        .range(0, width)
                        .mapToObj(currentWidth -> worldElements
                                .stream()
                                .filter(mapElement -> mapElement.getPosition().getAbsoluteNorthing() == currentHeight)
                                .filter(mapElement -> mapElement.getPosition().getAbsoluteEasting() == currentWidth)
                                .filter(mapElement -> !(mapElement instanceof Adventurer))
                                .findFirst()
                                .orElseGet(() -> null)
                        )
                        .toArray(WorldElement[]::new)
                ).toArray(WorldElement[][]::new);
        this.height = height;
        this.width = width;
        adventurers.putAll(
                worldElements
                        .stream()
                        .filter(mapElement -> mapElement instanceof Adventurer)
                        .collect(
                                Collectors.toMap(
                                        worldElement -> ((Adventurer) worldElement).getAdventurerID(),
                                        worldElement -> (Adventurer) worldElement
                                )
                        )
        );
    }

    @Override
    public Stream<WorldElement> at(Position position) {
        if(position.getAbsoluteNorthing() >= height || position.getAbsoluteNorthing() < 0){
            throw new IndexOutOfBoundsException();
        }
        if(position.getAbsoluteEasting() >= width || position.getAbsoluteEasting() < 0){
            throw new IndexOutOfBoundsException();
        }
        return Stream.concat(
                adventurers
                        .values()
                        .stream()
                        .filter(adventurer -> adventurer.getPosition().equals(position)),
                Stream.of(
                        this.worldElements[position.getAbsoluteNorthing()][position.getAbsoluteEasting()]
                )
        );
    }

    private Adventurer getAdventurerInternal(UUID adventurerID){
        Adventurer adventurer = this.adventurers.getOrDefault(adventurerID, null);
        if(adventurer == null){
            throw new IllegalStateException(
                    String.format(
                            "Adventurer '%s' does not exist",
                            adventurerID
                    )
            );
        }
        return adventurer;
    }

    @Override
    public synchronized MovementTryResult tryMoving(UUID adventurerID, Direction direction) {
        Adventurer adventurer = getAdventurerInternal(adventurerID);
        Position newPosition = adventurer.getCurrentOrientation().move(direction).adjust(adventurer.getPosition());
        Orientation newOrientation = adventurer.getCurrentOrientation().deduce(direction);

        boolean isNewPositionInBounds = newPosition.getAbsoluteEasting() >= 0 && newPosition.getAbsoluteNorthing() >= 0;
        return new MovementTryResult(
                isNewPositionInBounds && !(at(newPosition).filter(worldElement -> worldElement instanceof Mountain).count() == 1),
                isNewPositionInBounds ? newOrientation : null,
                isNewPositionInBounds ? newPosition : null
        );
    }

    @Override
    public synchronized void move(UUID adventurerID, Direction direction) {
        MovementTryResult result = tryMoving(adventurerID, direction);
        if(result.isASuccess()){
            Position newPosition = result.getNewPosition();
            Adventurer adventurer = getAdventurerInternal(adventurerID);
            adventurer.updatePosition(newPosition);
            adventurer.setCurrentOrientation(result.getNewOrientation());

            Optional<WorldElement> optionalTreasure = at(newPosition).filter(worldElement -> worldElement instanceof Treasure).findFirst();
            if(optionalTreasure.isPresent() && ((Treasure) optionalTreasure.get()).getRemainingLoots() != 0){
                adventurer.setPickedUpTreasures(adventurer.getPickedUpTreasures() + 1);
                ((Treasure) this.worldElements[newPosition.getAbsoluteNorthing()][newPosition.getAbsoluteEasting()]).removeALoot();
            }
            adventurer.getPathHistory().add(direction);
            adventurers.put(adventurerID, adventurer);
        }
    }

    @Override
    public long getNumberOf(Class<? extends WorldElement> desiredType) {
        return Stream.concat(
                        Arrays.stream(this.worldElements).flatMap(Arrays::stream),
                        adventurers.values().stream()
                )
                .filter(worldElement -> worldElement != null && desiredType.isAssignableFrom(worldElement.getClass()))
                .count();
    }

    @Override
    public java.util.Map<Class<? extends WorldElement>, Long> getOccupation() {
        return Stream.concat(
                        Arrays.stream(this.worldElements).flatMap(Arrays::stream),
                        adventurers.values().stream()
                )
                .filter(worldElement -> worldElement != null)
                .collect(Collectors.groupingBy(
                        WorldElement::getClass,
                        Collectors.counting()
                ));
    }
}
