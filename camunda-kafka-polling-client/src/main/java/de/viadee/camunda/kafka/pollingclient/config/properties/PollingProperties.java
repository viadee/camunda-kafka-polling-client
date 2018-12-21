package de.viadee.camunda.kafka.pollingclient.config.properties;

import java.io.File;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>PollingProperties class.</p>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Getter
@Setter
public class PollingProperties {

    /**
     * Initial timestamp to start polling with in case no polling has been performed before. (Default: Start timestamp of polling client)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date initialTimestamp = new Date();

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
