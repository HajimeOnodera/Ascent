# Run stage only - build JAR locally with: ./gradlew :core-lobby:shadowJar
FROM amazoncorretto:25-alpine
WORKDIR /app
COPY core-lobby/build/libs/CoreLobby.jar .
COPY maps/ ./maps

# Environment variables
ENV MONGODB_URI=mongodb://mongodb:27017
ENV REDIS_HOST=redis
ENV REDIS_PORT=6379

EXPOSE 25565
CMD ["java", "-jar", "CoreLobby.jar"]
