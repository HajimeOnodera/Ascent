FROM maven:3.9.11-eclipse-temurin-25 AS build

WORKDIR /workspace

ARG MODULE
ARG JAR_NAME

COPY pom.xml .
COPY commons/pom.xml commons/pom.xml
COPY core-lobby/pom.xml core-lobby/pom.xml
COPY core-skyblock/pom.xml core-skyblock/pom.xml
COPY core-proxy/pom.xml core-proxy/pom.xml

RUN mvn -q -pl "${MODULE}" -am dependency:go-offline

COPY commons/src commons/src
COPY core-lobby/src core-lobby/src
COPY core-skyblock/src core-skyblock/src
COPY core-proxy/src core-proxy/src

RUN mvn -q -pl "${MODULE}" -am package -DskipTests

FROM eclipse-temurin:25-jre

WORKDIR /app

ARG MODULE
ARG JAR_NAME

COPY --from=build /workspace/${MODULE}/target/${JAR_NAME}.jar /app/server.jar
COPY maps /app/maps

EXPOSE 25565 25566 25567

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75", "-jar", "/app/server.jar"]
