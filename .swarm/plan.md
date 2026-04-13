<!-- PLAN_HASH: 3lsdwvqb4ccx5 -->
# Aegis-AutoChess Core Engine
Swarm: default
Phase: 1 [COMPLETE] | Updated: 2026-04-13T22:06:25.278Z

---
## Phase 1: Project Scaffolding, Domain Model, and Data Layer [COMPLETE]
- [x] 1.1: Initialize Spring Boot 3.x Maven project with dependencies: spring-boot-starter-web, spring-boot-starter-data-jpa, SQLite JDBC driver, springdoc-openapi, and JUnit 5. Configure application.yml for SQLite datasource and JPA/Hibernate dialect. (FR-018, FR-031) [MEDIUM]
- [x] 1.2: Create JPA entity for Unit with fields: id, name, tier, cost, traits (comma-separated string). Add Hibernate SQLite dialect configuration. (FR-018) [SMALL] (depends: 1.1)
- [x] 1.3: Create JPA entity for LevelTierProbability with fields: id, playerLevel, tier, weight. This stores the probability matrix rows. (FR-019) [SMALL] (depends: 1.1)
- [x] 1.4: Create JPA entity for Player with fields: id, name, gold, level. Player represents the in-game player state. (FR-008, FR-009, FR-011, FR-012) [SMALL] (depends: 1.1)
- [x] 1.5: Create Spring Data JPA repositories: UnitRepository, LevelTierProbabilityRepository (with findByPlayerLevel query), and PlayerRepository. (FR-018, FR-019) [SMALL] (depends: 1.2, 1.3, 1.4)
- [x] 1.6: Create DTO classes: UnitDto, ShopOfferDto, PoolStatusDto, SynergyResultDto, PurchaseRequestDto, and ErrorResponseDto. All API responses use DTOs, never entities. (FR-020) [SMALL] (depends: 1.1)
- [x] 1.7: Create SQL schema initialization script (schema.sql) and data seed script (data.sql) with unit definitions (at least 3 units per tier across 5 tiers), tier copy limits as constants, and the level-to-tier probability matrix for levels 1-9. (FR-004, FR-018, FR-019, FR-029) [MEDIUM] (depends: 1.2, 1.3)
- [x] 1.8: Create custom exception classes: InsufficientGoldException, UnitPoolExhaustedException, UnitNotFoundException, InvalidPurchaseException. Each extends RuntimeException with a message field. (FR-026) [SMALL] (depends: 1.1)
- [x] 1.9: Create GlobalExceptionHandler using @ControllerAdvice that catches all custom exceptions and returns structured JSON ErrorResponseDto with HTTP status codes (400 for business errors, 404 for not found, 409 for conflicts). (FR-025) [SMALL] (depends: 1.6, 1.8)
- [x] 1.10: Create GameConstants class with named constants for tier copy limits (TIER_1_COPIES=29, TIER_2_COPIES=22, TIER_3_COPIES=18, TIER_4_COPIES=12, TIER_5_COPIES=10), shop size (SHOP_SIZE=5), and roll cost (ROLL_COST=2). No magic numbers. (FR-004, FR-005, FR-008, FR-029) [SMALL] (depends: 1.1)

---
## Phase 2: Core Game Engine Services [PENDING]
- [ ] 2.1: Implement GlobalUnitPool @Component with ConcurrentHashMap-based thread-safe storage. Constructor loads all units from UnitRepository and initializes counts based on tier copy limits from GameConstants. Provides acquire(unitId) and release(unitId) atomic operations using AtomicInteger. (FR-001, FR-002, FR-003, FR-004, FR-027) [MEDIUM] (depends: 1.10, 1.5)
- [ ] 2.2: Implement ShopService with weighted tier rolling logic. Method rollShop(playerId): loads player, deducts roll cost (2 gold), fetches probability matrix for player level, performs weighted random tier selection 5 times, selects random available unit from each tier via GlobalUnitPool. Returns list of ShopOfferDto. (FR-005, FR-006, FR-007, FR-008, FR-009, FR-027) [MEDIUM] (depends: 1.5, 1.6, 2.1)
- [ ] 2.3: Implement PurchaseService to handle unit purchases. Method purchaseUnit(playerId, unitId): validates player has sufficient gold, validates unit is available in pool, atomically acquires from pool, deducts gold (cost = tier), adds unit to player bench. (FR-010, FR-011, FR-012, FR-013, FR-027) [MEDIUM] (depends: 1.5, 1.6, 1.8, 2.1)
- [ ] 2.4: Create SynergyStrategy interface with method calculateBonus(int unitCount) returning SynergyBonus record. Implement four concrete strategies: WarriorSynergy (armor bonus at 2/4/6 thresholds), MageSynergy (spell power bonus), AssassinSynergy (critical strike bonus), RangerSynergy (attack speed bonus). Each is a @Component. (FR-015, FR-016, FR-017, FR-027) [MEDIUM] (depends: 1.1)
- [ ] 2.5: Implement SynergyEngine @Service that receives all SynergyStrategy implementations via constructor injection (List<SynergyStrategy>). Method calculateSynergies(List<UnitDto> boardUnits): counts units per trait, applies matching strategy for each trait, returns list of SynergyResultDto with active synergies and their bonuses. (FR-014, FR-015, FR-027) [MEDIUM] (depends: 1.6, 2.4)

