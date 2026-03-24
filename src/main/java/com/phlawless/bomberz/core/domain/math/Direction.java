package com.phlawless.bomberz.core.domain.math;

/**
 * Cardinal directions for movement.
 */
public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Returns a Vector2D representing this direction's offset.
     */
    public Vector2D toVector() {
        return new Vector2D(dx, dy);
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}
