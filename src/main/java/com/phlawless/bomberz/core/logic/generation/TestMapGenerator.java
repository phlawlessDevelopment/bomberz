package com.phlawless.bomberz.core.logic.generation;

import java.util.List;

import com.phlawless.bomberz.core.domain.math.Vector2D;
import com.phlawless.bomberz.core.domain.model.Tile;
import com.phlawless.bomberz.core.domain.model.World;

/**
 * A hardcoded test map generator for development purposes.
 * Generates a classic Bomberman-style map with:
 * - Solid walls around the border
 * - Solid walls in a grid pattern (every other row/column)
 * - Destructible walls filling most empty spaces
 * - Clear spawn areas in the corners for players
 */
public class TestMapGenerator implements MapGenerator {

    @Override
    public GeneratedMap generate(int width, int height) {
        World world = new World(width, height);

        // Fill the entire map with the classic pattern
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = determineTile(x, y, width, height);
                world.setTile(x, y, tile);
            }
        }

        // Clear spawn areas (corners) - player needs room to move and escape bombs
        clearSpawnArea(world, 1, 1);                          // Top-left (Player 1)
        clearSpawnArea(world, width - 2, 1);                  // Top-right (Player 2)
        clearSpawnArea(world, 1, height - 2);                 // Bottom-left (Player 3)
        clearSpawnArea(world, width - 2, height - 2);         // Bottom-right (Player 4)

        // Define spawn points (4 corners, inside the border)
        List<Vector2D> spawnPoints = List.of(
                new Vector2D(1, 1),                           // Player 1: Top-left
                new Vector2D(width - 2, 1),                   // Player 2: Top-right
                new Vector2D(1, height - 2),                  // Player 3: Bottom-left
                new Vector2D(width - 2, height - 2)           // Player 4: Bottom-right
        );

        return new GeneratedMap(world, spawnPoints);
    }

    /**
     * Determines what tile should be at the given position.
     */
    private Tile determineTile(int x, int y, int width, int height) {
        // Border walls (solid)
        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
            return Tile.SOLID_WALL;
        }

        // Grid pattern: solid walls at positions where both x and y are even
        // This creates the classic Bomberman pillar pattern
        if (x % 2 == 0 && y % 2 == 0) {
            return Tile.SOLID_WALL;
        }

        // Fill remaining spaces with destructible walls
        // (spawn areas will be cleared separately)
        return Tile.DESTRUCTIBLE_WALL;
    }

    /**
     * Clears a spawn area around the given position.
     * Creates an L-shaped clear zone so the player can move and place bombs safely.
     */
    private void clearSpawnArea(World world, int x, int y) {
        // Clear the spawn point itself
        world.setTile(x, y, Tile.EMPTY);

        // Clear adjacent tiles (but only if they're not solid walls)
        clearIfDestructible(world, x + 1, y);  // Right
        clearIfDestructible(world, x - 1, y);  // Left
        clearIfDestructible(world, x, y + 1);  // Down
        clearIfDestructible(world, x, y - 1);  // Up
    }

    /**
     * Clears a tile only if it's a destructible wall.
     */
    private void clearIfDestructible(World world, int x, int y) {
        if (world.isInBounds(x, y) && world.getTile(x, y) == Tile.DESTRUCTIBLE_WALL) {
            world.setTile(x, y, Tile.EMPTY);
        }
    }
}
