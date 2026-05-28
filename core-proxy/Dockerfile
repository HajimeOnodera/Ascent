# Stage 1: Build stage
FROM amazoncorretto:25-alpine AS builder
WORKDIR /builder

# Install bash and findutils for Gradle Wrapper
RUN apk add --no-cache bash findutils

# Copy gradle configuration files
COPY gradle/ ./gradle
COPY gradlew build.gradle.kts settings.gradle.kts global_config.json ./
COPY commons/ ./commons
COPY core-proxy/ ./core-proxy

# Build the proxy shadow jar
RUN ./gradlew :core-proxy:shadowJar

# Stage 2: Run stage
FROM amazoncorretto:25-alpine
WORKDIR /app

# Copy Velocity executable and settings
COPY proxy/velocity-3.5.0-SNAPSHOT-593.jar ./velocity.jar
COPY proxy/velocity.toml ./velocity.toml
COPY proxy/forwarding.secret ./forwarding.secret

# Setup Plugins and copy built jar
RUN mkdir -p plugins
COPY --from=builder /builder/core-proxy/build/libs/CoreProxy.jar ./plugins/CoreProxy.jar

# Environment variables (Dynamically set by Railway/Docker)
ENV REDIS_HOST=127.0.0.1
ENV REDIS_PORT=6379
ENV MONGODB_URI=

EXPOSE 25565
CMD ["java", "-Xmx512M", "-jar", "velocity.jar"]
