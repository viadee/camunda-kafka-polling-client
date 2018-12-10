package de.viadee.camunda.kafka.pollingclient.config;

import java.util.Collections;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import de.viadee.camunda.kafka.pollingclient.service.polling.PollingService;
import de.viadee.camunda.kafka.pollingclient.service.polling.rest.CamundaRestPollingServiceImpl;
import de.viadee.camunda.kafka.pollingclient.config.properties.CamundaRestPollingProperties;

@Configuration
@EnableConfigurationProperties(CamundaRestPollingProperties.class)
@Profile("rest")
public class CamundaRestPollingConfiguration {

    private final CamundaRestPollingProperties camundaProperties;

    public CamundaRestPollingConfiguration(CamundaRestPollingProperties camundaProperties) {
        this.camundaProperties = camundaProperties;
    }

    @Bean
    public PollingService pollingService() {
        return new CamundaRestPollingServiceImpl(camundaProperties, camundaApiRestTemplate());
    }

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
                    camundaProperties.getPassword()
            )));
        }

        return template;
    }
}
