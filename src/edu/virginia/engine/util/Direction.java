package edu.virginia.engine.util;

/**
 * Created by Guillaume Bailey on 3/20/2016.
 */
public enum Direction {
    LEFT,RIGHT,UP,DOWN, TELEPORTER;

    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                throw new IllegalStateException("This should never happen: " + this + " has no opposite.");
        }
    }
}
