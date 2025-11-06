# ---- Build Stage ----
FROM gradle:8.10.2-jdk21-jammy AS builder

# Set working directory
WORKDIR /app

# Copy only build configuration first (for better caching)
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle

# Download dependencies (cached between builds)
RUN ./gradlew dependencies || return 0

# Copy the rest of the project
COPY . .

# Build the WAR file
RUN ./gradlew clean build -x test

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy the WAR file from the build stage
COPY --from=builder /app/build/libs/*.war app.war

# Define build arguments for database configuration
ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD

# Set environment variables for database configuration
ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}

# Expose the application port
EXPOSE 8080

# Run the WAR
ENTRYPOINT ["java", "-jar", "/app/app.war"]
