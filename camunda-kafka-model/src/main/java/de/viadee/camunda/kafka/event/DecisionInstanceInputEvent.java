package de.viadee.camunda.kafka.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DecisionInstanceInputEvent extends DetailEvent {

    private String type;
    private Long longValue;
    private Double doubleValue;
    private String textValue;
    private Object complexValue;
    private String id;
    private String decisionInstanceId;
    private String clauseId;
    private String clauseName;
    private String serializerName;

    @java.lang.SuppressWarnings("all")
    public String getSerializerName() {
        return serializerName;
    }

    @java.lang.SuppressWarnings("all")
    public void setSerializerName(String serializerName) {
        this.serializerName = serializerName;
    }

    @java.lang.SuppressWarnings("all")
    public Long getLongValue() {
        return longValue;
    }

    @java.lang.SuppressWarnings("all")
    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

    @java.lang.SuppressWarnings("all")
    public Double getDoubleValue() {
        return doubleValue;
    }

    @java.lang.SuppressWarnings("all")
    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    @java.lang.SuppressWarnings("all")
    public String getTextValue() {
        return textValue;
    }

    @java.lang.SuppressWarnings("all")
    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    @java.lang.SuppressWarnings("all")
    public Object getComplexValue() {
        return complexValue;
    }

    @java.lang.SuppressWarnings("all")
    public void setComplexValue(Object complexValue) {
        this.complexValue = complexValue;
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

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public String toString() {
        return "DecisionInstanceInputEvent{" +
                "type='" + type + '\'' +
                ", longValue=" + longValue +
                ", doubleValue=" + doubleValue +
                ", textValue='" + textValue + '\'' +
                ", complexValue=" + complexValue +
                ", id='" + id + '\'' +
                ", decisionInstanceId='" + decisionInstanceId + '\'' +
                ", clauseId='" + clauseId + '\'' +
                ", clauseName='" + clauseName + '\'' +
                ", serializerName='" + serializerName +
                '}';
    }
}
