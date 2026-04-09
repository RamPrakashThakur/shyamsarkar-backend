# Use working JDK 17 image
FROM eclipse-temurin:17-jdk-focal

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src ./src

# Build the JAR inside container
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the JAR with exact name
ENTRYPOINT ["java", "-jar", "target/BuildingMaterials-0.0.1-SNAPSHOT.jar"]
