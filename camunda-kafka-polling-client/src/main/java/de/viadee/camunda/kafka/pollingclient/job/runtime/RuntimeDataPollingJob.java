package de.viadee.camunda.kafka.pollingclient.job.runtime;

import org.springframework.scheduling.annotation.Scheduled;

public class RuntimeDataPollingJob {

    private final RuntimeDataPollingService runtimeDataPollingService;

    public RuntimeDataPollingJob(final RuntimeDataPollingService runtimeDataPollingService) {
        this.runtimeDataPollingService = runtimeDataPollingService;
    }

    @Scheduled(initialDelay = 500L, fixedDelayString = "${polling.runtime-data.interval-in-ms}")
    public void executeScheduled() {
        runtimeDataPollingService.run();
    }
}