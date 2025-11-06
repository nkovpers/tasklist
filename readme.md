# Task List Application

## Overview

The **Task List Application** is a Spring Boot-based project designed to manage users and their tasks. It provides a REST API for creating, updating, retrieving, and deleting users and tasks. The project is built with Jakarta EE, Spring Data JPA, Spring MVC, Lombok, and PostgreSQL for data persistence.

## Features

- **User Management**: 
  - Create, retrieve, and delete users.
- **Task Management**: 
  - Create, update, retrieve, and delete tasks for specific users.
- **Validation**: 
  - Input validation for user and task records.
- **Exception Handling**: 
  - Custom exception handling for user and task not found scenarios.
- **API Documentation**: 
  - Swagger UI for interactive API documentation using Springdoc OpenAPI.

## Architecture

The project follows a standard MVC architecture:
- **Controllers**: Handle API requests and responses.
- **DTOs (Data Transfer Objects)**: Represent data used in requests and responses.
- **Services**: Contain the business logic.
- **Exception Handlers**: Manage global error handling.

## Endpoints Overview

| Endpoint                        | Method   | Description                                   |
|---------------------------------|----------|-----------------------------------------------|
| `/api/tasklist/user/`           | `GET`    | Retrieve the list of all users.               |
| `/api/tasklist/user/`           | `POST`   | Create a new user.                            |
| `/api/tasklist/user/{userId}`   | `DELETE` | Delete a user by ID.                          |
| `/api/tasklist/task/{userId}`   | `GET`    | Retrieve the task list of a specific user.    |
| `/api/tasklist/task/{userId}`   | `POST`   | Create a new task for a specific user.        |
| `/api/tasklist/task`            | `POST`   | Update an existing task.                      |
| `/api/tasklist/task/{taskId}`   | `DELETE` | Delete a task by ID.                          |

## Prerequisites

Before running the project, ensure you have the following installed:
- **Java 17** or higher
- **Gradle** (Optional, as the project can use the wrapper)
- **Docker** (For running the application in a containerized environment)
- **PostgreSQL** (Database)

## Configuration

### Application Profile

The project uses **Spring Profiles** for environment-specific configurations.

### Database

The application expects a PostgreSQL database. Update your database configurations under the `compose.yaml` or `application-postgres.properties` file if necessary.

### Docker

A `Dockerfile` is included to containerize the application. Build the Docker image as follows:
```bash
docker build -t tasklist-app .
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=postgres tasklist-app
```

## Technologies Used

- **Java 17**: Programming language.
- **Spring Boot**: Framework for building the application.
- **Spring Data JPA**: For database persistence.
- **Springdoc OpenAPI**: To provide an interactive API documentation interface.
- **Lombok**: For reducing boilerplate code.
- **PostgreSQL**: Database for data persistence.
- **Docker**: To containerize the application.

## Build and Run

### Build
To build the project, use:
```bash
./gradlew build
```

### Run
To run the project, use:
```bash
java -jar build/libs/tasklist-app-1.0-SNAPSHOT.jar
```

Alternatively, if using Docker:
```bash
docker build -t tasklist-app .
docker run -p 8080:8080 tasklist-app
```

## API Documentation

The project includes Swagger UI available at:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Exception Handling

- If a user or task is not found, the application responds with a **404 Not Found** status and provides error details in the response.
- Exceptions handled:
  - `UserNotFoundException`
  - `TaskNotFoundException`