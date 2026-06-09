# E-Shop Backend API

Backend REST API for a full-stack e-shop application built with **Spring Boot**, **PostgreSQL**, **Flyway**, and **JWT Authentication**.

This project provides the server-side functionality for an e-shop platform, including products, categories, users, authentication, authorization, cart items, and orders.

## Tech Stack

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* JWT Authentication
* PostgreSQL
* Flyway
* Maven
* Lombok
* Validation

## Main Features

* User registration and login
* JWT-based authentication
* Role-based authorization
* USER and ADMIN roles
* Product management
* Category management
* Cart item management
* Order management
* Flyway database migrations
* Global exception handling
* REST API structure with DTOs, mappers, services, repositories, and controllers

## Project Structure

```text
src/main/java/gr/aueb/cf/eshop_app
├── controller        # REST controllers
├── dto               # Data Transfer Objects
├── exception         # Global exception handling
├── mapper            # Entity to DTO mappers
├── models            # JPA entities and enums
├── repository        # Spring Data JPA repositories
├── security          # JWT and Spring Security configuration
├── service           # Service interfaces and implementations
└── EShopAppApplication.java
```

## Database

The application uses PostgreSQL.

Default local database name:

```text
eshop_db
```

Database schema is managed with Flyway migrations located in:

```text
src/main/resources/db/migration
```

## Configuration

The real `application.properties` file is not included in the repository for security reasons.

Use the provided example file:

```text
src/main/resources/application-example.properties
```

Create your own local configuration file:

```text
src/main/resources/application.properties
```

Example configuration:

```properties
spring.application.name=e-shop
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/eshop_db
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

app.jwt.secret=change_this_secret_key_at_least_32_characters_long
app.jwt.expiration-ms=86400000
```

## Running the Application

From the backend project root, run:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Or with Maven installed:

```bash
mvn spring-boot:run
```

The backend runs by default on:

```text
http://localhost:8080
```

## Authentication Endpoints

### Register

```http
POST /api/auth/register
```

Example request:

```json
{
  "username": "john1",
  "email": "john1@example.com",
  "password": "Password123!",
  "firstName": "John",
  "lastName": "Doe"
}
```

### Login

```http
POST /api/auth/login
```

Example request:

```json
{
  "username": "john1",
  "password": "Password123!"
}
```

Example response:

```json
{
  "token": "jwt_token_here",
  "userId": 1,
  "username": "john1",
  "email": "john1@example.com"
}
```

The returned JWT token must be included in protected requests:

```http
Authorization: Bearer jwt_token_here
```

## Authorization

The application supports two roles:

```text
USER
ADMIN
```

General access rules:

* Public users can view products and categories.
* Logged-in users can access their profile, cart, and orders.
* Admin users can manage products, categories, and users.

## Main API Endpoints

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

### Users

```http
GET    /api/users/me
GET    /api/users
GET    /api/users/{id}
DELETE /api/users/{id}
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

## Frontend

This backend is designed to work with an Angular frontend application.

Frontend default local URL:

```text
http://localhost:4200
```

CORS is configured to allow communication between the Angular frontend and the Spring Boot backend during local development.

## Development Notes

Before running the project locally:

1. Create a PostgreSQL database named `eshop_db`.
2. Create your local `application.properties`.
3. Start the Spring Boot application.
4. Flyway will automatically run the database migrations.
5. Use Postman or the Angular frontend to test the API.

## Status

This project is under active development.

Completed:

* Product API
* Category API
* User API
* JWT authentication
* Role-based authorization
* Cart structure
* Orders structure
* Flyway migrations
* Angular frontend integration

Planned improvements:

* Complete checkout flow
* Improve order creation from cart
* Add admin dashboard
* Add better validation responses
* Add more automated tests
* Improve API documentation

## Author

Created by Dimitris Roumpekas as part of a full-stack e-shop portfolio project.
