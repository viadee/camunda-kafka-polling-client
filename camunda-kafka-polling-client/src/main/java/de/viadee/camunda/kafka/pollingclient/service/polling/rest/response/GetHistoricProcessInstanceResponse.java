package de.viadee.camunda.kafka.pollingclient.service.polling.rest.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Response structure of Camunda REST API <code>GET /history/process-instance</code>
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetHistoricProcessInstanceResponse {

    private String id;

    private String superProcessInstanceId;

    private String superCaseInstanceId;

    private String caseInstanceId;

    private String processDefinitionName;

    private String processDefinitionKey;

    private Integer processDefinitionVersion;

    private String processDefinitionId;

    private String businessKey;

    private Date startTime;

    private Date endTime;

    private Long durationInMillis;

    private String startUserId;

    private String startActivityId;

    private String deleteReason;

    private String tenantId;

    private String state;
}
