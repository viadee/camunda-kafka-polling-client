package de.viadee.camunda.kafka.event;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * DecisionInstanceEvent class.
 * </p>
 *
 *
 *
 * @author viadee
 *
 * @version $Id: $Id
 */
public class DecisionInstanceEvent extends HistoryEvent {

    private String decisionDefinitionId;
    private String decisionDefinitionKey;
    private String decisionDefinitionName;
    private Date evaluationTime;
    private Date removalTime;
    private String activityId;
    private String activityInstanceId;
    private String tenantId;
    private String userId;
    private String rootDecisionInstanceId;
    private String rootProcessInstanceId;
    private String decisionRequirementsDefinitionId;
    private String decisionRequirementsDefinitionKey;
    private Double collectResultValue;
    private List<DecisionInstanceInputEvent> inputs;
    private List<DecisionInstanceOutputEvent> outputs;

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

    public Date getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(Date evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public Date getRemovalTime() {
        return removalTime;
    }

    public void setRemovalTime(Date removalTime) {
        this.removalTime = removalTime;
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

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRootDecisionInstanceId() {
        return rootDecisionInstanceId;
    }

    public void setRootDecisionInstanceId(String rootDecisionInstanceId) {
        this.rootDecisionInstanceId = rootDecisionInstanceId;
    }

    public String getRootProcessInstanceId() {
        return rootProcessInstanceId;
    }

    public void setRootProcessInstanceId(String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }

    public String getDecisionRequirementsDefinitionId() {
        return decisionRequirementsDefinitionId;
    }

    public void setDecisionRequirementsDefinitionId(String decisionRequirementsDefinitionId) {
        this.decisionRequirementsDefinitionId = decisionRequirementsDefinitionId;
    }

    public String getDecisionRequirementsDefinitionKey() {
        return decisionRequirementsDefinitionKey;
    }

    public void setDecisionRequirementsDefinitionKey(String decisionRequirementsDefinitionKey) {
        this.decisionRequirementsDefinitionKey = decisionRequirementsDefinitionKey;
    }

    public Double getCollectResultValue() {
        return collectResultValue;
    }

    public void setCollectResultValue(Double collectResultValue) {
        this.collectResultValue = collectResultValue;
    }

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

}
