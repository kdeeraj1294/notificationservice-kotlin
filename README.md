# Notification Service (Kotlin)

A Spring Boot backend service written in **Kotlin** that accepts notification requests and delivers them through a simulated notification provider. It demonstrates REST API design, validation, persistence, notification lifecycle management, error handling, and automated tests.

## Tech stack

- Kotlin 2.2.20
- java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Jakarta Bean Validation
- H2 in-memory database
- Maven
- JUnit 5 and Mockito-Kotlin

## Run the application

Requirements: JDK 21 and Maven.

```bash
mvn clean test
mvn spring-boot:run
```

The service starts at `http://localhost:8086`.

## API

### Create a notification

`POST /notifications`

```json
{
  "recipient": "test@gmail.com",
  "message": "Your order has been shipped",
  "channel": "EMAIL"
}
```

A successful request returns HTTP `201 Created` and a response similar to:

```json
{
  "id": "generated-uuid",
  "recipient": "test@gmail.com",
  "message": "Your order has been shipped",
  "channel": "EMAIL",
  "status": "SENT",
  "createdAt": "2026-10-08T07:30:00Z",
  "updatedAt": "2026-10-08T07:30:01Z"
}
```

### Get a notification

`GET /notifications/{id}` returns the notification and its current state.

## Lifecycle

`PENDING -> PROCESSING -> SENT | FAILED`

The notification is stored before the mock provider is called. If provider delivery fails, the notification is marked `FAILED` and can still be retrieved by ID.

For a deterministic failure demonstration, use `fail@test.com` as the recipient.

## Project structure

```text
src/
├── main/
│   ├── kotlin/com/example/notificationservice/
│   │   ├── controller/       # REST endpoints
│   │   ├── dto/               # Request/response models
│   │   ├── entity/            # JPA entity and enums
│   │   ├── exception/         # API error handling
│   │   ├── provider/          # Provider interface and mock implementation
│   │   ├── repository/        # Spring Data JPA repository
│   │   ├── service/           # Business logic
│   │   └── NotificationserviceApplication.kt
│   └── resources/application.properties
└── test/kotlin/com/example/notificationservice/
    ├── controller/
    └── service/
```

## Validation and errors

`recipient`, `message`, and `channel` are required. Invalid requests return `400 Bad Request`; unknown notification IDs return `404 Not Found`.

## H2 console

- URL: `http://localhost:8086/h2-console`
- JDBC URL: `jdbc:h2:mem:notificationdb`
- Username: `sa`
- Password: (blank)

The in-memory database is recreated when the application starts.

## Design notes

- Kotlin data classes are used for API DTOs.
- JPA uses an entity class with mutable persistence properties and a protected no-argument constructor.
- The service depends on the `NotificationProvider` interface, keeping delivery implementation replaceable.
- Delivery is synchronous and simulated. Real provider integrations, retries, messaging, authentication, rate limiting, and production database configuration are intentionally out of scope.
