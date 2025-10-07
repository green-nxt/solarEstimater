# Use OpenJDK 21 as the base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the WAR file from the build directory to the container
COPY build/libs/solarestimater-0.0.1-SNAPSHOT.war app.war

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the WAR file directly (Spring Boot executable WAR)
ENTRYPOINT ["java", "-jar", "/app/app.war"]
