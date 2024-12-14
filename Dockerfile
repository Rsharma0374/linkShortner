# Use an official OpenJDK runtime as a base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /linkShortner

# Copy the JAR file (or build the app if using source code)
COPY target/*.jar app.jar

# Expose the port your app will run on
EXPOSE 10007

# Define the command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
