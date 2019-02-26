FROM openjdk:8-alpine
RUN addgroup -g 1000 -S appuser && adduser -u 1000 -S appuser -G appuser && \
    mkdir -p /data && chown appuser:appuser /data
VOLUME /data
USER appuser
COPY camunda-kafka-polling-client/target/camunda-kafka-polling-client.jar app.jar
ENTRYPOINT ["/usr/bin/java", "-jar", "/app.jar"]
