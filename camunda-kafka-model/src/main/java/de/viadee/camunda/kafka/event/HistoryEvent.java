package de.viadee.camunda.kafka.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>HistoryEvent class.</p>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryEvent {

    private String id;

    private String processInstanceId;

    private String executionId;

    private String processDefinitionId;

    private String processDefinitionKey;

    private String processDefinitionName;

    private Integer processDefinitionVersion;

    private String caseInstanceId;

    private String caseExecutionId;

    private String caseDefinitionId;

    private String caseDefinitionKey;

    private String caseDefinitionName;

    private String eventType;

    private long sequenceCounter;
}
