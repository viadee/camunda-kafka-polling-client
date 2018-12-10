FROM openjdk:8-alpine

VOLUME /tmp

ADD target/*.jar app.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "/app.jar"]