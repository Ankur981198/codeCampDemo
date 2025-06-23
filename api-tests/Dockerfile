FROM openjdk:8-jdk-slim

WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy project files
COPY pom.xml .
COPY src ./src

# Download dependencies
RUN mvn dependency:go-offline -B

# Set environment variable
ENV DOCKER_CONTAINER=true

# Default command
CMD ["mvn", "test"]