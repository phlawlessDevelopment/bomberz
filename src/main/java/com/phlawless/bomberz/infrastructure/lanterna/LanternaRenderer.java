package com.phlawless.bomberz.infrastructure.lanterna;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.phlawless.bomberz.core.domain.math.Vector2D;
import com.phlawless.bomberz.core.domain.model.Bomb;
import com.phlawless.bomberz.core.domain.model.Explosion;
import com.phlawless.bomberz.core.domain.model.Tile;
import com.phlawless.bomberz.core.domain.model.World;
import com.phlawless.bomberz.core.domain.model.lanterna.LanternaEnemy;
import com.phlawless.bomberz.core.domain.model.lanterna.LanternaEntity;
import com.phlawless.bomberz.infrastructure.Renderer;

/**
 * Lanterna-specific renderer that draws the world and entities to a terminal screen.
 * Supports dirty-region tracking for optimized rendering.
 * Centers the game world in the terminal.
 */
public class LanternaRenderer extends Renderer<LanternaEntity> {

    private static final char SOLID_WALL_CHAR = '\u2588'; // Full block: █
    private static final char DESTRUCTIBLE_WALL_CHAR = '#';
    private static final char EMPTY_CHAR = '.';
    private static final char BOMB_CHAR = '\u25CF'; // Filled circle: ●
    private static final char EXPLOSION_CHAR = '*';

    private final Screen screen;
    private final TextGraphics textGraphics;
    private final Set<Vector2D> dirtyCells;

    // Offset to center the world in the terminal
    private int offsetX;
    private int offsetY;

    /**
     * Creates a new LanternaRenderer with the given screen.
     *
     * @param screen the Lanterna screen to render to
     */
    public LanternaRenderer(Screen screen) {
        this.screen = screen;
        this.textGraphics = screen.newTextGraphics();
        this.dirtyCells = new HashSet<>();
        this.offsetX = 0;
        this.offsetY = 0;
    }

    /**
     * Calculates and sets the offset to center the world in the terminal.
     * Should be called before rendering when the world size is known.
     *
     * @param worldWidth  the width of the world
     * @param worldHeight the height of the world
     */
    public void centerWorld(int worldWidth, int worldHeight) {
        TerminalSize terminalSize = screen.getTerminalSize();
        this.offsetX = Math.max(0, (terminalSize.getColumns() - worldWidth) / 2);
        this.offsetY = Math.max(0, (terminalSize.getRows() - worldHeight) / 2);
    }

    /**
     * Renders the entire world grid. Use this for initial rendering only.
     * For subsequent updates, use {@link #renderDirty(World)} instead.
     *
     * @param world the world to render
     */
    @Override
    public void render(World world) {
        // Calculate centering offset
        centerWorld(world.getWidth(), world.getHeight());

        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                redrawTile(world, x, y);
            }
        }
    }

    /**
     * Renders only the cells that have been marked as dirty, then clears the dirty set.
     * This is more efficient than re-rendering the entire world.
     *
     * @param world the world to render tiles from
     */
    public void renderDirty(World world) {
        for (Vector2D pos : dirtyCells) {
            if (world.isInBounds(pos.x(), pos.y())) {
                redrawTile(world, pos.x(), pos.y());
            }
        }
        dirtyCells.clear();
    }

    /**
     * Redraws a single tile at the specified position.
     *
     * @param world the world to get the tile from
     * @param x     the x coordinate (world space)
     * @param y     the y coordinate (world space)
     */
    public void redrawTile(World world, int x, int y) {
        Tile tile = world.getTile(x, y);
        char tileChar = getTileCharacter(tile);
        textGraphics.setCharacter(x + offsetX, y + offsetY, TextCharacter.fromCharacter(tileChar)[0]);
    }

    /**
     * Marks a cell as dirty, meaning it needs to be redrawn on the next render pass.
     *
     * @param position the position to mark as dirty (world space)
     */
    public void markDirty(Vector2D position) {
        dirtyCells.add(position);
    }

    @Override
    public void draw(LanternaEntity entity) {
        Vector2D position = entity.getPosition();
        if (position != null) {
            TextCharacter sprite = entity.getSprite();
            if (sprite != null) {
                textGraphics.setCharacter(position.x() + offsetX, position.y() + offsetY, sprite);
            }
        }
    }

    /**
     * Draws a bomb at its position.
     *
     * @param bomb the bomb to draw
     */
    public void drawBomb(Bomb bomb) {
        Vector2D pos = bomb.getPosition();
        textGraphics.setCharacter(pos.x() + offsetX, pos.y() + offsetY, TextCharacter.fromCharacter(BOMB_CHAR)[0]);
    }

    /**
     * Draws an explosion at all its affected positions.
     *
     * @param explosion the explosion to draw
     */
    public void drawExplosion(Explosion explosion) {
        for (Vector2D pos : explosion.getAffectedPositions()) {
            textGraphics.setCharacter(pos.x() + offsetX, pos.y() + offsetY, TextCharacter.fromCharacter(EXPLOSION_CHAR)[0]);
        }
    }

    /**
     * Draws an enemy at its position.
     *
     * @param enemy the enemy to draw
     */
    public void drawEnemy(LanternaEnemy enemy) {
        Vector2D pos = enemy.getPosition();
        if (pos != null) {
            TextCharacter sprite = enemy.getSprite();
            if (sprite != null) {
                textGraphics.setCharacter(pos.x() + offsetX, pos.y() + offsetY, sprite);
            }
        }
    }

    /**
     * Refreshes the screen to display all rendered content.
     * Should be called after all render() and draw() calls are complete.
     *
     * @throws IOException if there is an error refreshing the screen
     */
    public void refresh() throws IOException {
        screen.refresh();
    }

    /**
     * Clears the screen buffer.
     */
    public void clear() {
        screen.clear();
    }

    /**
     * Draws the game over screen centered in the terminal.
     *
     * @param worldWidth  the width of the world
     * @param worldHeight the height of the world
     */
    public void drawGameOver(int worldWidth, int worldHeight) {
        String gameOverText = "GAME OVER";
        String promptText = "Press ESC to exit";

        // Center the text in the terminal (using world center + offset)
        int centerX = offsetX + worldWidth / 2;
        int centerY = offsetY + worldHeight / 2;

        int gameOverX = centerX - gameOverText.length() / 2;
        int gameOverY = centerY - 1;
        int promptX = centerX - promptText.length() / 2;
        int promptY = centerY + 1;

        textGraphics.putString(gameOverX, gameOverY, gameOverText);
        textGraphics.putString(promptX, promptY, promptText);
    }

    /**
     * Maps a Tile enum value to its character representation.
     */
    private char getTileCharacter(Tile tile) {
        return switch (tile) {
            case SOLID_WALL -> SOLID_WALL_CHAR;
            case DESTRUCTIBLE_WALL -> DESTRUCTIBLE_WALL_CHAR;
            case EMPTY -> EMPTY_CHAR;
        };
    }
}
