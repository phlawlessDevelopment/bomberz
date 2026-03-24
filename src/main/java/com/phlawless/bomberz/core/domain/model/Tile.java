package com.phlawless.bomberz.core.domain.model;

/**
 * Represents the different types of tiles that can exist in a World grid.
 */
public enum Tile {
    /** Empty walkable space */
    EMPTY,
    /** Indestructible wall (borders and fixed pattern) */
    SOLID_WALL,
    /** Breakable wall (destroyed by explosions) */
    DESTRUCTIBLE_WALL
}
