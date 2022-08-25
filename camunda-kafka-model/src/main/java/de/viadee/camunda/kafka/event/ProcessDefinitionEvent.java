// Generated by delombok at Wed Jan 09 13:24:24 CET 2019
package de.viadee.camunda.kafka.event;

/**
 * <p>
 * ProcessDefinitionEvent class.
 * </p>
 *
 *
 *
 * @author viadee
 *
 * @version $Id: $Id
 */
public class ProcessDefinitionEvent extends DeploymentEvent {

    private String key;
    private String category;
    private String description;
    private Integer version;
    private String resource;
    private String xml;
    private Boolean suspended;
    private String versionTag;
    private Integer historyTimeToLive;

    @java.lang.SuppressWarnings("all")
    public String getKey() {
        return this.key;
    }

    @java.lang.SuppressWarnings("all")
    public String getCategory() {
        return this.category;
    }

    @java.lang.SuppressWarnings("all")
    public String getDescription() {
        return this.description;
    }

    @java.lang.SuppressWarnings("all")
    public Integer getVersion() {
        return this.version;
    }

    @java.lang.SuppressWarnings("all")
    public String getResource() {
        return this.resource;
    }

    @java.lang.SuppressWarnings("all")
    public String getXml() {
        return this.xml;
    }

    @java.lang.SuppressWarnings("all")
    public Boolean getSuspended() {
        return this.suspended;
    }

    @java.lang.SuppressWarnings("all")
    public String getVersionTag() {
        return this.versionTag;
    }

    @java.lang.SuppressWarnings("all")
    public Integer getHistoryTimeToLive() {
        return this.historyTimeToLive;
    }

    @java.lang.SuppressWarnings("all")
    public void setKey(final String key) {
        this.key = key;
    }

    @java.lang.SuppressWarnings("all")
    public void setCategory(final String category) {
        this.category = category;
    }

    @java.lang.SuppressWarnings("all")
    public void setDescription(final String description) {
        this.description = description;
    }

    @java.lang.SuppressWarnings("all")
    public void setVersion(final Integer version) {
        this.version = version;
    }

    @java.lang.SuppressWarnings("all")
    public void setResource(final String resource) {
        this.resource = resource;
    }

    @java.lang.SuppressWarnings("all")
    public void setXml(final String xml) {
        this.xml = xml;
    }

    @java.lang.SuppressWarnings("all")
    public void setSuspended(final Boolean suspended) {
        this.suspended = suspended;
    }

    @java.lang.SuppressWarnings("all")
    public void setVersionTag(final String versionTag) {
        this.versionTag = versionTag;
    }

    @java.lang.SuppressWarnings("all")
    public void setHistoryTimeToLive(final Integer historyTimeToLive) {
        this.historyTimeToLive = historyTimeToLive;
    }
}
