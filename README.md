# E-Shop Backend API

RESTful backend API for a full-stack e-shop portfolio application, built with **Spring Boot**, **PostgreSQL**, **Flyway**, **JWT Authentication**, **Spring Security**, and **OpenAPI/Swagger**.

The backend provides the server-side functionality for an e-commerce platform, including authentication, role-based authorization, product and category management, cart handling, checkout, order handling, database migrations, validation, exception handling, and service-layer testing.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Project Structure](#project-structure)
- [Database and Persistence](#database-and-persistence)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Running Tests](#running-tests)
- [API Documentation with Swagger](#api-documentation-with-swagger)
- [API Overview](#api-overview)
- [Security Rules](#security-rules)
- [Frontend Integration](#frontend-integration)
- [Local Development Setup](#local-development-setup)
- [Current Status](#current-status)
- [Planned Improvements](#planned-improvements)
- [Related Project](#related-project)
- [Author](#author)

---

## Overview

This project is the backend part of a full-stack e-shop application.

The backend is responsible for:

- Exposing REST API endpoints
- Managing user registration and login
- Securing protected resources with JWT authentication
- Applying role-based access control with `USER` and `ADMIN` roles
- Handling products, categories, cart items, checkout, and orders
- Managing database schema changes through Flyway migrations
- Mapping entities to DTOs
- Returning consistent error responses through global exception handling
- Using a shared audited base entity for common entity fields
- Providing API documentation through Swagger UI

---

## Tech Stack

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT Authentication
- PostgreSQL
- Flyway
- Maven
- Lombok
- Jakarta Validation
- JUnit
- Mockito
- Springdoc OpenAPI / Swagger UI

---

## Features

### Authentication and Authorization

- User registration
- User login
- JWT-based authentication
- `USER` and `ADMIN` roles
- Public, authenticated, and admin-only endpoint access
- Ordered request matchers in Spring Security configuration
- Current-user endpoint through JWT authentication

### Product and Category Management

- Retrieve all products
- Retrieve product details
- Retrieve all categories
- Retrieve category details
- Public product and category browsing
- Admin-only product creation, update, and deletion
- Admin-only category creation, update, and deletion

### Cart and Checkout

- Cart item model and API structure
- Authenticated cart access
- Checkout request handling
- Order creation from checkout
- Stock validation during checkout
- Product stock reduction after successful checkout

### Orders

- Order and order item structure
- Authenticated users can view their own orders
- Admin users can access administrative order endpoints
- Order total amount handling
- Order item quantity and price handling

### Database and Persistence

- PostgreSQL database
- Flyway database migrations
- Hibernate schema validation with `ddl-auto=validate`
- JPA entity relationships
- Common `AbstractEntity` base class
- Shared entity ID handling
- Audit fields with JPA auditing:
    - `created_at`
    - `updated_at`

### DTOs and Mapping

- DTO-based request and response handling
- Java record DTOs where appropriate
- Read-only DTOs for API responses
- Insert/update DTOs for request payloads
- Entity-to-DTO mapper classes

### Error Handling

- Global exception handling
- Custom application exceptions
- Consistent API error responses
- Centralized handling for controller-level errors

### Testing

- Service-layer unit tests
- Mockito-based repository mocking
- Tests updated to support inherited entity IDs from `AbstractEntity`

### API Documentation

- OpenAPI documentation generated with Springdoc
- Swagger UI for browsing and testing backend endpoints
- Public Swagger/OpenAPI access for local development
- JWT-protected endpoints can be tested through Swagger using a Bearer token

---

## Project Structure

```text
src/main/java/gr/aueb/cf/eshop_app
├── controller        # REST controllers
├── dto               # Data Transfer Objects
├── exception         # Custom exceptions and global exception handling
├── mapper            # Entity-to-DTO mappers
├── models            # JPA entities, AbstractEntity, and enums
├── repository        # Spring Data JPA repositories
├── security          # JWT and Spring Security configuration
├── service           # Service interfaces and implementations
└── EShopAppApplication.java
```

Migration files are located in:

```text
src/main/resources/db/migration
```

Tests are located in:

```text
src/test/java/gr/aueb/cf/eshop_app
```

---

## Database and Persistence

The application uses **PostgreSQL** as the relational database.

Database schema changes are handled through **Flyway migrations**. When the application starts, Flyway automatically applies any pending migrations.

Hibernate is configured with schema validation:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

This means that the application expects the database schema to match the JPA entities.

### Audited Base Entity

Entities inherit common fields from `AbstractEntity`:

```text
id
created_at
updated_at
```

JPA auditing is enabled through `@EnableJpaAuditing` in the main Spring Boot application class.

### Database Migrations

Flyway migration files are stored in:

```text
src/main/resources/db/migration
```

The project uses migrations for table creation, seed data, product display fields, user roles, and audit columns.

---

## Configuration

The real `application.properties` file is intentionally excluded from version control because it may contain local database credentials or JWT secrets.

An example configuration file is provided:

```text
src/main/resources/application-example.properties
```

Create a local configuration file:

```text
src/main/resources/application.properties
```

Example configuration:

```properties
spring.application.name=e-shop-app
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

app.jwt.secret=your_jwt_secret
app.jwt.expiration-ms=86400000
```

Do not commit real database credentials, JWT secrets, or environment-specific configuration.

### Test Profile

The project can also use a test-specific configuration file:

```text
application-test.properties
```

Spring Boot tests that need the test profile can use:

```java
@ActiveProfiles("test")
```

---

## Running the Application

From the project root, run:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Alternatively, if Maven is installed locally:

```bash
mvn spring-boot:run
```

The backend runs by default on:

```text
http://localhost:8080
```

---

## Running Tests

Run the test suite with:

```bash
mvn clean test
```

Compile the project without running the application:

```bash
mvn clean compile
```

---

## API Documentation with Swagger

The backend includes OpenAPI/Swagger documentation using Springdoc.

After starting the Spring Boot application, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

The raw OpenAPI specification is available at:

```text
http://localhost:8080/v3/api-docs
```

For protected endpoints:

1. Authenticate through the login endpoint.
2. Copy the returned JWT token.
3. Use the **Authorize** button in Swagger UI.
4. Enter the token in this format:

```text
Bearer <your_jwt_token>
```

Swagger-related endpoints are allowed through Spring Security:

```text
/swagger-ui/**
/swagger-ui.html
/v3/api-docs/**
```

---

## API Overview

### Authentication

```http
POST /api/auth/register
POST /api/auth/login
```

### Users

```http
GET    /api/users/me
GET    /api/users
GET    /api/users/{id}
DELETE /api/users/{id}
```

### Products

```http
GET    /api/products
GET    /api/products/{id}
POST   /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}
```

### Categories

```http
GET    /api/categories
GET    /api/categories/{id}
POST   /api/categories
PUT    /api/categories/{id}
DELETE /api/categories/{id}
```

### Cart

```http
GET    /api/cart
POST   /api/cart
DELETE /api/cart/{id}
```

### Orders

```http
GET  /api/orders
GET  /api/orders/{id}
GET  /api/orders/my-orders
GET  /api/orders/user/{userId}
POST /api/orders/checkout
```

---

## Security Rules

The application uses **Spring Security** with **JWT authentication**.

Public access is allowed for:

- Authentication endpoints
- Swagger/OpenAPI documentation
- Product browsing with `GET /api/products/**`
- Category browsing with `GET /api/categories/**`

Authenticated access is required for:

- Current user profile endpoint: `GET /api/users/me`
- Cart-related endpoints
- Checkout endpoint: `POST /api/orders/checkout`
- Current user's orders: `GET /api/orders/my-orders`

Admin access is required for:

- User management
- Product creation, update, and deletion
- Category creation, update, and deletion
- Administrative order endpoints such as all orders, order by ID, and orders by user ID

General access logic:

| Endpoint Type | Access |
|---|---|
| `POST /api/auth/**` | Public |
| `GET /api/products/**` | Public |
| `GET /api/categories/**` | Public |
| `GET /api/users/me` | Authenticated user |
| `/api/users/**` | Admin |
| `POST`, `PUT`, `PATCH`, `DELETE /api/products/**` | Admin |
| `POST`, `PUT`, `PATCH`, `DELETE /api/categories/**` | Admin |
| `/api/cart/**` | Authenticated user |
| `POST /api/orders/checkout` | Authenticated user |
| `GET /api/orders/my-orders` | Authenticated user |
| Administrative order endpoints | Admin |

---

## Frontend Integration

This backend is designed to work with an Angular frontend application.

Default frontend development URL:

```text
http://localhost:4200
```

CORS is configured for local frontend-backend communication during development.

---

## Local Development Setup

Before running the project locally:

1. Create a PostgreSQL database.
2. Create a local `application.properties` file based on `application-example.properties`.
3. Configure the local database connection and JWT secret.
4. Start the Spring Boot application.
5. Flyway will automatically apply the database migrations.
6. Open Swagger UI, the Angular frontend, or an API client to interact with the backend.

---

## Current Status

Completed:

- Product API
- Category API
- User API
- JWT authentication
- Role-based authorization
- Ordered endpoint authorization rules
- Cart structure
- Checkout endpoint
- Order structure
- Flyway migrations
- Global exception handling
- DTO-based request/response handling
- Java record DTO refactoring
- Abstract audited base entity
- Angular frontend integration
- OpenAPI/Swagger API documentation
- Mockito service-layer tests

---

## Planned Improvements

Future improvements may include:

- Improve order ownership checks for user-specific order access
- Add product search and filtering
- Add pagination if the product catalog grows
- Add admin dashboard integration from the frontend
- Improve validation error responses
- Expand automated test coverage
- Add integration/security tests
- Add deployment configuration

---

## Related Project

This backend is part of a full-stack e-shop application.

Frontend repository:

```text
https://github.com/jroumpekas/eshop-app-frontend
```

---

## Author

Created by Dimitris Roumpekas as part of a full-stack portfolio project.