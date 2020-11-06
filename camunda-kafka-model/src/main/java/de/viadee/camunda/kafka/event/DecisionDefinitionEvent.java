package de.viadee.camunda.kafka.event;

public class DecisionDefinitionEvent extends DeploymentEvent {

    private String key;
    private String category;
    private Integer version;
    private String resource;
    private String xml;
    private String versionTag;
    private Integer historyTimeToLive;

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

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getVersionTag() {
        return versionTag;
    }

    public void setVersionTag(String versionTag) {
        this.versionTag = versionTag;
    }

    public Integer getHistoryTimeToLive() {
        return historyTimeToLive;
    }

    public void setHistoryTimeToLive(Integer historyTimeToLive) {
        this.historyTimeToLive = historyTimeToLive;
    }
}
