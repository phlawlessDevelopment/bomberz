package com.phlawless.bomberz.core.logic.ai;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.phlawless.bomberz.core.domain.math.Direction;
import com.phlawless.bomberz.core.domain.math.Vector2D;
import com.phlawless.bomberz.core.domain.model.Bomb;
import com.phlawless.bomberz.core.domain.model.Tile;
import com.phlawless.bomberz.core.domain.model.World;

/**
 * Utility class for calculating danger zones from bombs.
 * Used by AI to make smarter decisions about movement and bomb placement.
 */
public final class DangerZoneCalculator {

    private DangerZoneCalculator() {
        // Utility class - no instantiation
    }

    /**
     * Calculates all positions that are in danger from active bombs.
     *
     * @param bombs the list of active bombs
     * @param world the game world
     * @return set of positions that will be hit when bombs explode
     */
    public static Set<Vector2D> calculateDangerZones(List<Bomb> bombs, World world) {
        Set<Vector2D> dangerZones = new HashSet<>();

        for (Bomb bomb : bombs) {
            dangerZones.addAll(calculateBombBlastZone(bomb, world));
        }

        return dangerZones;
    }

    /**
     * Calculates the blast zone for a single bomb.
     *
     * @param bomb  the bomb
     * @param world the game world
     * @return set of positions that will be hit when this bomb explodes
     */
    public static Set<Vector2D> calculateBombBlastZone(Bomb bomb, World world) {
        Set<Vector2D> blastZone = new HashSet<>();
        Vector2D bombPos = bomb.getPosition();
        int radius = bomb.getRadius();

        // The bomb's position is always dangerous
        blastZone.add(bombPos);

        // Check each cardinal direction
        for (Direction dir : Direction.values()) {
            for (int i = 1; i <= radius; i++) {
                Vector2D pos = new Vector2D(
                        bombPos.x() + dir.getDx() * i,
                        bombPos.y() + dir.getDy() * i);

                // Stop at world bounds
                if (!world.isInBounds(pos.x(), pos.y())) {
                    break;
                }

                Tile tile = world.getTile(pos.x(), pos.y());

                // Solid walls block explosions completely
                if (tile == Tile.SOLID_WALL) {
                    break;
                }

                // This position is in the blast zone
                blastZone.add(pos);

                // Destructible walls stop explosion spread (but are included in blast)
                if (tile == Tile.DESTRUCTIBLE_WALL) {
                    break;
                }
            }
        }

        return blastZone;
    }

    /**
     * Checks if a position is in any bomb's danger zone.
     *
     * @param pos   the position to check
     * @param bombs the list of active bombs
     * @param world the game world
     * @return true if the position is dangerous
     */
    public static boolean isDangerous(Vector2D pos, List<Bomb> bombs, World world) {
        for (Bomb bomb : bombs) {
            if (isInBlastRadius(pos, bomb, world)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a position is within a specific bomb's blast radius.
     *
     * @param pos   the position to check
     * @param bomb  the bomb
     * @param world the game world
     * @return true if the position will be hit when the bomb explodes
     */
    public static boolean isInBlastRadius(Vector2D pos, Bomb bomb, World world) {
        return calculateBombBlastZone(bomb, world).contains(pos);
    }

    /**
     * Checks if there's at least one safe adjacent tile to escape to.
     * Used to determine if it's safe to place a bomb at the current position.
     *
     * @param pos   the position where a bomb would be placed
     * @param bombs the list of currently active bombs
     * @param world the game world
     * @param bombRadius the radius of the bomb that would be placed
     * @return true if there's at least one escape route
     */
    public static boolean hasEscapeRoute(Vector2D pos, List<Bomb> bombs, World world, int bombRadius) {
        // Create a hypothetical bomb at this position
        Bomb hypotheticalBomb = new Bomb(pos, bombRadius, 1000, -1);

        // Calculate danger zones including the hypothetical bomb
        Set<Vector2D> dangerZones = calculateDangerZones(bombs, world);
        dangerZones.addAll(calculateBombBlastZone(hypotheticalBomb, world));

        // Check each adjacent tile for safety
        for (Direction dir : Direction.values()) {
            Vector2D adjacentPos = pos.add(dir.toVector());

            // Check if the adjacent tile is walkable
            if (!world.isInBounds(adjacentPos.x(), adjacentPos.y())) {
                continue;
            }

            Tile tile = world.getTile(adjacentPos.x(), adjacentPos.y());
            if (tile != Tile.EMPTY) {
                continue;
            }

            // Check if there's a bomb at this position
            boolean hasBomb = bombs.stream()
                    .anyMatch(b -> b.getPosition().equals(adjacentPos));
            if (hasBomb) {
                continue;
            }

            // Check if this adjacent tile is safe from all explosions
            if (!dangerZones.contains(adjacentPos)) {
                return true; // Found a safe escape route!
            }
        }

        return false; // No escape route found
    }

    /**
     * Gets all safe adjacent positions from a given position.
     *
     * @param pos   the current position
     * @param bombs the list of active bombs
     * @param world the game world
     * @return set of adjacent positions that are safe to move to
     */
    public static Set<Vector2D> getSafeAdjacentPositions(Vector2D pos, List<Bomb> bombs, World world) {
        Set<Vector2D> safePositions = new HashSet<>();
        Set<Vector2D> dangerZones = calculateDangerZones(bombs, world);

        for (Direction dir : Direction.values()) {
            Vector2D adjacentPos = pos.add(dir.toVector());

            // Check if walkable
            if (!world.isInBounds(adjacentPos.x(), adjacentPos.y())) {
                continue;
            }

            Tile tile = world.getTile(adjacentPos.x(), adjacentPos.y());
            if (tile != Tile.EMPTY) {
                continue;
            }

            // Check for bombs
            boolean hasBomb = bombs.stream()
                    .anyMatch(b -> b.getPosition().equals(adjacentPos));
            if (hasBomb) {
                continue;
            }

            // Check if safe
            if (!dangerZones.contains(adjacentPos)) {
                safePositions.add(adjacentPos);
            }
        }

        return safePositions;
    }
}
