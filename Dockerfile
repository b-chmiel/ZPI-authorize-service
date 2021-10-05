FROM openjdk:11-jre-slim
EXPOSE 8080
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]