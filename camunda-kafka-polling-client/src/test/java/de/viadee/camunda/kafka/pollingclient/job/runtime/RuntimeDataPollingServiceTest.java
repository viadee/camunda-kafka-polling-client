package de.viadee.camunda.kafka.pollingclient.job.runtime;

import de.viadee.camunda.kafka.event.ActivityInstanceEvent;
import de.viadee.camunda.kafka.event.HistoryEvent;
import de.viadee.camunda.kafka.event.ProcessInstanceEvent;
import de.viadee.camunda.kafka.pollingclient.config.properties.ApplicationProperties;
import de.viadee.camunda.kafka.pollingclient.service.event.EventService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.LastPolledService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.PollingTimeslice;
import de.viadee.camunda.kafka.pollingclient.service.polling.jdbc.CamundaJdbcPollingServiceImpl;
import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.util.ClockUtil;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Stream;

import static de.viadee.camunda.kafka.pollingclient.job.runtime.RuntimeDataPollingServiceTest.PointOfTime.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@TestInstance(PER_CLASS)
public class RuntimeDataPollingServiceTest {

    enum PointOfTime {

        // @formatter:off
		BEFORE_CUTOFF           (10,  0, 0),
        CUTOFF_TIME             (10, 10, 0),
        WITHIN_PAST_TIMESLICE   (10, 15, 0),
        START_TIME              (10, 20, 0),
		WITHIN_TIMESLICE        (10, 25, 0),
        END_TIME                (10, 30, 0),
        AFTER_TIMESLICE         (10, 35, 0),
        LONG_AFTER_TIMESLICE    (10, 40, 0);
		// @formatter:on

        final Date date;

        PointOfTime(int hour, int minute, int second) {
            date = Date.from(LocalDateTime.of(2018, 11, 30, hour, minute, second).toInstant(ZoneOffset.UTC));
        }
    }

    private CamundaJdbcPollingServiceImpl pollingApiService;
    private RuntimeDataPollingService pollingService;
    private LastPolledService lastPolledService;
    private EventService eventSendService;
    private ApplicationProperties applicationProperties;
    private ProcessEngine processEngine;

    @BeforeEach
    void setup() {
        LogFactory.useSlf4jLogging();
        processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
                                                  .setJobExecutorActivate(false)
                                                  .setHistory(ProcessEngineConfiguration.HISTORY_FULL)
                                                  .setDatabaseSchemaUpdate(
                                                          ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP)
                                                  .buildProcessEngine();

        lastPolledService = mock(LastPolledService.class);
        eventSendService = mock(EventService.class);

        applicationProperties = new ApplicationProperties();
        applicationProperties.setPollingEvents(new HashSet<>(asList(ApplicationProperties.PollingEvents.values())));
        applicationProperties.getRuntimeData().setEnabled(true);

        pollingApiService = new CamundaJdbcPollingServiceImpl(processEngine.getHistoryService(),
                                                              processEngine.getRepositoryService());

        pollingService = new RuntimeDataPollingService(pollingApiService, lastPolledService, eventSendService,
                                                       applicationProperties);
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
        verify(lastPolledService, times(1))
                .updatePollingTimeslice(pollingTimesliceCaptor.capture());

        final PollingTimeslice updatePollingTimeslice = pollingTimesliceCaptor.getValue();
        assertEquals(CUTOFF_TIME.date, updatePollingTimeslice.getCutoffTime());
        assertEquals(START_TIME.date, updatePollingTimeslice.getStartTime());
        assertEquals(END_TIME.date, updatePollingTimeslice.getEndTime());
    }

    @ParameterizedTest(name = "{index}: process start {0} and end {1} => should be polled={2}")
    @MethodSource
    @DisplayName("Polling of finished process instances")
    void pollFinishedProcessInstances(PointOfTime processStart, PointOfTime processEnd, boolean shouldBePolled) {

        // create testdata
        setCurrentTime(BEFORE_CUTOFF);
        processEngine.getRepositoryService()
                     .createDeployment()
                     .addClasspathResource("bpmn/simpleProcess.bpmn")
                     .deploy();

        setCurrentTime(processStart);
        final Map<String, Object> variables = new HashMap<>(); // The process instance carries one process variable, but
        // never uses it
        variables.put("waldo", "here");
        final ProcessInstance processInstance = processEngine.getRuntimeService()
                                                             .startProcessInstanceByKey("simpleProcess", variables);

        setCurrentTime(processEnd);
        processEngine.getRuntimeService()
                     .createMessageCorrelation("pauseMessage")
                     .processInstanceId(processInstance.getId())
                     .correlate();

        // define polling cycle
        when(lastPolledService.getPollingTimeslice())
                .thenReturn(new PollingTimeslice(CUTOFF_TIME.date, START_TIME.date,
                                                 END_TIME.date));

        // perform polling
        pollingService.run();

        // Verify process instance event
        final ArgumentCaptor<HistoryEvent> processInstanceEventCaptor = ArgumentCaptor.forClass(HistoryEvent.class);
        verify(eventSendService, atLeast(shouldBePolled ? 1 : 0))
                .sendEvent(processInstanceEventCaptor.capture());

        final List<String> polledProcessIds = processInstanceEventCaptor.getAllValues()
                                                                        .stream()
                                                                        .filter(event -> event instanceof ProcessInstanceEvent)
                                                                        .map(HistoryEvent::getId)
                                                                        .collect(toList());

        assertEquals(shouldBePolled ? 1 : 0, polledProcessIds.size());
        if (shouldBePolled) {
            assertEquals(processInstance.getProcessInstanceId(), polledProcessIds.get(0));
        }
    }

