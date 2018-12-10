package de.viadee.camunda.kafka.pollingclient.service.polling.rest.response;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Response structure of Camunda REST API <code>GET /history/variable-instance</code>
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetHistoricVariableInstancesResponse {

    private String id;

    private String name;

    private String type;

    private Object value;

    private Object valueInfo;

    private String processDefinitionKey;

    private String processDefinitionId;

    private String processInstanceId;

    private String executionId;

    private String activityInstanceId;

    private String caseDefinitionKey;

    private String caseDefinitionId;

    private String caseInstanceId;

    private String caseExecutionId;

    private String taskId;

    private String tenantId;

    private String errorMessage;

    private String state;

    /**
     * If {@link #valueInfo} is available as Map-Data, retrieve value of given key as String.
     */
    public String getValueInfoEntry(String key) {
        return valueInfo instanceof Map ?
                StringUtils.trimToNull(Objects.toString(((Map) valueInfo).get(key), null)) :
                null;
    }
}
