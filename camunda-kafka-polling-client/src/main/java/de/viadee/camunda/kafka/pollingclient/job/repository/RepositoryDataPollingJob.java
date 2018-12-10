package de.viadee.camunda.kafka.pollingclient.job.repository;

import org.springframework.scheduling.annotation.Scheduled;

public class RepositoryDataPollingJob {

    private final RepositoryDataPollingService repositoryDataPollingService;

    public RepositoryDataPollingJob(final RepositoryDataPollingService repositoryDataPollingService) {
        this.repositoryDataPollingService = repositoryDataPollingService;
    }

    @Scheduled(initialDelay = 500L, fixedDelayString = "${polling.repository-data.interval-in-ms}")
    public void executeScheduled() {
        repositoryDataPollingService.run();
    }
}