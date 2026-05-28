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
COPY skyblock.island/ ./skyblock.island

# Build the island shadow jar
RUN ./gradlew :skyblock.island:shadowJar

# Stage 2: Run stage
FROM amazoncorretto:25-alpine
WORKDIR /app

# Copy built jar, configurations, maps and secret
COPY --from=builder /builder/skyblock.island/build/libs/SkyblockIsland.jar .
COPY --from=builder /builder/proxy/forwarding.secret ./forwarding.secret
COPY maps/ ./maps
COPY configuration/ ./configuration/

# Environment variables (Dynamically set by Railway/Docker)
ENV REDIS_HOST=127.0.0.1
ENV REDIS_PORT=6379
ENV MONGODB_URI=
ENV ASCENT_SERVER_PORT=25568

EXPOSE 25568
CMD ["java", "-jar", "SkyblockIsland.jar"]
