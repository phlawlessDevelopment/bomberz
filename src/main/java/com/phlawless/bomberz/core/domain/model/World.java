package com.phlawless.bomberz.core.domain.model;

/**
 * Represents a game level/world containing a grid of tiles.
 * The world defines the playable area bounds and the layout of walls.
 */
public class World {
    private final int width;
    private final int height;
    private final Tile[][] tiles;

    /**
     * Creates a new World with the specified dimensions.
     * All tiles are initialized to EMPTY.
     *
     * @param width  the width of the world in tiles
     * @param height the height of the world in tiles
     */
    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[height][width];

        // Initialize all tiles to EMPTY
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x] = Tile.EMPTY;
            }
        }
    }

    /**
     * Gets the width of the world in tiles.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the world in tiles.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the tile at the specified coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the tile at the specified position
     * @throws IndexOutOfBoundsException if coordinates are outside world bounds
     */
    public Tile getTile(int x, int y) {
        if (!isInBounds(x, y)) {
            throw new IndexOutOfBoundsException(
                    "Coordinates (" + x + ", " + y + ") are outside world bounds (" + width + "x" + height + ")");
        }
        return tiles[y][x];
    }

    /**
     * Sets the tile at the specified coordinates.
     *
     * @param x    the x coordinate
     * @param y    the y coordinate
     * @param tile the tile to set
     * @throws IndexOutOfBoundsException if coordinates are outside world bounds
     */
    public void setTile(int x, int y, Tile tile) {
        if (!isInBounds(x, y)) {
            throw new IndexOutOfBoundsException(
                    "Coordinates (" + x + ", " + y + ") are outside world bounds (" + width + "x" + height + ")");
        }
        tiles[y][x] = tile;
    }

    /**
     * Checks if the specified coordinates are within the world bounds.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the coordinates are within bounds, false otherwise
     */
    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
