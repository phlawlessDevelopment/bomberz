package com.phlawless.bomberz.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.phlawless.bomberz.core.domain.math.Vector2D;
import com.phlawless.bomberz.core.domain.model.World;
import com.phlawless.bomberz.core.domain.model.lanterna.LanternaEnemy;
import com.phlawless.bomberz.core.domain.model.lanterna.Player;
import com.phlawless.bomberz.core.logic.ai.RandomEnemyAI;
import com.phlawless.bomberz.core.logic.engine.GameLoop;
import com.phlawless.bomberz.core.logic.generation.GeneratedMap;
import com.phlawless.bomberz.core.logic.generation.MapGenerator;
import com.phlawless.bomberz.core.logic.generation.TestMapGenerator;
import com.phlawless.bomberz.infrastructure.lanterna.LanternaInputSource;
import com.phlawless.bomberz.infrastructure.lanterna.LanternaRenderer;

public class Main {
    private static final int MAP_WIDTH = 11;
    private static final int MAP_HEIGHT = 11;
    private static final int NUM_ENEMIES = 3;

    public static void main(String[] args) {
        try {
            // 1. Create Lanterna screen
            Screen screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();
            screen.setCursorPosition(null); // Hide cursor

            // 2. Generate world using map generator
            MapGenerator generator = new TestMapGenerator();
            GeneratedMap generatedMap = generator.generate(MAP_WIDTH, MAP_HEIGHT);
            World world = generatedMap.getWorld();

            // 3. Create player at spawn point with '@' sprite
            Vector2D playerSpawn = generatedMap.getSpawnPoint(0); // Player 1 spawn
            Player player = new Player();
            player.setPosition(playerSpawn);
            player.setSprite(TextCharacter.fromCharacter('@')[0]);

            // 4. Create enemies at remaining spawn points
            List<LanternaEnemy> enemies = createEnemies(generatedMap, NUM_ENEMIES);

            // 5. Create renderer and input source
            LanternaRenderer renderer = new LanternaRenderer(screen);
            LanternaInputSource inputSource = new LanternaInputSource(screen);

            // 6. Create and run game loop
            GameLoop gameLoop = new GameLoop(world, player, enemies, renderer, inputSource, screen);
            gameLoop.run();

            // 7. Cleanup
            screen.stopScreen();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates enemies at spawn points 1, 2, 3, etc. (0 is reserved for player).
     *
     * @param generatedMap the generated map with spawn points
     * @param count        the number of enemies to create
     * @return list of enemies
     */
    private static List<LanternaEnemy> createEnemies(GeneratedMap generatedMap, int count) {
        List<LanternaEnemy> enemies = new ArrayList<>();

        // Use spawn points 1+ for enemies (0 is player)
        for (int i = 1; i <= count && i < generatedMap.getMaxPlayers(); i++) {
            Vector2D spawnPoint = generatedMap.getSpawnPoint(i);
            if (spawnPoint != null) {
                LanternaEnemy enemy = new LanternaEnemy(new RandomEnemyAI());
                enemy.setPosition(spawnPoint);
                enemies.add(enemy);
            }
        }

        return enemies;
    }
}
