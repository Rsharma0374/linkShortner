# Stage 1: Build the application using Maven
FROM maven:3.9.4-eclipse-temurin-17-alpine AS build

# Set the working directory
WORKDIR /build

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -Pprod

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /linkShortner

# Copy the jar from the build stage
COPY --from=build /build/target/*.jar app.jar

# Create logs directory
RUN mkdir -p /opt/logs && chmod 755 /opt/logs

# Expose application port
EXPOSE 10007

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]