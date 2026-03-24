package com.phlawless.bomberz.core.logic.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.phlawless.bomberz.core.domain.math.Direction;
import com.phlawless.bomberz.core.domain.math.Vector2D;
import com.phlawless.bomberz.core.domain.model.Bomb;
import com.phlawless.bomberz.core.domain.model.Enemy;
import com.phlawless.bomberz.core.domain.model.Explosion;
import com.phlawless.bomberz.core.domain.model.Tile;
import com.phlawless.bomberz.core.domain.model.World;
import com.phlawless.bomberz.core.domain.model.lanterna.LanternaEnemy;
import com.phlawless.bomberz.core.domain.model.lanterna.Player;
import com.phlawless.bomberz.core.logic.input.Command;
import com.phlawless.bomberz.core.logic.input.MoveCommand;
import com.phlawless.bomberz.core.logic.input.PlaceBombCommand;
import com.phlawless.bomberz.infrastructure.lanterna.LanternaInputSource;
import com.phlawless.bomberz.infrastructure.lanterna.LanternaRenderer;

/**
 * The main game loop that handles input, updates game state, and renders.
 * Only re-renders when game state changes for optimal performance.
 */
public class GameLoop {
	private static final long TARGET_FRAME_TIME_MS = 33; // ~30 FPS
	private static final long BOMB_TIMER_MS = 3000; // 3 seconds
	private static final long EXPLOSION_DURATION_MS = 500; // 0.5 seconds
	private static final int BOMB_RADIUS = 2;
	private static final int MAX_BOMBS_PER_PLAYER = 3;
	private static final int PLAYER_ID = 1; // Single player for now

	private final World world;
	private final Player player;
	private final LanternaRenderer renderer;
	private final LanternaInputSource inputSource;
	private final Screen screen;

	private final List<Bomb> activeBombs;
	private final List<Explosion> activeExplosions;
	private final List<LanternaEnemy> enemies;

	private boolean running;
	private boolean needsRender;
	private boolean initialRenderDone;
	private boolean gameOver;
	private long lastUpdateTime;

	public GameLoop(World world, Player player, List<LanternaEnemy> enemies,
			LanternaRenderer renderer, LanternaInputSource inputSource, Screen screen) {
		this.world = world;
		this.player = player;
		this.enemies = new ArrayList<>(enemies);
		this.renderer = renderer;
		this.inputSource = inputSource;
		this.screen = screen;
		this.activeBombs = new ArrayList<>();
		this.activeExplosions = new ArrayList<>();
		this.running = false;
		this.needsRender = false;
		this.initialRenderDone = false;
		this.gameOver = false;
		this.lastUpdateTime = System.currentTimeMillis();
	}

