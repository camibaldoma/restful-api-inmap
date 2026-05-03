FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY src src

RUN chmod +x mvnw && ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /app/target/restfulApiInMap-0.0.1-SNAPSHOT.jar app_Inmap.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app_Inmap.jar"]
