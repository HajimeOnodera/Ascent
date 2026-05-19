# Run stage only - build JAR locally with: ./gradlew :service.party:shadowJar
FROM amazoncorretto:25-alpine
WORKDIR /app
COPY service.party/build/libs/PartyService.jar .

# Environment variables
ENV MONGODB_URI=mongodb://mongodb:27017
ENV REDIS_HOST=redis
ENV REDIS_PORT=6379

CMD ["java", "-jar", "PartyService.jar"]
