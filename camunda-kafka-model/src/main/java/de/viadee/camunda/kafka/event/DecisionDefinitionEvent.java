package de.viadee.camunda.kafka.event;

public class DecisionDefinitionEvent extends DeploymentEvent {

    private String key;
    private String category;
    private Integer version;
    private String resource;
    private String xml;
    private String versionTag;
    private Integer historyTimeToLive;

    @java.lang.SuppressWarnings("all")
    public String getKey() {
        return key;
    }

    @java.lang.SuppressWarnings("all")
    public void setKey(String key) {
        this.key = key;
    }

    @java.lang.SuppressWarnings("all")
    public String getCategory() {
        return category;
    }

    @java.lang.SuppressWarnings("all")
    public void setCategory(String category) {
        this.category = category;
    }

    @java.lang.SuppressWarnings("all")
    public Integer getVersion() {
        return version;
    }

    @java.lang.SuppressWarnings("all")
    public void setVersion(Integer version) {
        this.version = version;
    }

    @java.lang.SuppressWarnings("all")
    public String getResource() {
        return resource;
    }

    @java.lang.SuppressWarnings("all")
    public void setResource(String resource) {
        this.resource = resource;
    }

    @java.lang.SuppressWarnings("all")
    public String getXml() {
        return xml;
    }

    @java.lang.SuppressWarnings("all")
    public void setXml(String xml) {
        this.xml = xml;
    }

    @java.lang.SuppressWarnings("all")
    public String getVersionTag() {
        return versionTag;
    }

    @java.lang.SuppressWarnings("all")
    public void setVersionTag(String versionTag) {
        this.versionTag = versionTag;
    }

    @java.lang.SuppressWarnings("all")
    public Integer getHistoryTimeToLive() {
        return historyTimeToLive;
    }

    @java.lang.SuppressWarnings("all")
    public void setHistoryTimeToLive(Integer historyTimeToLive) {
        this.historyTimeToLive = historyTimeToLive;
    }
}
