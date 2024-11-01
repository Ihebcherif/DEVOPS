FROM openjdk:17

# Set the working directory
WORKDIR /app

COPY target/*.jar /app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app.jar"]