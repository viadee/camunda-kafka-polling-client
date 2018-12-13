package de.viadee.camunda.kafka.pollingclient.job.repository;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * <p>RepositoryDataPollingJob class.</p>
 *
 * @author viadee
 * @version $Id: $Id
 */
public class RepositoryDataPollingJob {

    private final RepositoryDataPollingService repositoryDataPollingService;

    /**
     * <p>Constructor for RepositoryDataPollingJob.</p>
     *
     * @param repositoryDataPollingService a {@link de.viadee.camunda.kafka.pollingclient.job.repository.RepositoryDataPollingService} object.
     */
    public RepositoryDataPollingJob(final RepositoryDataPollingService repositoryDataPollingService) {
        this.repositoryDataPollingService = repositoryDataPollingService;
    }

    /**
     * <p>executeScheduled.</p>
     */
    @Scheduled(initialDelay = 500L, fixedDelayString = "${polling.repository-data.interval-in-ms}")
    public void executeScheduled() {
        repositoryDataPollingService.run();
    }
}
