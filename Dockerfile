#FROM maven:3.6.3-openjdk-17 AS build
#WORKDIR /
#COPY pom.xml /
#COPY /src /src
#RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY target/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]