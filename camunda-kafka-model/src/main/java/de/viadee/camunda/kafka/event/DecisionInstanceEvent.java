package de.viadee.camunda.kafka.event;

import java.util.Date;
import java.util.List;

public class DecisionInstanceEvent extends DetailEvent {

    private String decisionDefinitionId;
    private String decisionDefinitionKey;
    private String decisionDefinitionName;
    private String activityId;
    private String activityInstanceId;
    private Date evaluationTime;
    private List<DecisionInstanceInputEvent> inputs;
    private List<DecisionInstanceOutputEvent> outputs;

    @java.lang.SuppressWarnings("all")
    public List<DecisionInstanceInputEvent> getInputs() {
        return inputs;
    }

    @java.lang.SuppressWarnings("all")
    public void setInputs(List<DecisionInstanceInputEvent> inputs) {
        this.inputs = inputs;
    }

    @java.lang.SuppressWarnings("all")
    public List<DecisionInstanceOutputEvent> getOutputs() {
        return outputs;
    }

    @java.lang.SuppressWarnings("all")
    public void setOutputs(List<DecisionInstanceOutputEvent> outputs) {
        this.outputs = outputs;
    }

    @java.lang.SuppressWarnings("all")
    public String getDecisionDefinitionId() {
        return decisionDefinitionId;
    }

    @java.lang.SuppressWarnings("all")
    public void setDecisionDefinitionId(String decisionDefinitionId) {
        this.decisionDefinitionId = decisionDefinitionId;
    }

    @java.lang.SuppressWarnings("all")
    public String getDecisionDefinitionKey() {
        return decisionDefinitionKey;
    }

    @java.lang.SuppressWarnings("all")
    public void setDecisionDefinitionKey(String decisionDefinitionKey) {
        this.decisionDefinitionKey = decisionDefinitionKey;
    }

    @java.lang.SuppressWarnings("all")
    public String getDecisionDefinitionName() {
        return decisionDefinitionName;
    }

    @java.lang.SuppressWarnings("all")
    public void setDecisionDefinitionName(String decisionDefinitionName) {
        this.decisionDefinitionName = decisionDefinitionName;
    }

    @java.lang.SuppressWarnings("all")
    public String getActivityId() {
        return activityId;
    }

    @java.lang.SuppressWarnings("all")
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    @java.lang.SuppressWarnings("all")
    public String getActivityInstanceId() {
        return activityInstanceId;
    }

    @java.lang.SuppressWarnings("all")
    public void setActivityInstanceId(String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
    }

    @java.lang.SuppressWarnings("all")
    public Date getEvaluationTime() {
        return evaluationTime;
    }

    @java.lang.SuppressWarnings("all")
    public void setEvaluationTime(Date evaluationTime) {
        this.evaluationTime = evaluationTime;
    }
}
