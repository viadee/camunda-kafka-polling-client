package de.viadee.camunda.kafka.pollingclient.service.polling.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDecisionDefinitionXmlResponse {

    private String id;
    private String dmnXml;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDmnXml() {
        return dmnXml;
    }

    public void setDmnXml(String dmnXml) {
        this.dmnXml = dmnXml;
    }
}
