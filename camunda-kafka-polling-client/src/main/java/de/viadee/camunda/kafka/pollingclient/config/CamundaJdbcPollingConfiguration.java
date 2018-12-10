package de.viadee.camunda.kafka.pollingclient.config;

import javax.sql.DataSource;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.viadee.camunda.kafka.pollingclient.service.polling.PollingService;
import de.viadee.camunda.kafka.pollingclient.service.polling.jdbc.CamundaJdbcPollingServiceImpl;

@Configuration
@ImportAutoConfiguration(DataSourceAutoConfiguration.class)
@Profile("jdbc")
public class CamundaJdbcPollingConfiguration {

    @Bean
    public PollingService pollingService(HistoryService historyService, RepositoryService repositoryService) {
        return new CamundaJdbcPollingServiceImpl(historyService, repositoryService);
    }

    @Bean
    public ProcessEngine processEngine(DataSource dataSource) {
        return ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                .setDataSource(dataSource)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
                .setJobExecutorActivate(false)
                .setHistory(ProcessEngineConfiguration.HISTORY_AUTO)
                .buildProcessEngine();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }
}
