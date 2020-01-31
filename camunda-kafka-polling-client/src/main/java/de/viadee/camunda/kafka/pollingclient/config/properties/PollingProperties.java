package de.viadee.camunda.kafka.pollingclient.config.properties;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.File;
import java.util.Date;

/**
 * <p>
 * PollingProperties class.
 * </p>
 *
 * @author viadee
 * @version $Id: $Id
 */

public class PollingProperties {

    /**
     * Initial timestamp to start polling with in case no polling has been performed before. (Default: Start timestamp
     * of polling client)
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

    /**
     * The ms polling slice ends before the current time stamp
     */
    private long backwardOffsetInMs;

    /**
     * Initial timestamp to start polling with in case no polling has been performed before. (Default: Start timestamp
     * of polling client)
     */
    @java.lang.SuppressWarnings("all")
    public Date getInitialTimestamp() {
        return this.initialTimestamp;
    }

    /**
     * Polling intervall in ms
     */
    @java.lang.SuppressWarnings("all")
    public long getIntervalInMs() {
        return this.intervalInMs;
    }

    /**
     * File to store properties of last polled marker
     */
    @java.lang.SuppressWarnings("all")
    public File getLastPolledFile() {
        return this.lastPolledFile;
    }

    /**
     * Enable/disable polling
     */
    @java.lang.SuppressWarnings("all")
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Initial timestamp to start polling with in case no polling has been performed before. (Default: Start timestamp
     * of polling client)
     */
    @java.lang.SuppressWarnings("all")
    public void setInitialTimestamp(final Date initialTimestamp) {
        this.initialTimestamp = initialTimestamp;
    }

    /**
     * Polling intervall in ms
     */
    @java.lang.SuppressWarnings("all")
    public void setIntervalInMs(final long intervalInMs) {
        this.intervalInMs = intervalInMs;
    }

    /**
     * File to store properties of last polled marker
     */
    @java.lang.SuppressWarnings("all")
    public void setLastPolledFile(final File lastPolledFile) {
        this.lastPolledFile = lastPolledFile;
    }

    /**
     * Enable/disable polling
     */
    @java.lang.SuppressWarnings("all")
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * The ms polling slice ends before the current time stamp
     */
    @java.lang.SuppressWarnings("all")
    public long getBackwardOffsetInMs() {
        return backwardOffsetInMs;
    }

    /**
     * The ms polling slice ends before the current time stamp
     */
    @java.lang.SuppressWarnings("all")
    public void setBackwardOffsetInMs(long backwardOffsetInMs) {
        this.backwardOffsetInMs = backwardOffsetInMs;
    }

}
