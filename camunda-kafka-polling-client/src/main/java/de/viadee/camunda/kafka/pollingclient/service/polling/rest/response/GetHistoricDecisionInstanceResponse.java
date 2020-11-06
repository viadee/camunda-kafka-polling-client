package de.viadee.camunda.kafka.pollingclient.service.polling.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Response structure of Camunda REST API <code>GET /history/decision-instance</code>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetHistoricDecisionInstanceResponse {

    private String id;
    private String decisionDefinitionId;
    private String decisionDefinitionKey;
    private String decisionDefinitionName;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String processInstanceId;
    private String caseDefinitionId;
    private String caseDefinitionKey;
    private String caseInstanceId;
    private String activityId;
    private String activityInstanceId;
    private Date evaluationTime;
    private List<GetHistoricDecisionInstanceInputResponse> inputs;
    private List<GetHistoricDecisionInstanceOutputResponse> outputs;

    public List<GetHistoricDecisionInstanceOutputResponse> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<GetHistoricDecisionInstanceOutputResponse> outputs) {
        this.outputs = outputs;
    }

    public List<GetHistoricDecisionInstanceInputResponse> getInputs() {
        return inputs;
    }

    public void setInputs(List<GetHistoricDecisionInstanceInputResponse> inputs) {
        this.inputs = inputs;
    }

    public Date getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(Date evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getCaseDefinitionId() {
        return caseDefinitionId;
    }

    public void setCaseDefinitionId(String caseDefinitionId) {
        this.caseDefinitionId = caseDefinitionId;
    }

    public String getCaseDefinitionKey() {
        return caseDefinitionKey;
    }

    public void setCaseDefinitionKey(String caseDefinitionKey) {
        this.caseDefinitionKey = caseDefinitionKey;
    }

    public String getCaseInstanceId() {
        return caseInstanceId;
    }

    public void setCaseInstanceId(String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
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
}
