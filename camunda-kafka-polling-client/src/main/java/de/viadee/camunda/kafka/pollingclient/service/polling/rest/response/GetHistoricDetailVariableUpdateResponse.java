package de.viadee.camunda.kafka.pollingclient.service.polling.rest.response;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Response structure of Camunda REST API <code>GET /history/detail for type=variableUpdate</code>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetHistoricDetailVariableUpdateResponse {

    private String id;

    private String type;

    private String processDefinitionKey;

    private String processDefinitionId;

    private String processInstanceId;

    private String activityInstanceId;

    private String executionId;

    private String caseDefinitionKey;

    private String caseDefinitionId;

    private String caseInstanceId;

    private String caseExecutionId;

    private String taskId;

    private String tenantId;

    private Date time;

    private String variableName;

    private String variableInstanceId;

    private String variableType;

    private Object value;

    private Object valueInfo;

    private Long revision;

    private String errorMessage;

    /**
     * If {@link #valueInfo} is available as Map-Data, retrieve value of given key as String.
     *
     * @param key a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String getValueInfoEntry(String key) {
        return valueInfo instanceof Map ?
                StringUtils.trimToNull(Objects.toString(((Map) valueInfo).get(key), null)) :
                null;
    }
}
