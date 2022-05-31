package de.viadee.camunda.kafka.pollingclient.job.repository;

import de.viadee.camunda.kafka.event.DecisionDefinitionEvent;
import de.viadee.camunda.kafka.event.ProcessDefinitionEvent;
import de.viadee.camunda.kafka.event.ProcessInstanceEvent;
import de.viadee.camunda.kafka.pollingclient.config.properties.ApplicationProperties;
import de.viadee.camunda.kafka.pollingclient.service.event.EventService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.LastPolledService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.PollingTimeslice;
import de.viadee.camunda.kafka.pollingclient.service.polling.PollingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Implementation of polling repository data
 *
 * @author viadee
 * @version $Id: $Id
 */
public class RepositoryDataPollingService implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryDataPollingService.class);

    private final PollingService pollingService;

    private final LastPolledService lastPolledService;

    private final EventService eventService;

    private final ApplicationProperties properties;

    /**
     * <p>
     * Constructor for RepositoryDataPollingService.
     * </p>
     *
     * @param pollingService
     *            a {@link de.viadee.camunda.kafka.pollingclient.service.polling.PollingService} object.
     * @param lastPolledService
     *            a {@link de.viadee.camunda.kafka.pollingclient.service.lastpolled.LastPolledService} object.
     * @param eventService
     *            a {@link de.viadee.camunda.kafka.pollingclient.service.event.EventService} object.
     * @param properties
     *            a {@link de.viadee.camunda.kafka.pollingclient.config.properties.ApplicationProperties} object.
     */
    public RepositoryDataPollingService(PollingService pollingService, LastPolledService lastPolledService,
            EventService eventService, ApplicationProperties properties) {
        this.pollingService = pollingService;
        this.lastPolledService = lastPolledService;
        this.eventService = eventService;
        this.properties = properties;
    }

    /** {@inheritDoc} */
    @Override
    public void run() {
        final PollingTimeslice pollingTimeslice = lastPolledService.getPollingTimeslice();

        LOGGER.info("Start polling repository data: {}", pollingTimeslice);

        pollProcessDefinitions(pollingTimeslice);
        pollDecisionDefinitions(pollingTimeslice);

        lastPolledService.updatePollingTimeslice(pollingTimeslice);

        LOGGER.info("Finished polling repository data: {}", pollingTimeslice);
    }

    private void pollDecisionDefinitions(PollingTimeslice pollingTimeslice) {
        if (properties.getPollingEvents().contains(ApplicationProperties.PollingEvents.DECISION_DEFINITION)) {
            for (final DecisionDefinitionEvent decisionDefinitionEvent : pollingService.pollDecisionDefinitions(pollingTimeslice.getStartTime(),
                                                                                                                pollingTimeslice.getEndTime())) {
                eventService.sendEvent(decisionDefinitionEvent);
            }
        }
    }

    private void pollProcessDefinitions(final PollingTimeslice pollingTimeslice) {
        if (properties.getPollingEvents().contains(ApplicationProperties.PollingEvents.PROCESS_DEFINITION)) {
            for (final ProcessDefinitionEvent processDefinitionEvent : pollingService
                                                                                     .pollProcessDefinitions(pollingTimeslice.getStartTime(),
                                                                                                             pollingTimeslice.getEndTime())) {
                eventService.sendEvent(processDefinitionEvent);
            }
        }
    }
}
