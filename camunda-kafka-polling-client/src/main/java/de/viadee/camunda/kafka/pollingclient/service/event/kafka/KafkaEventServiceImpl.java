package de.viadee.camunda.kafka.pollingclient.service.event.kafka;

import static org.apache.commons.lang3.StringUtils.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.viadee.camunda.kafka.pollingclient.config.properties.ApplicationProperties;
import de.viadee.camunda.kafka.pollingclient.service.event.EventService;
import de.viadee.camunda.kafka.event.DeploymentEvent;
import de.viadee.camunda.kafka.event.HistoryEvent;

@Component
public class KafkaEventServiceImpl implements EventService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ApplicationProperties properties;

    @Autowired
    public KafkaEventServiceImpl(KafkaTemplate<String, String> kafkaTemplate, ApplicationProperties properties) {
        this.kafkaTemplate = kafkaTemplate;
        this.properties = properties;
    }

    @Override
    public void sendEvent(HistoryEvent event) {
        try {
            final String payload = this.objectMapper.writeValueAsString(event);

            kafkaTemplate.send(getTopicName(event), event.getId(), payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not convert history event to json", e);
        }
    }

    @Override
    public void sendEvent(final DeploymentEvent event) {
        try {
            final String payload = this.objectMapper.writeValueAsString(event);

            kafkaTemplate.send(getTopicName(event), event.getId(), payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not convert deployment event to json", e);
        }
    }

    private String getTopicName(DeploymentEvent event) {
        final String eventName = uncapitalize(removeEnd(event.getClass().getSimpleName(), "Event"));

        return properties.getEventTopics().getOrDefault(eventName, eventName);
    }

    private String getTopicName(HistoryEvent event) {
        final String eventName = uncapitalize(removeEnd(event.getClass().getSimpleName(), "Event"));

        return properties.getEventTopics().getOrDefault(eventName, eventName);
    }
}
