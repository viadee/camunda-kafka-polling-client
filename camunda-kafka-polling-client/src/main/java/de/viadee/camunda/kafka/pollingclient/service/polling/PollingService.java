package de.viadee.camunda.kafka.pollingclient.service.polling;

import java.util.Date;

import de.viadee.camunda.kafka.event.ActivityInstanceEvent;
import de.viadee.camunda.kafka.event.ProcessDefinitionEvent;
import de.viadee.camunda.kafka.event.ProcessInstanceEvent;
import de.viadee.camunda.kafka.event.VariableUpdateEvent;

public interface PollingService {

    /**
     * Poll finished process instances from history.
     *
     * @param startedAfter  inclusive
     * @param startedBefore exclusive
     * @param finishedAfter inclusive
     */
    Iterable<ProcessInstanceEvent> pollFinishedProcessInstances(Date startedAfter, Date startedBefore,
                                                                Date finishedAfter);

    /**
     * Poll unfinished process instances.
     *
     * @param startedAfter  inclusive
     * @param startedBefore exclusive
     */
    Iterable<ProcessInstanceEvent> pollUnfinishedProcessInstances(Date startedAfter, Date startedBefore);

    /**
     * Poll finished activities from history.
     *
     * @param processInstanceId
     * @param finishedAfter     inclusive
     * @param finishedBefore    exclusive
     */
    Iterable<ActivityInstanceEvent> pollFinishedActivities(String processInstanceId, Date finishedAfter,
                                                           Date finishedBefore);

    /**
     * Poll unfinished activities.
     *
     * @param processInstanceId
     * @param startedAfter      inclusive
     * @param startedBefore     exclusive
     *
     * @return
     */
    Iterable<ActivityInstanceEvent> pollUnfinishedActivities(String processInstanceId, Date startedAfter,
                                                             Date startedBefore);

    Iterable<VariableUpdateEvent> pollCurrentVariables(String activityInstanceId);

    Iterable<VariableUpdateEvent> pollVariableDetails(String activityInstanceId);

    /**
     * Poll process definitions
     *
     * @param deploymentAfter  inclusive
     * @param deploymentBefore exclusive
     */
    Iterable<ProcessDefinitionEvent> pollProcessDefinitions(Date deploymentAfter, Date deploymentBefore);
}
