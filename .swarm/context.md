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
| read | 77 | 77 | 0 | 188ms |
| bash | 41 | 41 | 0 | 1641ms |
| write | 31 | 31 | 0 | 415ms |
| glob | 24 | 24 | 0 | 404ms |
| task | 22 | 22 | 0 | 34264ms |
| update_task_status | 20 | 20 | 0 | 6ms |
| declare_scope | 11 | 11 | 0 | 1ms |
| syntax_check | 11 | 11 | 0 | 9ms |
| pre_check_batch | 10 | 10 | 0 | 7ms |
| apply_patch | 5 | 5 | 0 | 721ms |
| test_runner | 4 | 4 | 0 | 1613ms |
| todowrite | 3 | 3 | 0 | 1ms |
| search | 3 | 3 | 0 | 7ms |
| placeholder_scan | 2 | 2 | 0 | 3ms |
| check_gate_status | 2 | 2 | 0 | 1ms |
| lint_spec | 1 | 1 | 0 | 1ms |
| save_plan | 1 | 1 | 0 | 17ms |
| diff | 1 | 1 | 0 | 3ms |
