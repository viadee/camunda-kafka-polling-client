package de.viadee.camunda.kafka.pollingclient.service.polling.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response structure of Camunda REST API <code>GET /decision-definition/123/xml</code>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDecisionDefinitionXmlResponse {

    private String id;
    private String dmnXml;

    @SuppressWarnings("all")
    public String getId() {
        return this.id;
    }

    @SuppressWarnings("all")
    public String getDmnXml() {
        return this.dmnXml;
    }

    @SuppressWarnings("all")
    public void setId(final String id) {
        this.id = id;
    }

    @SuppressWarnings("all")
    public void setBpmn20Xml(final String dmnXml) {
        this.dmnXml = dmnXml;
    }
}
