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

    // REVIEW: Add @java.lang.SuppressWarnings("all") as in other responses? For instance, see GetHistoricActivityInstanceRespone
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getValueInfoEntry(String key) {
        return valueInfo instanceof Map ? StringUtils.trimToNull(Objects.toString(((Map) valueInfo).get(key), null))
                : null;
    }

    public String getRootProcessInstanceId() {
        return rootProcessInstanceId;
    }

    public void setRootProcessInstanceId(String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDecisionInstanceId() {
        return decisionInstanceId;
    }

    public void setDecisionInstanceId(String decisionInstanceId) {
        this.decisionInstanceId = decisionInstanceId;
    }

    public String getClauseId() {
        return clauseId;
    }

    public void setClauseId(String clauseId) {
        this.clauseId = clauseId;
    }

    public String getClauseName() {
        return clauseName;
    }

    public void setClauseName(String clauseName) {
        this.clauseName = clauseName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
