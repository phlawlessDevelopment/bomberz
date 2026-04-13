# Specification: Aegis-AutoChess Core Engine

## Feature Description

### What
A headless, production-grade backend engine for an Auto-Chess game that manages a shared unit economy, data-driven shop mechanics, and a composable synergy bonus system. The system serves as a Clean Code showcase with strict adherence to SOLID principles and enterprise design patterns.

### Why
Auto-Chess games require a reliable, concurrent backend that fairly manages a finite shared resource pool across multiple players. The engine must guarantee atomicity of unit transactions, data-driven configurability of game balance parameters, and extensibility of synergy mechanics without modifying existing code.

## User Scenarios

### Scenario 1: Player Rolls Shop
**Given** a player at a specific level with sufficient gold  
**When** the player requests a shop roll  
**Then** the system generates a selection of units where each unit's tier is determined by the player's level-to-tier probability matrix, and each unit is drawn from the remaining stock in the global pool  

### Scenario 2: Player Purchases a Unit
**Given** a player viewing their current shop selection  
**When** the player purchases a specific unit  
**Then** the unit is atomically removed from the global pool and added to the player's bench, the player's gold is deducted, and the shop slot is marked as purchased  

### Scenario 3: Pool Exhaustion During Roll
**Given** a tier in the global pool has zero remaining units of any type  
**When** a shop roll targets that tier  
**Then** the system gracefully handles the empty tier by either selecting from another available tier or returning fewer units in the shop selection  

### Scenario 4: Synergy Calculation
**Given** a player's board contains multiple units sharing traits  
**When** synergies are calculated for the board  
**Then** each active synergy applies its bonus according to the number of matching units and the synergy's threshold breakpoints (e.g., 2/4/6 units)  

### Scenario 5: Unit Return to Pool
**Given** a player sells or loses a unit  
**When** the unit is released  
**Then** the unit's count is atomically incremented back in the global pool, making it available for other players  

### Scenario 6: Concurrent Shop Rolls
**Given** multiple players request shop rolls simultaneously  
**When** the global pool processes these requests  
**Then** no unit is over-allocated beyond its maximum count, and all operations complete without deadlock or data corruption  

### Scenario 7: Pool Status Inspection
**Given** a game administrator or debugging session  
**When** the pool status endpoint is queried  
**Then** the system returns the current count of every unit in the global pool organized by tier  

## Functional Requirements

### Global Unit Pool
- **FR-001**: The system MUST maintain a global unit pool as a thread-safe shared resource where each unit type has a finite maximum quantity determined by its tier.
- **FR-002**: The global pool MUST support atomic acquire operations that decrement a unit's available count and fail gracefully if the count is zero.
- **FR-003**: The global pool MUST support atomic release operations that increment a unit's available count and never exceed the tier's maximum.
- **FR-004**: The system MUST use the following tier-based copy limits: Tier 1 = 29 copies, Tier 2 = 22 copies, Tier 3 = 18 copies, Tier 4 = 12 copies, Tier 5 = 10 copies.

### Shop Service
- **FR-005**: The shop service MUST generate a selection of exactly 5 unit offers per roll.
- **FR-006**: The shop service MUST determine each offered unit's tier using a weighted random selection based on the player's current level, with probabilities loaded from the database.
- **FR-007**: The shop service MUST select a specific unit within the rolled tier randomly from units that have remaining stock in the global pool.
- **FR-008**: The shop roll MUST cost 2 gold, deducted before generating the selection.
- **FR-009**: The system MUST reject shop rolls when the player has insufficient gold, returning a clear error.

### Purchase Mechanics
- **FR-010**: The system MUST support purchasing a unit from the player's current shop selection, atomically acquiring it from the global pool and adding it to the player's bench.
- **FR-011**: Each unit MUST have a purchase cost equal to its tier level (Tier 1 = 1 gold, Tier 2 = 2 gold, etc.).
- **FR-012**: The system MUST reject purchases when the player has insufficient gold.
- **FR-013**: The system MUST reject purchases for units no longer available in the global pool (race condition between roll and purchase).

