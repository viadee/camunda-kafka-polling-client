package de.viadee.camunda.kafka.pollingclient.job.repository;

import de.viadee.camunda.kafka.event.ProcessDefinitionEvent;
import de.viadee.camunda.kafka.pollingclient.config.properties.ApplicationProperties;
import de.viadee.camunda.kafka.pollingclient.service.event.EventService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.LastPolledService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.PollingTimeslice;
import de.viadee.camunda.kafka.pollingclient.service.polling.jdbc.CamundaJdbcPollingServiceImpl;
import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.util.ClockUtil;
import org.camunda.bpm.engine.repository.Deployment;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Stream;

import static de.viadee.camunda.kafka.pollingclient.job.repository.RepositoryDataPollingServiceTest.PointOfTime.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@TestInstance(PER_CLASS)
public class RepositoryDataPollingServiceTest {

    enum PointOfTime {

        // @formatter:off
        BEFORE_CUTOFF         (10,  0, 0),
        CUTOFF_TIME           (10, 10, 0),
        WITHIN_PAST_TIMESLICE (10, 15, 0),
        START_TIME            (10, 20, 0),
        WITHIN_TIMESLICE      (10, 25, 0),
        END_TIME              (10, 30, 0),
        AFTER_TIMESLICE       (10, 35, 0),
        LONG_AFTER_TIMESLICE  (10, 40, 0);
        // @formatter:on

        final Date date;

        PointOfTime(int hour, int minute, int second) {
            this.date = Date.from(LocalDateTime.of(2018, 11, 30, hour, minute, second).toInstant(ZoneOffset.UTC));
        }
    }

    private CamundaJdbcPollingServiceImpl pollingApiService;
    private RepositoryDataPollingService pollingService;
    private LastPolledService lastPolledService;
    private EventService eventSendService;
    private ApplicationProperties applicationProperties;
    private ProcessEngine processEngine;

    @BeforeEach
    void setup() {
        LogFactory.useSlf4jLogging();
        processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .setJobExecutorActivate(false)
                .setHistory(ProcessEngineConfiguration.HISTORY_FULL)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP)
                .buildProcessEngine();

        lastPolledService = mock(LastPolledService.class);
        eventSendService = mock(EventService.class);

        applicationProperties = new ApplicationProperties();
        applicationProperties.setPollingEvents(new HashSet<>(asList(ApplicationProperties.PollingEvents.values())));
        applicationProperties.getRepositoryData().setEnabled(true);

        pollingApiService = new CamundaJdbcPollingServiceImpl(
                processEngine.getHistoryService(),
                processEngine.getRepositoryService(), processEngine.getTaskService());

        pollingService = new RepositoryDataPollingService(pollingApiService, lastPolledService, eventSendService, applicationProperties);
    }

    @AfterEach
    void cleanup() {
        processEngine.close();
        ClockUtil.reset();
    }


    @Test
    @DisplayName("Update of timeslice after polling")
    void updatePollingTimeslice() {

        // define polling cycle
        when(lastPolledService.getPollingTimeslice())
                .thenReturn(new PollingTimeslice(CUTOFF_TIME.date, START_TIME.date, END_TIME.date));

        // perform polling
        pollingService.run();

        // verify timeslice update
        final ArgumentCaptor<PollingTimeslice> pollingTimesliceCaptor = ArgumentCaptor.forClass(PollingTimeslice.class);
        verify(lastPolledService, times(1)).updatePollingTimeslice(pollingTimesliceCaptor.capture());

        final PollingTimeslice updatePollingTimeslice = pollingTimesliceCaptor.getValue();
        Assertions.assertEquals(CUTOFF_TIME.date, updatePollingTimeslice.getCutoffTime());
        Assertions.assertEquals(START_TIME.date, updatePollingTimeslice.getStartTime());
        Assertions.assertEquals(END_TIME.date, updatePollingTimeslice.getEndTime());
    }



    @ParameterizedTest(name = "{index}: deployment time {0} => should be polled={1}")
    @MethodSource
    @DisplayName("Polling of process definitions")
    public void pollProcessDefinitions(PointOfTime deploymentTime, boolean shouldBePolled) {

        // create testdata
        setCurrentTime(deploymentTime);
        final Deployment deployment = processEngine
                .getRepositoryService()
                .createDeployment()
                .addClasspathResource("bpmn/simpleProcess.bpmn")
                .deploy();

        // define polling cycle
        when(lastPolledService.getPollingTimeslice())
                .thenReturn(new PollingTimeslice(CUTOFF_TIME.date, START_TIME.date, END_TIME.date));

        // perform polling
        pollingService.run();

        // Verify process instance event
        final ArgumentCaptor<ProcessDefinitionEvent> processDefinitionEventCaptor = ArgumentCaptor.forClass(ProcessDefinitionEvent.class);
        verify(eventSendService, times(shouldBePolled ? 1 : 0)).sendEvent(processDefinitionEventCaptor.capture());
    }

    static Stream<Arguments> pollProcessDefinitions() {
        // @formatter:off
        return Stream.of(
                //        Process start          Should be polled?
                arguments(BEFORE_CUTOFF,         false),
                arguments(CUTOFF_TIME,           false),
                arguments(WITHIN_PAST_TIMESLICE, false),
                arguments(START_TIME,            true),
                arguments(WITHIN_TIMESLICE,      true),
                arguments(END_TIME,              false),
                arguments(AFTER_TIMESLICE,       false)
        );
        // @formatter:on
    }


    private static void setCurrentTime(PointOfTime time) {
        ClockUtil.setCurrentTime(time.date);
    }
}
