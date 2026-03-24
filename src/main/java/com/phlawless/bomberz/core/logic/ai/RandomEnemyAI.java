package com.phlawless.bomberz.core.logic.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.phlawless.bomberz.core.domain.math.Direction;
import com.phlawless.bomberz.core.domain.math.Vector2D;
import com.phlawless.bomberz.core.domain.model.Bomb;
import com.phlawless.bomberz.core.domain.model.Enemy;
import com.phlawless.bomberz.core.domain.model.Tile;
import com.phlawless.bomberz.core.domain.model.World;

/**
 * A random AI that moves in random valid directions and occasionally places bombs.
 * This AI is "smart" enough to:
 * - Avoid walking into bomb blast zones
 * - Only place bombs if there's an escape route
 */
public class RandomEnemyAI implements EnemyAI {
    private static final double BOMB_CHANCE = 0.1; // 10% chance to place a bomb each move

    private final Random random;

    public RandomEnemyAI() {
        this.random = new Random();
    }

    /**
     * Creates a RandomEnemyAI with a specific seed for reproducible behavior.
     *
     * @param seed the random seed
     */
    public RandomEnemyAI(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public Direction decideMove(Enemy enemy, World world, List<Bomb> bombs, Vector2D playerPosition) {
        Vector2D currentPos = enemy.getPosition();
        
        // Calculate danger zones from all active bombs
        Set<Vector2D> dangerZones = DangerZoneCalculator.calculateDangerZones(bombs, world);
        
        List<Direction> safeDirections = new ArrayList<>();
        List<Direction> validDirections = new ArrayList<>();

        // Check each direction for validity and safety
        for (Direction dir : Direction.values()) {
            Vector2D newPos = currentPos.add(dir.toVector());

            if (isValidMove(newPos, world, bombs)) {
                validDirections.add(dir);
                
                // Check if this direction is safe (not in a danger zone)
                if (!dangerZones.contains(newPos)) {
                    safeDirections.add(dir);
                }
            }
        }

        // Prefer safe directions
        if (!safeDirections.isEmpty()) {
            return safeDirections.get(random.nextInt(safeDirections.size()));
        }

        // If no safe directions but we're currently in danger, try to escape anyway
        if (dangerZones.contains(currentPos) && !validDirections.isEmpty()) {
            // We're in danger! Move somewhere, anywhere valid
            return validDirections.get(random.nextInt(validDirections.size()));
        }

        // If no valid moves at all, stay still
        if (validDirections.isEmpty()) {
            return null;
        }

        // Not currently in danger but all moves lead to danger - stay still
        return null;
    }

    @Override
    public boolean shouldPlaceBomb(Enemy enemy, World world, List<Bomb> bombs, Vector2D playerPosition) {
        // First check the random chance
        if (random.nextDouble() >= BOMB_CHANCE) {
            return false;
        }

        // Only place a bomb if we have an escape route
        Vector2D currentPos = enemy.getPosition();
        return DangerZoneCalculator.hasEscapeRoute(currentPos, bombs, world, enemy.getBombRadius());
    }

    /**
     * Checks if a move to the given position is valid (walkable and no bomb).
     * Note: This doesn't check for danger - that's handled separately.
     */
    private boolean isValidMove(Vector2D pos, World world, List<Bomb> bombs) {
        // Check bounds
        if (!world.isInBounds(pos.x(), pos.y())) {
            return false;
        }

        // Check tile is walkable
        Tile tile = world.getTile(pos.x(), pos.y());
        if (tile != Tile.EMPTY) {
            return false;
        }

        // Check for bombs blocking the path
        for (Bomb bomb : bombs) {
            if (bomb.getPosition().equals(pos)) {
                return false;
            }
        }

        return true;
    }
}
