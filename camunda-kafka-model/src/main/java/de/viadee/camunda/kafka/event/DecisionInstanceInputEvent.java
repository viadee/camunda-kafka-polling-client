package de.viadee.camunda.kafka.event;

import java.util.Map;

public class DecisionInstanceInputEvent {

    private String id;
    private String decisionInstanceId;
    private String clauseId;
    private String clauseName;
    private String errorMessage;
    private String type;
    private String createTime;
    private String removalTime;
    private String rootProcessInstanceId;
    private String value;
    private Map<String, Object> valueInfo;

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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemovalTime() {
        return removalTime;
    }

    public void setRemovalTime(String removalTime) {
        this.removalTime = removalTime;
    }

    public String getRootProcessInstanceId() {
        return rootProcessInstanceId;
    }

    public void setRootProcessInstanceId(String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, Object> getValueInfo() {
        return valueInfo;
    }

    public void setValueInfo(Map<String, Object> valueInfo) {
        this.valueInfo = valueInfo;
    }
}
