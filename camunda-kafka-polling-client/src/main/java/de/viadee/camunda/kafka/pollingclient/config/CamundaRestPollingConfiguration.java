package de.viadee.camunda.kafka.pollingclient.config;

import de.viadee.camunda.kafka.pollingclient.config.properties.CamundaRestPollingProperties;
import de.viadee.camunda.kafka.pollingclient.service.polling.PollingService;
import de.viadee.camunda.kafka.pollingclient.service.polling.rest.CamundaRestPollingServiceImpl;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Collections;

/**
 * <p>
 * CamundaRestPollingConfiguration class.
 * </p>
 * {@link DataSourceAutoConfiguration} is disabled, since data source must not be configured if using rest.
 *
 * @author viadee
 * @version $Id: $Id
 */
@Configuration
@EnableConfigurationProperties(CamundaRestPollingProperties.class)
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@Profile("rest")
public class CamundaRestPollingConfiguration {

    private final CamundaRestPollingProperties camundaProperties;

    /**
     * <p>
     * Constructor for CamundaRestPollingConfiguration.
     * </p>
     *
     * @param camundaProperties
     *            a {@link de.viadee.camunda.kafka.pollingclient.config.properties.CamundaRestPollingProperties} object.
     */
    public CamundaRestPollingConfiguration(CamundaRestPollingProperties camundaProperties) {
        this.camundaProperties = camundaProperties;
    }

    /**
     * <p>
     * pollingService.
     * </p>
     *
     * @return a {@link de.viadee.camunda.kafka.pollingclient.service.polling.PollingService} object.
     */
    @Bean
    public PollingService pollingService() {
        return new CamundaRestPollingServiceImpl(camundaProperties, camundaApiRestTemplate());
    }

    /**
     * <p>
     * camundaApiRestTemplate.
     * </p>
     *
     * @return a {@link org.springframework.web.client.RestTemplate} object.
     */
    @Bean
    public RestTemplate camundaApiRestTemplate() {
        final RestTemplate template = new RestTemplate();

        // Enable escaping url parameter values.
        // Otherwise timezone offset designator (+/-) will result in invalid urls.
        final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        template.setUriTemplateHandler(factory);

        if (camundaProperties.isAuthenticationEnabled()) {
            template.setInterceptors(Collections.singletonList(new BasicAuthorizationInterceptor(
                                                                                                 camundaProperties.getUsername(),
                                                                                                 camundaProperties.getPassword())));
        }

        return template;
    }
}
