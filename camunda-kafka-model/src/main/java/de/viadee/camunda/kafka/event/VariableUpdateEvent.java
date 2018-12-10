package de.viadee.camunda.kafka.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariableUpdateEvent extends DetailEvent {

    private int revision;

    private String variableName;

    private String variableInstanceId;

    private String scopeActivityInstanceId;

    private String serializerName;

    private Long longValue;

    private Double doubleValue;

    private String textValue;

    private Object complexValue;
}
