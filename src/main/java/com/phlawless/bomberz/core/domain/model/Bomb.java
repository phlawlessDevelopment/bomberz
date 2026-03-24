package com.phlawless.bomberz.core.domain.model;

import com.phlawless.bomberz.core.domain.math.Vector2D;

/**
 * Represents a bomb that will explode after a timer expires.
 * Bombs are placed by players and create cross-shaped explosions.
 */
public class Bomb {
    private final Vector2D position;
    private final int radius;
    private final int ownerId;
    private long remainingTimeMs;
    private boolean exploded;

    /**
     * Creates a new bomb at the specified position.
     *
     * @param position       the position of the bomb
     * @param radius         the explosion radius in tiles
     * @param timerMs        the time in milliseconds before the bomb explodes
     * @param ownerId        the ID of the player who placed the bomb
     */
    public Bomb(Vector2D position, int radius, long timerMs, int ownerId) {
        this.position = position;
        this.radius = radius;
        this.remainingTimeMs = timerMs;
        this.ownerId = ownerId;
        this.exploded = false;
    }

    /**
     * Updates the bomb timer.
     *
     * @param deltaMs time elapsed since last update in milliseconds
     * @return true if the bomb should explode (timer reached zero)
     */
    public boolean tick(long deltaMs) {
        if (exploded) {
            return false;
        }
        remainingTimeMs -= deltaMs;
        if (remainingTimeMs <= 0) {
            exploded = true;
            return true;
        }
        return false;
    }

    /**
     * Forces the bomb to explode immediately (e.g., from chain reaction).
     */
    public void detonate() {
        exploded = true;
        remainingTimeMs = 0;
    }

    public Vector2D getPosition() {
        return position;
    }

    public int getRadius() {
        return radius;
    }

    public long getRemainingTimeMs() {
        return remainingTimeMs;
    }

    public boolean isExploded() {
        return exploded;
    }

    public int getOwnerId() {
        return ownerId;
    }
}
