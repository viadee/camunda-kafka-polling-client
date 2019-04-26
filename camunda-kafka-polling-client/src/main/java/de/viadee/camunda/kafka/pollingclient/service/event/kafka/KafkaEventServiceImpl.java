package de.viadee.camunda.kafka.pollingclient.service.event.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.viadee.camunda.kafka.event.DeploymentEvent;
import de.viadee.camunda.kafka.event.HistoryEvent;
import de.viadee.camunda.kafka.pollingclient.config.properties.ApplicationProperties;
import de.viadee.camunda.kafka.pollingclient.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

/**
 * <p>
 * KafkaEventServiceImpl class.
 * </p>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Component
public class KafkaEventServiceImpl implements EventService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ApplicationProperties properties;

    @Autowired
    /**
     * <p>
     * Constructor for KafkaEventServiceImpl.
     * </p>
     *
     * @param kafkaTemplate
     *            a {@link org.springframework.kafka.core.KafkaTemplate} object.
     * @param properties
     *            a {@link de.viadee.camunda.kafka.pollingclient.config.properties.ApplicationProperties} object.
     */
    public KafkaEventServiceImpl(KafkaTemplate<String, String> kafkaTemplate, ApplicationProperties properties) {
        this.kafkaTemplate = kafkaTemplate;
        this.properties = properties;
    }

    /** {@inheritDoc} */
    @Override
    public void sendEvent(HistoryEvent event) {
        try {
            final String payload = this.objectMapper.writeValueAsString(event);

            kafkaTemplate.send(getTopicName(event), event.getId(), payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not convert history event to json", e);
        }
    }

    /** {@inheritDoc} */
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
