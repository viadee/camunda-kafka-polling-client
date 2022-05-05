package de.viadee.camunda.kafka.pollingclient.job.runtime;

import de.viadee.camunda.kafka.event.*;
import de.viadee.camunda.kafka.pollingclient.config.properties.ApplicationProperties;
import de.viadee.camunda.kafka.pollingclient.service.event.EventService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.LastPolledService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.PollingTimeslice;
import de.viadee.camunda.kafka.pollingclient.service.polling.PollingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Implementation of polling runtime data
 *
 * <p>
 * In General, polling is done for running and finished process instances. We have to keep in mind, this state is by now
 * and the data is polled for a given time slice. This time slice might be slightly in the past or (in case we redo a
 * broken polling) quite a lot.
 * </p>
 *
 * @author viadee
 * @version $Id: $Id
 */
public class RuntimeDataPollingService implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeDataPollingService.class);

    private final PollingService pollingService;

    private final LastPolledService lastPolledService;

    private final EventService eventService;

    private final ApplicationProperties properties;

    /**
     * <p>
     * Constructor for RuntimeDataPollingService.
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
    public RuntimeDataPollingService(PollingService pollingService, LastPolledService lastPolledService,
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

        LOGGER.info("Start polling runtime data: {}", pollingTimeslice);

        pollUnfinishedProcessInstances(pollingTimeslice);
        pollFinishedProcessInstances(pollingTimeslice);

        lastPolledService.updatePollingTimeslice(pollingTimeslice);

        LOGGER.info("Finished polling runtime data: {}", pollingTimeslice);
    }

    private void pollUnfinishedProcessInstances(final PollingTimeslice pollingTimeslice) {
        if (properties.getPollingEvents()
                      .contains(ApplicationProperties.PollingEvents.PROCESS_INSTANCE_UNFINISHED)) {
            // Select all running process instances which have been started before end of our polling slice. (We might
            // resume a poll of a slice long ago.)
            // All started before cutoff are ignored by general rule.
            for (final ProcessInstanceEvent processInstanceEvent : pollingService
                                                                                 .pollUnfinishedProcessInstances(pollingTimeslice.getCutoffTime(),
                                                                                                                 pollingTimeslice.getEndTime())) {

                // To prevent always sending process started events during every polling cycle,
                // limit event to the polling cycle where the process has been started.
                if (isProcessInstanceStartedBetween(processInstanceEvent, pollingTimeslice.getStartTime(),
                                                    pollingTimeslice.getEndTime())) {
                    // Send process event only once if started during polling intervall
                    eventService.sendEvent(processInstanceEvent);
                }

                pollUnfinishedActivities(processInstanceEvent.getProcessInstanceId(), pollingTimeslice);
                pollFinishedActivities(processInstanceEvent.getProcessInstanceId(), pollingTimeslice);
            }
        }
    }

    private void pollFinishedProcessInstances(final PollingTimeslice pollingTimeslice) {
        if (properties.getPollingEvents()
                      .contains(ApplicationProperties.PollingEvents.PROCESS_INSTANCE_FINISHED)) {
            // Select all finished process instances, which have been active during polling slice. (=Overlapping with
            // the slice)
            // All started before cutoff are ignored by general rule.
            for (final ProcessInstanceEvent processInstanceEvent : pollingService
                                                                                 .pollFinishedProcessInstances(pollingTimeslice.getCutoffTime(),
                                                                                                               pollingTimeslice.getEndTime(),
                                                                                                               pollingTimeslice.getStartTime())) {

                // To prevent sending to many process instance events, limit to the two polling cycles the process has
                // been started and finished.
                if (isProcessInstanceStartedBetween(processInstanceEvent, pollingTimeslice.getStartTime(),
                                                    pollingTimeslice.getEndTime())
                        || isProcessInstanceEndedBetween(processInstanceEvent, pollingTimeslice.getStartTime(),
                                                         pollingTimeslice.getEndTime())) {
                    eventService.sendEvent(processInstanceEvent);
                }

                pollFinishedActivities(processInstanceEvent.getProcessInstanceId(), pollingTimeslice);
            }
        }
    }

    private void pollUnfinishedActivities(final String processInstanceId, final PollingTimeslice pollingTimeslice) {
        if (properties.getPollingEvents()
                      .contains(ApplicationProperties.PollingEvents.ACTIVITY_UNFINISHED)) {
            for (final ActivityInstanceEvent activityInstanceEvent : pollingService
                                                                                   .pollUnfinishedActivities(processInstanceId,
                                                                                                             pollingTimeslice.getStartTime(),
                                                                                                             pollingTimeslice.getEndTime())) {
                eventService.sendEvent(activityInstanceEvent);

                if (properties.getPollingEvents()
                              .contains(ApplicationProperties.PollingEvents.VARIABLE_DETAILS_UNFINISHED)) {
                    pollVariableDetails(activityInstanceEvent.getActivityInstanceId());
                }

                if (properties.getPollingEvents()
                              .contains(ApplicationProperties.PollingEvents.VARIABLE_CURRENT_UNFINISHED)) {
                    pollCurrentVariables(activityInstanceEvent.getActivityInstanceId());
                }

                if (properties.getPollingEvents()
                              .contains(ApplicationProperties.PollingEvents.TASK_COMMENTS)
                        && activityInstanceEvent.getActivityType().equals("userTask")) {
                    pollComments(activityInstanceEvent);
                }

                if (properties.getPollingEvents()
                              .contains(ApplicationProperties.PollingEvents.IDENTITY_LINKS_UNFINISHED_ACTIVITIES)
                        && activityInstanceEvent.getActivityType().equals("userTask")) {
                    pollIdentityLinks(activityInstanceEvent);
                }
            }
        }
    }

    private void pollFinishedActivities(final String processInstanceId, final PollingTimeslice pollingTimeslice) {
        if (properties.getPollingEvents().contains(ApplicationProperties.PollingEvents.ACTIVITY_FINISHED)) {
            for (final ActivityInstanceEvent activityInstanceEvent : pollingService
                                                                                   .pollFinishedActivities(processInstanceId,
                                                                                                           pollingTimeslice.getStartTime(),
                                                                                                           pollingTimeslice.getEndTime())) {
                eventService.sendEvent(activityInstanceEvent);

                if (properties.getPollingEvents()
                              .contains(ApplicationProperties.PollingEvents.VARIABLE_DETAILS_FINISHED)) {
                    pollVariableDetails(activityInstanceEvent.getActivityInstanceId());
                }

                if (properties.getPollingEvents()
                              .contains(ApplicationProperties.PollingEvents.VARIABLE_CURRENT_FINISHED)) {
                    pollCurrentVariables(activityInstanceEvent.getActivityInstanceId());
                }

                if (properties.getPollingEvents()
                              .contains(ApplicationProperties.PollingEvents.TASK_COMMENTS)
                        && activityInstanceEvent.getActivityType().equals("userTask")) {
                    pollComments(activityInstanceEvent);
                }

                if (properties.getPollingEvents()
                              .contains(ApplicationProperties.PollingEvents.IDENTITY_LINKS_FINISHED_ACTIVITIES)
                        && activityInstanceEvent.getActivityType().equals("userTask")) {
                    pollIdentityLinks(activityInstanceEvent);
                }
            }
        }
    }

    private void pollCurrentVariables(final String activityInstanceId) {
        for (final VariableUpdateEvent variableUpdateEvent : pollingService
                                                                           .pollCurrentVariables(activityInstanceId)) {
            eventService.sendEvent(variableUpdateEvent);
        }
    }

    private void pollVariableDetails(final String activityInstanceId) {
        for (final VariableUpdateEvent variableUpdateEvent : pollingService
                                                                           .pollVariableDetails(activityInstanceId)) {
            eventService.sendEvent(variableUpdateEvent);
        }
    }

    private void pollComments(final ActivityInstanceEvent activityInstanceEvent) {
        for (final CommentEvent commentEvent : pollingService
                                                             .pollComments(activityInstanceEvent)) {
            eventService.sendEvent(commentEvent);
        }
    }

    private void pollIdentityLinks(final ActivityInstanceEvent activityInstanceEvent) {
        for (final IdentityLinkEvent identityLinkEvent : pollingService
                                                                       .pollIdentityLinks(activityInstanceEvent)) {
            eventService.sendEvent(identityLinkEvent);
        }
    }

    private boolean isProcessInstanceStartedBetween(final ProcessInstanceEvent processInstanceEvent,
                                                    final Date startTime, final Date endTime) {
        if (processInstanceEvent.getStartTime() == null) {
            return true;
        }

        return processInstanceEvent.getStartTime().compareTo(startTime) >= 0
                && processInstanceEvent.getStartTime().compareTo(endTime) < 0;
    }

    private boolean isProcessInstanceEndedBetween(final ProcessInstanceEvent processInstanceEvent, final Date startTime,
                                                  final Date endTime) {
        if (processInstanceEvent.getEndTime() == null) {
            return false;
        }

        return processInstanceEvent.getEndTime().compareTo(startTime) >= 0
                && processInstanceEvent.getEndTime().compareTo(endTime) < 0;
    }

}
