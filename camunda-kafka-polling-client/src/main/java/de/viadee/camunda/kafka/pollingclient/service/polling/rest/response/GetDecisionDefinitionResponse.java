package de.viadee.camunda.kafka.pollingclient.service.polling.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response structure of Camunda REST API <code>GET /decision-definition</code>
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
    private String versionTag;
    private Integer historyTimeToLive;

    @SuppressWarnings("all")
    public String getId() {
        return id;
    }

    @SuppressWarnings("all")
    public void setId(String id) {
        this.id = id;
    }

    @SuppressWarnings("all")
    public String getKey() {
        return key;
    }

    @SuppressWarnings("all")
    public void setKey(String key) {
        this.key = key;
    }

    @SuppressWarnings("all")
    public String getCategory() {
        return category;
    }

    @SuppressWarnings("all")
    public void setCategory(String category) {
        this.category = category;
    }

    @SuppressWarnings("all")
    public String getName() {
        return name;
    }

    @SuppressWarnings("all")
    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("all")
    public Integer getVersion() {
        return version;
    }

    @SuppressWarnings("all")
    public void setVersion(Integer version) {
        this.version = version;
    }

    @SuppressWarnings("all")
    public String getResource() {
        return resource;
    }

    @SuppressWarnings("all")
    public void setResource(String resource) {
        this.resource = resource;
    }

    @SuppressWarnings("all")
    public String getDeploymentId() {
        return deploymentId;
    }

    @SuppressWarnings("all")
    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    @SuppressWarnings("all")
    public String getTenantId() {
        return tenantId;
    }

    @SuppressWarnings("all")
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @SuppressWarnings("all")
    public String getVersionTag() {
        return versionTag;
    }

    @SuppressWarnings("all")
    public void setVersionTag(String versionTag) {
        this.versionTag = versionTag;
    }

    @SuppressWarnings("all")
    public Integer getHistoryTimeToLive() {
        return historyTimeToLive;
    }

    @SuppressWarnings("all")
    public void setHistoryTimeToLive(Integer historyTimeToLive) {
        this.historyTimeToLive = historyTimeToLive;
    }
}
