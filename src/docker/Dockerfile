FROM openjdk:11

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=local -jar /app.jar"]