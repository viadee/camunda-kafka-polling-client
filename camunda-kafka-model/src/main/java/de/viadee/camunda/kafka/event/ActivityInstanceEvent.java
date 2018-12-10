package de.viadee.camunda.kafka.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityInstanceEvent extends ScopeInstanceEvent {

    private String activityId;

    private String activityName;

    private String activityType;

    private String activityInstanceId;

    private int activityInstanceState;

    private String parentActivityInstanceId;

    private String calledProcessInstanceId;

    private String calledCaseInstanceId;

    private String taskId;

    private String taskAssignee;

    private String tenantId;
}
