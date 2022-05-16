package de.viadee.camunda.kafka.pollingclient.service.polling.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *  Response structure of Camunda REST API <code>GET /decision-definition</code>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDecisionDefinitionResponse {

    private String id;
    private String key;
    private String category;
    private String name;
    private Integer version;
    private String resource;
    private String deploymentId;
    private String tenantId;
    private String decisionRequirementsDefinitionId;
    private String decisionRequirementsDefinitionKey;
    private Integer historyTimeToLive;
    private String versionTag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    public Integer getHistoryTimeToLive() {
        return historyTimeToLive;
    }

    public void setHistoryTimeToLive(Integer historyTimeToLive) {
        this.historyTimeToLive = historyTimeToLive;
    }

    public String getVersionTag() {
        return versionTag;
    }

    public void setVersionTag(String versionTag) {
        this.versionTag = versionTag;
    }
}
