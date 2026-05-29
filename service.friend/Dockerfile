# Run stage only - build JAR locally with: ./gradlew :service.friend:shadowJar
FROM amazoncorretto:25-alpine
WORKDIR /app
COPY service.friend/build/libs/FriendService.jar .

# Environment variables
ENV MONGODB_URI=mongodb://mongodb:27017
ENV REDIS_HOST=redis
ENV REDIS_PORT=6379

CMD ["java", "-jar", "FriendService.jar"]