    static Stream<Arguments> pollFinishedProcessInstances() {
        // @formatter:off
		return Stream.of(
				//        Process start             Process end                 Should be polled?
				arguments(BEFORE_CUTOFF,            CUTOFF_TIME,                false),
                arguments(BEFORE_CUTOFF,            WITHIN_PAST_TIMESLICE,      false),
				arguments(BEFORE_CUTOFF,            START_TIME,                 false),
                arguments(BEFORE_CUTOFF,            WITHIN_TIMESLICE,           false),
				arguments(BEFORE_CUTOFF,            END_TIME,                   false),
                arguments(BEFORE_CUTOFF,            AFTER_TIMESLICE,            false),

				arguments(CUTOFF_TIME,              WITHIN_PAST_TIMESLICE,      false),
                arguments(CUTOFF_TIME,              START_TIME,                 true),
				arguments(CUTOFF_TIME,              WITHIN_TIMESLICE,           true),
                arguments(CUTOFF_TIME,              END_TIME,                   false),
				arguments(CUTOFF_TIME,              AFTER_TIMESLICE,            false),

				arguments(WITHIN_PAST_TIMESLICE,    START_TIME,                 true),
				arguments(WITHIN_PAST_TIMESLICE,    WITHIN_TIMESLICE,           true),
				arguments(WITHIN_PAST_TIMESLICE,    END_TIME,                   false),
				arguments(WITHIN_PAST_TIMESLICE,    AFTER_TIMESLICE,            false),

				arguments(START_TIME,               WITHIN_TIMESLICE,           true),
                arguments(START_TIME,               END_TIME,                   true),
				arguments(START_TIME,               AFTER_TIMESLICE,            true),

				arguments(WITHIN_TIMESLICE,         END_TIME,                   true),
                arguments(WITHIN_TIMESLICE,         AFTER_TIMESLICE,            true),

				arguments(END_TIME,                 AFTER_TIMESLICE,            false),

				arguments(AFTER_TIMESLICE,          LONG_AFTER_TIMESLICE,       false)
        );
		// @formatter:on
    }

    @ParameterizedTest(name = "{index}: process started {0} with activity started {1} and finished {2} => should be polled={3}")
    @MethodSource
    @DisplayName("Polling finished activities of unfinished process")
    void pollFinishedActivitiesOfUnfinishedProcess(PointOfTime processStart, PointOfTime activityStart,
                                                   PointOfTime activityEnd, boolean shouldBePolled) {

        // create testdata
        setCurrentTime(BEFORE_CUTOFF);
        processEngine.getRepositoryService()
                     .createDeployment()
                     .addClasspathResource("bpmn/activityTestProcess.bpmn")
                     .deploy();

        // Start process
        setCurrentTime(processStart);
        final ProcessInstance processInstance = processEngine.getRuntimeService()
                                                             .startProcessInstanceByKey("activityTestProcess");

        // send pause1 message on activityStart to resume and start pause2 activity on
        // given time
        setCurrentTime(activityStart);
        processEngine.getRuntimeService()
                     .createMessageCorrelation("pause1")
                     .processInstanceId(processInstance.getId())
                     .correlate();

        // send pause2 message on activityEnd to resume and end pause2 activity on given
        // time
        setCurrentTime(activityEnd);
        processEngine.getRuntimeService()
                     .createMessageCorrelation("pause2")
                     .processInstanceId(processInstance.getId())
                     .correlate();

        // define polling cycle
        when(lastPolledService.getPollingTimeslice())
                .thenReturn(new PollingTimeslice(CUTOFF_TIME.date, START_TIME.date, END_TIME.date));

        // perform polling
        pollingService.run();

        // Verify process instance event
        final ArgumentCaptor<HistoryEvent> historyEventCaptor = ArgumentCaptor.forClass(HistoryEvent.class);
        verify(eventSendService, atLeast(shouldBePolled ? 1 : 0))
                .sendEvent(historyEventCaptor.capture());

        assertEquals(shouldBePolled ? 1 : 0,
                     historyEventCaptor.getAllValues()
                                       .stream()
                                       .filter(event -> event instanceof ActivityInstanceEvent)
                                       .map(event -> (ActivityInstanceEvent) event)
                                       .filter(event -> "pause2".equals(event.getActivityId())
                                               && activityStart.date.equals(event.getStartTime())
                                               && activityEnd.date.equals(event.getEndTime()))
                                       .count());
    }

