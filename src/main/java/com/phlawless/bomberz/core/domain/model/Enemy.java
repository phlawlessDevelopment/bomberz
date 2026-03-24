package com.phlawless.bomberz.core.domain.model;

import com.phlawless.bomberz.core.domain.math.Vector2D;
import com.phlawless.bomberz.core.logic.ai.EnemyAI;

/**
 * Abstract base class for enemies in the game.
 * Enemies have AI behavior that can be swapped via the strategy pattern.
 */
public abstract class Enemy extends Entity {
    private static int nextId = 1;

    protected final int id;
    protected EnemyAI ai;
    protected boolean alive;
    protected long moveIntervalMs;
    protected long timeSinceLastMove;
    protected int maxBombs;
    protected int bombRadius;

    /**
     * Creates a new enemy with the specified AI and movement speed.
     *
     * @param ai             the AI strategy for this enemy
     * @param moveIntervalMs milliseconds between moves (lower = faster)
     * @param maxBombs       maximum bombs this enemy can have active at once
     * @param bombRadius     explosion radius for bombs placed by this enemy
     */
    protected Enemy(EnemyAI ai, long moveIntervalMs, int maxBombs, int bombRadius) {
        this.id = nextId++;
        this.ai = ai;
        this.alive = true;
        this.moveIntervalMs = moveIntervalMs;
        this.timeSinceLastMove = 0;
        this.maxBombs = maxBombs;
        this.bombRadius = bombRadius;
    }

    /**
     * Returns the unique ID for this enemy.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the AI strategy for this enemy.
     */
    public EnemyAI getAI() {
        return ai;
    }

    /**
     * Sets a new AI strategy for this enemy.
     */
    public void setAI(EnemyAI ai) {
        this.ai = ai;
    }

    /**
     * Returns true if this enemy is still alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Kills this enemy.
     */
    public void kill() {
        this.alive = false;
    }

    /**
     * Returns the move interval in milliseconds.
     */
    public long getMoveIntervalMs() {
        return moveIntervalMs;
    }

    /**
     * Returns the time since the last move in milliseconds.
     */
    public long getTimeSinceLastMove() {
        return timeSinceLastMove;
    }

    /**
     * Updates the time since last move and returns true if it's time to move.
     *
     * @param deltaMs time elapsed since last update
     * @return true if enough time has passed to allow movement
     */
    public boolean canMove(long deltaMs) {
        timeSinceLastMove += deltaMs;
        if (timeSinceLastMove >= moveIntervalMs) {
            timeSinceLastMove = 0;
            return true;
        }
        return false;
    }

    /**
     * Returns the maximum number of bombs this enemy can have active.
     */
    public int getMaxBombs() {
        return maxBombs;
    }

    /**
     * Returns the explosion radius for bombs placed by this enemy.
     */
    public int getBombRadius() {
        return bombRadius;
    }

    /**
     * Moves the enemy to a new position.
     *
     * @param newPosition the new position
     */
    public void moveTo(Vector2D newPosition) {
        setPosition(newPosition);
    }
}
