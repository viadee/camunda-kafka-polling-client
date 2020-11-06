package de.viadee.camunda.kafka.pollingclient.service.event;

import de.viadee.camunda.kafka.event.DecisionInstanceEvent;
import de.viadee.camunda.kafka.event.DecisionInstanceInputEvent;
import de.viadee.camunda.kafka.event.DeploymentEvent;
import de.viadee.camunda.kafka.event.HistoryEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <p>
 * EventService interface.
 * </p>
 *
 * @author viadee
 * @version $Id: $Id
 */
public interface EventService {

    /**
     * <p>
     * sendEvent.
     * </p>
     *
     * @param event
     *            a {@link de.viadee.camunda.kafka.event.HistoryEvent} object.
     */
    void sendEvent(HistoryEvent event);

    /**
     * <p>
     * sendEvent.
     * </p>
     *
     * @param event
     *            a {@link de.viadee.camunda.kafka.event.DeploymentEvent} object.
     */
    void sendEvent(DeploymentEvent event);
}
