package de.viadee.camunda.kafka.pollingclient.service.polling.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Response structure of Camunda REST API <code>GET /process-definition</code>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetProcessDefinitionResponse {

    private String id;

    private String key;

    private String category;

    private String description;

    private String name;

    private Integer version;

    private String resource;

    private String deploymentId;

    private String diagram;

    private Boolean suspended;

    private String tenantId;

    private String versionTag;

    private Integer historyTimeToLive;
}
