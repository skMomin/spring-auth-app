# Stage 1: Build the application
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# Copy build files
COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle ./gradle/

# Copy source code
COPY src ./src/

# Grant execute permission
RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew bootJar -x test

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

RUN groupadd -r app && useradd -r -g app -u 10001 app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENV JAVA_OPTS=""
USER app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]