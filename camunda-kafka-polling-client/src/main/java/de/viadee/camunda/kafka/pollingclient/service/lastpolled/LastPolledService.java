package de.viadee.camunda.kafka.pollingclient.service.lastpolled;

/**
 * <p>
 * LastPolledService interface.
 * </p>
 *
 * @author viadee
 * @version $Id: $Id
 */
public interface LastPolledService {

    /**
     * Provide time slice of last polling. If none already available, initial slice is provided.
     *
     * @return a {@link de.viadee.camunda.kafka.pollingclient.service.lastpolled.PollingTimeslice} object.
     */
    PollingTimeslice getPollingTimeslice();

    /**
     * Update polling time slice to mark given slice as polled.
     *
     * @param pollingTimeslice
     *            a {@link de.viadee.camunda.kafka.pollingclient.service.lastpolled.PollingTimeslice} object.
     */
    void updatePollingTimeslice(PollingTimeslice pollingTimeslice);

}
