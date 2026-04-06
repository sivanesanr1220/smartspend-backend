# Step 1: Build stage (Maven + JDK 17)
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first (for caching dependencies)
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Step 2: Run stage (Lightweight JDK 17)
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (Spring Boot default)
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]