### Synergy Engine
- **FR-014**: The synergy engine MUST calculate active synergies based on the traits of units placed on a player's board.
- **FR-015**: Each synergy type MUST be implemented as an interchangeable strategy that can be added or replaced without modifying existing synergy code (Strategy Pattern).
- **FR-016**: Synergies MUST activate at defined threshold breakpoints (e.g., 2/4/6 matching units) with increasing bonus levels.
- **FR-017**: The system MUST support at least the following synergy types at launch: Warrior (armor bonus), Mage (spell power bonus), Assassin (critical strike bonus), Ranger (attack speed bonus).

### Data Layer
- **FR-018**: All unit definitions (name, tier, cost, traits) MUST be stored in and loaded from a SQLite database.
- **FR-019**: Level-to-tier probability matrices MUST be stored in and loaded from a SQLite database, not hardcoded.
- **FR-020**: The system MUST use the DTO pattern for all API responses — JPA entities MUST NOT be exposed directly.

### REST API
- **FR-021**: The system MUST expose a GET endpoint to trigger a shop roll for a given player.
- **FR-022**: The system MUST expose a GET endpoint to view the current global pool status.
- **FR-023**: The system MUST expose a POST endpoint to purchase a unit from a player's shop.
- **FR-024**: The system MUST expose a GET endpoint to calculate synergies for a given board state.

### Error Handling
- **FR-025**: The system MUST use a global exception handler that returns structured JSON error responses with appropriate HTTP status codes.
- **FR-026**: The system MUST define custom exception types for domain-specific errors (insufficient gold, pool exhausted, unit not found, invalid purchase).

### Code Quality
- **FR-027**: All Spring beans MUST use constructor-based dependency injection exclusively (no field or setter injection).
- **FR-028**: No method MUST exceed 20 lines of code.
- **FR-029**: All numeric balancing parameters MUST be defined as named constants or externalized configuration — no magic numbers.
- **FR-030**: The system MUST include JUnit 5 tests for shop roll logic and global pool concurrency.

### API Documentation
- **FR-031**: The system MUST integrate SpringDoc OpenAPI to provide interactive Swagger UI for all endpoints.

## Success Criteria

- **SC-001**: A shop roll for a level-5 player returns exactly 5 unit offers with tier distribution matching the configured probability matrix within statistical tolerance.
- **SC-002**: Under concurrent load (simulated via multi-threaded test), the global pool never allows a unit's count to go below zero or above its tier maximum.
- **SC-003**: Purchasing a unit that another player acquired between roll and purchase returns a clear error response, not a server error.
- **SC-004**: Adding a new synergy type requires creating only one new class implementing the synergy strategy interface, with zero modifications to existing classes.
- **SC-005**: All API endpoints return DTO objects, never JPA entity objects.
- **SC-006**: The Swagger UI is accessible and documents all endpoints with request/response schemas.
- **SC-007**: All JUnit 5 tests pass, covering shop roll distribution, pool atomicity, purchase flow, and synergy calculation.

## Key Entities

- Unit (game piece with name, tier, cost, and traits)
- Player (has gold, level, bench, board, and current shop selection)
- Synergy (trait-based bonus with threshold breakpoints)
- Shop Selection (set of unit offers generated per roll)
- Probability Matrix (level-to-tier weight mappings)

## Edge Cases and Failure Modes

- **Pool exhaustion**: All copies of a specific unit are acquired — shop must skip that unit and select another from the same tier, or handle empty tiers gracefully.
- **Complete tier exhaustion**: All units of an entire tier are acquired — shop must handle this without error, potentially offering fewer than 5 units or substituting from adjacent tiers.
- **Race condition on purchase**: Two players roll the same unit, one purchases first — second purchase must fail cleanly.
- **Concurrent pool modifications**: Multiple threads acquiring/releasing simultaneously — must never corrupt counts.
- **Player at max level**: Probability matrix must have entries for all valid player levels.
- **Negative gold prevention**: Gold balance must never go below zero through any operation sequence.
- **Empty board synergy check**: Calculating synergies on an empty board must return an empty result, not an error.