	/**
	 * Starts the game loop. Blocks until the game exits.
	 *
	 * @throws IOException if there is an error with screen I/O
	 */
	public void run() throws IOException {
		running = true;
		lastUpdateTime = System.currentTimeMillis();

		// Do initial full render before entering the main loop
		renderInitial();

		while (running) {
			long frameStart = System.currentTimeMillis();
			long deltaMs = frameStart - lastUpdateTime;
			lastUpdateTime = frameStart;

			// 1. Poll and process input
			processInput();

			// 2. Update game state (bombs, explosions, etc.)
			update(deltaMs);

			// 3. Render only if something changed
			render();

			// 4. Sleep to maintain target FPS
			long frameTime = System.currentTimeMillis() - frameStart;
			long sleepTime = TARGET_FRAME_TIME_MS - frameTime;
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					running = false;
				}
			}
		}
	}

	/**
	 * Stops the game loop.
	 */
	public void stop() {
		running = false;
	}

	/**
	 * Performs the initial full render of the world, enemies, and player.
	 * Called once at startup.
	 */
	private void renderInitial() throws IOException {
		renderer.render(world);
		for (LanternaEnemy enemy : enemies) {
			renderer.drawEnemy(enemy);
		}
		renderer.draw(player);
		renderer.refresh();
		initialRenderDone = true;
	}

	/**
	 * Polls input and processes all commands.
	 */
	private void processInput() throws IOException {
		KeyStroke keyStroke;
		while ((keyStroke = screen.pollInput()) != null) {
			if (isExitKey(keyStroke)) {
				running = false;
				return;
			}

			// Don't process game commands when game is over
			if (gameOver) {
				continue;
			}

			Command command = translateKeyStroke(keyStroke);
			if (command != null) {
				executeCommand(command);
			}
		}
	}

	/**
	 * Checks if the keystroke is an exit command.
	 */
	private boolean isExitKey(KeyStroke keyStroke) {
		if (keyStroke.getKeyType() == KeyType.Escape) {
			return true;
		}
		if (keyStroke.getKeyType() == KeyType.Character
				&& keyStroke.getCharacter() == 'c'
				&& keyStroke.isCtrlDown()) {
			return true;
		}
		return false;
	}

	/**
	 * Translates a keystroke to a command.
	 */
	private Command translateKeyStroke(KeyStroke keyStroke) {
		return switch (keyStroke.getKeyType()) {
			case ArrowUp -> new MoveCommand(Direction.UP);
			case ArrowDown -> new MoveCommand(Direction.DOWN);
			case ArrowLeft -> new MoveCommand(Direction.LEFT);
			case ArrowRight -> new MoveCommand(Direction.RIGHT);
			case Character -> translateCharacter(keyStroke.getCharacter());
			default -> null;
		};
	}

	/**
	 * Translates character keys to commands.
	 */
	private Command translateCharacter(char c) {
		return switch (Character.toLowerCase(c)) {
			case 'w' -> new MoveCommand(Direction.UP);
			case 's' -> new MoveCommand(Direction.DOWN);
			case 'a' -> new MoveCommand(Direction.LEFT);
			case 'd' -> new MoveCommand(Direction.RIGHT);
			// vim keys
			case 'h' -> new MoveCommand(Direction.LEFT);
			case 'j' -> new MoveCommand(Direction.DOWN);
			case 'k' -> new MoveCommand(Direction.UP);
			case 'l' -> new MoveCommand(Direction.RIGHT);
			case ' ' -> new PlaceBombCommand();
			default -> null;
		};
	}

	/**
	 * Executes a command.
	 */
	private void executeCommand(Command command) {
		if (command instanceof MoveCommand moveCommand) {
			movePlayer(moveCommand.getDirection());
		} else if (command instanceof PlaceBombCommand) {
			placeBomb();
		}
	}

	/**
	 * Moves the player in the specified direction if the target tile is walkable
	 * and not blocked by a bomb (unless they're standing on it - classic rules).
	 */
	private void movePlayer(Direction direction) {
		Vector2D oldPos = player.getPosition();
		Vector2D newPos = oldPos.add(direction.toVector());

		// Check bounds
		if (!world.isInBounds(newPos.x(), newPos.y())) {
			return;
		}

		// Check tile walkability
		Tile targetTile = world.getTile(newPos.x(), newPos.y());
		if (targetTile != Tile.EMPTY) {
			return;
		}

		// Check for bomb collision (classic rules: can't walk onto bombs)
		if (hasBombAt(newPos)) {
			return;
		}

		// Check for active explosion (walking into explosion = death, but for now just
		// block)
		if (hasExplosionAt(newPos)) {
			return;
		}

		// Move is valid
		renderer.markDirty(oldPos);
		player.setPosition(newPos);
		needsRender = true;
	}

	/**
	 * Places a bomb at the player's current position if allowed.
	 */
	private void placeBomb() {
		Vector2D playerPos = player.getPosition();

		// Check bomb limit for this player
		long playerBombCount = activeBombs.stream()
				.filter(b -> b.getOwnerId() == PLAYER_ID)
				.count();
		if (playerBombCount >= MAX_BOMBS_PER_PLAYER) {
			return;
		}

		// Check if there's already a bomb at this position
		if (hasBombAt(playerPos)) {
			return;
		}

		// Place the bomb
		Bomb bomb = new Bomb(playerPos, BOMB_RADIUS, BOMB_TIMER_MS, PLAYER_ID);
		activeBombs.add(bomb);
		renderer.markDirty(playerPos);
		needsRender = true;
	}

	/**
	 * Checks if there's a bomb at the specified position.
	 */
	private boolean hasBombAt(Vector2D pos) {
		return activeBombs.stream()
				.anyMatch(b -> b.getPosition().equals(pos));
	}

	/**
	 * Checks if there's an active explosion at the specified position.
	 */
	private boolean hasExplosionAt(Vector2D pos) {
		return activeExplosions.stream()
				.anyMatch(e -> e.getAffectedPositions().contains(pos));
	}

	/**
	 * Gets the bomb at the specified position, if any.
	 */
	private Bomb getBombAt(Vector2D pos) {
		return activeBombs.stream()
				.filter(b -> b.getPosition().equals(pos))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Updates game state (bombs, explosions, enemies, etc.).
	 */
	private void update(long deltaMs) {
		if (gameOver) {
			return;
		}

		updateBombs(deltaMs);
		updateExplosions(deltaMs);
		updateEnemies(deltaMs);
		checkPlayerDeath();
		checkEnemyDeaths();
	}

	/**
	 * Checks if the player is caught in an explosion and triggers game over.
	 */
	private void checkPlayerDeath() {
		if (hasExplosionAt(player.getPosition())) {
			gameOver = true;
			needsRender = true;
		}
	}

	/**
	 * Checks if any enemies are caught in explosions and kills them.
	 */
	private void checkEnemyDeaths() {
		for (LanternaEnemy enemy : enemies) {
			if (enemy.isAlive() && hasExplosionAt(enemy.getPosition())) {
				enemy.kill();
				renderer.markDirty(enemy.getPosition());
				needsRender = true;
			}
		}
	}

	/**
	 * Updates all enemies - movement and bomb placement.
	 */
	private void updateEnemies(long deltaMs) {
		for (LanternaEnemy enemy : enemies) {
			if (!enemy.isAlive()) {
				continue;
			}

			// Check if enemy can move this tick
			if (enemy.canMove(deltaMs)) {
				// Get AI decision for movement
				Direction moveDir = enemy.getAI().decideMove(
						enemy, world, activeBombs, player.getPosition());

				if (moveDir != null) {
					moveEnemy(enemy, moveDir);
				}

				// Check if enemy should place a bomb
				if (enemy.getAI().shouldPlaceBomb(enemy, world, activeBombs, player.getPosition())) {
					placeEnemyBomb(enemy);
				}
			}
		}
	}

	/**
	 * Moves an enemy in the specified direction.
	 */
	private void moveEnemy(LanternaEnemy enemy, Direction direction) {
		Vector2D oldPos = enemy.getPosition();
		Vector2D newPos = oldPos.add(direction.toVector());

		// Validate move (AI should have checked, but double-check)
		if (!world.isInBounds(newPos.x(), newPos.y())) {
			return;
		}

		Tile targetTile = world.getTile(newPos.x(), newPos.y());
		if (targetTile != Tile.EMPTY) {
			return;
		}

		if (hasBombAt(newPos)) {
			return;
		}

		// Move is valid
		renderer.markDirty(oldPos);
		enemy.moveTo(newPos);
		needsRender = true;
	}

	/**
	 * Places a bomb for an enemy at its current position.
	 */
	private void placeEnemyBomb(LanternaEnemy enemy) {
		Vector2D enemyPos = enemy.getPosition();

		// Check bomb limit for this enemy
		int enemyId = getEnemyOwnerId(enemy);
		long enemyBombCount = activeBombs.stream()
				.filter(b -> b.getOwnerId() == enemyId)
				.count();
		if (enemyBombCount >= enemy.getMaxBombs()) {
			return;
		}

		// Check if there's already a bomb at this position
		if (hasBombAt(enemyPos)) {
			return;
		}

		// Place the bomb
		Bomb bomb = new Bomb(enemyPos, enemy.getBombRadius(), BOMB_TIMER_MS, enemyId);
		activeBombs.add(bomb);
		renderer.markDirty(enemyPos);
		needsRender = true;
	}

	/**
	 * Gets a unique owner ID for an enemy (offset from player IDs).
	 */
	private int getEnemyOwnerId(Enemy enemy) {
		return 1000 + enemy.getId(); // Offset to avoid collision with player IDs
	}

	/**
	 * Updates all active bombs, triggering explosions when timers expire.
	 */
	private void updateBombs(long deltaMs) {
		List<Bomb> bombsToExplode = new ArrayList<>();

		// Tick all bombs and collect those that should explode
		for (Bomb bomb : activeBombs) {
			if (bomb.tick(deltaMs)) {
				bombsToExplode.add(bomb);
			}
		}

		// Process explosions (may trigger chain reactions)
		for (Bomb bomb : bombsToExplode) {
			explodeBomb(bomb);
		}

		// Remove exploded bombs
		activeBombs.removeIf(Bomb::isExploded);
	}

	/**
	 * Explodes a bomb, creating an explosion and handling chain reactions.
	 */
	private void explodeBomb(Bomb bomb) {
		if (bomb.isExploded() && !activeBombs.contains(bomb)) {
			return; // Already processed
		}

		bomb.detonate();

		// Calculate affected positions
		List<Vector2D> affectedPositions = Explosion.calculateAffectedPositions(bomb, world);

		// Create explosion
		Explosion explosion = new Explosion(affectedPositions, EXPLOSION_DURATION_MS);
		activeExplosions.add(explosion);

		// Mark all affected positions dirty
		for (Vector2D pos : affectedPositions) {
			renderer.markDirty(pos);

			// Destroy destructible walls
			if (world.isInBounds(pos.x(), pos.y())) {
				Tile tile = world.getTile(pos.x(), pos.y());
				if (tile == Tile.DESTRUCTIBLE_WALL) {
					world.setTile(pos.x(), pos.y(), Tile.EMPTY);
				}
			}

			// Chain reaction: detonate any bombs caught in the explosion
			Bomb chainBomb = getBombAt(pos);
			if (chainBomb != null && !chainBomb.isExploded()) {
				explodeBomb(chainBomb);
			}
		}

		needsRender = true;
	}

	/**
	 * Updates all active explosions, removing finished ones.
	 */
	private void updateExplosions(long deltaMs) {
		Iterator<Explosion> iterator = activeExplosions.iterator();
		while (iterator.hasNext()) {
			Explosion explosion = iterator.next();
			if (explosion.tick(deltaMs)) {
				// Explosion finished - mark affected positions dirty to restore tiles
				for (Vector2D pos : explosion.getAffectedPositions()) {
					renderer.markDirty(pos);
				}
				iterator.remove();
				needsRender = true;
			}
		}
	}

	/**
	 * Renders only if something has changed since the last render.
	 * Redraws only dirty cells, then draws entities on top.
	 */
	private void render() throws IOException {
		if (!needsRender) {
			return;
		}

		if (gameOver) {
			renderGameOver();
			needsRender = false;
			return;
		}

		// Redraw only the dirty cells (e.g., where the player was)
		renderer.renderDirty(world);

		// Draw bombs
		for (Bomb bomb : activeBombs) {
			renderer.drawBomb(bomb);
		}

		// Draw explosions
		for (Explosion explosion : activeExplosions) {
			renderer.drawExplosion(explosion);
		}

		// Draw enemies
		for (LanternaEnemy enemy : enemies) {
			if (enemy.isAlive()) {
				renderer.drawEnemy(enemy);
			}
		}

		// Draw player on top
		renderer.draw(player);

		// Push changes to the terminal
		renderer.refresh();

		needsRender = false;
	}

	/**
	 * Renders the game over screen.
	 */
	private void renderGameOver() throws IOException {
		renderer.clear();
		renderer.drawGameOver(world.getWidth(), world.getHeight());
		renderer.refresh();
	}
}
