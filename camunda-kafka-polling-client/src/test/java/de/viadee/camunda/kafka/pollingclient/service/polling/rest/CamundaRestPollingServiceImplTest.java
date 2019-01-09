package de.viadee.camunda.kafka.pollingclient.service.polling.rest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.viadee.camunda.kafka.event.ProcessInstanceEvent;
import de.viadee.camunda.kafka.pollingclient.config.properties.CamundaRestPollingProperties;
import de.viadee.camunda.kafka.pollingclient.service.polling.rest.response.GetHistoricProcessInstanceResponse;

import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
		when(mockedRestTemplate.exchange(any(), any(), any(), (ParameterizedTypeReference) any(), (Map<String, Object>) any())).thenReturn(mockedResponseEntity);
		
		// call functions
		CamundaRestPollingServiceImpl c = new CamundaRestPollingServiceImpl(prop, mockedRestTemplate);				
		Iterable<ProcessInstanceEvent> pieList = c.pollFinishedProcessInstances(startedAfter, startedBefore, finishedAfter);
		
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
		
		List<GetHistoricProcessInstanceResponse> emptyResultList = new ArrayList();
		
		// mocking			
		ResponseEntity mockedResponseEntity = mock(ResponseEntity.class);
		when(mockedResponseEntity.getBody()).thenReturn(emptyResultList);
		when(mockedRestTemplate.exchange(any(), any(), any(), (ParameterizedTypeReference) any(), (Map<String, Object>) any())).thenReturn(mockedResponseEntity);
		
		// call functions
		CamundaRestPollingServiceImpl c = new CamundaRestPollingServiceImpl(prop, mockedRestTemplate);				
		Iterable<ProcessInstanceEvent> pieList = c.pollFinishedProcessInstances(startedAfter, startedBefore, finishedAfter);
		
		assertFalse(pieList.iterator().hasNext());
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
