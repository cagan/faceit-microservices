# FACEIT User Microservice
User Management microservice that makes CRUD operations and notifies other services when operations has been made.

## Tech Stack:
- Java 17
- Spring Boot 2.7.5
- MongoDB
- Kafka
- Spring Cloud Streams
- Mapstruct
- Jib
- Passay

## Highlights:
- Created request and response DTO classes and used mapstruct for mappings between Java bean types based on a convention over configuration approach
- Created custom annotation validators both for Enums, and also passwords.
- Used Passay package to customize validating passwords, and made it configurable, and made it configurable
- Considered **Separation of Concerns** principle and each layer are seperated from each other:
    - Presentation Layer: For serialization/deserialization and routing.
    - Service Layer: For business logic.
    - Repository Layer: For data logic.
    - Mappers: Converting classes between DTO's and POJO's.
    - Events: Represents user events (Created, Updated, Deleted)
    - Aspects: Applies Cross Cutting Concerns, In this case it's used for sending notifications via kafka.
    - Listeners: Listens events that created by Aspects.Sends events to kafka topics.
    - Configs: Mongo, Password encoding, Password Properties setup, Swagger.
    - Errors: Error handlers, 
    - Validators: Validates requests.
- Used JUnit 5 and also Mockito, for testing service layer, requests and controllers.
- Unit tests and Integration tests written.
- I wrote tests for presentation layer and service layer in this project. 

## Installation:
- Go to root project.
- Type: ```docker-compose -f docker/docker-compose.yml up -d```
- You can see the logs running: ```docker-compose -f docker/docker-compose.yml logs -f```

### For Building Maven Project:
- Change application-dev.properties for your local environment. You can use docker-compose-local.yaml file for local environments.
- Run this command to build the project ```./mvnw spring-boot:run -Dspring-boot.run.profiles=dev```

### Run Tests:
```./mvnw clean test```

## Postman Collection:
You can find Postman collection in root directory.

## Swagger Link:
You can access API endpoints entering this link below:
```http://localhost:8082/swagger-ui/index.html#/```

## What would I implement if it was a real project?
- I would add Kubernetes, and Gateway layer.
- Outbox Pattern: I would implement Debezium with mongo connector and kafka in order to make sure that data is pushing safely.
- More tests, more coverage rate.
- Implementing security tools such as Spring Security.

**Completed architecture would be like this:**

![Screenshot 2022-11-2 at 14 17 51](https://iili.io/b6Paln.png)
