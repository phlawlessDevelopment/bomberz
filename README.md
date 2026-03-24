# Bomberz

A terminal-based Bomberman clone built with Java and [Lanterna](https://github.com/mabe02/lanterna).

## Screenshots

```
███████████
█@.......&█
█.█.█.█.█.█
█.........█
█.█.█.█.█.█
█....●....█
█.█.█.█.█.█
█.........█
█.█.█.█.█.█
█&.......&█
███████████
```

## Controls

| Action | Keys |
|--------|------|
| Move Up | `W` / `↑` / `K` |
| Move Down | `S` / `↓` / `J` |
| Move Left | `A` / `←` / `H` |
| Move Right | `D` / `→` / `L` |
| Place Bomb | `Space` |
| Quit | `Esc` / `Ctrl+C` |

## Features

### Core Gameplay
- Classic Bomberman-style gameplay
- Place bombs that explode in a cross pattern
- Destroy destructible walls to clear paths
- Avoid explosions or it's game over

### Bombs & Explosions
- 3-second bomb timer
- Explosion radius of 2 tiles in each direction
- Chain reactions - explosions trigger nearby bombs
- Maximum of 3 active bombs per player

### Enemies
- 3 AI-controlled enemies per level
- Random movement with self-preservation instincts
- Enemies avoid walking into blast zones
- Enemies only place bombs when they have an escape route
- Enemies die when caught in explosions

### Technical Features
- Optimized rendering - only redraws changed tiles
- Game world centered in terminal
- Strategy pattern for enemy AI (easily extensible)
- Map generation interface for custom levels

## Building & Running

### Requirements
- Java 21+
- Maven

### Build
```bash
mvn package
```

### Run
```bash
java -jar target/bomberz-1.0-SNAPSHOT.jar
```

Or with Maven:
```bash
mvn exec:java -Dexec.mainClass="com.phlawless.bomberz.application.Main"
```

## Architecture

```
src/main/java/com/phlawless/bomberz/
├── application/          # Entry point
│   └── Main.java
├── core/
│   ├── domain/
│   │   ├── math/         # Vector2D, Direction
│   │   └── model/        # Entity, World, Bomb, Explosion, Enemy
│   └── logic/
│       ├── ai/           # EnemyAI interface, RandomEnemyAI
│       ├── engine/       # GameLoop
│       └── generation/   # MapGenerator, TestMapGenerator
└── infrastructure/
    └── lanterna/         # LanternaRenderer, LanternaInputSource
```

### Key Design Patterns
- **Strategy Pattern**: Enemy AI is swappable via `EnemyAI` interface
- **Template Method**: Abstract `Enemy` class with concrete `LanternaEnemy`
- **Dirty Region Rendering**: Only changed cells are redrawn for performance

## Legend

| Symbol | Description |
|--------|-------------|
| `@` | Player |
| `&` | Enemy |
| `█` | Solid wall (indestructible) |
| `#` | Destructible wall |
| `.` | Empty space |
| `●` | Bomb |
| `*` | Explosion |

## License

MIT
