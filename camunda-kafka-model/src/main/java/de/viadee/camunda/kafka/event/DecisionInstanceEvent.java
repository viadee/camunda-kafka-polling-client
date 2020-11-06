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

    public List<DecisionInstanceInputEvent> getInputs() {
        return inputs;
    }

    public void setInputs(List<DecisionInstanceInputEvent> inputs) {
        this.inputs = inputs;
    }

    public List<DecisionInstanceOutputEvent> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<DecisionInstanceOutputEvent> outputs) {
        this.outputs = outputs;
    }

    public String getDecisionDefinitionId() {
        return decisionDefinitionId;
    }

    public void setDecisionDefinitionId(String decisionDefinitionId) {
        this.decisionDefinitionId = decisionDefinitionId;
    }

    public String getDecisionDefinitionKey() {
        return decisionDefinitionKey;
    }

    public void setDecisionDefinitionKey(String decisionDefinitionKey) {
        this.decisionDefinitionKey = decisionDefinitionKey;
    }

    public String getDecisionDefinitionName() {
        return decisionDefinitionName;
    }

    public void setDecisionDefinitionName(String decisionDefinitionName) {
        this.decisionDefinitionName = decisionDefinitionName;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityInstanceId() {
        return activityInstanceId;
    }

    public void setActivityInstanceId(String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
    }

    public Date getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(Date evaluationTime) {
        this.evaluationTime = evaluationTime;
    }
}
