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

    @java.lang.SuppressWarnings("all")
    public List<GetHistoricDecisionInstanceOutputResponse> getOutputs() {
        return outputs;
    }

    @java.lang.SuppressWarnings("all")
    public void setOutputs(List<GetHistoricDecisionInstanceOutputResponse> outputs) {
        this.outputs = outputs;
    }

    @java.lang.SuppressWarnings("all")
    public List<GetHistoricDecisionInstanceInputResponse> getInputs() {
        return inputs;
    }

    @java.lang.SuppressWarnings("all")
    public void setInputs(List<GetHistoricDecisionInstanceInputResponse> inputs) {
        this.inputs = inputs;
    }

    @java.lang.SuppressWarnings("all")
    public Date getEvaluationTime() {
        return evaluationTime;
    }

    @java.lang.SuppressWarnings("all")
    public void setEvaluationTime(Date evaluationTime) {
        this.evaluationTime = evaluationTime;
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
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    @java.lang.SuppressWarnings("all")
    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    @java.lang.SuppressWarnings("all")
    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    @java.lang.SuppressWarnings("all")
    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    @java.lang.SuppressWarnings("all")
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    @java.lang.SuppressWarnings("all")
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @java.lang.SuppressWarnings("all")
    public String getCaseDefinitionId() {
        return caseDefinitionId;
    }

    @java.lang.SuppressWarnings("all")
    public void setCaseDefinitionId(String caseDefinitionId) {
        this.caseDefinitionId = caseDefinitionId;
    }

    @java.lang.SuppressWarnings("all")
    public String getCaseDefinitionKey() {
        return caseDefinitionKey;
    }

    @java.lang.SuppressWarnings("all")
    public void setCaseDefinitionKey(String caseDefinitionKey) {
        this.caseDefinitionKey = caseDefinitionKey;
    }

    @java.lang.SuppressWarnings("all")
    public String getCaseInstanceId() {
        return caseInstanceId;
    }

    @java.lang.SuppressWarnings("all")
    public void setCaseInstanceId(String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
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
}