    static Stream<Arguments> pollFinishedActivitiesOfUnfinishedProcess() {
        // @formatter:off
		return Stream.of(
				//        Process Started           Activity Start,         Activity End,           Should be polled?
				arguments(BEFORE_CUTOFF,            BEFORE_CUTOFF,          BEFORE_CUTOFF,          false),
				arguments(BEFORE_CUTOFF,            WITHIN_TIMESLICE,       WITHIN_TIMESLICE,       false),

				arguments(CUTOFF_TIME,              CUTOFF_TIME,            CUTOFF_TIME,            false),
				arguments(CUTOFF_TIME,              WITHIN_TIMESLICE,       WITHIN_TIMESLICE,       true),

				arguments(WITHIN_PAST_TIMESLICE,    WITHIN_PAST_TIMESLICE,  WITHIN_PAST_TIMESLICE,  false),
				arguments(WITHIN_PAST_TIMESLICE,    WITHIN_PAST_TIMESLICE,  START_TIME,             true),
				arguments(WITHIN_PAST_TIMESLICE,    WITHIN_PAST_TIMESLICE,  WITHIN_TIMESLICE,       true),
				arguments(WITHIN_PAST_TIMESLICE,    WITHIN_PAST_TIMESLICE,  END_TIME,               false),
				arguments(WITHIN_PAST_TIMESLICE,    WITHIN_PAST_TIMESLICE,  AFTER_TIMESLICE,        false),

				arguments(WITHIN_PAST_TIMESLICE,    START_TIME,             START_TIME,             true),
				arguments(WITHIN_PAST_TIMESLICE,    START_TIME,             WITHIN_TIMESLICE,       true),
				arguments(WITHIN_PAST_TIMESLICE,    START_TIME,             END_TIME,               false),
				arguments(WITHIN_PAST_TIMESLICE,    START_TIME,             AFTER_TIMESLICE,        false),

				arguments(WITHIN_PAST_TIMESLICE,    WITHIN_TIMESLICE,       WITHIN_TIMESLICE,       true),
				arguments(WITHIN_PAST_TIMESLICE,    WITHIN_TIMESLICE,       END_TIME,               false),
				arguments(WITHIN_PAST_TIMESLICE,    WITHIN_TIMESLICE,       AFTER_TIMESLICE,        false),

				arguments(WITHIN_PAST_TIMESLICE,    END_TIME,               END_TIME,               false),
				arguments(WITHIN_PAST_TIMESLICE,    END_TIME,               AFTER_TIMESLICE,        false),

				arguments(WITHIN_PAST_TIMESLICE,    AFTER_TIMESLICE,        AFTER_TIMESLICE,        false),
				arguments(WITHIN_PAST_TIMESLICE,    AFTER_TIMESLICE,        LONG_AFTER_TIMESLICE,   false),

				arguments(WITHIN_PAST_TIMESLICE,    LONG_AFTER_TIMESLICE,   LONG_AFTER_TIMESLICE,   false)
        );
		// @formatter:on
    }

