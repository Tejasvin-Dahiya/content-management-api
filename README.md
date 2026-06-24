# Content Management API

A REST API for managing content with JWT-based authentication. Users can register, log in, and perform full CRUD operations on content items. All content endpoints are protected and require a valid Bearer token.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Runtime |
| Spring Boot 3 | Application framework |
| Spring Security | Authentication and authorization |
| JWT (jjwt 0.11.5) | Stateless token-based auth |
| Spring Data JPA | Data persistence |
| H2 | In-memory database |
| Maven | Build and dependency management |
| Lombok | Boilerplate reduction |

## Prerequisites

- Java 17 or later
- Maven 3.6+

## Running Locally

Clone the repository and start the application from the project root:

```bash
mvn spring-boot:run
```

The server starts on `http://localhost:8080`.

> **Note:** Visiting `http://localhost:8080/` in a browser returns HTTP 403. This is expected — the application is a REST API with no public homepage. Use the endpoints below or the H2 console.

## Authentication

Public endpoints:

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Log in and receive a JWT |

All `/api/content/**` endpoints require an `Authorization` header:

```
Authorization: Bearer <your-jwt-token>
```

### Register

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "secret123",
    "role": "ROLE_USER"
  }'
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "alice",
  "role": "ROLE_USER"
}
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "secret123"
  }'
```

Save the `token` from the response and use it in subsequent requests.

## Content Endpoints

All content routes require a valid JWT.

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/content` | List content (paginated, optional status filter) |
| `GET` | `/api/content/{id}` | Get a single content item |
| `POST` | `/api/content` | Create new content |
| `PUT` | `/api/content/{id}` | Update content |
| `DELETE` | `/api/content/{id}` | Delete content |
| `GET` | `/api/content/search` | Search by keyword in title or body |

**Content status values:** `DRAFT`, `PUBLISHED`

### Create Content

```bash
curl -X POST http://localhost:8080/api/content \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "title": "Getting Started with Spring Boot",
    "body": "Spring Boot makes it easy to create stand-alone, production-grade applications.",
    "status": "PUBLISHED",
    "tags": ["java", "spring", "tutorial"]
  }'
```

### Get All Content (with Pagination)

```bash
curl "http://localhost:8080/api/content?page=0&size=10" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

Filter by status:

```bash
curl "http://localhost:8080/api/content?page=0&size=10&status=PUBLISHED" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Search by Keyword

```bash
curl "http://localhost:8080/api/content/search?keyword=spring&page=0&size=10" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Get, Update, and Delete

```bash
# Get by ID
curl http://localhost:8080/api/content/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# Update
curl -X PUT http://localhost:8080/api/content/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "title": "Updated Title",
    "body": "Updated body text.",
    "status": "DRAFT",
    "tags": ["updated"]
  }'

# Delete
curl -X DELETE http://localhost:8080/api/content/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## H2 Database Console

The H2 web console is enabled for local development at:

**http://localhost:8080/h2-console**

| Setting | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:contentdb` |
| Username | `sa` |
| Password | *(leave empty)* |

## Configuration

Key settings in `src/main/resources/application.properties`:

| Property | Description | Default |
|---|---|---|
| `jwt.secret` | Secret key for signing JWTs | Configured in properties |
| `jwt.expiration` | Token lifetime in milliseconds | `86400000` (24 hours) |
| `spring.jpa.show-sql` | Log SQL statements | `true` |

## Error Responses

The API returns structured JSON error responses:

| Status | Cause |
|---|---|
| `400` | Validation errors or bad request |
| `403` | Missing or invalid JWT |
| `404` | Resource not found |

Example:

```json
{
  "timestamp": "2026-06-24T10:21:55.116765",
  "status": 404,
  "error": "Not Found",
  "message": "Content not found with id: 999"
}
```

## Project Structure

```
src/main/java/com/contentmanagement/api/
├── controller/       # REST endpoints
├── service/          # Business logic
├── repository/       # JPA repositories
├── model/            # Entity classes
├── dto/              # Request/response objects
├── security/         # JWT filter and security config
├── exception/        # Global error handling
└── converter/        # JPA attribute converters
```