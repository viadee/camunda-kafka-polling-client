package de.viadee.camunda.kafka.pollingclient.service.event;

import de.viadee.camunda.kafka.event.DeploymentEvent;
import de.viadee.camunda.kafka.event.HistoryEvent;

public interface EventService {

    void sendEvent(HistoryEvent event);

    void sendEvent(DeploymentEvent event);
}
