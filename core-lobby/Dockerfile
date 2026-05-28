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
COPY core-lobby/ ./core-lobby

# Build the lobby shadow jar
RUN ./gradlew :core-lobby:shadowJar

# Stage 2: Run stage
FROM amazoncorretto:25-alpine
WORKDIR /app

# Copy built jar and forwarding secret
COPY --from=builder /builder/core-lobby/build/libs/CoreLobby.jar .
COPY --from=builder /builder/proxy/forwarding.secret ./forwarding.secret

# Copy game maps
COPY maps/ ./maps

# Environment variables (Dynamically set by Railway/Docker)
ENV REDIS_HOST=127.0.0.1
ENV REDIS_PORT=6379
ENV MONGODB_URI=

EXPOSE 25567
CMD ["java", "-jar", "CoreLobby.jar"]
