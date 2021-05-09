package de.viadee.camunda.kafka.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DecisionInstanceOutputEvent extends DecisionInstanceInputEvent {

    private String ruleId;
    private Integer ruleOrder;
    private String variableName;

    @SuppressWarnings("all")
    public String getRuleId() {
        return ruleId;
    }

    @SuppressWarnings("all")
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    @SuppressWarnings("all")
    public Integer getRuleOrder() {
        return ruleOrder;
    }

    @SuppressWarnings("all")
    public void setRuleOrder(Integer ruleOrder) {
        this.ruleOrder = ruleOrder;
    }

    @SuppressWarnings("all")
    public String getVariableName() {
        return variableName;
    }

    @SuppressWarnings("all")
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
}
