# Use an official Maven image with OpenJDK 17 to build and run the application
FROM maven:3.8.8-eclipse-temurin-17

# Set the working directory inside the container
WORKDIR /app

# Copy the project files to the container
COPY . .

# Expose port 9000
EXPOSE 9000


# Run both mvn clean install and mvn spring-boot:run -f pom.xml when the container starts
CMD ["sh", "-c", "mvn clean install && mvn spring-boot:run -f pom.xml"]
