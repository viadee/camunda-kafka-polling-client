package de.viadee.camunda.kafka.event;

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

    public String getSerializerName() {
        return serializerName;
    }

    public void setSerializerName(String serializerName) {
        this.serializerName = serializerName;
    }

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public Object getComplexValue() {
        return complexValue;
    }

    public void setComplexValue(Object complexValue) {
        this.complexValue = complexValue;
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

    @Override
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
