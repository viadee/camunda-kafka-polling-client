package de.viadee.camunda.kafka.event;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>DetailEvent class.</p>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Getter
@Setter
public class DetailEvent extends HistoryEvent {

    private String activityInstanceId;

    private String taskId;

    private Date timestamp;

    private String tenantId;

    private String userOperationId;
}
