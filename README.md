# E-Shop Backend API

A RESTful backend API for a full-stack e-shop application built with **Spring Boot**, **PostgreSQL**, **Flyway**, **JWT Authentication**, **Spring Security**, and **OpenAPI/Swagger documentation**.

The backend provides the server-side functionality for an e-commerce platform, including authentication, role-based authorization, product and category management, cart handling, order handling, database migrations, validation, exception handling, and service-layer testing.

---

## Overview

This project is part of a full-stack e-shop portfolio application.

The backend is responsible for:

- Exposing REST API endpoints
- Managing users and authentication
- Securing protected resources with JWT
- Applying role-based access control with `USER` and `ADMIN` roles
- Handling products, categories, cart items, and orders
- Managing the database schema with Flyway migrations
- Mapping entities to DTOs
- Returning consistent error responses through global exception handling
- Using an audited base entity for common entity fields

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
- Mockito
- JUnit
- Springdoc OpenAPI / Swagger UI

---

## Features

### Authentication & Authorization

- User registration
- User login
- JWT-based authentication
- Role-based authorization
- `USER` and `ADMIN` roles
- Public, authenticated, and admin-only endpoint access
- Ordered request matchers in Spring Security configuration

### Product & Category Management

- Retrieve all products
- Retrieve product details
- Retrieve categories
- Admin-only product creation, update, and deletion
- Admin-only category creation, update, and deletion
- Public product and category browsing

### Cart & Orders

- Cart item structure
- Order and order item structure
- Authenticated access for cart and order-related operations
- Order-related service layer logic

### Database & Persistence

- PostgreSQL database
- Flyway database migrations
- JPA / Hibernate entity mapping
- Common `AbstractEntity` base class
- Shared entity ID handling
- Audit fields with JPA auditing:
    - `created_at`
    - `updated_at`
- Database schema validation with Hibernate

### DTOs & Mapping

- DTO-based request and response handling
- DTOs refactored to Java records where appropriate
- Entity-to-DTO mappers
- Read-only DTOs for API responses
- Request DTOs for insert/update operations

### Error Handling

- Global exception handling
- Custom application exceptions
- Consistent API error responses

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

## Database

The application uses **PostgreSQL** as the relational database.

Database schema changes are handled through **Flyway migrations**. When the application starts, Flyway automatically applies any pending migrations.

Hibernate is configured with schema validation, so the application expects the database schema to match the JPA entities.

```properties
spring.jpa.hibernate.ddl-auto=validate
```

### Auditing

Entities inherit common fields from `AbstractEntity`, including:

```text
id
created_at
updated_at
```

JPA auditing is enabled through `@EnableJpaAuditing` in the main Spring Boot application class.

---

## Configuration

The real `application.properties` file is intentionally excluded from version control for security reasons.

An example configuration file is provided:

```text
src/main/resources/application-example.properties
```

Create a local configuration file:

```text
src/main/resources/application.properties
```

Example structure:

```properties
spring.application.name=e-shop
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

To compile the project without running the application:

```bash
mvn clean compile
```

---

## API Documentation with Swagger

The backend includes **OpenAPI/Swagger documentation** using Springdoc.

After starting the Spring Boot application, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

The raw OpenAPI specification is available at:

```text
http://localhost:8080/v3/api-docs
```

For protected endpoints, authenticate through the login endpoint, copy the returned JWT token, and use the **Authorize** button in Swagger UI with:

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
GET    /api/orders
GET    /api/orders/{id}
POST   /api/orders
PUT    /api/orders/{id}
DELETE /api/orders/{id}
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

- Current user profile endpoint
- Cart-related endpoints
- Order-related endpoints

Admin access is required for:

- User management
- Product creation, update, and deletion
- Category creation, update, and deletion

General access logic:

| Endpoint Type | Access |
| --- | --- |
| `POST /api/auth/**` | Public |
| `GET /api/products/**` | Public |
| `GET /api/categories/**` | Public |
| `/api/users/me` | Authenticated user |
| `/api/users/**` | Admin |
| `POST`, `PUT`, `PATCH`, `DELETE /api/products/**` | Admin |
| `POST`, `PUT`, `PATCH`, `DELETE /api/categories/**` | Admin |
| `/api/cart/**` | Authenticated user |
| `/api/orders/**` | Authenticated user |

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
3. Configure the local database connection.
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
- Order structure
- Flyway migrations
- Global exception handling
- DTO-based request/response handling
- Java record DTO refactoring
- Abstract audited base entity
- Angular frontend integration
- OpenAPI/Swagger API documentation
- Mockito service-layer tests

Planned improvements:

- Complete checkout flow
- Improve order creation from cart
- Add admin dashboard
- Improve validation error responses
- Add product search and filtering
- Add pagination if the product catalog grows
- Expand automated test coverage
- Add deployment configuration

---

## Related Project

This backend is part of a full-stack e-shop application.

Frontend repository:

```text
Angular E-Shop Frontend
```

---

## Author

Created by Dimitris Roumpekas as part of a full-stack portfolio project.
