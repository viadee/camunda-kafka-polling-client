package de.viadee.camunda.kafka.pollingclient.service.polling.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Response structure of Camunda REST API <code>GET /process-definition/123/xml</code>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetProcessDefinitionXmlResponse {

    private String id;

    private String bpmn20Xml;
}