---
## Phase 3: REST API Controllers and OpenAPI Integration [PENDING]
- [ ] 3.1: Implement ShopController with GET /api/shop/roll/{playerId} endpoint that delegates to ShopService.rollShop() and returns list of ShopOfferDto. Add OpenAPI annotations for Swagger documentation. (FR-021, FR-031) [SMALL] (depends: 2.2)
- [ ] 3.2: Implement PoolController with GET /api/pool/status endpoint that delegates to GlobalUnitPool to retrieve current counts and returns PoolStatusDto organized by tier. Add OpenAPI annotations. (FR-022, FR-031) [SMALL] (depends: 2.1)
- [ ] 3.3: Implement BenchController with POST /api/bench/purchase endpoint that accepts PurchaseRequestDto (playerId, unitId), delegates to PurchaseService, and returns the purchased UnitDto. Add OpenAPI annotations. (FR-023, FR-031) [SMALL] (depends: 2.3)
- [ ] 3.4: Implement BoardController with GET /api/board/synergies endpoint that accepts a list of unit IDs as query parameters, resolves units, delegates to SynergyEngine, and returns list of SynergyResultDto. Add OpenAPI annotations. (FR-024, FR-031) [SMALL] (depends: 2.5)
- [ ] 3.5: Configure SpringDoc OpenAPI bean with API title 'Aegis-AutoChess API', version, and description. Verify Swagger UI is accessible at /swagger-ui.html. (FR-031) [SMALL] (depends: 3.1, 3.2, 3.3, 3.4)

---
## Phase 4: JUnit 5 Test Suite and Final Verification [PENDING]
- [ ] 4.1: Write JUnit 5 tests for GlobalUnitPool: test acquire decrements count, test acquire returns false at zero, test release increments count, test release never exceeds max, test concurrent acquire/release with multiple threads (ExecutorService with 100 threads) verifying no count goes below 0 or above max. (FR-030, SC-002) [MEDIUM] (depends: 2.1)
- [ ] 4.2: Write JUnit 5 tests for ShopService: test rollShop returns exactly 5 offers, test tier distribution matches probability matrix over 1000 rolls (statistical tolerance), test insufficient gold throws InsufficientGoldException, test pool exhaustion handling. (FR-030, SC-001) [MEDIUM] (depends: 2.2)
- [ ] 4.3: Write JUnit 5 tests for SynergyEngine: test empty board returns no synergies, test single-trait board at each threshold breakpoint, test multi-trait board with overlapping synergies, test adding a new synergy strategy requires no modification to engine. (SC-004, SC-007) [MEDIUM] (depends: 2.5)
- [ ] 4.4: Write JUnit 5 integration test for the purchase flow: create player with gold, roll shop, purchase unit, verify pool count decremented, verify player gold deducted, verify purchasing unavailable unit returns error. Uses @SpringBootTest. (SC-003, SC-005, SC-007) [MEDIUM] (depends: 3.3)
- [ ] 4.5: Run full Maven build (mvn clean verify) to confirm all tests pass, application starts, and Swagger UI is accessible. Fix any remaining compilation or test issues. (SC-006, SC-007) [SMALL] (depends: 4.1, 4.2, 4.3, 4.4)
