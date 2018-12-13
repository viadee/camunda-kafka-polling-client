package de.viadee.camunda.kafka.event;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>ProcessDefinitionEvent class.</p>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Getter
@Setter
public class ProcessDefinitionEvent extends DeploymentEvent {

    private String key;

    private String category;

    private String description;

    private Integer version;

    private String resource;

    private String xml;

    private Boolean suspended;

    private String versionTag;

    private Integer historyTimeToLive;
}
