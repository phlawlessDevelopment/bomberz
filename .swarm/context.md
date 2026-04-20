# Context
Swarm: default

## Project Context
Language: Java 21
Framework: Spring Boot 3.x
Build command: mvn clean verify
Test command: mvn test
Lint command: mvn checkstyle:check (if configured)
Entry points: src/main/java/**/AegisAutoChessApplication.java

## Decisions
- Maven over Gradle: Maven is more conventional for Spring Boot enterprise projects and has better IDE support
- SQLite via Hibernate community dialect: Using org.xerial:sqlite-jdbc driver with community Hibernate dialect
- Java records for DTOs: Leveraging Java 21 records for immutable DTOs
- ConcurrentHashMap + AtomicInteger for pool: Best thread-safety pattern for the shared unit pool
- Strategy Pattern for synergies: Each synergy type is a @Component implementing SynergyStrategy interface

## Patterns
- Constructor-based DI only: All @Service/@Component classes use constructor injection, no @Autowired on fields
- DTO pattern: JPA entities never exposed via REST API, always mapped to DTOs
- Global exception handler: @ControllerAdvice catches all custom exceptions
- Named constants: GameConstants class holds all balancing parameters

## Phase Metrics
- total_tool_calls: 0
- coder_revisions: 0
- reviewer_rejections: 0
- test_failures: 0
- security_findings: 0
- integration_issues: 0

## Notes
- Subagent models (sme, critic, reviewer, test_engineer, coder) returning ProviderModelNotFoundError
- May need to implement directly if coder agent is also unavailable

## Agent Activity

| Tool | Calls | Success | Failed | Avg Duration |
|------|-------|---------|--------|--------------|
| read | 105 | 105 | 0 | 138ms |
| bash | 59 | 59 | 0 | 1302ms |
| write | 41 | 41 | 0 | 423ms |
| glob | 33 | 33 | 0 | 295ms |
| update_task_status | 28 | 28 | 0 | 5ms |
| task | 27 | 27 | 0 | 32323ms |
| declare_scope | 17 | 17 | 0 | 1ms |
| syntax_check | 15 | 15 | 0 | 8ms |
| pre_check_batch | 13 | 13 | 0 | 7ms |
| apply_patch | 5 | 5 | 0 | 721ms |
| todowrite | 4 | 4 | 0 | 2ms |
| search | 4 | 4 | 0 | 7ms |
| test_runner | 4 | 4 | 0 | 1613ms |
| grep | 3 | 3 | 0 | 8ms |
| placeholder_scan | 2 | 2 | 0 | 3ms |
| check_gate_status | 2 | 2 | 0 | 1ms |
| lint_spec | 1 | 1 | 0 | 1ms |
| save_plan | 1 | 1 | 0 | 17ms |
| diff | 1 | 1 | 0 | 3ms |
| write_retro | 1 | 1 | 0 | 4ms |
| phase_complete | 1 | 1 | 0 | 26ms |
| checkpoint | 1 | 1 | 0 | 7ms |
| edit | 1 | 1 | 0 | 794ms |
