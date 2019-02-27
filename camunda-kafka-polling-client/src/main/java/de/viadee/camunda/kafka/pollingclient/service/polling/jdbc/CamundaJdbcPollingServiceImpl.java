package de.viadee.camunda.kafka.pollingclient.service.polling.jdbc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricDetail;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntity;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.viadee.camunda.kafka.pollingclient.service.polling.PollingService;
import de.viadee.camunda.kafka.pollingclient.service.polling.rest.CamundaRestPollingServiceImpl;
import de.viadee.camunda.kafka.event.ActivityInstanceEvent;
import de.viadee.camunda.kafka.event.CommentEvent;
import de.viadee.camunda.kafka.event.ProcessDefinitionEvent;
import de.viadee.camunda.kafka.event.ProcessInstanceEvent;
import de.viadee.camunda.kafka.event.VariableUpdateEvent;

/**
 * <p>CamundaJdbcPollingServiceImpl class.</p>
 *
 * @author viadee
 * @version $Id: $Id
 */
public class CamundaJdbcPollingServiceImpl implements PollingService {

    private final HistoryService historyService;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaRestPollingServiceImpl.class);

    private final RepositoryService repositoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TaskService taskService;

    /**
     * <p>Constructor for CamundaJdbcPollingServiceImpl.</p>
     *
     * @param historyService a {@link HistoryService} object.
     * @param repositoryService a {@link RepositoryService} object.
     * @param taskService a {@link TaskService} object.
     */
    public CamundaJdbcPollingServiceImpl(HistoryService historyService, RepositoryService repositoryService, TaskService taskService) {
        this.historyService = historyService;
        this.repositoryService = repositoryService;
        this.taskService = taskService;
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<ProcessInstanceEvent> pollFinishedProcessInstances(Date startedAfter, Date startedBefore,
            Date finishedAfter) {
        return historyService.createHistoricProcessInstanceQuery()
                .finished()
                .startedAfter(startedAfter)
                .startedBefore(startedBefore)
                .finishedAfter(finishedAfter)
                .list()
                .stream()
                .filter(event -> event.getStartTime().compareTo(startedBefore) < 0) // startedBefore ist selected as <= by Camunda - thus add filter
                .map(this::createProcessInstanceEvent)
                ::iterator;
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<ProcessInstanceEvent> pollUnfinishedProcessInstances(Date startedAfter, Date startedBefore) {
        return historyService.createHistoricProcessInstanceQuery()
                .unfinished()
                .startedAfter(startedAfter)
                .startedBefore(startedBefore)
                .list()
                .stream()
                .filter(event -> event.getStartTime().compareTo(startedBefore) < 0) // startedBefore ist selected as <= by Camunda - thus add filter
                .map(this::createProcessInstanceEvent)
                ::iterator;
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<ActivityInstanceEvent> pollFinishedActivities(String processInstanceId, Date finishedAfter,
            Date finishedBefore) {
        return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .finishedAfter(finishedAfter)
                .finishedBefore(finishedBefore)
                .list()
                .stream()
                .filter(event -> event.getEndTime().compareTo(finishedBefore) < 0) // finishedBefore ist selected as <= by Camunda - thus add filter
                .map(this::createActivityInstanceEvent)
                ::iterator;
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<ActivityInstanceEvent> pollUnfinishedActivities(String processInstanceId, Date startedAfter,
            Date startedBefore) {
        return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .unfinished()
                .startedAfter(startedAfter)
                .startedBefore(startedBefore)
                .list()
                .stream()
                .filter(event -> event.getStartTime().compareTo(startedBefore) < 0) // startedBefore ist selected as <= by Camunda - thus add filter
                .map(this::createActivityInstanceEvent)
                ::iterator;
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<VariableUpdateEvent> pollCurrentVariables(String activityInstanceId) {
        return historyService.createHistoricVariableInstanceQuery()
                .activityInstanceIdIn(activityInstanceId)
                .disableCustomObjectDeserialization()
                .list()
                .stream()
                .map(this::createVariableUpdateEventFromInstance)
                ::iterator;
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<VariableUpdateEvent> pollVariableDetails(String activityInstanceId) {
        return historyService.createHistoricDetailQuery()
                .activityInstanceId(activityInstanceId)
                .disableCustomObjectDeserialization()
                .variableUpdates()
                .list()
                .stream()
                .map(this::createVariableUpdateEventFromDetail)
                ::iterator;
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<ProcessDefinitionEvent> pollProcessDefinitions(Date deploymentAfter, Date deploymentBefore) {

        // There seems to be a slight bug in Camunda SQL queries regarding deployments.
        // Where the other history queries regarding time boundaries are inclusive (startedBefore, startedAfter, ...),
        // deploymentBefore and deploymentAfter are implemented exclusive.
        // Thus we have to slightly adjust the deploymentAfter parameter by 1 millisecond to act inclusive:
        deploymentAfter = new Date(deploymentAfter.getTime()-1);

        // query deployments
        List<Deployment> deployments = repositoryService.createDeploymentQuery()
                .deploymentAfter(deploymentAfter)
                .deploymentBefore(deploymentBefore)
                .list();

        List<ProcessDefinitionEvent> result = new ArrayList<>();

        for (Deployment deployment : deployments) {
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId()).list();

            // query proc def
            for (ProcessDefinition processDefinition : processDefinitions) {
                ProcessDefinitionEvent processDefinitionEvent = createProcessDefinitionEvent(deployment,
                        processDefinition);

                // query xml
                try {
                    String xml = IOUtils.toString(repositoryService.getProcessModel(processDefinition.getId()));
                    processDefinitionEvent.setXml(xml);
                } catch (IOException e) {
                    throw new RuntimeException(
                            "error while reading xml for process definition " + processDefinition.getId(), e);
                }

                result.add(processDefinitionEvent);
            }

        }

        return result;
    }


    @Override
    public Iterable<CommentEvent> pollComments(ActivityInstanceEvent activityInstanceEvent) {

        return  taskService.getTaskComments(activityInstanceEvent.getTaskId())
                .stream()
                .map(comment ->  createCommentEventFromDetails(comment, activityInstanceEvent))
                ::iterator;
    }


    private ProcessDefinitionEvent createProcessDefinitionEvent(Deployment d, ProcessDefinition pd) {

        ProcessDefinitionEvent e = new ProcessDefinitionEvent();
        e.setId(pd.getId());
        e.setCategory(pd.getCategory());
        e.setDescription(pd.getDescription());
        e.setHistoryTimeToLive(pd.getHistoryTimeToLive());
        e.setKey(pd.getKey());
        e.setName(pd.getName());
        e.setResource(pd.getResourceName());
        e.setSuspended(pd.isSuspended());
        e.setVersion(pd.getVersion());
        e.setVersionTag(pd.getVersionTag());
        e.setDeploymentId(pd.getDeploymentId());
        e.setTenantId(pd.getTenantId());
        e.setDeploymentTime(d.getDeploymentTime());
        e.setSource(d.getSource());
        return e;

    }

    private ProcessInstanceEvent createProcessInstanceEvent(HistoricProcessInstance historicProcessInstance) {
        final ProcessInstanceEvent event = new ProcessInstanceEvent();
        BeanUtils.copyProperties(historicProcessInstance, event);
        return event;
    }

    private ActivityInstanceEvent createActivityInstanceEvent(HistoricActivityInstance historicActivityInstance) {
        final ActivityInstanceEvent event = new ActivityInstanceEvent();
        BeanUtils.copyProperties(historicActivityInstance, event);

        event.setActivityInstanceId(event.getId());

        return event;
    }

    private VariableUpdateEvent createVariableUpdateEventFromInstance(
            HistoricVariableInstance historicVariableInstance) {
        final VariableUpdateEvent event = new VariableUpdateEvent();

        BeanUtils.copyProperties(historicVariableInstance, event);

        event.setEventType(historicVariableInstance.getState());
        event.setVariableInstanceId(historicVariableInstance.getId());

        copyVariableLongValueToDoubleValue(event);

        if (historicVariableInstance instanceof HistoricVariableInstanceEntity) {
            final HistoricVariableInstanceEntity historicVariableInstanceEntity = (HistoricVariableInstanceEntity) historicVariableInstance;
            setVariableComplexValue(event,
                    historicVariableInstanceEntity.getSerializerName(),
                    historicVariableInstanceEntity.getByteArrayValue());
        }

        return event;
    }

    private VariableUpdateEvent createVariableUpdateEventFromDetail(HistoricDetail historicDetail) {
        final VariableUpdateEvent event = new VariableUpdateEvent();

        BeanUtils.copyProperties(historicDetail, event);

        copyVariableLongValueToDoubleValue(event);

        if (historicDetail instanceof HistoricDetailVariableInstanceUpdateEntity) {
            final HistoricDetailVariableInstanceUpdateEntity historicVariableDetail = (HistoricDetailVariableInstanceUpdateEntity) historicDetail;
            setVariableComplexValue(event,
                    historicVariableDetail.getSerializerName(),
                    historicVariableDetail.getByteArrayValue());
        }

        return event;
    }


    private CommentEvent createCommentEventFromDetails(
            Comment comment, ActivityInstanceEvent activityInstanceEvent) {

        final CommentEvent event = new CommentEvent();

        BeanUtils.copyProperties(activityInstanceEvent, event);
        event.setId(comment.getId());
        event.setUserId(comment.getUserId());
        event.setTimestamp(comment.getTime());
        event.setMessage(comment.getFullMessage());

        return event;
    }

    private void setVariableComplexValue(VariableUpdateEvent event, String serializerName, byte[] value) {
        if (StringUtils.contains(serializerName, "spin:") && value != null) {
            try {
                final Object decodedValue = this.objectMapper.readValue(value, Object.class);

                if (decodedValue != null) {
                    event.setComplexValue(decodedValue);
                }
            } catch (IOException e) {        	
            	LOGGER.error("IOException found.");
            }
        }
    }

    private void copyVariableLongValueToDoubleValue(VariableUpdateEvent event) {

        if (event.getLongValue() != null) {
            event.setDoubleValue(event.getLongValue().doubleValue());
        }

    }
}
