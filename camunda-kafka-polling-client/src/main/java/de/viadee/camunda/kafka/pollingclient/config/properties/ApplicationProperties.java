package de.viadee.camunda.kafka.pollingclient.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * ApplicationProperties class.
 * </p>
 *
 * @author viadee
 * @version $Id: $Id
 */

@ConfigurationProperties(prefix = "polling")
public class ApplicationProperties {

    /**
     * Configuration for polling runtime data
     */
    @NestedConfigurationProperty
    private PollingProperties runtimeData = new PollingProperties();

    /**
     * Configuration for polling repository data
     */
    @NestedConfigurationProperty
    private PollingProperties repositoryData = new PollingProperties();

    /**
     * Configuration of kafka topics to use on event type basis: Mapping of event type (event class name without "Event"
     * suffix) to kafka topic name. Default topic of an event is the event type.
     */
    private Map<String, String> eventTopics = new HashMap<>();

    /**
     * Configuration of events to poll
     */
    private Set<PollingEvents> pollingEvents = new HashSet<>();

    public enum PollingEvents {
        PROCESS_INSTANCE_UNFINISHED,
        PROCESS_INSTANCE_FINISHED,
        ACTIVITY_UNFINISHED,
        ACTIVITY_FINISHED,
        /**
         * Poll variable details of finished process instances. (Only possible if finished process instances are also
         * polled {@link #PROCESS_INSTANCE_FINISHED})
         */
        VARIABLE_DETAILS_FINISHED,
        /**
         * Poll variable details of unfinished process instances. (Only possible if unfinished process instances are
         * also polled {@link #PROCESS_INSTANCE_UNFINISHED})
         */
        VARIABLE_DETAILS_UNFINISHED,
        /**
         * Poll last variable values of finished process instances. (Only possible if finished process instances are
         * also polled {@link #PROCESS_INSTANCE_FINISHED})
         */
        VARIABLE_CURRENT_FINISHED,
        /**
         * Poll last variable values of unfinished process instances. (Only possible if unfinished process instances are
         * also polled {@link #PROCESS_INSTANCE_UNFINISHED})
         */
        VARIABLE_CURRENT_UNFINISHED,
        PROCESS_DEFINITION,
        TASK_COMMENTS,

        // TODO see https://github.com/viadee/camunda-kafka-polling-client/issues/34#issuecomment-503677011
        IDENTITY_LINKS
    }

    /**
     * Configuration for polling runtime data
     */
    @java.lang.SuppressWarnings("all")
    public PollingProperties getRuntimeData() {
        return this.runtimeData;
    }

    /**
     * Configuration for polling repository data
     */
    @java.lang.SuppressWarnings("all")
    public PollingProperties getRepositoryData() {
        return this.repositoryData;
    }

    /**
     * Configuration of kafka topics to use on event type basis:
     * 
     * Mapping of event type (event class name without "Event" suffix) to kafka topic name.
     * 
     * Default topic of an event is the event type.
     */
    @java.lang.SuppressWarnings("all")
    public Map<String, String> getEventTopics() {
        return this.eventTopics;
    }

    /**
     * Configuration of events to poll
     */
    @java.lang.SuppressWarnings("all")
    public Set<PollingEvents> getPollingEvents() {
        return this.pollingEvents;
    }

    /**
     * Configuration for polling runtime data
     */
    @java.lang.SuppressWarnings("all")
    public void setRuntimeData(final PollingProperties runtimeData) {
        this.runtimeData = runtimeData;
    }

    /**
     * Configuration for polling repository data
     */
    @java.lang.SuppressWarnings("all")
    public void setRepositoryData(final PollingProperties repositoryData) {
        this.repositoryData = repositoryData;
    }

    /**
     * Configuration of kafka topics to use on event type basis:
     * 
     * Mapping of event type (event class name without "Event" suffix) to kafka topic name.
     * 
     * Default topic of an event is the event type.
     */
    @java.lang.SuppressWarnings("all")
    public void setEventTopics(final Map<String, String> eventTopics) {
        this.eventTopics = eventTopics;
    }

    /**
     * Configuration of events to poll
     */
    @java.lang.SuppressWarnings("all")
    public void setPollingEvents(final Set<PollingEvents> pollingEvents) {
        this.pollingEvents = pollingEvents;
    }
}
