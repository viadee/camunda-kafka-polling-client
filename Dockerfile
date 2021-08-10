FROM openjdk:11-jre

RUN addgroup --system --gid 1000 appuser && \
    adduser --system --no-create-home --disabled-login --uid 1000 --ingroup appuser appuser && \
    mkdir -p /app && \
    mkdir -p /data

ARG APP_COMPONENT_DIR=camunda-kafka-polling-client/target/dependency

COPY ${APP_COMPONENT_DIR}/BOOT-INF/lib /app/lib
COPY ${APP_COMPONENT_DIR}/BOOT-INF/classes /app/bin
COPY ${APP_COMPONENT_DIR}/META-INF /app/bin/META-INF

RUN chown -R appuser:appuser /app && \
    chown -R appuser:appuser /data && \
    find /app -type d -exec chmod 550 {} + && \
    find /app -type f -exec chmod 660 {} + && \
    chmod 770 /data

VOLUME /data
USER appuser

WORKDIR /data
ENTRYPOINT ["java", "-cp", "/app/bin:/app/lib/*", "de.viadee.camunda.kafka.pollingclient.PollingClientApplication"]
