package de.viadee.camunda.kafka.pollingclient.job.runtime;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * <p>RuntimeDataPollingJob class.</p>
 *
 * @author viadee
 * @version $Id: $Id
 */
public class RuntimeDataPollingJob {

    private final RuntimeDataPollingService runtimeDataPollingService;

    /**
     * <p>Constructor for RuntimeDataPollingJob.</p>
     *
     * @param runtimeDataPollingService a {@link de.viadee.camunda.kafka.pollingclient.job.runtime.RuntimeDataPollingService} object.
     */
    public RuntimeDataPollingJob(final RuntimeDataPollingService runtimeDataPollingService) {
        this.runtimeDataPollingService = runtimeDataPollingService;
    }

    /**
     * <p>executeScheduled.</p>
     */
    @Scheduled(initialDelay = 500L, fixedDelayString = "${polling.runtime-data.interval-in-ms}")
    public void executeScheduled() {
        runtimeDataPollingService.run();
    }
}
