package de.viadee.camunda.kafka.pollingclient.service.polling.rest;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.viadee.camunda.kafka.event.ActivityInstanceEvent;
import de.viadee.camunda.kafka.event.CommentEvent;
import de.viadee.camunda.kafka.event.ProcessDefinitionEvent;
import de.viadee.camunda.kafka.event.ProcessInstanceEvent;
import de.viadee.camunda.kafka.event.VariableUpdateEvent;
import de.viadee.camunda.kafka.pollingclient.service.polling.rest.response.GetCommentResponse;
import de.viadee.camunda.kafka.pollingclient.service.polling.rest.response.GetDeploymentResponse;
import de.viadee.camunda.kafka.pollingclient.service.polling.rest.response.GetHistoricDetailVariableUpdateResponse;
import de.viadee.camunda.kafka.pollingclient.service.polling.rest.response.GetHistoricVariableInstancesResponse;
import de.viadee.camunda.kafka.pollingclient.service.polling.rest.response.GetProcessDefinitionResponse;
import de.viadee.camunda.kafka.pollingclient.config.properties.CamundaRestPollingProperties;
import de.viadee.camunda.kafka.pollingclient.service.polling.PollingService;
import de.viadee.camunda.kafka.pollingclient.service.polling.rest.response.GetHistoricActivityInstanceRespone;
import de.viadee.camunda.kafka.pollingclient.service.polling.rest.response.GetHistoricProcessInstanceResponse;
import de.viadee.camunda.kafka.pollingclient.service.polling.rest.response.GetProcessDefinitionXmlResponse;


/**
 * <p>CamundaRestPollingServiceImpl class.</p>
 *
 * @author viadee
 * @version $Id: $Id
 */
