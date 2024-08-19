FROM maven:3.6.3-openjdk-17 AS build
WORKDIR /
COPY pom.xml /
RUN mvn dependency:go-offline
COPY /src /src
RUN mvn clean package -DskipTests && ls -la /target

FROM openjdk:17-jdk-slim
COPY --from=build /target/*.jar /application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]