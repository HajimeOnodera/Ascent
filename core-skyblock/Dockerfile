# Run stage only - build JAR locally with: ./gradlew :core-skyblock:shadowJar
FROM amazoncorretto:25-alpine
WORKDIR /app
COPY core-skyblock/build/libs/CoreSkyblock.jar .
COPY maps/ ./maps
COPY core-skyblock/configuration/ ./configuration/
COPY proxy/forwarding.secret ./forwarding.secret

# Environment variables
ENV MONGODB_URI=mongodb://host.docker.internal:27017
ENV REDIS_HOST=host.docker.internal
ENV REDIS_PORT=6379

EXPOSE 25565
CMD ["java", "-jar", "CoreSkyblock.jar"]
