package de.viadee.camunda.kafka.pollingclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.viadee.camunda.kafka.pollingclient.config.properties.ApplicationProperties;

/**
 * @author viadee
 * @version $Id: $Id
 */
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ApplicationProperties.class)
public class PollingClientApplication {

    /**
     * <p>
     * main.
     * </p>
     *
     * @param args
     *            an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        SpringApplication.run(PollingClientApplication.class, args);
    }
}
