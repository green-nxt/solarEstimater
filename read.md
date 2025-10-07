# Solar Plant Management System

This project is a **Spring Boot application** designed to manage solar plants, customer data, inverters, and daily
energy generation records. It provides **REST APIs** for managing plants, tracking daily power generation, and
integrating external data sources for real-time monitoring. The application follows a **modular architecture** with
DTOs, custom exceptions, authentication, and containerized deployment.

---

## Local Setup Requirements

To set up the application locally, ensure the following prerequisites are installed:

### Java Development Kit

- **Java 21 JDK** is required to run the Spring Boot application.
- Verify installation with: `java --version`

### Database

- **PostgreSQL** for database storage.
- **pgAdmin** (optional but recommended) to manage and visualize the database.
- Create a database schema before running the application (configured in `application.properties`).

### Integrated Development Environment (IDE)

- **IntelliJ IDEA** is recommended for development.
- Provides built-in support for Maven, Spring Boot, and debugging tools.
- Open the project by selecting the root folder (`solar-plant-management`) in IntelliJ.

### Docker

- **Docker Desktop** or **Docker Engine** is required to build and run containers.
- Use the provided `Dockerfile` for creating application images.
- Compose files can be added for running multi-container setups (Spring Boot + PostgreSQL).

---

## Getting Started

1. Clone the repository:`git clone https://github.com/green-nxt/solarEstimater.git
cd solarEstimater`
2. Build the project: `gradlew clean install`
3. Run the application: `gradlew spring-boot:run`
4. Access the application at `http://localhost:8080`
5. Use Postman or any API client to test the REST endpoints.
6. Manage database via pgAdmin at: `http://localhost:5432`
7. Install Docker and build the image:
   #### Build the Docker image: `docker build -t solarestimator:latest .`
   #### Run the container: `docker run -d -p 8080:8080 --name solarestimator-app solarestimator:latest`
   #### View logs: `docker logs solarestimator-app`
   #### Stop the container: `docker stop solarestimator-app`



