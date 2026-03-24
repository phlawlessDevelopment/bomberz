package com.phlawless.bomberz.core.domain.model.lanterna;

import com.googlecode.lanterna.TextCharacter;
import com.phlawless.bomberz.core.domain.model.Enemy;
import com.phlawless.bomberz.core.logic.ai.EnemyAI;

/**
 * A Lanterna-specific enemy with a text character sprite.
 */
public class LanternaEnemy extends Enemy {
    private static final char DEFAULT_SPRITE = '&';
    private static final long DEFAULT_MOVE_INTERVAL_MS = 500; // Move every 0.5 seconds
    private static final int DEFAULT_MAX_BOMBS = 1;
    private static final int DEFAULT_BOMB_RADIUS = 2;

    private TextCharacter sprite;

    /**
     * Creates a new LanternaEnemy with default settings.
     *
     * @param ai the AI strategy for this enemy
     */
    public LanternaEnemy(EnemyAI ai) {
        this(ai, DEFAULT_MOVE_INTERVAL_MS, DEFAULT_MAX_BOMBS, DEFAULT_BOMB_RADIUS);
    }

    /**
     * Creates a new LanternaEnemy with custom settings.
     *
     * @param ai             the AI strategy for this enemy
     * @param moveIntervalMs milliseconds between moves
     * @param maxBombs       maximum bombs this enemy can have active
     * @param bombRadius     explosion radius for bombs
     */
    public LanternaEnemy(EnemyAI ai, long moveIntervalMs, int maxBombs, int bombRadius) {
        super(ai, moveIntervalMs, maxBombs, bombRadius);
        this.sprite = TextCharacter.fromCharacter(DEFAULT_SPRITE)[0];
    }

    /**
     * Returns the sprite for this enemy.
     */
    public TextCharacter getSprite() {
        return sprite;
    }

    /**
     * Sets the sprite for this enemy.
     */
    public void setSprite(TextCharacter sprite) {
        this.sprite = sprite;
    }
}
