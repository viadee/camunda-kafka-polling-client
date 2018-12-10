package de.viadee.camunda.kafka.pollingclient.service.lastpolled;

public interface LastPolledService {

    /**
     * Provide time slice of last polling. If none already available, initial slice is provided.
     */
    PollingTimeslice getPollingTimeslice();
    
    /**
     * Update polling time slice to mark given slice as polled.
     */
    void updatePollingTimeslice(PollingTimeslice pollingTimeslice);
    
}
