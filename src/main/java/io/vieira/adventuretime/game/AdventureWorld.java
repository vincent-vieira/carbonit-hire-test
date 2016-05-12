package io.vieira.adventuretime.game;

import io.vieira.adventuretime.game.elements.Adventurer;
import io.vieira.adventuretime.game.elements.Mountain;
import io.vieira.adventuretime.game.elements.Treasure;
import io.vieira.adventuretime.game.elements.WorldElement;
import io.vieira.adventuretime.game.helpers.MovementTryResult;
import io.vieira.adventuretime.game.helpers.WorldSize;
import io.vieira.adventuretime.game.io.write.AdventureReporter;
import io.vieira.adventuretime.game.routines.ElementsRepartitionAccessor;
import io.vieira.adventuretime.game.routines.MovementProvider;
import io.vieira.adventuretime.game.routines.PositionAccessor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
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
        private final List<WorldElement> worldElements = new ArrayList<>();
        private final List<String> registeredAdventurers = new ArrayList<>();
        private int width = -1;
        private int height = -1;
        private AdventureReporter reporter = new AdventureReporter.NoOpReporter();

        public Builder width(int width){
            this.width = width;
            return this;
        }

        public Builder height(int height){
            this.height = height;
            return this;
        }

        public Builder size(WorldSize size){
            Objects.requireNonNull(size, "A valid size must be supplied");
            this.height = size.getHeight();
            this.width = size.getWidth();
            return this;
        }

        public Builder adventurer(Adventurer adventurer){
            Objects.requireNonNull(adventurer, "A valid adventurer must be supplied");
            if(registeredAdventurers.contains(adventurer.getAdventurerName())){
                throw new IllegalArgumentException(
                        String.format(
                                "Adventurer '%s' already exists",
                                adventurer.getAdventurerName()
                        )
                );
            }
            if(this.worldElements.contains(adventurer)){
                throwCellAlreadyOccupiedInternal(adventurer);
            }
            registeredAdventurers.add(adventurer.getAdventurerName());
            this.worldElements.add(adventurer);
            return this;
        }

        public Builder adventurers(Adventurer... adventurers){
            Arrays.stream(adventurers).forEach(this::adventurer);
            return this;
        }

        public Builder treasure(Treasure treasure){
            Objects.requireNonNull(treasure, "A valid treasure must be supplied");
            if(this.worldElements.contains(treasure)){
                throwCellAlreadyOccupiedInternal(treasure);
            }
            this.worldElements.add(treasure);
            return this;
        }

        public Builder treasures(Treasure... treasures){
            Arrays.stream(treasures).forEach(this::treasure);
            return this;
        }

        public Builder mountain(Mountain mountain){
            Objects.requireNonNull(mountain, "A valid mountain must be supplied");
            if(this.worldElements.contains(mountain)){
                throwCellAlreadyOccupiedInternal(mountain);
            }
            this.worldElements.add(mountain);
            return this;
        }

        public Builder mountains(Mountain... mountains){
            Arrays.stream(mountains).forEach(this::mountain);
            return this;
        }

        public Builder reporter(AdventureReporter reporter){
            Objects.requireNonNull(reporter, "A valid reporter must be supplied");
            this.reporter = reporter;
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
            return new AdventureWorld(width, height, worldElements, reporter);
        }
    }

    private final WorldElement[][] worldElements;

    private final int height;

    private final int width;

    private final Map<String, Adventurer> adventurers = new ConcurrentHashMap<>();

    private final AdventureReporter reporter;

    private AdventureWorld(int width, int height, List<WorldElement> worldElements, AdventureReporter reporter){
        this.reporter = reporter;
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
                                        worldElement -> ((Adventurer) worldElement).getAdventurerName(),
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
        ).filter(worldElement -> worldElement != null);
    }

    private Adventurer getAdventurerInternal(String adventurerName){
        Adventurer adventurer = this.adventurers.getOrDefault(adventurerName, null);
        if(adventurer == null){
            throw new IllegalStateException(
                    String.format(
                            "Adventurer '%s' does not exist",
                            adventurerName
                    )
            );
        }
        return adventurer;
    }

    @Override
    public MovementTryResult tryMoving(String adventurerName, Direction direction) {
        Adventurer adventurer = getAdventurerInternal(adventurerName);
        Position newPosition = adventurer.getCurrentOrientation().move(direction).adjust(adventurer.getPosition());
        Orientation newOrientation = adventurer.getCurrentOrientation().deduce(direction);

        boolean isNewPositionInBounds = newPosition.getAbsoluteEasting() >= 0 && newPosition.getAbsoluteNorthing() >= 0;
        WorldElement elementAtNewPosition = at(newPosition)
                .filter(worldElement -> worldElement instanceof Mountain || worldElement instanceof Adventurer)
                .findFirst()
                .orElse(null);

        return new MovementTryResult(
                isNewPositionInBounds && elementAtNewPosition == null,
                isNewPositionInBounds ? newOrientation : null,
                isNewPositionInBounds ? newPosition : null,
                elementAtNewPosition
        );
    }

    @Override
    public void move(String adventurerName, Direction direction) {
        MovementTryResult result = tryMoving(adventurerName, direction);
        if(result.isASuccess()){
            Position newPosition = result.getNewPosition();
            Adventurer adventurer = getAdventurerInternal(adventurerName);
            adventurer.updatePosition(newPosition);
            adventurer.setCurrentOrientation(result.getNewOrientation());

            Optional<WorldElement> optionalTreasure = at(newPosition).filter(worldElement -> worldElement instanceof Treasure).findFirst();
            if(optionalTreasure.isPresent() && ((Treasure) optionalTreasure.get()).getRemainingLoots() != 0){
                adventurer.setPickedUpTreasures(adventurer.getPickedUpTreasures() + 1);
                ((Treasure) this.worldElements[newPosition.getAbsoluteNorthing()][newPosition.getAbsoluteEasting()]).removeALoot();
            }
            adventurer.markMove(direction);
            adventurers.put(adventurerName, adventurer);
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

    /**
     * Fetches the size of the current {@link AdventureWorld}.
     *
     * @return the underlying {@link WorldSize} object.
     */
    public WorldSize getSize(){
        return new WorldSize(
                height,
                width
        );
    }

    /**
     * Used to signal the game end, in order to trigger reporting to a file, or whatever {@link AdventureReporter} implementation.
     */
    public void end(){
        if(reporter != null){
            reporter.report(
                    Stream.of(
                            adventurers.values().stream(),
                            Stream.of(getSize()),
                            Arrays.stream(this.worldElements).flatMap(Arrays::stream)
                    )
                    .flatMap(Function.identity())
                    .filter(worldElement -> worldElement != null)
            );
        }
    }
}
