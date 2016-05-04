package io.vieira.adventuretime.game;

import io.vieira.adventuretime.game.elements.*;
import io.vieira.adventuretime.game.routines.ElementsRepartitionAccessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Map class, wrapping all game routines.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class Map implements ElementsRepartitionAccessor {

    public static class Builder {
        private List<MapElement> mapElements = new ArrayList<>();
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
            if(this.mapElements.contains(adventurer)){
                throwCellAlreadyOccupiedInternal(adventurer);
            }
            this.mapElements.add(adventurer);
            return this;
        }

        public Builder treasure(Treasure treasure){
            if(this.mapElements.contains(treasure)){
                throwCellAlreadyOccupiedInternal(treasure);
            }
            this.mapElements.add(treasure);
            return this;
        }

        public Builder mountain(Mountain mountain){
            if(this.mapElements.contains(mountain)){
                throwCellAlreadyOccupiedInternal(mountain);
            }
            this.mapElements.add(mountain);
            return this;
        }

        private void throwCellAlreadyOccupiedInternal(MapElement element){
            throw new IllegalStateException(
                    String.format(
                            "Unable to add '%s' as the cell is already occupied by '%s'",
                            element,
                            this.mapElements.get(this.mapElements.indexOf(element))
                    )
            );
        }

        public Map build(){
            if(height <= 0){
                throw new IllegalArgumentException("Map height must be positive");
            }
            if(width <= 0){
                throw new IllegalArgumentException("Map width must be positive");
            }
            return new Map(width, height, mapElements);
        }
    }

    private final MapElement[][] mapElements;

    private final int height;

    private final int width;

    private Map(int width, int height, List<MapElement> mapElements){
        this.mapElements = IntStream
                .range(0, height)
                .mapToObj(currentHeight -> IntStream
                        .range(0, width)
                        .mapToObj(currentWidth -> mapElements
                                .stream()
                                .filter(mapElement -> mapElement.getDeclaredNorthing() == currentHeight && mapElement.getDeclaredEasting() == currentWidth)
                                .findFirst()
                                .orElseGet(() -> new Grassland(currentWidth, currentHeight))
                        )
                        .toArray(MapElement[]::new)
                ).toArray(MapElement[][]::new);
        this.height = height;
        this.width = width;
    }


    @Override
    public long getNumberOf(Class<? extends MapElement> desiredType) {
        return Arrays
                .stream(this.mapElements)
                .flatMap(Arrays::stream)
                .filter(mapElement -> desiredType.isAssignableFrom(mapElement.getClass()))
                .count();
    }

    @Override
    public java.util.Map<Class<? extends MapElement>, Long> getOccupation() {
        return Arrays
                .stream(this.mapElements)
                .flatMap(Arrays::stream)
                .collect(Collectors.groupingBy(
                        MapElement::getClass,
                        Collectors.counting()
                ));
    }
}
