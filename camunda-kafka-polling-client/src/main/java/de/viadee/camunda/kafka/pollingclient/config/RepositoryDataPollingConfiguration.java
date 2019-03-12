package de.viadee.camunda.kafka.pollingclient.config;

import de.viadee.camunda.kafka.pollingclient.config.properties.ApplicationProperties;
import de.viadee.camunda.kafka.pollingclient.job.repository.RepositoryDataPollingJob;
import de.viadee.camunda.kafka.pollingclient.job.repository.RepositoryDataPollingService;
import de.viadee.camunda.kafka.pollingclient.service.event.EventService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.LastPolledService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.filebased.FilebasedLastPolledServiceImpl;
import de.viadee.camunda.kafka.pollingclient.service.polling.PollingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * RepositoryDataPollingConfiguration class.
 * </p>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Configuration
public class RepositoryDataPollingConfiguration {

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private PollingService pollingService;

    @Autowired
    private EventService eventService;

    /**
     * <p>
     * repositoryDataLastPolledService.
     * </p>
     *
     * @return a {@link de.viadee.camunda.kafka.pollingclient.service.lastpolled.LastPolledService} object.
     */
    @Bean
    public LastPolledService repositoryDataLastPolledService() {
        return new FilebasedLastPolledServiceImpl(properties.getRepositoryData());
    }

    /**
     * <p>
     * repositoryDataPollingService.
     * </p>
     *
     * @return a {@link de.viadee.camunda.kafka.pollingclient.job.repository.RepositoryDataPollingService} object.
     */
    @Bean
    public RepositoryDataPollingService repositoryDataPollingService() {
        return new RepositoryDataPollingService(pollingService, repositoryDataLastPolledService(), eventService,
                                                properties);
    }

    /**
     * <p>
     * repositoryDataPollingJob.
     * </p>
     *
     * @return a {@link de.viadee.camunda.kafka.pollingclient.job.repository.RepositoryDataPollingJob} object.
     */
    @Bean
    @ConditionalOnProperty(name = "polling.repository-data.enabled", havingValue = "true", matchIfMissing = true)
    public RepositoryDataPollingJob repositoryDataPollingJob() {
        return new RepositoryDataPollingJob(repositoryDataPollingService());
    }
}
