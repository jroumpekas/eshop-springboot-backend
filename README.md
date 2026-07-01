# E-Shop Backend API

A RESTful backend API for a full-stack e-shop application, built with **Spring Boot**, **PostgreSQL**, **Flyway**, **JWT Authentication**, and **OpenAPI/Swagger documentation**.

The application provides the server-side functionality for an e-commerce platform, including product management, categories, users, authentication, authorization, cart handling, and orders.

## Overview

This project is part of a full-stack e-shop portfolio application.

The backend is responsible for:

* Exposing REST API endpoints
* Managing users and authentication
* Securing protected resources with JWT
* Applying role-based access control
* Handling products, categories, cart items, and orders
* Managing the database schema with Flyway migrations

## Tech Stack

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* JWT Authentication
* PostgreSQL
* Flyway
* Maven
* Lombok
* Jakarta Validation
* Springdoc OpenAPI / Swagger UI

## Features

### Authentication & Authorization

* User registration
* User login
* JWT-based authentication
* Role-based authorization
* USER and ADMIN roles
* Protected API endpoints

### Product & Category Management

* Retrieve products
* Retrieve product details
* Manage products
* Manage categories
* Public product and category browsing

### Cart & Orders

* Cart item structure
* Order structure
* Authenticated user access for cart and order-related operations

### API Documentation

* OpenAPI documentation generated with Springdoc
* Swagger UI for browsing and testing backend endpoints
* Public documentation access for local development
* JWT-protected endpoints can be tested through Swagger using a Bearer token

### Database & Architecture

* PostgreSQL database
* Flyway database migrations
* Layered backend architecture
* DTO-based request and response handling
* Entity-to-DTO mapping
* Global exception handling

## Project Structure

```text
src/main/java/gr/aueb/cf/eshop_app
├── controller        # REST controllers
├── dto               # Data Transfer Objects
├── exception         # Global exception handling
├── mapper            # Entity-to-DTO mappers
├── models            # JPA entities and enums
├── repository        # Spring Data JPA repositories
├── security          # JWT and Spring Security configuration
├── service           # Service interfaces and implementations
└── EShopAppApplication.java
```

## Database

The application uses **PostgreSQL** as the relational database.

Database schema changes are handled through **Flyway migrations**.

Migration files are located in:

```text
src/main/resources/db/migration
```

When the application starts, Flyway automatically applies any pending migrations.

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

Swagger can be used to inspect and test the available REST endpoints, including authentication, products, categories, users, cart, and orders.

For protected endpoints, first authenticate through the login endpoint, copy the returned JWT token, and use the **Authorize** button in Swagger UI with:

```text
Bearer <your_jwt_token>
```

Swagger-related endpoints are allowed through Spring Security:

```text
/swagger-ui/**
/swagger-ui.html
/v3/api-docs/**
```

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

## Security

The application uses **Spring Security** with **JWT authentication**.

Public access is allowed for authentication endpoints, basic product/category browsing, and Swagger/OpenAPI documentation endpoints.

Protected resources require a valid JWT token.

Role-based access is applied as follows:

* `USER`: access to user-specific resources such as profile, cart, and orders
* `ADMIN`: access to administrative actions such as managing users, products, and categories

## Frontend Integration

This backend is designed to work with an Angular frontend application.

Default frontend development URL:

```text
http://localhost:4200
```

CORS is configured for local frontend-backend communication during development.

## Local Development Setup

Before running the project locally:

1. Create a PostgreSQL database.
2. Create a local `application.properties` file based on `application-example.properties`.
3. Configure the local database connection.
4. Start the Spring Boot application.
5. Flyway will automatically apply the database migrations.
6. Open Swagger UI or use the Angular frontend / an API client to interact with the backend.

## Current Status

Completed:

* Product API
* Category API
* User API
* JWT authentication
* Role-based authorization
* Cart structure
* Order structure
* Flyway migrations
* Global exception handling
* Angular frontend integration
* OpenAPI/Swagger API documentation

Planned improvements:

* Complete checkout flow
* Improve order creation from cart
* Add admin dashboard
* Add automated tests
* Improve validation error responses

## Related Project

This backend is part of a full-stack e-shop application.

Frontend repository:

```text
Angular E-Shop Frontend
```

## Author

Created by Dimitris Roumpékas as part of a full-stack portfolio project.
