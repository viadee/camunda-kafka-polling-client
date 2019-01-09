package de.viadee.camunda.kafka.pollingclient.config.properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for polling using Camunda REST API
 *
 * @author viadee
 * @version $Id: $Id
 */

@ConfigurationProperties(prefix = "polling.camunda.rest")
public class CamundaRestPollingProperties {

    /**
     * URL of Camunda REST API
     */
    private String url;

    /**
     * Username used for authentication
     */
    private String username;

    /**
     * Password used for authentication
     */
    private String password;

    /**
     * <p>isAuthenticationEnabled.</p>
     *
     * @return a boolean.
     */
    public boolean isAuthenticationEnabled() {
        return StringUtils.isNotEmpty(username);
    }
    
    /**
     * URL of Camunda REST API
     */
    @java.lang.SuppressWarnings("all")
    public String getUrl() {
        return this.url;
    }

    /**
     * Username used for authentication
     */
    @java.lang.SuppressWarnings("all")
    public String getUsername() {
        return this.username;
    }

    /**
     * Password used for authentication
     */
    @java.lang.SuppressWarnings("all")
    public String getPassword() {
        return this.password;
    }

    /**
     * URL of Camunda REST API
     */
    @java.lang.SuppressWarnings("all")
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * Username used for authentication
     */
    @java.lang.SuppressWarnings("all")
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Password used for authentication
     */
    @java.lang.SuppressWarnings("all")
    public void setPassword(final String password) {
        this.password = password;
    }
}
