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
COPY skyblock.dungeon/ ./skyblock.dungeon

# Build the dungeon shadow jar
RUN ./gradlew :skyblock.dungeon:shadowJar

# Stage 2: Run stage
FROM amazoncorretto:25-alpine
WORKDIR /app

# Copy built jar, configurations, maps, resources and secret
COPY --from=builder /builder/skyblock.dungeon/build/libs/SkyblockDungeon.jar .
COPY --from=builder /builder/proxy/forwarding.secret ./forwarding.secret
COPY --from=builder /builder/skyblock.dungeon/akyblockResources/ ./skyblock.dungeon/akyblockResources/
COPY maps/ ./maps
COPY configuration/ ./configuration/

# Environment variables (Dynamically set by Railway/Docker)
ENV REDIS_HOST=127.0.0.1
ENV REDIS_PORT=6379
ENV MONGODB_URI=
ENV ASCENT_SERVER_PORT=25569

EXPOSE 25569
CMD ["java", "-jar", "SkyblockDungeon.jar"]
