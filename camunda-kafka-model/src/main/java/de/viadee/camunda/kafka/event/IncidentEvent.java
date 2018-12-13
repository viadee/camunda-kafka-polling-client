package de.viadee.camunda.kafka.event;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>IncidentEvent class.</p>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Getter
@Setter
public class IncidentEvent extends HistoryEvent {

    private Date createTime;

    private Date endTime;

    private String incidentType;

    private String activityId;

    private String causeIncidentId;

    private String rootCauseIncidentId;

    private String configuration;

    private String incidentMessage;

    private int incidentState;

    private String tenantId;

    private String jobDefinitionId;
}
