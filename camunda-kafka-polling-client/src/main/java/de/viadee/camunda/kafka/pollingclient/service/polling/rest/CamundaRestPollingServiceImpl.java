package de.viadee.camunda.kafka.pollingclient.service.polling.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.viadee.camunda.kafka.event.*;
import de.viadee.camunda.kafka.pollingclient.config.properties.CamundaRestPollingProperties;
import de.viadee.camunda.kafka.pollingclient.service.polling.PollingService;
import de.viadee.camunda.kafka.pollingclient.service.polling.rest.response.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * CamundaRestPollingServiceImpl class.
 * </p>
 *
 * @author viadee
 * @version $Id: $Id
 */
public class CamundaRestPollingServiceImpl implements PollingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaRestPollingServiceImpl.class);

    private static final String STARTED_AFTER = "startedAfter";
    private static final String STARTED_BEFORE = "startedBefore";
    private static final String FINISHED_AFTER = "finishedAfter";
    private static final String FINISHED_BEFORE = "finishedBefore";
    private static final String PROCESS_INSTANCE_ID = "processInstanceId";
    private static final String ACTIVITY_INSTANCE_ID = "activityInstanceId";
    private static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    private static final String TASK_ID = "taskId";
    private static final String DECISION_DEFINITION_ID = "decisionDefinitionId";
    private static final String DECISION_INSTANCE_ID = "decisionInstanceId";

    private final ObjectMapper objectMapper;

    private final CamundaRestPollingProperties camundaProperties;

    private final RestTemplate restTemplate;

    /**
     * <p>
     * Constructor for CamundaRestPollingServiceImpl.
     * </p>
     *
     * @param camundaProperties
     *            a {@link de.viadee.camunda.kafka.pollingclient.config.properties.CamundaRestPollingProperties} object.
     * @param restTemplate
     *            a {@link org.springframework.web.client.RestTemplate} object.
     */
    public CamundaRestPollingServiceImpl(CamundaRestPollingProperties camundaProperties, RestTemplate restTemplate) {
        this.camundaProperties = camundaProperties;
        this.restTemplate = restTemplate;

        String dateFormatPattern = camundaProperties.getDateFormatPattern();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setDateFormat(new SimpleDateFormat(dateFormatPattern));

    }

    /** {@inheritDoc} */
    @Override
    public Iterable<ProcessInstanceEvent> pollFinishedProcessInstances(Date startedAfter, Date startedBefore,
                                                                       Date finishedAfter) {
        final String url = camundaProperties.getUrl()
                + "history/process-instance?finished=true&startedBefore={startedBefore}&startedAfter={startedAfter}&finishedAfter={finishedAfter}";
        try {
            final Map<String, Object> variables = new HashMap<>();
            variables.put(STARTED_AFTER, formatDate(startedAfter));
            variables.put(STARTED_BEFORE, formatDate(startedBefore));
            variables.put(FINISHED_AFTER, formatDate(finishedAfter));

            LOGGER.debug("Polling finished process instances from {} ({})", url, variables);

            List<GetHistoricProcessInstanceResponse> result = this.restTemplate
                                                                               .exchange(url,
                                                                                         HttpMethod.GET,
                                                                                         null,
                                                                                         new ParameterizedTypeReference<List<GetHistoricProcessInstanceResponse>>() {

                                                                                         },
                                                                                         variables)
                                                                               .getBody();

            if (result == null) {
                return new ArrayList<>();
            }

            LOGGER.debug("Found {} finished process instances from {} ({})", result.size(), url, variables);

            return result
                         .stream()
                         .filter(event -> event.getStartTime().compareTo(startedBefore) < 0) // startedBefore ist
                                                                                             // selected as <= by
                                                                                             // Camunda - thus add
                                                                                             // filter
                         .map(this::createProcessInstanceEvent)::iterator;
        } catch (RestClientException e) {
            throw new RuntimeException("Error requesting Camunda REST API (" + url + ") for process instances", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<ProcessInstanceEvent> pollUnfinishedProcessInstances(Date startedAfter, Date startedBefore) {
        final String url = camundaProperties.getUrl()
                + "history/process-instance?unfinished=true&startedBefore={startedBefore}&startedAfter={startedAfter}";
        try {
            final Map<String, Object> variables = new HashMap<>();
            variables.put(STARTED_BEFORE, formatDate(startedBefore));
            variables.put(STARTED_AFTER, formatDate(startedAfter));

            LOGGER.debug("Polling unfinished process instances from {} ({})", url, variables);

            List<GetHistoricProcessInstanceResponse> result = this.restTemplate
                                                                               .exchange(url,
                                                                                         HttpMethod.GET,
                                                                                         null,
                                                                                         new ParameterizedTypeReference<List<GetHistoricProcessInstanceResponse>>() {

                                                                                         },
                                                                                         variables)
                                                                               .getBody();

            if (result == null) {
                return new ArrayList<>();
            }

            LOGGER.debug("Found {} unfinished process instances from {}", result.size(), url);

            return result
                         .stream()
                         .filter(event -> event.getStartTime().compareTo(startedBefore) < 0) // startedBefore ist
                                                                                             // selected as <= by
                                                                                             // Camunda - thus add
                                                                                             // filter
                         .map(this::createProcessInstanceEvent)::iterator;
        } catch (RestClientException e) {
            throw new RuntimeException("Error requesting Camunda REST API (" + url + ") for process instances", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<ActivityInstanceEvent> pollFinishedActivities(String processInstanceId, Date finishedAfter,
                                                                  Date finishedBefore) {
        final String url = camundaProperties.getUrl()
                + "history/activity-instance?finished=true&processInstanceId={processInstanceId}&finishedBefore={finishedBefore}&finishedAfter={finishedAfter}";
        try {
            final Map<String, Object> variables = new HashMap<>();
            variables.put(FINISHED_BEFORE, formatDate(finishedBefore));
            variables.put(FINISHED_AFTER, formatDate(finishedAfter));
            variables.put(PROCESS_INSTANCE_ID, processInstanceId);

            LOGGER.debug("Polling finished activity instances from {} ({})", url, variables);

            List<GetHistoricActivityInstanceRespone> result = this.restTemplate
                                                                               .exchange(url,
                                                                                         HttpMethod.GET,
                                                                                         null,
                                                                                         new ParameterizedTypeReference<List<GetHistoricActivityInstanceRespone>>() {

                                                                                         },
                                                                                         variables)
                                                                               .getBody();

            if (result == null) {
                return new ArrayList<>();
            }

            LOGGER.debug("Found {} finished activity instances from {} ({})", result.size(), url, variables);

            return result
                         .stream()
                         .filter(event -> event.getEndTime().compareTo(finishedBefore) < 0) // finishedBefore ist
                                                                                            // selected as <= by Camunda
                                                                                            // - thus add filter
                         .map(this::createActivityInstanceEvent)::iterator;
        } catch (RestClientException e) {
            throw new RuntimeException("Error requesting Camunda REST API (" + url + ") for activity instances", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<ActivityInstanceEvent> pollUnfinishedActivities(String processInstanceId, Date startedAfter,
                                                                    Date startedBefore) {
        final String url = camundaProperties.getUrl()
                + "history/activity-instance?unfinished=true&processInstanceId={processInstanceId}&startedBefore={startedBefore}&startedAfter={startedAfter}";
        try {
            final Map<String, Object> variables = new HashMap<>();
            variables.put(STARTED_BEFORE, formatDate(startedBefore));
            variables.put(STARTED_AFTER, formatDate(startedAfter));
            variables.put(PROCESS_INSTANCE_ID, processInstanceId);

            LOGGER.debug("Polling unfinished activity instances from {} ({})", url, variables);

            List<GetHistoricActivityInstanceRespone> result = this.restTemplate
                                                                               .exchange(url,
                                                                                         HttpMethod.GET,
                                                                                         null,
                                                                                         new ParameterizedTypeReference<List<GetHistoricActivityInstanceRespone>>() {

                                                                                         },
                                                                                         variables)
                                                                               .getBody();

            if (result == null) {
                return new ArrayList<>();
            }

            LOGGER.debug("Found {} unfinished activity instances from {} ({})", result.size(), url, variables);

            return result
                         .stream()
                         .filter(event -> event.getStartTime().compareTo(startedBefore) < 0) // startedBefore ist
                                                                                             // selected as <= by
                                                                                             // Camunda - thus add
                                                                                             // filter
                         .map(this::createActivityInstanceEvent)::iterator;
        } catch (RestClientException e) {
            throw new RuntimeException("Error requesting Camunda REST API (" + url + ") for activity instances", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<VariableUpdateEvent> pollCurrentVariables(String activityInstanceId) {
        final String url = camundaProperties.getUrl()
                + "history/variable-instance?deserializeValues=false&activityInstanceIdIn={activityInstanceId}";
        try {
            final Map<String, Object> variables = new HashMap<>();
            variables.put(ACTIVITY_INSTANCE_ID, activityInstanceId);

            LOGGER.debug("Polling variables from {} ({})", url, variables);

            final Date pollingTimestamp = new Date();
            List<GetHistoricVariableInstancesResponse> result = this.restTemplate
                                                                                 .exchange(url,
                                                                                           HttpMethod.GET,
                                                                                           null,
                                                                                           new ParameterizedTypeReference<List<GetHistoricVariableInstancesResponse>>() {

                                                                                           },
                                                                                           variables)
                                                                                 .getBody();

            if (result == null) {
                return new ArrayList<>();
            }

            LOGGER.debug("Found {} variables from {} ({})", result.size(), url, variables);

            return result
                         .stream()
                         .map(response -> createVariableUpdateEventFromInstance(response, pollingTimestamp))::iterator;
        } catch (RestClientException e) {
            throw new RuntimeException("Error requesting Camunda REST API (" + url + ") for variables", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<VariableUpdateEvent> pollVariableDetails(String activityInstanceId) {
        final String url = camundaProperties.getUrl()
                + "history/detail?deserializeValues=false&type=variableUpdate&activityInstanceId={activityInstanceId}";
        try {
            final Map<String, Object> variables = new HashMap<>();
            variables.put(ACTIVITY_INSTANCE_ID, activityInstanceId);

            LOGGER.debug("Polling variables from {} ({})", url, variables);

            List<GetHistoricDetailVariableUpdateResponse> result = this.restTemplate
                                                                                    .exchange(url,
                                                                                              HttpMethod.GET,
                                                                                              null,
                                                                                              new ParameterizedTypeReference<List<GetHistoricDetailVariableUpdateResponse>>() {

                                                                                              },
                                                                                              variables)
                                                                                    .getBody();

            if (result == null) {
                return new ArrayList<>();
            }

            LOGGER.debug("Found {} variables from {} ({})", result.size(), url, variables);

            return result
                         .stream()
                         .map(this::createVariableUpdateEventFromDetails)::iterator;
        } catch (RestClientException e) {
            throw new RuntimeException("Error requesting Camunda REST API (" + url + ") for variables details", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<ProcessDefinitionEvent> pollProcessDefinitions(final Date startTime,
                                                                   final Date endTime) {

        List<GetDeploymentResponse> deploymentList = getDeployments(startTime, endTime);

        List<ProcessDefinitionEvent> processDefinitionList = new ArrayList<>();

        for (GetDeploymentResponse deployment : deploymentList) {
            processDefinitionList.addAll(getProcessDefinitions(deployment));
        }

        for (ProcessDefinitionEvent processDefinitionEvent : processDefinitionList) {
            GetProcessDefinitionXmlResponse processDefinitionXML = getProcessDefinitionXML(
                                                                                           processDefinitionEvent.getId());
            if (processDefinitionXML != null) {
                processDefinitionEvent.setXml(processDefinitionXML.getBpmn20Xml());
            }
        }

        return processDefinitionList
                                    .stream()::iterator;
    }

    /**
     * {@inheritDoc}
     * 
     * @param activityInstanceEvent
     */
    @Override
    public Iterable<CommentEvent> pollComments(final ActivityInstanceEvent activityInstanceEvent) {

        final String url = camundaProperties.getUrl()
                + "task/{taskId}/comment";
        try {
            final Map<String, Object> variables = new HashMap<>();
            variables.put(TASK_ID, activityInstanceEvent.getTaskId());
            LOGGER.debug("Polling comments from {} ({})", url, variables);

            List<GetCommentResponse> result = this.restTemplate
                                                               .exchange(url,
                                                                         HttpMethod.GET,
                                                                         null,
                                                                         new ParameterizedTypeReference<List<GetCommentResponse>>() {

                                                                         }, variables)
                                                               .getBody();

            if (result == null) {
                return new ArrayList<>();
            }

            LOGGER.debug("Found {} comments for taskId: {} ", result.size(), activityInstanceEvent.getTaskId());

            return result
                         .stream()
                         .map(getCommentResponse -> createCommentEventFromDetails(getCommentResponse,
                                                                                  activityInstanceEvent))::iterator;
        } catch (RestClientException e) {
            throw new RuntimeException("Error requesting Camunda REST API (" + url + ") for comments", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param activityInstanceEvent
     */
    @Override
    public Iterable<IdentityLinkEvent> pollIdentityLinks(final ActivityInstanceEvent activityInstanceEvent) {

        final String url = camundaProperties.getUrl()
                + "history/identity-link-log/?taskId={taskId}";
        try {
            final Map<String, Object> variables = new HashMap<>();
            variables.put(TASK_ID, activityInstanceEvent.getTaskId());
            LOGGER.debug("Polling identity-links from {} ({})", url, variables);

            List<GetIdentityLinkResponse> result = this.restTemplate
                                                                    .exchange(url,
                                                                              HttpMethod.GET,
                                                                              null,
                                                                              new ParameterizedTypeReference<List<GetIdentityLinkResponse>>() {

                                                                              }, variables)
                                                                    .getBody();

            if (result == null) {
                return new ArrayList<>();
            }

            LOGGER.debug("Found {} identity-links for taskId: {} ", result.size(), activityInstanceEvent.getTaskId());

            return result
                         .stream()
                         .map(getIdentityLinkResponse -> createIdentityLinkEventFromDetails(getIdentityLinkResponse))::iterator;
        } catch (RestClientException e) {
            throw new RuntimeException("Error requesting Camunda REST API (" + url + ") for identity-link-log", e);
        }
    }

    @Override
    public Iterable<DecisionDefinitionEvent> pollDecisionDefinitions(final Date startTime,
                                                                     final Date endTime) {

        List<GetDeploymentResponse> deploymentList = getDeployments(startTime, endTime);

        List<DecisionDefinitionEvent> decisionDefinitionList = new ArrayList<>();

        for (GetDeploymentResponse deployment : deploymentList) {
            decisionDefinitionList.addAll(getDecisionDefinitions(deployment));
        }

        for (DecisionDefinitionEvent decisionDefinitionEvent : decisionDefinitionList) {
            GetDecisionDefinitionXmlResponse decisionDefinitionXML = getDecisionDefinitionXML(
                                                                                              decisionDefinitionEvent.getId());
            if (decisionDefinitionXML != null) {
                decisionDefinitionEvent.setXml(decisionDefinitionXML.getDmnXml());
            }
        }

        return decisionDefinitionList
                                     .stream()::iterator;
    }

    @Override
    public Iterable<DecisionInstanceEvent> pollDecisionInstances(String processInstanceId) {

        final String url = camundaProperties.getUrl()
                + "history/decision-instance?processInstanceId={processInstanceId}&includeInputs=true&includeOutputs=true&disableBinaryFetching=true&disableCustomObjectDeserialization=true";
        try {
            final Map<String, Object> variables = new HashMap<>();
            variables.put(PROCESS_INSTANCE_ID, processInstanceId);

            LOGGER.debug("Polling decision instances from {} ({})", url, variables);

            List<GetHistoricDecisionInstanceResponse> result = this.restTemplate
                                                                                .exchange(url,
                                                                                          HttpMethod.GET,
                                                                                          null,
                                                                                          new ParameterizedTypeReference<List<GetHistoricDecisionInstanceResponse>>() {
                                                                                          },
                                                                                          variables)
                                                                                .getBody();

            LOGGER.debug("Found {} decision instances from {} ", result.size(), url);

            if (result == null) {
                result = new ArrayList<>();
            }

            return result
                         .stream()
                         .map(this::createDecisionInstanceEvent)::iterator;

        } catch (RestClientException e) {
            throw new RuntimeException("Error requesting Camunda REST API (" + url + ") for decision instances", e);
        }

    }

    public Iterable<DecisionInstanceInputEvent> pollDecisionInstanceInputs(DecisionInstanceEvent decisionInstanceEvent) {

        final String url = camundaProperties.getUrl()
                + "history/decision-instance?decisionInstanceId={decisionInstanceId}&includeInputs=true&disableBinaryFetching=true&disableCustomObjectDeserialization=true";
        try {

            String decisionInstanceId = decisionInstanceEvent.getId();
            final Map<String, Object> variables = new HashMap<>();
            variables.put(DECISION_INSTANCE_ID, decisionInstanceId);

            LOGGER.debug("Polling finished decision input instances from {} ({})", url, variables);

            // To extract inputs from decision instance, whole nested DecisionInstance must be polled first
            List<GetHistoricDecisionInstanceResponse> result = this.restTemplate
                                                                                .exchange(url,
                                                                                          HttpMethod.GET,
                                                                                          null,
                                                                                          new ParameterizedTypeReference<List<GetHistoricDecisionInstanceResponse>>() {
                                                                                          },
                                                                                          variables)
                                                                                .getBody();

            if (result == null) {
                return new ArrayList<>();
            }

            // get process instance id so extracted input can be assigned to parent process instance
            String processInstanceId = decisionInstanceEvent.getProcessInstanceId();

            // get process definition id as well
            String processDefinitionId = decisionInstanceEvent.getProcessDefinitionId();

            // extracted Inputs are missing a timestamp, therefore take time from decisionInstanceEvent
            Date evaluationTime = decisionInstanceEvent.getEvaluationTime();

            // extract inputs from list result
            List<GetHistoricDecisionInstanceInputResponse> inputs = new ArrayList<>();
            result.forEach(r -> inputs.addAll(r.getInputs()));

            LOGGER.debug("Found {} finished decision instance inputs from {} ({})", inputs.size(), url, variables);

            return () -> inputs
                               .stream()
                               .map(getHistoricDecisionInstanceInputResponse -> createDecisionInstanceInputEvent(getHistoricDecisionInstanceInputResponse,
                                                                                                                 processInstanceId,
                                                                                                                 processDefinitionId,
                                                                                                                 evaluationTime))
                               .iterator();

        } catch (RestClientException e) {
            throw new RuntimeException("Error requesting Camunda REST API (" + url + ") for decision instance inputs",
                                       e);
        }
    }

    public Iterable<DecisionInstanceOutputEvent> pollDecisionInstanceOutputs(DecisionInstanceEvent decisionInstanceEvent) {

        final String url = camundaProperties.getUrl()
                + "history/decision-instance?decisionInstanceId={decisionInstanceId}&includeOutputs=true&disableBinaryFetching=true&disableCustomObjectDeserialization=true";
        try {

            String decisionInstanceId = decisionInstanceEvent.getId();
            final Map<String, Object> variables = new HashMap<>();
            variables.put(DECISION_INSTANCE_ID, decisionInstanceId);

            LOGGER.debug("Polling finished decision output instances from {} ({})", url, variables);

            List<GetHistoricDecisionInstanceResponse> result = this.restTemplate
                                                                                .exchange(url,
                                                                                          HttpMethod.GET,
                                                                                          null,
                                                                                          new ParameterizedTypeReference<List<GetHistoricDecisionInstanceResponse>>() {
                                                                                          },
                                                                                          variables)
                                                                                .getBody();

            if (result == null) {
                return new ArrayList<>();
            }

            // get process instance id so extracted input can be assigned to parent process instance
            String processInstanceId = decisionInstanceEvent.getProcessInstanceId();

            // get process definition id as well
            String processDefinitionId = decisionInstanceEvent.getProcessDefinitionId();

            // extracted Inputs are missing a timestamp, therefore take time from decisionInstanceEvent
            Date evaluationTime = decisionInstanceEvent.getEvaluationTime();

            // extract inputs from list result
            List<GetHistoricDecisionInstanceOutputResponse> outputs = new ArrayList<>();
            result.forEach(r -> outputs.addAll(r.getOutputs()));

            LOGGER.debug("Found {} finished decision instance outputs from {} ({})", outputs.size(), url, variables);

            return () -> outputs
                                .stream()
                                .map(getHistoricDecisionInstanceOutputResponse -> createDecisionInstanceOutputEvent(getHistoricDecisionInstanceOutputResponse,
                                                                                                                    processInstanceId,
                                                                                                                    processDefinitionId,
                                                                                                                    evaluationTime))
                                .iterator();
        } catch (RestClientException e) {
            throw new RuntimeException("Error requesting Camunda REST API (" + url + ") for decision instance inputs",
                                       e);
        }
    }

    private DecisionInstanceEvent createDecisionInstanceEvent(GetHistoricDecisionInstanceResponse getHistoricDecisionInstanceResponse) {

        final DecisionInstanceEvent e = new DecisionInstanceEvent();
        BeanUtils.copyProperties(getHistoricDecisionInstanceResponse, e);

        return e;
    }

    private DecisionInstanceInputEvent createDecisionInstanceInputEvent(GetHistoricDecisionInstanceInputResponse getHistoricDecisionInstanceInputResponse,
                                                                        String processInstanceId,
                                                                        String processDefinitionId,
                                                                        Date evaluationTime) {

        final DecisionInstanceInputEvent event = new DecisionInstanceInputEvent();
        BeanUtils.copyProperties(getHistoricDecisionInstanceInputResponse, event);
        event.setProcessInstanceId(processInstanceId);
        event.setProcessDefinitionId(processDefinitionId);

        event.setTimestamp(evaluationTime);

        setInputValue(event,
                      getHistoricDecisionInstanceInputResponse.getValue(),
                      getHistoricDecisionInstanceInputResponse.getType(),
                      getHistoricDecisionInstanceInputResponse.getValueInfoEntry("serializationDataFormat"));

        return event;

    }

    private DecisionInstanceOutputEvent createDecisionInstanceOutputEvent(GetHistoricDecisionInstanceOutputResponse getHistoricDecisionInstanceOutputResponse,
                                                                          String processInstanceId,
                                                                          String processDefinitionId,
                                                                          Date evaluationTime) {

        final DecisionInstanceOutputEvent event = new DecisionInstanceOutputEvent();
        BeanUtils.copyProperties(getHistoricDecisionInstanceOutputResponse, event);
        event.setProcessInstanceId(processInstanceId);
        event.setProcessDefinitionId(processDefinitionId);
        event.setTimestamp(evaluationTime);

        setOutputValue(event,
                       getHistoricDecisionInstanceOutputResponse.getValue(),
                       getHistoricDecisionInstanceOutputResponse.getType(),
                       getHistoricDecisionInstanceOutputResponse.getValueInfoEntry("serializationDataFormat"));

        return event;

    }

    private List<DecisionDefinitionEvent> getDecisionDefinitions(GetDeploymentResponse deploymentResponse) {

        final String url = camundaProperties.getUrl()
                + "decision-definition?deploymentId={deploymentId}";

        List<GetDecisionDefinitionResponse> decisionDefinitions = new ArrayList<>();
        try {

            final Map<String, Object> variables = new HashMap<>();
            variables.put("deploymentId", deploymentResponse.getId());

            LOGGER.debug("Polling decision definitions from {} ({})", url, variables);

            decisionDefinitions = this.restTemplate
                                                   .exchange(url,
                                                             HttpMethod.GET,
                                                             null,
                                                             new ParameterizedTypeReference<List<GetDecisionDefinitionResponse>>() {

                                                             },
                                                             variables)
                                                   .getBody();

            if (decisionDefinitions == null) {
                decisionDefinitions = new ArrayList<>();
            }

            LOGGER.debug("Found {} decision definitions from {} ({})", decisionDefinitions.size(), url, variables);
        } catch (RestClientException e) {
            throw new RuntimeException(
                                       "Error requesting Camunda REST API (" + url + ") for process definitions", e);
        }

        return decisionDefinitions
                                  .stream()
                                  .map(response -> createDecisionDefinitionEvent(response, deploymentResponse))
                                  .collect(Collectors.toList());
    }

    private DecisionDefinitionEvent createDecisionDefinitionEvent(GetDecisionDefinitionResponse resp,
                                                                  final GetDeploymentResponse deploymentResponse) {

        DecisionDefinitionEvent e = new DecisionDefinitionEvent();

        e.setDeploymentId(resp.getDeploymentId());
        e.setDeploymentTime(deploymentResponse.getDeploymentTime());

        e.setId(resp.getId());
        e.setName(resp.getName());
        e.setSource(deploymentResponse.getSource());
        e.setTenantId(resp.getTenantId());

        e.setCategory(resp.getCategory());
        e.setHistoryTimeToLive(resp.getHistoryTimeToLive());
        e.setKey(resp.getKey());
        e.setResource(resp.getResource());
        e.setVersion(resp.getVersion());
        e.setVersionTag(resp.getVersionTag());

        return e;
    }

    private GetProcessDefinitionXmlResponse getProcessDefinitionXML(String processDefinitionId) {
        final String url = camundaProperties.getUrl()
                + "process-definition/{processDefinitionId}/xml";

        GetProcessDefinitionXmlResponse resp;
        try {
            final Map<String, Object> variables = new HashMap<>();
            variables.put(PROCESS_DEFINITION_ID, processDefinitionId);

            LOGGER.debug("Polling process definition xml from {} ({})", url, variables);

            resp = this.restTemplate
                                    .exchange(url,
                                              HttpMethod.GET,
                                              null,
                                              GetProcessDefinitionXmlResponse.class,
                                              variables)
                                    .getBody();

            if (resp != null) {
                LOGGER.debug("Found process definition xml from {} ({})", url, variables);
            } else {
                LOGGER.debug("No process definition xml found from {} ({})", url, variables);
            }
        } catch (RestClientException e) {
            throw new RuntimeException(
                                       "Error requesting Camunda REST API (" + url + ") for process definition xml", e);
        }

        return resp;
    }

    private GetDecisionDefinitionXmlResponse getDecisionDefinitionXML(String decisionDefinitionId) {
        final String url = camundaProperties.getUrl()
                + "decision-definition/{decisionDefinitionId}/xml";

        GetDecisionDefinitionXmlResponse resp;
        try {
            final Map<String, Object> variables = new HashMap<>();
            variables.put(DECISION_DEFINITION_ID, decisionDefinitionId);

            LOGGER.debug("Polling decision definition xml from {} ({})", url, variables);

            resp = this.restTemplate
                                    .exchange(url,
                                              HttpMethod.GET,
                                              null,
                                              GetDecisionDefinitionXmlResponse.class,
                                              variables)
                                    .getBody();

            if (resp != null) {
                LOGGER.debug("Found decision definition xml from {} ({})", url, variables);
            } else {
                LOGGER.debug("No decision definition xml found from {} ({})", url, variables);
            }
        } catch (RestClientException e) {
            throw new RuntimeException(
                                       "Error requesting Camunda REST API (" + url + ") for decision definition xml",
                                       e);
        }

        return resp;
    }

    private List<ProcessDefinitionEvent> getProcessDefinitions(GetDeploymentResponse deploymentResponse) {
        final String url = camundaProperties.getUrl()
                + "process-definition?deploymentId={deploymentId}";

        List<GetProcessDefinitionResponse> processDefinitions = new ArrayList<>();
        try {
            final Map<String, Object> variables = new HashMap<>();
            variables.put("deploymentId", deploymentResponse.getId());

            LOGGER.debug("Polling process definitions from {} ({})", url, variables);

            processDefinitions = this.restTemplate
                                                  .exchange(url,
                                                            HttpMethod.GET,
                                                            null,
                                                            new ParameterizedTypeReference<List<GetProcessDefinitionResponse>>() {

                                                            },
                                                            variables)
                                                  .getBody();

            if (processDefinitions == null) {
                processDefinitions = new ArrayList<>();
            }

            LOGGER.debug("Found {} process definitions from {} ({})", processDefinitions.size(), url, variables);
        } catch (RestClientException e) {
            throw new RuntimeException(
                                       "Error requesting Camunda REST API (" + url + ") for process definitions", e);
        }

        return processDefinitions
                                 .stream()
                                 .map(response -> createProcessDefinitionEvent(response, deploymentResponse))
                                 .collect(Collectors.toList());
    }

    private List<GetDeploymentResponse> getDeployments(Date deploymentAfter, Date deploymentBefore) {

        // There seems to be a slight bug in Camunda SQL queries regarding deployments.
        // Where the other history queries regarding time boundaries are inclusive (startedBefore, startedAfter, ...),
        // deploymentBefore and deploymentAfter are implemented exclusive.
        // Thus we have to slightly adjust the deploymentAfter parameter by 1 millisecond to act inclusive:
        deploymentAfter = new Date(deploymentAfter.getTime() - 1);

        final String deploymentUrl = camundaProperties.getUrl()
                + "deployment?before={before}&after={after}";

        List<GetDeploymentResponse> deployments = new ArrayList<>();
        try {
            final Map<String, Object> variables = new HashMap<>();
            variables.put("before", formatDate(deploymentBefore));
            variables.put("after", formatDate(deploymentAfter));

            LOGGER.debug("Polling deployments from {} ({})", deploymentUrl, variables);

            deployments = this.restTemplate
                                           .exchange(deploymentUrl,
                                                     HttpMethod.GET,
                                                     null,
                                                     new ParameterizedTypeReference<List<GetDeploymentResponse>>() {

                                                     },
                                                     variables)
                                           .getBody();

            if (deployments == null) {
                deployments = new ArrayList<>();
            }

            LOGGER.debug("Found {} deployments from {} ({})", deployments.size(), deploymentUrl, variables);
        } catch (RestClientException e) {
            throw new RuntimeException(
                                       "Error requesting Camunda REST API (" + deploymentUrl + ") for deployments", e);
        }

        return deployments;
    }

    private ProcessInstanceEvent createProcessInstanceEvent(
                                                            GetHistoricProcessInstanceResponse getHistoricProcessInstanceResponse) {
        final ProcessInstanceEvent event = new ProcessInstanceEvent();
        BeanUtils.copyProperties(getHistoricProcessInstanceResponse, event);

        event.setProcessInstanceId(getHistoricProcessInstanceResponse.getId());
        event.setEventType(getHistoricProcessInstanceResponse.getState());

        return event;
    }

    private ActivityInstanceEvent createActivityInstanceEvent(
                                                              GetHistoricActivityInstanceRespone getHistoricActivityInstanceRespone) {
        final ActivityInstanceEvent event = new ActivityInstanceEvent();
        BeanUtils.copyProperties(getHistoricActivityInstanceRespone, event);

        event.setActivityInstanceId(getHistoricActivityInstanceRespone.getId());

        return event;
    }

    private VariableUpdateEvent createVariableUpdateEventFromInstance(
                                                                      GetHistoricVariableInstancesResponse getHistoricVariableInstancesResponse,
                                                                      Date pollingTimestamp) {
        final VariableUpdateEvent event = new VariableUpdateEvent();
        BeanUtils.copyProperties(getHistoricVariableInstancesResponse, event);

        event.setVariableName(getHistoricVariableInstancesResponse.getName());
        event.setVariableInstanceId(getHistoricVariableInstancesResponse.getId());
        event.setEventType(getHistoricVariableInstancesResponse.getState());
        event.setTimestamp(pollingTimestamp);

        setVariableValue(event,
                         getHistoricVariableInstancesResponse.getValue(),
                         getHistoricVariableInstancesResponse.getType(),
                         getHistoricVariableInstancesResponse.getValueInfoEntry("serializationDataFormat"));

        return event;
    }

    private VariableUpdateEvent createVariableUpdateEventFromDetails(
                                                                     GetHistoricDetailVariableUpdateResponse getHistoricDetailVariableUpdateResponse) {
        final VariableUpdateEvent event = new VariableUpdateEvent();
        BeanUtils.copyProperties(getHistoricDetailVariableUpdateResponse, event);

        event.setTimestamp(getHistoricDetailVariableUpdateResponse.getTime());

        setVariableValue(event,
                         getHistoricDetailVariableUpdateResponse.getValue(),
                         getHistoricDetailVariableUpdateResponse.getVariableType(),
                         getHistoricDetailVariableUpdateResponse.getValueInfoEntry("serializationDataFormat"));

        return event;
    }

    private ProcessDefinitionEvent createProcessDefinitionEvent(GetProcessDefinitionResponse resp,
                                                                final GetDeploymentResponse deploymentResponse) {

        ProcessDefinitionEvent e = new ProcessDefinitionEvent();
        e.setId(resp.getId());
        e.setCategory(resp.getCategory());
        e.setDescription(resp.getDescription());
        e.setHistoryTimeToLive(resp.getHistoryTimeToLive());
        e.setKey(resp.getKey());
        e.setName(resp.getName());
        e.setResource(resp.getResource());
        e.setSuspended(resp.getSuspended());
        e.setVersion(resp.getVersion());
        e.setVersionTag(resp.getVersionTag());
        e.setDeploymentId(resp.getDeploymentId());
        e.setTenantId(resp.getTenantId());
        e.setDeploymentTime(deploymentResponse.getDeploymentTime());
        e.setSource(deploymentResponse.getSource());
        return e;

    }

    private CommentEvent createCommentEventFromDetails(
                                                       GetCommentResponse commentResponse,
                                                       ActivityInstanceEvent activityInstanceEvent) {
        final CommentEvent event = new CommentEvent();

        BeanUtils.copyProperties(activityInstanceEvent, event);

        event.setId(commentResponse.getId());
        event.setUserId(commentResponse.getUserId());
        event.setTimestamp(commentResponse.getTime());
        event.setMessage(commentResponse.getMessage());

        return event;
    }

    private IdentityLinkEvent createIdentityLinkEventFromDetails(GetIdentityLinkResponse identityLinkResponse) {
        final IdentityLinkEvent event = new IdentityLinkEvent();

        event.setId(identityLinkResponse.getId());
        event.setTimestamp(identityLinkResponse.getTime());
        event.setType(identityLinkResponse.getType());
        event.setUserId(identityLinkResponse.getUserId());
        event.setGroupId(identityLinkResponse.getGroupId());
        event.setTaskId(identityLinkResponse.getTaskId());
        event.setProcessDefinitionId(identityLinkResponse.getProcessDefinitionId());
        event.setProcessDefinitionKey(identityLinkResponse.getProcessDefinitionKey());
        event.setOperationType(IdentityLinkEvent.OperationType.valueOf(identityLinkResponse.getOperationType()));
        event.setAssignerId(identityLinkResponse.getAssignerId());
        event.setTenantId(identityLinkResponse.getTenantId());
        event.setRemovalTime(identityLinkResponse.getRemovalTime());
        event.setProcessInstanceId(identityLinkResponse.getRootProcessInstanceId());

        return event;
    }

    private void setVariableValue(VariableUpdateEvent event, Object value, String type,
                                  String serializationDataFormat) {

        if (value != null) {
            switch (StringUtils.defaultString(type)) {
                case "Object": {
                    if (StringUtils.equalsIgnoreCase(serializationDataFormat, "application/json")
                            && value instanceof String) {
                        try {
                            final Object decodedValue = this.objectMapper.readValue((String) value, Object.class);

                            if (decodedValue != null) {
                                event.setComplexValue(decodedValue);
                                event.setSerializerName("spin://application/json");
                            }
                        } catch (IOException e) {
                            LOGGER.error("IOException found.");
                        }
                    }

                    break;
                }

                case "String": {
                    event.setTextValue(Objects.toString(value));
                    event.setSerializerName(StringUtils.lowerCase(type));
                    break;
                }

                case "Long":
                case "Integer":
                case "Byte":
                case "Short": {
                    event.setDoubleValue(((Number) value).doubleValue());
                    event.setLongValue(((Number) value).longValue());
                    event.setTextValue(value.toString());
                    event.setSerializerName(StringUtils.lowerCase(type));
                    break;
                }

                case "Double":
                case "Float": {
                    event.setDoubleValue(((Number) value).doubleValue());
                    event.setSerializerName(StringUtils.lowerCase(type));
                    break;
                }

                case "Boolean": {
                    event.setSerializerName(StringUtils.lowerCase(type));
                    if (Boolean.TRUE.equals(value)) {
                        event.setLongValue(1L);
                    } else {
                        event.setLongValue(0L);
                    }
                    break;
                }
                default: {
                    LOGGER.warn("Data type does not exist.");
                }
            }
        }
    }

    private void setInputValue(DecisionInstanceInputEvent event, Object value, String type,
                               String serializationDataFormat) {
        if (value != null) {
            switch (StringUtils.defaultString(type)) {
                case "Object": {
                    if (StringUtils.equalsIgnoreCase(serializationDataFormat, "application/json")
                            && value instanceof String) {
                        try {
                            final Object decodedValue = this.objectMapper.readValue((String) value, Object.class);

                            if (decodedValue != null) {
                                event.setComplexValue(decodedValue);
                                event.setSerializerName("spin://application/json");
                            }
                        } catch (IOException e) {
                            LOGGER.error("IOException found.");
                        }
                    }

                    break;
                }

                case "String": {
                    event.setTextValue(Objects.toString(value));
                    event.setSerializerName(StringUtils.lowerCase(type));
                    break;
                }

                case "Long":
                case "Integer":
                case "Byte":
                case "Short": {
                    event.setDoubleValue(((Number) value).doubleValue());
                    event.setLongValue(((Number) value).longValue());
                    event.setTextValue(value.toString());
                    event.setSerializerName(StringUtils.lowerCase(type));
                    break;
                }

                case "Double":
                case "Float": {
                    event.setDoubleValue(((Number) value).doubleValue());
                    event.setSerializerName(StringUtils.lowerCase(type));
                    break;
                }

                case "Boolean": {
                    event.setSerializerName(StringUtils.lowerCase(type));
                    if (Boolean.TRUE.equals(value)) {
                        event.setLongValue(1L);
                    } else {
                        event.setLongValue(0L);
                    }
                    break;
                }
                default: {
                    LOGGER.warn("Data type does not exist.");
                }
            }
        }
    }

    private void setOutputValue(DecisionInstanceOutputEvent event, Object value, String type,
                                String serializationDataFormat) {
        if (value != null) {
            switch (StringUtils.defaultString(type)) {
                case "Object": {
                    if (StringUtils.equalsIgnoreCase(serializationDataFormat, "application/json")
                            && value instanceof String) {
                        try {
                            final Object decodedValue = this.objectMapper.readValue((String) value, Object.class);

                            if (decodedValue != null) {
                                event.setComplexValue(decodedValue);
                                event.setSerializerName("spin://application/json");
                            }
                        } catch (IOException e) {
                            LOGGER.error("IOException found.");
                        }
                    }

                    break;
                }

                case "String": {
                    event.setTextValue(Objects.toString(value));
                    event.setSerializerName(StringUtils.lowerCase(type));
                    break;
                }

                case "Long":
                case "Integer":
                case "Byte":
                case "Short": {
                    event.setDoubleValue(((Number) value).doubleValue());
                    event.setLongValue(((Number) value).longValue());
                    event.setTextValue(value.toString());
                    event.setSerializerName(StringUtils.lowerCase(type));
                    break;
                }

                case "Double":
                case "Float": {
                    event.setDoubleValue(((Number) value).doubleValue());
                    event.setSerializerName(StringUtils.lowerCase(type));
                    break;
                }

                case "Boolean": {
                    event.setSerializerName(StringUtils.lowerCase(type));
                    if (Boolean.TRUE.equals(value)) {
                        event.setLongValue(1L);
                    } else {
                        event.setLongValue(0L);
                    }
                    break;
                }
                default: {
                    LOGGER.warn("Data type does not exist.");
                }
            }
        }
    }

    /**
     *
     * Format Date to String - used in REST API
     *
     * @param date
     *            to format
     * @return formated Date according to configured format
     *
     */
    String formatDate(Date date) {

        String dateFormatPattern = camundaProperties.getDateFormatPattern();
        SimpleDateFormat apiDateFormat = new SimpleDateFormat(dateFormatPattern);

        String sourceTimeZone = camundaProperties.getSourceTimeZone();
        if (sourceTimeZone != null && !sourceTimeZone.isEmpty())
            apiDateFormat.setTimeZone(TimeZone.getTimeZone(sourceTimeZone));

        return apiDateFormat.format(date);

    }
}
