package de.viadee.camunda.kafka.pollingclient.config.properties;

import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for polling using Camunda JDBC API
 *
 * @version $Id: $Id
 */
@ConfigurationProperties(prefix = "polling.camunda.jdbc")
public class CamundaJdbcPollingProperties {

    /**
     * History level to set in process engine. One of 'auto', 'full', 'audit', 'variable'. For details, see Camunda
     * documentation.
     */
    private String historyLevel = ProcessEngineConfiguration.HISTORY_AUTO;

    /**
     * Camunda history level
     */
    public String getHistoryLevel() {
        return historyLevel;
    }

    /**
     * Camunda history level
     */
    public void setHistoryLevel(String historyLevel) {
        this.historyLevel = historyLevel;
    }

}
