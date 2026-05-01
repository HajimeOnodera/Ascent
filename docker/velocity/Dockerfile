FROM maven:3.9.11-eclipse-temurin-25 AS plugin-build

WORKDIR /workspace

COPY pom.xml .
COPY commons/pom.xml commons/pom.xml
COPY core-lobby/pom.xml core-lobby/pom.xml
COPY core-proxy/pom.xml core-proxy/pom.xml
COPY core-skyblock/pom.xml core-skyblock/pom.xml
COPY core-proxy/src core-proxy/src

RUN mvn -q -pl core-proxy package -DskipTests

FROM eclipse-temurin:25-jre

WORKDIR /app

RUN mkdir -p /app/plugins

COPY proxy/velocity-3.5.0-SNAPSHOT-593.jar /app/velocity.jar
COPY --from=plugin-build /workspace/core-proxy/target/CoreProxy.jar /app/plugins/CoreProxy.jar
COPY deploy/docker/velocity.toml /app/velocity.toml

EXPOSE 25565

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75", "-jar", "/app/velocity.jar"]
