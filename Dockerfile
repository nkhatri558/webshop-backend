# Use a multi-stage build to reduce the size of the final image

# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the project and build the application
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/webshop-backend-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
