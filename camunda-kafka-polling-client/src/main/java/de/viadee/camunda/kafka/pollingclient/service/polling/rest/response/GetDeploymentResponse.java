package de.viadee.camunda.kafka.pollingclient.service.polling.rest.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Response structure of Camunda REST API <code>GET /deployment</code>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDeploymentResponse {

    private String id;

    private String name;

    private String source;

    private Date deploymentTime;

    private String tenantId;
}