public class CamundaRestPollingServiceImpl implements PollingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaRestPollingServiceImpl.class);

    private static final String API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private final ObjectMapper objectMapper;

    private final CamundaRestPollingProperties camundaProperties;

    private final RestTemplate restTemplate;

    /**
     * <p>Constructor for CamundaRestPollingServiceImpl.</p>
     *
     * @param camundaProperties a {@link de.viadee.camunda.kafka.pollingclient.config.properties.CamundaRestPollingProperties} object.
     * @param restTemplate a {@link org.springframework.web.client.RestTemplate} object.
     */
    public CamundaRestPollingServiceImpl(CamundaRestPollingProperties camundaProperties, RestTemplate restTemplate) {
        this.camundaProperties = camundaProperties;
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setDateFormat(getAPIDateFormat(false));
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<ProcessInstanceEvent> pollFinishedProcessInstances(Date startedAfter, Date startedBefore,
            Date finishedAfter) {
        final String url = camundaProperties.getUrl()
                + "history/process-instance?finished=true&startedBefore={startedBefore}&startedAfter={startedAfter}&finishedAfter={finishedAfter}";
        try {
            final DateFormat apiDateFormat = getAPIDateFormat(true);
            final Map<String, Object> variables = new HashMap<>();
            variables.put("startedAfter", apiDateFormat.format(startedAfter));
            variables.put("startedBefore", apiDateFormat.format(startedBefore));
            variables.put("finishedAfter", apiDateFormat.format(finishedAfter));

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
                    .filter(event -> event.getStartTime().compareTo(startedBefore) < 0) // startedBefore ist selected as <= by Camunda - thus add filter
                    .map(this::createProcessInstanceEvent)
                    ::iterator;
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
            final DateFormat apiDateFormat = getAPIDateFormat(true);
            final Map<String, Object> variables = new HashMap<>();
            variables.put("startedBefore", apiDateFormat.format(startedBefore));
            variables.put("startedAfter", apiDateFormat.format(startedAfter));

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
                    .filter(event -> event.getStartTime().compareTo(startedBefore) < 0) // startedBefore ist selected as <= by Camunda - thus add filter
                    .map(this::createProcessInstanceEvent)
                    ::iterator;
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
            final DateFormat apiDateFormat = getAPIDateFormat(true);
            final Map<String, Object> variables = new HashMap<>();
            variables.put("finishedBefore", apiDateFormat.format(finishedBefore));
            variables.put("finishedAfter", apiDateFormat.format(finishedAfter));
            variables.put("processInstanceId", processInstanceId);

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
                    .filter(event -> event.getEndTime().compareTo(finishedBefore) < 0) // finishedBefore ist selected as <= by Camunda - thus add filter
                    .map(this::createActivityInstanceEvent)
                    ::iterator;
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
            final DateFormat apiDateFormat = getAPIDateFormat(true);
            final Map<String, Object> variables = new HashMap<>();
            variables.put("startedBefore", apiDateFormat.format(startedBefore));
            variables.put("startedAfter", apiDateFormat.format(startedAfter));
            variables.put("processInstanceId", processInstanceId);

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
                    .filter(event -> event.getStartTime().compareTo(startedBefore) < 0) // startedBefore ist selected as <= by Camunda - thus add filter
                    .map(this::createActivityInstanceEvent)
                    ::iterator;
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
            variables.put("activityInstanceId", activityInstanceId);

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
                    .map(response -> createVariableUpdateEventFromInstance(response, pollingTimestamp))
                    ::iterator;
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
            variables.put("activityInstanceId", activityInstanceId);

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
                    .map(this::createVariableUpdateEventFromDetails)
                    ::iterator;
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
                .stream()
                ::iterator;
    }


    /** {@inheritDoc}
     * @param activityInstanceEvent*/
    @Override
    public Iterable<CommentEvent> pollComments(final ActivityInstanceEvent activityInstanceEvent) {

        final String url = camundaProperties.getUrl()
                + "task/"+activityInstanceEvent.getTaskId()+"/comment";
        try {
            final Map<String, Object> variables = new HashMap<>();
            LOGGER.debug("Polling comments for taskId: {}", activityInstanceEvent.getTaskId());

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
                    .map(getCommentResponse ->  createCommentEventFromDetails(getCommentResponse, activityInstanceEvent))
                    ::iterator;
        } catch (RestClientException e) {
            throw new RuntimeException("Error requesting Camunda REST API (" + url + ") for comments", e);
        }
    }

    private GetProcessDefinitionXmlResponse getProcessDefinitionXML(String processDefinitionId) {
        final String url = camundaProperties.getUrl()
                + "process-definition/{processDefinitionId}/xml";

        GetProcessDefinitionXmlResponse resp;
        try {
            final Map<String, Object> variables = new HashMap<>();
            variables.put("processDefinitionId", processDefinitionId);

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
        deploymentAfter = new Date(deploymentAfter.getTime()-1);

        final String deploymentUrl = camundaProperties.getUrl()
                + "deployment?before={before}&after={after}";

        List<GetDeploymentResponse> deployments = new ArrayList<>();
        try {
            final DateFormat apiDateFormat = getAPIDateFormat(true);
            final Map<String, Object> variables = new HashMap<>();
            variables.put("before", apiDateFormat.format(deploymentBefore));
            variables.put("after", apiDateFormat.format(deploymentAfter));

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
            GetHistoricVariableInstancesResponse getHistoricVariableInstancesResponse, Date pollingTimestamp) {
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
            GetCommentResponse commentResponse, ActivityInstanceEvent activityInstanceEvent) {
        final CommentEvent event = new CommentEvent();

        BeanUtils.copyProperties(activityInstanceEvent, event);

        event.setId(commentResponse.getId());
        event.setUserId(commentResponse.getUserId());
        event.setTimestamp(commentResponse.getTime());
        event.setMessage(commentResponse.getMessage());

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
                default :{
                	LOGGER.warn("Data type does not exist.");
                }
            }
        }
    }

    /**
     * DateFormat used in REST API and serialization
     *
     * @param isSource format for Source or Client
     * @return DateFormat used for Date serialization
     */
    private DateFormat getAPIDateFormat(boolean isSource) {

        final SimpleDateFormat apiDateFormat = new SimpleDateFormat(API_DATE_FORMAT);

        String sourceTimeZone = camundaProperties.getSourceTimeZone();

        if(isSource && sourceTimeZone!= null && !sourceTimeZone.isEmpty())
            apiDateFormat.setTimeZone(TimeZone.getTimeZone(sourceTimeZone));

        return apiDateFormat;
    }
}
