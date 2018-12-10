package de.viadee.camunda.kafka.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessInstanceEvent extends ScopeInstanceEvent {

    private String businessKey;

    private String startUserId;

    private String superProcessInstanceId;

    private String superCaseInstanceId;

    private String deleteReason;

    private String endActivityId;

    private String startActivityId;

    private String tenantId;

    private String state;
}
