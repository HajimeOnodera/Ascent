# Stage 1: Build stage
FROM amazoncorretto:25-alpine AS builder
WORKDIR /builder

# Install bash and findutils for Gradle Wrapper
RUN apk add --no-cache bash findutils

# Copy gradle configuration files
COPY gradle/ ./gradle
COPY gradlew build.gradle.kts settings.gradle.kts global_config.json ./
COPY commons/ ./commons
COPY database/ ./database
COPY service.friend/ ./service.friend

# Build the friend service shadow jar
RUN ./gradlew :service.friend:shadowJar

# Stage 2: Run stage
FROM amazoncorretto:25-alpine
WORKDIR /app

# Copy built jar
COPY --from=builder /builder/service.friend/build/libs/FriendService.jar .

# Environment variables (Dynamically set by Railway/Docker)
ENV REDIS_HOST=127.0.0.1
ENV REDIS_PORT=6379
ENV MONGODB_URI=

CMD ["java", "-jar", "FriendService.jar"]
