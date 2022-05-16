package de.viadee.camunda.kafka.pollingclient.service.polling;

import de.viadee.camunda.kafka.event.*;

import java.util.Date;

/**
 * <p>
 * PollingService interface.
 * </p>
 *
 * @author viadee
 * @version $Id: $Id
 */
public interface PollingService {

    /**
     * Poll finished process instances from history.
     *
     * @param startedAfter
     *            inclusive
     * @param startedBefore
     *            exclusive
     * @param finishedAfter
     *            inclusive
     * @return a {@link java.lang.Iterable} object.
     */
    Iterable<ProcessInstanceEvent> pollFinishedProcessInstances(Date startedAfter, Date startedBefore,
                                                                Date finishedAfter);

    /**
     * Poll unfinished process instances.
     *
     * @param startedAfter
     *            inclusive
     * @param startedBefore
     *            exclusive
     * @return a {@link java.lang.Iterable} object.
     */
    Iterable<ProcessInstanceEvent> pollUnfinishedProcessInstances(Date startedAfter, Date startedBefore);

    /**
     * Poll finished activities from history.
     *
     * @param processInstanceId
     *            a {@link java.lang.String} object.
     * @param finishedAfter
     *            inclusive
     * @param finishedBefore
     *            exclusive
     * @return a {@link java.lang.Iterable} object.
     */
    Iterable<ActivityInstanceEvent> pollFinishedActivities(String processInstanceId, Date finishedAfter,
                                                           Date finishedBefore);

    /**
     * Poll unfinished activities.
     *
     * @param processInstanceId
     *            a {@link java.lang.String} object.
     * @param startedAfter
     *            inclusive
     * @param startedBefore
     *            exclusive
     * @return a {@link java.lang.Iterable} object.
     */
    Iterable<ActivityInstanceEvent> pollUnfinishedActivities(String processInstanceId, Date startedAfter,
                                                             Date startedBefore);

    /**
     * <p>
     * pollCurrentVariables.
     * </p>
     *
     * @param activityInstanceId
     *            a {@link java.lang.String} object.
     * @return a {@link java.lang.Iterable} object.
     */
    Iterable<VariableUpdateEvent> pollCurrentVariables(String activityInstanceId);

    /**
     * <p>
     * pollVariableDetails.
     * </p>
     *
     * @param activityInstanceId
     *            a {@link java.lang.String} object.
     * @return a {@link java.lang.Iterable} object.
     */
    Iterable<VariableUpdateEvent> pollVariableDetails(String activityInstanceId);

    /**
     * Poll process definitions
     *
     * @param deploymentAfter
     *            inclusive
     * @param deploymentBefore
     *            exclusive
     * @return a {@link java.lang.Iterable} object.
     */
    Iterable<ProcessDefinitionEvent> pollProcessDefinitions(Date deploymentAfter, Date deploymentBefore);

    /**
     * Poll comments for specified task
     *
     * @param activityInstanceEvent
     * @return a {@link java.lang.Iterable} object.
     */
    Iterable<CommentEvent> pollComments(ActivityInstanceEvent activityInstanceEvent);

    /**
     * Poll Identity-Links for specified task
     *
     * @param activityInstanceEvent
     * @return a {@link java.lang.Iterable} object.
     */
    Iterable<IdentityLinkEvent> pollIdentityLinks(ActivityInstanceEvent activityInstanceEvent);

    /**
     * Poll decision definitions
     *
     * @param deploymentAfter
     *            inclusive
     * @param deploymentBefore
     *            exclusive
     * @return a {@link java.lang.Iterable} object.
     */
    Iterable<DecisionDefinitionEvent> pollDecisionDefinitions(Date deploymentAfter, Date deploymentBefore);
}
