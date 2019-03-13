package de.viadee.camunda.kafka.pollingclient.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for polling using Camunda JDBC API
 *
 * @version $Id: $Id
 */

@ConfigurationProperties(prefix = "polling.camunda.jdbc")
public class CamundaJdbcPollingProperties {

    private static final String DEFAULT_HISTORY_LEVEL = "auto";

    private String historyLevel;

    /**
     * Camunda history level
     */
    public String getHistoryLevel() {

        if (historyLevel == null)
            this.historyLevel = DEFAULT_HISTORY_LEVEL;

        return historyLevel;
    }

    /**
     * Camunda history level
     */
    public void setHistoryLevel(String historyLevel) {
        this.historyLevel = historyLevel;
    }

}
