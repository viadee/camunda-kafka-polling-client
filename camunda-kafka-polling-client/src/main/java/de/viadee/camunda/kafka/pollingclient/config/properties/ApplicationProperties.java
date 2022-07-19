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

    /**
     * Timeout to wait while sending an polling event to kafka
     */
    private long kafkaSendTimeoutInSeconds = 60;

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
        IDENTITY_LINKS_UNFINISHED_ACTIVITIES,
        IDENTITY_LINKS_FINISHED_ACTIVITIES,
        DECISION_DEFINITION
    }

    public long getKafkaSendTimeoutInSeconds() {
        return kafkaSendTimeoutInSeconds;
    }

    public void setKafkaSendTimeoutInSeconds(long kafkaSendTimeoutInSeconds) {
        this.kafkaSendTimeoutInSeconds = kafkaSendTimeoutInSeconds;
    }

    public PollingProperties getRuntimeData() {
        return runtimeData;
    }

    public void setRuntimeData(PollingProperties runtimeData) {
        this.runtimeData = runtimeData;
    }

    public PollingProperties getRepositoryData() {
        return repositoryData;
    }

    public void setRepositoryData(PollingProperties repositoryData) {
        this.repositoryData = repositoryData;
    }

    public Map<String, String> getEventTopics() {
        return eventTopics;
    }

    public void setEventTopics(Map<String, String> eventTopics) {
        this.eventTopics = eventTopics;
    }

    public Set<PollingEvents> getPollingEvents() {
        return pollingEvents;
    }

    public void setPollingEvents(Set<PollingEvents> pollingEvents) {
        this.pollingEvents = pollingEvents;
    }
}
