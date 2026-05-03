FROM eclipse-temurin:17-jre
ARG JAR_FILE=target/restfulApiInMap-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_Inmap.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app_Inmap.jar"]
