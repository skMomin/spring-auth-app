# Spring Auth App

Spring Boot application using PostgreSQL, Flyway, and jOOQ with a layered structure:

- `endpoint` handles HTTP
- `service` handles orchestration and transactions
- `persistence.repository` defines repository contracts
- `persistence.repository.impl` contains jOOQ-based implementations
- `persistence.repository.mappers` converts generated records to application models

## Stack

- Java 17
- Spring Boot 3.3
- PostgreSQL 16
- Flyway
- jOOQ
- Docker Compose

## Project Structure

```text
src/main/java/com/momin/springauthapp
├── auth
├── company
├── config
├── endpoint
├── persistence
│   └── repository
│       ├── impl
│       └── mappers
├── service
│   └── impl
├── user
└── SpringAuthAppApplication.java
```

## Current Features

- User registration
- User login
- Refresh token rotation
- Company create/read/update/delete
- Ownership validation for company mutation operations

## Data Model

Current tables:

- `users`
- `roles`
- `user_roles`
- `refresh_tokens`
- `companies`

Relationship notes:

- one user can own many companies
- each company has exactly one owner through `companies.owner_id`

## Local Setup

Prerequisites:

- Java 17 available in your shell
- Docker with Compose support

Start PostgreSQL:

```bash
docker compose up -d postgres
```

Set environment variables:

```bash
export DB_URL='jdbc:postgresql://localhost:5432/spring_auth'
export DB_USER='postgres'
export DB_PASSWORD='postgres'
export APP_AUTH_JWT_SECRET='replace-with-a-long-random-secret'
```

Run the application:

```bash
./gradlew bootRun
```

## Build and Schema Workflow

This project treats Flyway migrations as the schema source of truth.

Recommended workflow:

1. Add a new Flyway migration in `src/main/resources/db/migration`
2. Regenerate jOOQ classes
3. Update repository/service/endpoint code
4. Run the application

Commands:

```bash
./gradlew generateJooq
./gradlew bootRun
```

Important:

- `bootRun` is configured to depend on `generateJooq`
- jOOQ code is generated from migration SQL files, not from a live database
- Flyway applies migrations to PostgreSQL at application startup

## PostgreSQL Management

Start database:

```bash
docker compose up -d postgres
```

Stop database:

```bash
docker compose stop postgres
```

Remove container:

```bash
docker compose down
```

Reset database completely:

```bash
docker compose down -v
docker compose up -d postgres
```

Inspect status:

```bash
docker compose ps
docker compose logs postgres
```

Open `psql`:

```bash
docker compose exec postgres psql -U postgres -d spring_auth
```

## API

### Auth

Register:

```bash
curl -X POST http://localhost:8080/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"email":"user@example.com","password":"password123"}'
```

Login:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"user@example.com","password":"password123"}'
```

Refresh:

```bash
curl -X POST http://localhost:8080/auth/refresh \
  -H 'Content-Type: application/json' \
  -d '{"refreshToken":"<refresh-token>"}'
```

List users:

```bash
curl http://localhost:8080/users
```

### Companies

Create company:

```bash
curl -X POST http://localhost:8080/companies \
  -H 'Content-Type: application/json' \
  -d '{
    "ownerId":"<user-id>",
    "name":"Acme Inc",
    "description":"Main holding company"
  }'
```

Get company by id:

```bash
curl http://localhost:8080/companies/<company-id>
```

Update company:

```bash
curl -X PUT http://localhost:8080/companies/<company-id> \
  -H 'Content-Type: application/json' \
  -d '{
    "ownerId":"<user-id>",
    "name":"Acme Holdings",
    "description":"Updated description"
  }'
```

Delete company:

```bash
curl -X DELETE "http://localhost:8080/companies/<company-id>?ownerId=<user-id>"
```

List companies by owner:

```bash
curl http://localhost:8080/users/<user-id>/companies
```

## Ownership and Security Notes

Current ownership validation for company updates and deletes is service-level only.

Right now:

- mutation endpoints accept `ownerId`
- service checks that the owner exists
- service checks that the company belongs to that owner

This is not full request authentication yet. The app already issues access tokens, but request authorization is not wired into Spring Security yet.

## Migrations

Current migrations:

- [V1__create_users.sql](/home/momin/Projects/spring-auth-app/src/main/resources/db/migration/V1__create_users.sql)
- [V2__create_auth_tables.sql](/home/momin/Projects/spring-auth-app/src/main/resources/db/migration/V2__create_auth_tables.sql)
- [V3__add_auth_indexes.sql](/home/momin/Projects/spring-auth-app/src/main/resources/db/migration/V3__add_auth_indexes.sql)
- [V4__create_companies.sql](/home/momin/Projects/spring-auth-app/src/main/resources/db/migration/V4__create_companies.sql)

## Notes

- generated jOOQ sources are written to `build/generated-src/jooq/main`
- access tokens are HMAC-signed JWTs
- refresh tokens are random opaque secrets stored as SHA-256 hashes
- repository implementations use generated jOOQ records and table references

## Next Improvements

1. Add Spring Security JWT authentication for protected routes.
2. Stop accepting `ownerId` on mutation requests and derive it from the authenticated user.
3. Add tests for auth flow and company ownership rules.
