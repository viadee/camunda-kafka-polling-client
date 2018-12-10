package de.viadee.camunda.kafka.pollingclient.service.polling.rest.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Response structure of Camunda REST API <code>GET /history/activity-instance</code>
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetHistoricActivityInstanceRespone {

    private String id;

    private String parentActivityInstanceId;

    private String activityId;

    private String activityName;

    private String activityType;

    private String processDefinitionKey;

    private String processDefinitionId;

    private String processInstanceId;

    private String executionId;

    private String taskId;

    private String assignee;

    private String calledProcessInstanceId;

    private String calledCaseInstanceId;

    private Date startTime;

    private Date endTime;

    private Long durationInMillis;

    private Boolean canceled;

    private Boolean completeScope;

    private String tenantId;
}
