package de.viadee.camunda.kafka.pollingclient.service.lastpolled;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

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

    public PollingTimeslice(final Date cutoffTime, final Date startTime, final Date endTime) {
        this.cutoffTime = cutoffTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * First polling slice start. No data should be polled before this point.
     */
    public Date getCutoffTime() {
        return this.cutoffTime;
    }

    /**
     * Start of polling slice
     */
    public Date getStartTime() {
        return this.startTime;
    }

    /**
     * End of polling slice
     */
    public Date getEndTime() {
        return this.endTime;
    }

    @Override
    public java.lang.String toString() {
        return "PollingTimeslice(cutoffTime=" + timeFormat.format(cutoffTime) + ", startTime=" + timeFormat
                .format(startTime) + ", endTime=" + timeFormat.format(endTime) + ")";
    }
}
