FROM gradle:jdk21-jammy as build
LABEL authors="mrpri"
WORKDIR /app
COPY . .
RUN gradle clean build

FROM openjdk:21-ea-32-jdk-slim
COPY --from=build /app/build/libs/authorization-microservice-api-1.0-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
