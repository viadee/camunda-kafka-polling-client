package de.viadee.camunda.kafka.pollingclient.service.polling.rest;

import de.viadee.camunda.kafka.event.DecisionInstanceEvent;
import de.viadee.camunda.kafka.event.ProcessInstanceEvent;
import de.viadee.camunda.kafka.pollingclient.config.properties.CamundaRestPollingProperties;
import de.viadee.camunda.kafka.pollingclient.service.polling.rest.response.*;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class CamundaRestPollingServiceImplTest {

    RestTemplate mockedRestTemplate = mock(RestTemplate.class);
    Date startedAfter = parseDate("2012-10-01T09:45:00.000UTC+00:00");
    Date startedBefore = parseDate("2019-10-01T09:45:00.000UTC+00:00");
    Date finishedAfter = parseDate("2012-10-01T09:45:00.000UTC+00:00");

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    void pollFinishedProcessInstancesWithEmptyList() {

        // create CamundaRestPollingProperties
        CamundaRestPollingProperties prop = new CamundaRestPollingProperties();
        prop.setPassword("XY");
        prop.setUrl("XY");
        prop.setUsername("XY");

        List<GetHistoricProcessInstanceResponse> emptyResultList = new ArrayList();

        // mocking
        ResponseEntity mockedResponseEntity = mock(ResponseEntity.class);
        when(mockedResponseEntity.getBody()).thenReturn(emptyResultList);
        when(mockedRestTemplate.exchange(any(), any(), any(), (ParameterizedTypeReference) any(),
                                         (Map<String, Object>) any())).thenReturn(mockedResponseEntity);

        // call functions
        CamundaRestPollingServiceImpl c = new CamundaRestPollingServiceImpl(prop, mockedRestTemplate);
        Iterable<ProcessInstanceEvent> pieList = c.pollFinishedProcessInstances(startedAfter, startedBefore,
                                                                                finishedAfter);

        assertFalse(pieList.iterator().hasNext());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    void pollFinishedProcessInstances() {

        // create CamundaRestPollingProperties
        CamundaRestPollingProperties prop = new CamundaRestPollingProperties();
        prop.setPassword("XY");
        prop.setUrl("XY");
        prop.setUsername("XY");

        // fill list with two events: one before start time and one exactly at start
        // time
        Date startTime = parseDate("2009-11-02T09:45:00.000UTC+00:00");

        List<GetHistoricProcessInstanceResponse> resultList = new ArrayList();
        GetHistoricProcessInstanceResponse g = new GetHistoricProcessInstanceResponse();
        g.setId("123");
        g.setStartTime(startTime);
        resultList.add(g);
        GetHistoricProcessInstanceResponse g2 = new GetHistoricProcessInstanceResponse();
        g2.setId("124");
        g2.setStartTime(startedBefore);
        resultList.add(g2);

        // mocking
        ResponseEntity mockedResponseEntity = mock(ResponseEntity.class);
        when(mockedResponseEntity.getBody()).thenReturn(resultList);
        when(mockedRestTemplate.exchange(any(), any(), any(), (ParameterizedTypeReference) any(),
                                         (Map<String, Object>) any())).thenReturn(mockedResponseEntity);

        // call functions
        CamundaRestPollingServiceImpl c = new CamundaRestPollingServiceImpl(prop, mockedRestTemplate);
        Iterable<ProcessInstanceEvent> pieIterator = c.pollFinishedProcessInstances(startedAfter, startedBefore,
                                                                                    finishedAfter);

        Iterator<ProcessInstanceEvent> iter = pieIterator.iterator();

        assertEquals("123", iter.next().getId());
        assertFalse(iter.hasNext());
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    void pollDecisionInstances() {

        // create CamundaRestPollingProperties
        CamundaRestPollingProperties prop = new CamundaRestPollingProperties();
        prop.setPassword("XY");
        prop.setUrl("XY");
        prop.setUsername("XY");

        Date startTime = parseDate("2009-11-02T09:45:00.000UTC+00:00");

        GetHistoricActivityInstanceRespone a = new GetHistoricActivityInstanceRespone();
        a.setActivityId("1");
        a.setStartTime(startTime);

        List<GetHistoricDecisionInstanceInputResponse> inputList = new ArrayList<>();
        GetHistoricDecisionInstanceInputResponse i = new GetHistoricDecisionInstanceInputResponse();
        i.setId("1");
        i.setValue("100");
        inputList.add(i);

        List<GetHistoricDecisionInstanceOutputResponse> outputList = new ArrayList<>();
        GetHistoricDecisionInstanceOutputResponse o = new GetHistoricDecisionInstanceOutputResponse();
        o.setId("1");
        o.setValue("true");
        outputList.add(o);

        List<GetHistoricDecisionInstanceResponse> decisionInstanceList = new ArrayList();
        GetHistoricDecisionInstanceResponse d = new GetHistoricDecisionInstanceResponse();
        d.setId("123");
        d.setActivityId(a.getActivityId());
        d.setInputs(inputList);
        d.setOutputs(outputList);
        decisionInstanceList.add(d);

        // mocking
        ResponseEntity mockedResponseEntity = mock(ResponseEntity.class);
        when(mockedResponseEntity.getBody()).thenReturn(decisionInstanceList);
        when(mockedRestTemplate.exchange(any(), any(), any(), (ParameterizedTypeReference) any(),
                (Map<String, Object>) any())).thenReturn(mockedResponseEntity);

        // call functions
        CamundaRestPollingServiceImpl c = new CamundaRestPollingServiceImpl(prop, mockedRestTemplate);
        Iterable<DecisionInstanceEvent> pieIterator = c.pollDecisionInstances(a.getActivityId());

        Iterator<DecisionInstanceEvent> iter = pieIterator.iterator();

        assertEquals("123", iter.next().getId());
        assertFalse(iter.hasNext());

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    void diffSourceTimeZone() throws ParseException {

        final String noTimeZonePattern = "yyyy-MM-dd'T'HH:mm:ss";
        final String testDateStr = "2012-10-01T09:45:00";

        final String simulatedLocalTimeZone = "GMT";
        final long simulatedHoursDiff = 3;
        final String simulatedSourceTimeZone = "GMT+" + simulatedHoursDiff;

        DateFormat noTimeZoneDateFormat = new SimpleDateFormat(noTimeZonePattern);
        noTimeZoneDateFormat.setTimeZone(TimeZone.getTimeZone(simulatedLocalTimeZone));

        Date localDate = noTimeZoneDateFormat.parse(testDateStr);

        // create CamundaRestPollingProperties
        CamundaRestPollingProperties prop = new CamundaRestPollingProperties();
        prop.setSourceTimeZone(simulatedSourceTimeZone);
        CamundaRestPollingServiceImpl camundaRestPollingService = new CamundaRestPollingServiceImpl(prop,
                                                                                                    mockedRestTemplate);
        String sourceFormatedDateStr = camundaRestPollingService.formatDate(localDate);

        // source Date without TimeZone information
        Date sourceDate = noTimeZoneDateFormat.parse(sourceFormatedDateStr);

        long diffInMillies = Math.abs(localDate.getTime() - sourceDate.getTime());
        long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        assertEquals(diff, simulatedHoursDiff);

    }

    // Generate date in needed format
    public static Date parseDate(String date) {
        final String API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        try {
            return new SimpleDateFormat(API_DATE_FORMAT).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

}
