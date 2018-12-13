package de.viadee.camunda.kafka.event;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>DeploymentEvent class.</p>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Getter
@Setter
public class DeploymentEvent {

    private String id;

    private String deploymentId;

    private String name;

    private String source;

    private Date deploymentTime;

    private String tenantId;
}
