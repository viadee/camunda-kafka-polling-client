package de.viadee.camunda.kafka.pollingclient.service.lastpolled;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Defines the time slice to perform polling for.
 *
 * The slice is defined by the interval to poll data starting with {@link #startTime} and ending with {@link #endTime}.
 * To prevent polling incomplete process data, an initial cutoff timestamp {@link #cutoffTime} is provided.
 * All data polled must be after this cutoff time. This means, a process started before this cutoff time, must not be polled at all.
 *
 * Following rules apply:
 * <ol>
 * <li>{@link #cutoffTime} &lt; {@link #startTime}</li>
 * <li>{@link #startTime} &lt; {@link #endTime}</li>
 * <li>{@link #cutoffTime} is inclusive</li>
 * <li>{@link #startTime} is inclusive</li>
 * <li>{@link #endTime} is exclusive</li>
 * </ol>
 *
 * @author viadee
 * @version $Id: $Id
 */


public class PollingTimeslice {

    /**
     * First polling slice start. No data should be polled before this point.
     */
    private final Date cutoffTime;

    /**
     * Start of polling slice
     */
    private final Date startTime;

    /**
     * End of polling slice
     */
    private final Date endTime;
    
    /**
     * First polling slice start. No data should be polled before this point.
     */
    @java.lang.SuppressWarnings("all")
    public Date getCutoffTime() {
        return this.cutoffTime;
    }

    /**
     * Start of polling slice
     */
    @java.lang.SuppressWarnings("all")
    public Date getStartTime() {
        return this.startTime;
    }

    /**
     * End of polling slice
     */
    @java.lang.SuppressWarnings("all")
    public Date getEndTime() {
        return this.endTime;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
        return "PollingTimeslice(cutoffTime=" + this.getCutoffTime() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ")";
    }

    @java.lang.SuppressWarnings("all")
    public PollingTimeslice(final Date cutoffTime, final Date startTime, final Date endTime) {
        this.cutoffTime = cutoffTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
