package com.phlawless.bomberz.infrastructure;

import com.phlawless.bomberz.core.domain.model.Entity;
import com.phlawless.bomberz.core.domain.model.World;

/**
 * Abstract renderer responsible for drawing the game world and entities.
 *
 * @param <T> the type of entity this renderer can draw
 */
public abstract class Renderer<T extends Entity> {

    /**
     * Renders the entire world grid (tiles, walls, etc.).
     *
     * @param world the world to render
     */
    public abstract void render(World world);

    /**
     * Draws a single entity on top of the world.
     *
     * @param entity the entity to draw
     */
    public abstract void draw(T entity);
}
