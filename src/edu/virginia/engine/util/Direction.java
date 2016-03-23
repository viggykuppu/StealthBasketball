package edu.virginia.engine.util;

/**
 * Created by Guillaume Bailey on 3/20/2016.
 */
public enum Direction {
    LEFT,RIGHT,UP,DOWN;

    private Direction opposite;

    static {
        LEFT.opposite = RIGHT;
        DOWN.opposite = UP;
        RIGHT.opposite = LEFT;
        UP.opposite = DOWN;
    }

    public Direction getOppositeDirection() {
        return opposite;
    }
}
