package de.viadee.camunda.kafka.pollingclient.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.viadee.camunda.kafka.pollingclient.config.properties.ApplicationProperties;
import de.viadee.camunda.kafka.pollingclient.job.runtime.RuntimeDataPollingJob;
import de.viadee.camunda.kafka.pollingclient.job.runtime.RuntimeDataPollingService;
import de.viadee.camunda.kafka.pollingclient.service.event.EventService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.LastPolledService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.filebased.FilebasedLastPolledServiceImpl;
import de.viadee.camunda.kafka.pollingclient.service.polling.PollingService;

/**
 * <p>RuntimeDataPollingConfiguration class.</p>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Configuration
public class RuntimeDataPollingConfiguration {

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private PollingService pollingService;

    @Autowired
    private EventService eventService;

    /**
     * <p>runtimeDataLastPolledService.</p>
     *
     * @return a {@link de.viadee.camunda.kafka.pollingclient.service.lastpolled.LastPolledService} object.
     */
    @Bean
    public LastPolledService runtimeDataLastPolledService() {
        return new FilebasedLastPolledServiceImpl(properties.getRuntimeData());
    }

    /**
     * <p>runtimeDataPollingService.</p>
     *
     * @return a {@link de.viadee.camunda.kafka.pollingclient.job.runtime.RuntimeDataPollingService} object.
     */
    @Bean
    public RuntimeDataPollingService runtimeDataPollingService() {
        return new RuntimeDataPollingService(pollingService, runtimeDataLastPolledService(), eventService, properties);
    }

    /**
     * <p>runtimeDataPollingJob.</p>
     *
     * @return a {@link de.viadee.camunda.kafka.pollingclient.job.runtime.RuntimeDataPollingJob} object.
     */
    @Bean
    @ConditionalOnProperty(name = "polling.runtime-data.enabled", havingValue = "true", matchIfMissing = true)
    public RuntimeDataPollingJob runtimeDataPollingJob() {
        return new RuntimeDataPollingJob(runtimeDataPollingService());
    }
}
