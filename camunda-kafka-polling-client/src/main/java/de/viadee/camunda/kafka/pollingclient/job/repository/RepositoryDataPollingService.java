package de.viadee.camunda.kafka.pollingclient.job.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.viadee.camunda.kafka.pollingclient.config.properties.ApplicationProperties;
import de.viadee.camunda.kafka.pollingclient.service.event.EventService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.LastPolledService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.PollingTimeslice;
import de.viadee.camunda.kafka.pollingclient.service.polling.PollingService;
import de.viadee.camunda.kafka.event.ProcessDefinitionEvent;

/**
 * Implementation of polling repository data
 */
public class RepositoryDataPollingService implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryDataPollingService.class);

    private final PollingService pollingService;

    private final LastPolledService lastPolledService;

    private final EventService eventService;

    private final ApplicationProperties properties;

    public RepositoryDataPollingService(PollingService pollingService, LastPolledService lastPolledService,
            EventService eventService, ApplicationProperties properties) {
        this.pollingService = pollingService;
        this.lastPolledService = lastPolledService;
        this.eventService = eventService;
        this.properties = properties;
    }

    @Override
    public void run() {
        final PollingTimeslice pollingTimeslice = lastPolledService.getPollingTimeslice();

        LOGGER.info("Start polling data: {}", pollingTimeslice);

        pollProcessDefinitions(pollingTimeslice);

        lastPolledService.updatePollingTimeslice(pollingTimeslice);

        LOGGER.info("Finished polling data: {}", pollingTimeslice);
    }

    private void pollProcessDefinitions(final PollingTimeslice pollingTimeslice) {
        if (properties.getPollingEvents().contains(ApplicationProperties.PollingEvents.PROCESS_DEFINITION)) {
            for (final ProcessDefinitionEvent processDefinitionEvent : pollingService
                    .pollProcessDefinitions(pollingTimeslice.getStartTime(), pollingTimeslice.getEndTime())) {
                eventService.sendEvent(processDefinitionEvent);
            }
        }
    }
}
