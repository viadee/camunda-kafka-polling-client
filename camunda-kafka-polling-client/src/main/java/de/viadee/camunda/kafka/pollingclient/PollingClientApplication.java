package de.viadee.camunda.kafka.pollingclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.viadee.camunda.kafka.pollingclient.config.properties.ApplicationProperties;

/**
 * DataSourceAutoConfiguration is disabled, since data source must not be configured if using rest.
 * Thus data source auto configuration is enabled when using jdbc polling mode by polling configuration.
 *
 * @author viadee
 * @version $Id: $Id
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableScheduling
@EnableConfigurationProperties(ApplicationProperties.class)
public class PollingClientApplication {

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        SpringApplication.run(PollingClientApplication.class, args);
    }
}
