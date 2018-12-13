package de.viadee.camunda.kafka.event;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>ScopeInstanceEvent class.</p>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Getter
@Setter
public class ScopeInstanceEvent extends HistoryEvent {

    private Long durationInMillis;

    private Date startTime;

    private Date endTime;
}
