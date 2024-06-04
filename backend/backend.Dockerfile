# Use a base image with Java (since you're using Spring Boot)
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app


# Copy the Maven executable wrapper
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Ensure mvnw is executable
RUN chmod +x mvnw

# Copy your source code into the container
COPY src ./src

# Ensure mvnw uses Unix line endings
RUN sed -i 's/\r$//' mvnw

# Install dependencies and package the application
RUN ./mvnw install -Dmaven.test.skip=true

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]