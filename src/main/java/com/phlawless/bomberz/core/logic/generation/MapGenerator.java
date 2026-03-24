package com.phlawless.bomberz.core.logic.generation;

/**
 * Strategy interface for generating game maps.
 * Implementations can provide different generation algorithms
 * (e.g., hardcoded test levels, random generation, file-based loading).
 */
public interface MapGenerator {

    /**
     * Generates a map with the specified dimensions.
     *
     * @param width  the width of the world in tiles
     * @param height the height of the world in tiles
     * @return a GeneratedMap containing the world and spawn points
     */
    GeneratedMap generate(int width, int height);
}
