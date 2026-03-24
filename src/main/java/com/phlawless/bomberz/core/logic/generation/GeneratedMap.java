package com.phlawless.bomberz.core.logic.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.phlawless.bomberz.core.domain.math.Vector2D;
import com.phlawless.bomberz.core.domain.model.World;

/**
 * Contains the result of map generation: a World and player spawn points.
 */
public class GeneratedMap {
    private final World world;
    private final List<Vector2D> spawnPoints;

    /**
     * Creates a GeneratedMap with the given world and spawn points.
     *
     * @param world       the generated world
     * @param spawnPoints the player spawn points (index 0 = player 1, etc.)
     */
    public GeneratedMap(World world, List<Vector2D> spawnPoints) {
        this.world = world;
        this.spawnPoints = new ArrayList<>(spawnPoints);
    }

    /**
     * Returns the generated world.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Returns an unmodifiable list of spawn points.
     * Index 0 is player 1's spawn, index 1 is player 2's spawn, etc.
     */
    public List<Vector2D> getSpawnPoints() {
        return Collections.unmodifiableList(spawnPoints);
    }

    /**
     * Returns the spawn point for the specified player index.
     *
     * @param playerIndex the player index (0-based)
     * @return the spawn point, or null if index is out of bounds
     */
    public Vector2D getSpawnPoint(int playerIndex) {
        if (playerIndex < 0 || playerIndex >= spawnPoints.size()) {
            return null;
        }
        return spawnPoints.get(playerIndex);
    }

    /**
     * Returns the number of spawn points (i.e., max number of players).
     */
    public int getMaxPlayers() {
        return spawnPoints.size();
    }
}
