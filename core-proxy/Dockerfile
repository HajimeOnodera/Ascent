# Run stage only - build JAR locally with: ./gradlew :core-proxy:shadowJar
FROM amazoncorretto:25-alpine
WORKDIR /app

# Copy Velocity
COPY proxy/velocity-3.5.0-SNAPSHOT-593.jar ./velocity.jar
COPY proxy/velocity.toml ./velocity.toml
COPY proxy/forwarding.secret ./forwarding.secret

# Setup Plugins
RUN mkdir -p plugins
COPY core-proxy/build/libs/CoreProxy.jar ./plugins/CoreProxy.jar

# Environment variables
ENV REDIS_HOST=redis
ENV REDIS_PORT=6379

EXPOSE 25577
CMD ["java", "-Xmx512M", "-jar", "velocity.jar"]
