package de.viadee.camunda.kafka.pollingclient.service.polling.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Response structure of Camunda REST API
 * <code>GET /history/decision-instance?decisionInstanceId={}&includeInputs=true</code>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetHistoricDecisionInstanceInputResponse {

    private String type;
    private Object value;
    private String id;
    private String decisionInstanceId;
    private String clauseId;
    private String clauseName;
    private Object valueInfo;
    private Date createTime;
    private String rootProcessInstanceId;

    @java.lang.SuppressWarnings("all")
    public Date getCreateTime() {
        return createTime;
    }

    @java.lang.SuppressWarnings("all")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @java.lang.SuppressWarnings("all")
    public String getValueInfoEntry(String key) {
        return valueInfo instanceof Map ? StringUtils.trimToNull(Objects.toString(((Map) valueInfo).get(key), null))
                : null;
    }

    @java.lang.SuppressWarnings("all")
    public String getRootProcessInstanceId() {
        return rootProcessInstanceId;
    }

    @java.lang.SuppressWarnings("all")
    public void setRootProcessInstanceId(String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }

    @java.lang.SuppressWarnings("all")
    public String getId() {
        return id;
    }

    @java.lang.SuppressWarnings("all")
    public void setId(String id) {
        this.id = id;
    }

    @java.lang.SuppressWarnings("all")
    public String getDecisionInstanceId() {
        return decisionInstanceId;
    }

    @java.lang.SuppressWarnings("all")
    public void setDecisionInstanceId(String decisionInstanceId) {
        this.decisionInstanceId = decisionInstanceId;
    }

    @java.lang.SuppressWarnings("all")
    public String getClauseId() {
        return clauseId;
    }

    @java.lang.SuppressWarnings("all")
    public void setClauseId(String clauseId) {
        this.clauseId = clauseId;
    }

    @java.lang.SuppressWarnings("all")
    public String getClauseName() {
        return clauseName;
    }

    @java.lang.SuppressWarnings("all")
    public void setClauseName(String clauseName) {
        this.clauseName = clauseName;
    }

    @java.lang.SuppressWarnings("all")
    public String getType() {
        return type;
    }

    @java.lang.SuppressWarnings("all")
    public void setType(String type) {
        this.type = type;
    }

    @java.lang.SuppressWarnings("all")
    public Object getValue() {
        return value;
    }

    @java.lang.SuppressWarnings("all")
    public void setValue(Object value) {
        this.value = value;
    }
}
