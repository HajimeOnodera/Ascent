# Run stage only - build JAR locally with: ./gradlew :core-skyblock:shadowJar
FROM amazoncorretto:25-alpine
WORKDIR /app
COPY core-skyblock/build/libs/CoreSkyblock.jar .
COPY maps/ ./maps

# Environment variables
ENV MONGODB_URI=mongodb://mongodb:27017
ENV REDIS_HOST=redis
ENV REDIS_PORT=6379

EXPOSE 25565
CMD ["java", "-jar", "CoreSkyblock.jar"]
