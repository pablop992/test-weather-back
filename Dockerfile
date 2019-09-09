
FROM openjdk:8-jdk-alpine

LABEL owner="pabloe.pelaez06@gmail.com"

EXPOSE 9091
ARG JAR_FILE=target/test-weather-back-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} test-weather-back.jar
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /test-weather-back.jar" ]