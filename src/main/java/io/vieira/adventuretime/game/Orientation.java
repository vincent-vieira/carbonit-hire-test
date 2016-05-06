package io.vieira.adventuretime.game;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Orientation enumeration that serves as descriptor for {@link io.vieira.adventuretime.game.elements.Adventurer} initial placement.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public enum Orientation {
    //TODO : use an internal String field to map parsed data from files
    NORTH(
            "N",
            new ImmutableMap.Builder<Direction, PositionAdjuster>()
                    .put(Direction.FORWARD, new PositionAdjuster(
                            PositionAdjuster.RETREAT,
                            PositionAdjuster.IDENTITY
                    ))
                    .put(Direction.LEFT, new PositionAdjuster(
                        PositionAdjuster.IDENTITY,
                        PositionAdjuster.RETREAT
                    ))
                    .put(Direction.RIGHT, new PositionAdjuster(
                        PositionAdjuster.IDENTITY,
                        PositionAdjuster.ADD
                    ))
                    .build()
    ),
    SOUTH(
            "S",
            new HashMap<Direction, PositionAdjuster>(){{
                put(Direction.FORWARD, new PositionAdjuster(
                        PositionAdjuster.ADD,
                        PositionAdjuster.IDENTITY
                ));
                put(Direction.LEFT, new PositionAdjuster(
                        PositionAdjuster.IDENTITY,
                        PositionAdjuster.ADD
                ));
                put(Direction.RIGHT, new PositionAdjuster(
                        PositionAdjuster.IDENTITY,
                        PositionAdjuster.RETREAT
                ));
            }}
    ),
    WEST(
            "W",
            new HashMap<Direction, PositionAdjuster>(){{
                put(Direction.FORWARD, new PositionAdjuster(
                        PositionAdjuster.IDENTITY,
                        PositionAdjuster.RETREAT
                ));
                put(Direction.LEFT, new PositionAdjuster(
                        PositionAdjuster.ADD,
                        PositionAdjuster.IDENTITY
                ));
                put(Direction.RIGHT, new PositionAdjuster(
                        PositionAdjuster.RETREAT,
                        PositionAdjuster.IDENTITY
                ));
            }}
    ),
    EAST(
            "E",
            new HashMap<Direction, PositionAdjuster>(){{
                put(Direction.FORWARD, new PositionAdjuster(
                        PositionAdjuster.IDENTITY,
                        PositionAdjuster.ADD
                ));
                put(Direction.LEFT, new PositionAdjuster(
                        PositionAdjuster.RETREAT,
                        PositionAdjuster.IDENTITY
                ));
                put(Direction.RIGHT, new PositionAdjuster(
                        PositionAdjuster.ADD,
                        PositionAdjuster.IDENTITY
                ));
            }}
    );

    @Getter
    private final String orientationCode;

    private final Map<Direction, PositionAdjuster> positionAdjusters;

    Orientation(String orientationCode, Map<Direction, PositionAdjuster> adjusters){
        this.orientationCode = orientationCode;
        this.positionAdjusters = adjusters;
    }

    public static Orientation fromString(String orientationCode) {
        return Arrays
                .stream(Orientation.values())
                .filter(orientation -> orientation.getOrientationCode().equalsIgnoreCase(orientationCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("'%s' is not a valid orientation code.", orientationCode)
                ));
    }

    /**
     * Inner class providing fluent coordinates calculations, without doing anything.
     *
     * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
     */
    public static class PositionAdjuster {

        static final Function<Integer, Integer> IDENTITY = integer -> integer;
        static final Function<Integer, Integer> RETREAT = integer -> --integer;
        static final Function<Integer, Integer> ADD = integer -> ++integer;

        private final Function<Integer, Integer> northingAdjuster;
        private final Function<Integer, Integer> eastingAdjuster;

        PositionAdjuster(Function<Integer, Integer> northingAdjuster, Function<Integer, Integer> eastingAdjuster){
            this.northingAdjuster = northingAdjuster;
            this.eastingAdjuster = eastingAdjuster;
        }

        /**
         * Adjusts the position passed as a parameter into a new one an returns it.
         *
         * @param currentPosition the current {@link Position}
         * @return the new computed {@link Position}
         */
        Position adjust(Position currentPosition){
            return new Position(
                    northingAdjuster.apply(currentPosition.getNorthing()),
                    eastingAdjuster.apply(currentPosition.getEasting())
            );
        }
    }

    /**
     * Returns the {@link PositionAdjuster} associated to the supplied direction.
     *
     * @param direction the direction
     * @return the associated {@link PositionAdjuster}
     */
    public PositionAdjuster move(Direction direction) {
        return this.positionAdjusters.get(direction);
    }

    /**
     * Deduces the new {@link Orientation} based on the current one and the next {@link Direction} to follow.
     *
     * @param direction the future direction to follow
     * @return the new {@link Orientation}
     */
    public Orientation deduce(Direction direction){
        if(direction == Direction.FORWARD){
            return this;
        }

        ImmutableMap<Direction, Orientation> orientationChanges = new ImmutableMap.Builder<Direction, Orientation>().build();
        switch(this){
            case NORTH:
                orientationChanges = new ImmutableMap.Builder<Direction, Orientation>()
                        .put(Direction.LEFT, Orientation.WEST)
                        .put(Direction.RIGHT, Orientation.EAST)
                        .build();
                break;
            case SOUTH:
                orientationChanges = new ImmutableMap.Builder<Direction, Orientation>()
                        .put(Direction.LEFT, Orientation.EAST)
                        .put(Direction.RIGHT, Orientation.WEST)
                        .build();
                break;
            case EAST:
                orientationChanges = new ImmutableMap.Builder<Direction, Orientation>()
                        .put(Direction.LEFT, Orientation.NORTH)
                        .put(Direction.RIGHT, Orientation.SOUTH)
                        .build();
                break;
            case WEST:
                orientationChanges = new ImmutableMap.Builder<Direction, Orientation>()
                        .put(Direction.LEFT, Orientation.SOUTH)
                        .put(Direction.RIGHT, Orientation.NORTH)
                        .build();
                break;
        }
        return orientationChanges.get(direction);
    }
}
