FROM maven:3.9.9-eclipse-temurin-21 AS buidler
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=buidler /app/target/identity-service.jar /app/identity-service.jar
CMD ["java", "-jar", "/app/identity-service.jar"]