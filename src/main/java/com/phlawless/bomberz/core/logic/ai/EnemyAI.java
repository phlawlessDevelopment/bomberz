package com.phlawless.bomberz.core.logic.ai;

import java.util.List;

import com.phlawless.bomberz.core.domain.math.Direction;
import com.phlawless.bomberz.core.domain.math.Vector2D;
import com.phlawless.bomberz.core.domain.model.Bomb;
import com.phlawless.bomberz.core.domain.model.Enemy;
import com.phlawless.bomberz.core.domain.model.World;

/**
 * Strategy interface for enemy AI behavior.
 * Implementations can provide different decision-making algorithms
 * (e.g., random movement, pathfinding, aggressive pursuit).
 */
public interface EnemyAI {

    /**
     * Decides the next movement direction for the enemy.
     *
     * @param enemy          the enemy making the decision
     * @param world          the game world
     * @param bombs          list of active bombs
     * @param playerPosition the player's current position
     * @return the direction to move, or null to stay still
     */
    Direction decideMove(Enemy enemy, World world, List<Bomb> bombs, Vector2D playerPosition);

    /**
     * Decides whether the enemy should place a bomb.
     *
     * @param enemy          the enemy making the decision
     * @param world          the game world
     * @param bombs          list of active bombs
     * @param playerPosition the player's current position
     * @return true if the enemy should place a bomb
     */
    boolean shouldPlaceBomb(Enemy enemy, World world, List<Bomb> bombs, Vector2D playerPosition);
}
