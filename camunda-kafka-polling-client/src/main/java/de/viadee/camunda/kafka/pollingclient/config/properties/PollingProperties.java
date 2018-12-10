package de.viadee.camunda.kafka.pollingclient.config.properties;

import java.io.File;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PollingProperties {

    /**
     * Initial timestamp to start polling with in case no polling has been performed before. (Default: Start timestamp of polling client)
     */
    private Date initalTimestamp = new Date();

    /**
     * Polling intervall in ms
     */
    private long intervalInMs;

    /**
     * File to store properties of last polled marker
     */
    private File lastPolledFile;

    /**
     * Enable/disable polling
     */
    private boolean enabled;
}
