# Copilot Training – Employee API

A Spring Boot REST API for managing employees, built as a multi-module Maven project. This project is used as a training reference for GitHub Copilot practices at Accenture.

## Project Structure

```
copilot-training/
├── employee-service/       # Business logic, DTOs, entities, repository, mapper
└── employee-launcher/      # Spring Boot entry point, Flyway migrations, configuration
```

## Tech Stack

| Layer         | Technology                          |
|---------------|-------------------------------------|
| Language      | Java 21                             |
| Framework     | Spring Boot 4.x                     |
| Persistence   | Spring Data JPA + Hibernate         |
| Database      | MySQL (production), H2 (local)      |
| Migrations    | Flyway                              |
| Mapping       | MapStruct                           |
| Validation    | Jakarta Bean Validation             |
| Boilerplate   | Lombok                              |

## API Endpoints

Base path: `/api/employees`

| Method | Path          | Description                      |
|--------|---------------|----------------------------------|
| POST   | `/`           | Create a new employee            |
| GET    | `/{id}`       | Get an employee by ID            |
| GET    | `/`           | List employees (paginated)       |
| PUT    | `/{id}`       | Update an employee               |
| DELETE | `/{id}`       | Delete an employee               |

### Pagination parameters (GET `/`)

| Parameter  | Default     | Description                        |
|------------|-------------|------------------------------------|
| `page`     | `0`         | Page number (0-based)              |
| `size`     | `10`        | Page size (1–100)                  |
| `sortBy`   | `createdAt` | Field to sort by                   |
| `direction`| `asc`       | Sort direction (`asc` or `desc`)   |

### Employee fields

| Field        | Type       | Constraints              |
|--------------|------------|--------------------------|
| firstName    | String     | Required                 |
| lastName     | String     | Required                 |
| email        | String     | Required, valid email    |
| phoneNumber  | String     | Required                 |
| department   | Department | Required (see enum below)|

**Department** values: `HR`, `IT`, `FINANCE`, `OPERATIONS`, `MARKETING`, `LEGAL`

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- MySQL instance (for non-local profiles)

### Run locally (H2 in-memory database)

```bash
./mvnw spring-boot:run -pl employee-launcher -Plocal \
  -Dspring-boot.run.profiles=local
```

The server starts on port **8085** with an H2 in-memory database. Flyway is disabled in this profile.

### Run against MySQL

Set the following environment variables before starting:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/otel_employee_db
SPRING_DATASOURCE_USERNAME=<user>
SPRING_DATASOURCE_PASSWORD=<password>
SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
```

Then build and run:

```bash
./mvnw clean package -DskipTests
java -jar employee-launcher/target/copilot-training.jar
```

### Build

```bash
./mvnw clean package
```

## Database Migrations

Flyway migrations are located in `employee-launcher/src/main/resources/db/migration/`:

| Version | Description               |
|---------|---------------------------|
| V1      | Create `t_employee` table |
| V2      | Insert sample employees   |

## Logging

Application logs are written to the `logs/` directory (configured via `logging.file.path`).

## OpenAPI / Swagger

The OpenAPI specification is located at:

```
employee-launcher/src/main/resources/swagger/openapi.yaml
```
