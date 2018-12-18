FROM openjdk:8-alpine

VOLUME /tmp

ADD target/camunda-kafka-polling-client.jar app.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "/app.jar"]
