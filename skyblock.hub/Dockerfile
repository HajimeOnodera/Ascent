FROM amazoncorretto:25-alpine
WORKDIR /app
COPY skyblock.hub/build/libs/SkyblockHub.jar .
COPY maps/ ./maps
COPY configuration/ ./configuration/
COPY skyblock.dungeon/akyblockResources/ ./skyblock.dungeon/akyblockResources/
COPY proxy/forwarding.secret ./forwarding.secret

# Environment variables
ENV MONGODB_URI=mongodb://host.docker.internal:27017
ENV REDIS_HOST=host.docker.internal
ENV REDIS_PORT=6379

EXPOSE 25565
CMD ["java", "-jar", "SkyblockHub.jar"]