    @ParameterizedTest(name = "{index}: process started {0} with activity started {1} => should be polled={2}")
    @MethodSource
    @DisplayName("Polling unfinished activities of unfinished process")
    void pollUnfinishedActivitiesOfUnfinishedProcess(PointOfTime processStart, PointOfTime activityStart,
                                                     boolean shouldBePolled) {

        // create testdata
        setCurrentTime(BEFORE_CUTOFF);
        processEngine.getRepositoryService()
                     .createDeployment()
                     .addClasspathResource("bpmn/activityTestProcess.bpmn")
                     .deploy();

        // Start process
        setCurrentTime(processStart);
        final ProcessInstance processInstance = processEngine.getRuntimeService()
                                                             .startProcessInstanceByKey("activityTestProcess");

        // send pause1 message on activityStart to resume and start pause2 activity on
        // given time
        setCurrentTime(activityStart);
        processEngine.getRuntimeService()
                     .createMessageCorrelation("pause1")
                     .processInstanceId(processInstance.getId())
                     .correlate();

        // define polling cycle
        when(lastPolledService.getPollingTimeslice())
                .thenReturn(new PollingTimeslice(CUTOFF_TIME.date, START_TIME.date, END_TIME.date));

        // perform polling
        pollingService.run();

        // Verify process instance event
        final ArgumentCaptor<HistoryEvent> historyEventCaptor = ArgumentCaptor.forClass(HistoryEvent.class);
        verify(eventSendService, atLeast(shouldBePolled ? 1 : 0))
                .sendEvent(historyEventCaptor.capture());

        assertEquals(shouldBePolled ? 1 : 0,
                     historyEventCaptor.getAllValues()
                                       .stream()
                                       .filter(event -> event instanceof ActivityInstanceEvent)
                                       .map(event -> (ActivityInstanceEvent) event)
                                       .filter(event -> "pause2".equals(event.getActivityId())
                                               && activityStart.date.equals(event.getStartTime())
                                               && event.getEndTime() == null)
                                       .count());
    }

    static Stream<Arguments> pollUnfinishedActivitiesOfUnfinishedProcess() {
        // @formatter:off
		return Stream.of(
				//        Process Started           Activity Start,         Should be polled?
				arguments(BEFORE_CUTOFF,            BEFORE_CUTOFF,          false),
                arguments(BEFORE_CUTOFF,            WITHIN_TIMESLICE,       false),

				arguments(CUTOFF_TIME,              CUTOFF_TIME,            false),
                arguments(CUTOFF_TIME,              WITHIN_TIMESLICE,       true),

				arguments(WITHIN_PAST_TIMESLICE,    WITHIN_PAST_TIMESLICE,  false),
				arguments(WITHIN_PAST_TIMESLICE,    START_TIME,             true),
				arguments(WITHIN_PAST_TIMESLICE,    WITHIN_TIMESLICE,       true),
				arguments(WITHIN_PAST_TIMESLICE,    END_TIME,               false),
				arguments(WITHIN_PAST_TIMESLICE,    AFTER_TIMESLICE,        false)
        );
		// @formatter:on
    }

    @ParameterizedTest(name = "{index}: process start {0} => should be polled={1}")
    @MethodSource
    @DisplayName("Polling of unfinished process instances")
    void pollUnfinishedProcessInstances(PointOfTime processStart, boolean shouldBePolled) {

        // create testdata
        setCurrentTime(BEFORE_CUTOFF);
        processEngine.getRepositoryService()
                     .createDeployment()
                     .addClasspathResource("bpmn/simpleProcess.bpmn")
                     .deploy();

        setCurrentTime(processStart);
        final ProcessInstance processInstance = processEngine.getRuntimeService()
                                                             .startProcessInstanceByKey("simpleProcess");

        // define polling cycle
        when(lastPolledService.getPollingTimeslice())
                .thenReturn(new PollingTimeslice(CUTOFF_TIME.date, START_TIME.date, END_TIME.date));

        // perform polling
        pollingService.run();

        // Verify process instance event
        final ArgumentCaptor<HistoryEvent> processInstanceEventCaptor = ArgumentCaptor.forClass(HistoryEvent.class);
        verify(eventSendService, atLeast(shouldBePolled ? 1 : 0))
                .sendEvent(processInstanceEventCaptor.capture());

        final List<String> polledProcessIds = processInstanceEventCaptor.getAllValues()
                                                                        .stream()
                                                                        .filter(event -> event instanceof ProcessInstanceEvent)
                                                                        .map(HistoryEvent::getId)
                                                                        .collect(toList());

        assertEquals(shouldBePolled ? 1 : 0, polledProcessIds.size());
        if (shouldBePolled) {
            assertEquals(processInstance.getProcessInstanceId(), polledProcessIds.get(0));
        }
    }

    static Stream<Arguments> pollUnfinishedProcessInstances() {
        // @formatter:off
		return Stream.of(
				//        Process start             Should be polled?
				arguments(BEFORE_CUTOFF,            false),
                arguments(CUTOFF_TIME,              false),
                arguments(WITHIN_PAST_TIMESLICE,    false),
				arguments(START_TIME,               true),
                arguments(WITHIN_TIMESLICE,         true),
                arguments(END_TIME,                 false),
				arguments(AFTER_TIMESLICE,          false)
        );
		// @formatter:on
    }

    private static void setCurrentTime(PointOfTime time) {
        ClockUtil.setCurrentTime(time.date);
    }

}
