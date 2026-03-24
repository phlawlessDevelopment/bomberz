package com.phlawless.bomberz.core.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.phlawless.bomberz.core.domain.math.Direction;
import com.phlawless.bomberz.core.domain.math.Vector2D;

/**
 * Represents an explosion that spreads in a cross shape from a bomb.
 * The explosion persists for a short duration for visual effect.
 */
public class Explosion {
    private final List<Vector2D> affectedPositions;
    private long remainingTimeMs;

    /**
     * Creates an explosion with the specified affected positions.
     *
     * @param affectedPositions the positions affected by this explosion
     * @param durationMs        how long the explosion visual lasts in milliseconds
     */
    public Explosion(List<Vector2D> affectedPositions, long durationMs) {
        this.affectedPositions = new ArrayList<>(affectedPositions);
        this.remainingTimeMs = durationMs;
    }

    /**
     * Calculates the positions affected by an explosion originating from a bomb.
     * The explosion spreads in a cross shape (up, down, left, right) and stops
     * at solid walls. Destructible walls are included but stop further spread.
     *
     * @param bomb  the bomb that is exploding
     * @param world the world to check for obstacles
     * @return list of positions affected by the explosion
     */
    public static List<Vector2D> calculateAffectedPositions(Bomb bomb, World world) {
        List<Vector2D> positions = new ArrayList<>();
        Vector2D center = bomb.getPosition();
        int radius = bomb.getRadius();

        // Always include the bomb's position
        positions.add(center);

        // Spread in each cardinal direction
        for (Direction dir : Direction.values()) {
            for (int i = 1; i <= radius; i++) {
                Vector2D pos = center.add(new Vector2D(dir.getDx() * i, dir.getDy() * i));

                // Stop if out of bounds
                if (!world.isInBounds(pos.x(), pos.y())) {
                    break;
                }

                Tile tile = world.getTile(pos.x(), pos.y());

                // Stop at solid walls (don't include them)
                if (tile == Tile.SOLID_WALL) {
                    break;
                }

                // Include this position
                positions.add(pos);

                // Stop after hitting a destructible wall (but include it)
                if (tile == Tile.DESTRUCTIBLE_WALL) {
                    break;
                }
            }
        }

        return positions;
    }

    /**
     * Updates the explosion timer.
     *
     * @param deltaMs time elapsed since last update in milliseconds
     * @return true if the explosion has finished and should be removed
     */
    public boolean tick(long deltaMs) {
        remainingTimeMs -= deltaMs;
        return remainingTimeMs <= 0;
    }

    /**
     * Returns an unmodifiable view of the affected positions.
     */
    public List<Vector2D> getAffectedPositions() {
        return Collections.unmodifiableList(affectedPositions);
    }

    public long getRemainingTimeMs() {
        return remainingTimeMs;
    }

    public boolean isFinished() {
        return remainingTimeMs <= 0;
    }
}
