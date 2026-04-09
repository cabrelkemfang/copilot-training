# Copilot Instructions — Employee Service (Accenture Training Project)

> This file is part of the **Advanced GitHub Copilot Mastery** training.
> It is loaded automatically at the start of every Copilot Chat session.
> It demonstrates **Primitive 1: Always-On Instructions**.

---

## Project Overview

This is a **Java 21 / Spring Boot 3.x** REST microservice for managing employees.
It serves as a hands-on training project for GitHub Copilot advanced features.

**Modules:**
- `employee-service` — business logic library (service, repository, mapper, DTOs)
- `employee-launcher` — Spring Boot application, Flyway migrations, OpenAPI spec

---

## Tech Stack

- **Language:** Java 21 (use records, sealed classes, pattern matching where appropriate)
- **Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA, Spring Validation)
- **Database:** PostgreSQL (managed by Flyway migrations)
- **Persistence:** Spring Data JPA + Hibernate
- **Validation:** Jakarta Validation (`@Valid`, `@NotNull`, `@Email`, etc.)
- **Mapping:** MapStruct
- **Boilerplate reduction:** Lombok (`@Data`, `@Builder`, `@RequiredArgsConstructor`, `@Slf4j`)
- **Testing:** JUnit 5, Mockito, AssertJ, Spring Boot Test
- **Build:** Maven (multi-module)
- **API Docs:** OpenAPI 3 / Swagger (spec in `swagger/openapi.yaml`)

---

## Architecture & Layering Rules

Follow strict **n-tier architecture**: Controller → Service → Repository. Never skip layers.

```
Controller  →  Service Interface  →  Service Impl  →  Repository  →  Entity
                                                              ↑
                                                         Flyway Migration
```

- **Controllers** (`controller/`): HTTP mapping only. No business logic. Call service only.
- **Services** (`service/`): Business logic. Use interfaces (`EmployeeService`) + implementations in `impl/`.
- **Repositories** (`repository/`): Spring Data JPA only. No queries in service layer.
- **DTOs** (`dto/`): Use for API request/response. NEVER expose entities directly.
- **Entities** (`entity/`): JPA only. No business logic on entities.
- **Mappers** (`mapper/`): MapStruct. Never do manual mapping in service or controller.

### ✅ Correct Pattern

```java
// Controller delegates to service, returns DTO
@PostMapping
public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(request));
}

// Service uses mapper, never returns entity
public EmployeeResponse create(EmployeeRequest request) {
    Employee entity = employeeMapper.toEntity(request);
    return employeeMapper.toResponse(employeeRepository.save(entity));
}
```

### ❌ Incorrect Pattern

```java
// Never return entities from controllers
@GetMapping("/{id}")
public Employee findById(@PathVariable Long id) { ... }  // WRONG: exposes entity

// Never do business logic in controllers
@PostMapping
public ResponseEntity<EmployeeResponse> create(@RequestBody EmployeeRequest req) {
    if (employeeRepository.existsByEmail(req.getEmail())) { ... }  // WRONG: logic in controller
}
```

---

## Coding Standards

### General
- Use **constructor injection** only — Lombok `@RequiredArgsConstructor`. Never `@Autowired` on fields.
- Prefer **early returns** to reduce nesting.
- Maximum method length: **30 lines** in service/controller. Extract private helpers when exceeding.
- Name methods with verb + noun: `createEmployee`, `findById`, `validateEmail`.
- Use `@Slf4j` for logging. Log at `info` for incoming requests, `debug` for internal steps, `error` for exceptions.

### Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Package | lowercase | `com.accenture.employee.service` |
| Class | PascalCase | `EmployeeServiceImpl` |
| Method | camelCase verb+noun | `findById`, `createEmployee` |
| Variable | camelCase | `employeeResponse` |
| Constant | UPPER_SNAKE | `MAX_PAGE_SIZE` |
| REST endpoint | kebab-case, plural | `/api/employees`, `/api/departments` |

### Exception Handling
Use the custom exception hierarchy in `exception/` package:
- `EmployeeNotFoundException` → maps to `404 Not Found`
- `DuplicateEmailException` → maps to `409 Conflict`
- Never throw `RuntimeException` directly — use domain exceptions.
- Global handler lives in `exception/` — do not handle exceptions in controllers.

```java
// ✅ Correct
public EmployeeResponse findById(Long id) {
    return employeeRepository.findById(id)
        .map(employeeMapper::toResponse)
        .orElseThrow(() -> new EmployeeNotFoundException(id));
}

// ❌ Incorrect
public EmployeeResponse findById(Long id) {
    Employee emp = employeeRepository.findById(id).get(); // throws NoSuchElementException, not domain exception
    return employeeMapper.toResponse(emp);
}
```

### Validation
- Validate **at the controller boundary** using `@Valid` on `@RequestBody`.
- Put constraints on DTO fields (`@NotNull`, `@Email`, `@Size`, etc.).
- Never re-validate in the service layer.
- All input from external sources MUST be validated.

---

## Security Requirements

- **Never** log passwords, tokens, or sensitive PII fields.
- Use **parameterized queries** only (Spring Data JPA handles this — never use raw JPQL string concatenation).
- Validate all inputs at the API boundary — see Validation section above.
- Do not expose stack traces in API error responses — use structured error DTOs.
- Never commit secrets, API keys, or credentials. Use environment variables.

---

## Testing Requirements

- Use **JUnit 5** (`@ExtendWith(MockitoExtension.class)`) for unit tests.
- Use **AssertJ** (`assertThat(...)`) — never use JUnit `assertEquals` directly.
- Use **Mockito** for mocking dependencies.
- Follow **AAA pattern**: Arrange → Act → Assert with blank line separating each block.
- Test class naming: `EmployeeServiceTest`, `EmployeeControllerTest`.
- Test method naming: `should_[expected]_when_[condition]`.
- **Minimum 80% coverage** on service layer.
- Integration tests use `@SpringBootTest` with H2 in-memory database.

```java
// ✅ Correct test structure
@Test
void should_return_employee_when_valid_id() {
    // Arrange
    Employee employee = buildTestEmployee();
    when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

    // Act
    EmployeeResponse result = employeeService.findById(1L);

    // Assert
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getEmail()).isEqualTo("john.doe@accenture.com");
}
```

---

## Database & Migrations

- All schema changes MUST go through Flyway migrations in `db/migration/`.
- Migration naming: `V{n}__{description}.sql` (double underscore).
- Never alter existing migration files — create a new migration instead.
- Use `snake_case` for table and column names.

---

## What NOT to Do

- ❌ Do NOT use `var` where the type is not obvious from context
- ❌ Do NOT use `@Autowired` field injection (use constructor injection via Lombok)
- ❌ Do NOT return `null` from service methods — use `Optional` or throw a domain exception
- ❌ Do NOT use `System.out.println` — use SLF4J logger
- ❌ Do NOT put `@Transactional` on controller methods
- ❌ Do NOT import `*` (wildcard imports)
- ❌ Do NOT use Lombok `@Data` on JPA entities (causes issues with `equals`/`hashCode`)

---

## Why These Rules Exist

### Why MapStruct over manual mapping?
Manual mapping in service layers caused 3 production bugs in Q1 2025 due to missing field updates when new DTO fields were added. MapStruct generates compile-time checked mappers.

### Why interface + impl for services?
Enables mocking in unit tests without Spring context. Also follows the Dependency Inversion Principle — controllers depend on the abstraction, not the implementation.

### Why no entity exposure?
Entities are persistence models. Exposing them directly couples API consumers to database schema — making schema changes a breaking API change.
