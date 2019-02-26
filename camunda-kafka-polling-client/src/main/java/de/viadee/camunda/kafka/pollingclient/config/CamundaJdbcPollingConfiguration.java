package de.viadee.camunda.kafka.pollingclient.config;

import javax.sql.DataSource;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.viadee.camunda.kafka.pollingclient.service.polling.PollingService;
import de.viadee.camunda.kafka.pollingclient.service.polling.jdbc.CamundaJdbcPollingServiceImpl;

/**
 * <p>CamundaJdbcPollingConfiguration class.</p>
 *
 * @author viadee
 * @version $Id: $Id
 */
@Configuration
@ImportAutoConfiguration(DataSourceAutoConfiguration.class)
@Profile("jdbc")
public class CamundaJdbcPollingConfiguration {

    /**
     * <p>pollingService.</p>
     *
     * @param historyService a {@link org.camunda.bpm.engine.HistoryService} object.
     * @param repositoryService a {@link org.camunda.bpm.engine.RepositoryService} object.
     * @return a {@link de.viadee.camunda.kafka.pollingclient.service.polling.PollingService} object.
     */
    @Bean
    public PollingService pollingService(HistoryService historyService, RepositoryService repositoryService, TaskService taskService) {
        return new CamundaJdbcPollingServiceImpl(historyService, repositoryService, taskService);
    }

    /**
     * <p>processEngine.</p>
     *
     * @param dataSource a {@link javax.sql.DataSource} object.
     * @return a {@link org.camunda.bpm.engine.ProcessEngine} object.
     */
    @Bean
    public ProcessEngine processEngine(DataSource dataSource) {
        return ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                .setDataSource(dataSource)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
                .setJobExecutorActivate(false)
                .setHistory(ProcessEngineConfiguration.HISTORY_AUTO)
                .buildProcessEngine();
    }

    /**
     * <p>historyService.</p>
     *
     * @param processEngine a {@link org.camunda.bpm.engine.ProcessEngine} object.
     * @return a {@link org.camunda.bpm.engine.HistoryService} object.
     */
    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }
}
