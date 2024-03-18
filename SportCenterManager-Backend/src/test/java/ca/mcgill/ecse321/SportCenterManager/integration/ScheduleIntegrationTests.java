package ca.mcgill.ecse321.SportCenterManager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Time;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportCenterManager.dto.ErrorDto;
import ca.mcgill.ecse321.SportCenterManager.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ScheduleIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    private final Time VALID_START_TIME = new Time(1);
    private final Time VALID_END_TIME = new Time(3600000);
    private final Time INVALID_START_TIME = null;
    private final Time INVALID_END_TIME = null;
    
    @Test 
    @Order(1)
    public void validFirstScheduleUpdate(){
        //Set up
        ScheduleRequestDto request = new ScheduleRequestDto(VALID_START_TIME, VALID_END_TIME);
        
        //Act
        ResponseEntity<Schedule> response = client.postForEntity("/schedule",request,Schedule.class);
        
        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        Schedule createdSchedule = response.getBody();
        assertNotNull(createdSchedule);
        assertEquals(VALID_START_TIME,createdSchedule.getOpeningHours());
        assertEquals(VALID_END_TIME,createdSchedule.getClosingHours());
    }

    @Test 
    @Order(2)
    public void validScheduleUpdate(){
        //Set up
        ScheduleRequestDto request = new ScheduleRequestDto(VALID_START_TIME, VALID_END_TIME);
        
        //Act
        ResponseEntity<Schedule> response = client.postForEntity("/schedule",request,Schedule.class);
        
        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        Schedule createdSchedule = response.getBody();
        assertNotNull(createdSchedule);
        assertEquals(VALID_START_TIME,createdSchedule.getOpeningHours());
        assertEquals(VALID_END_TIME,createdSchedule.getClosingHours());

    }


    @Test 
    @Order(3)
    public void invalidScheduleUpdateStartEmpty(){
        ScheduleRequestDto request = new ScheduleRequestDto(INVALID_START_TIME, VALID_END_TIME);
        
        ResponseEntity<ErrorDto> response = client.postForEntity("/schedule",request,ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        ErrorDto createdSchedule = response.getBody();
        assertNotNull(createdSchedule);
        assertEquals("Cannot have an empty time",createdSchedule.getMessage());

    }
    @Test
    @Order(4) 
    public void invalidScheduleUpdateEndEmpty(){
        ScheduleRequestDto request = new ScheduleRequestDto(VALID_START_TIME, INVALID_END_TIME);
        
        ResponseEntity<ErrorDto> response = client.postForEntity("/schedule",request,ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        ErrorDto createdSchedule = response.getBody();
        assertNotNull(createdSchedule);
        assertEquals("Cannot have an empty time",createdSchedule.getMessage());

    }
    @Test
    @Order(5) 
    public void invalidScheduleUpdateBothEmpty(){
        ScheduleRequestDto request = new ScheduleRequestDto(INVALID_START_TIME, INVALID_END_TIME);
        
        ResponseEntity<ErrorDto> response = client.postForEntity("/schedule",request,ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        ErrorDto createdSchedule = response.getBody();
        assertNotNull(createdSchedule);
        assertEquals("Cannot have an empty time",createdSchedule.getMessage());

    }

    @Test
    @Order(6)
    public void invalidScheduleUpdateCombo(){
        ScheduleRequestDto request = new ScheduleRequestDto(VALID_END_TIME, VALID_START_TIME);
        
        ResponseEntity<ErrorDto> response = client.postForEntity("/schedule",request,ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        ErrorDto createdSchedule = response.getBody();
        assertNotNull(createdSchedule);
        assertEquals("Cannot have closing hour occur before opening hour",createdSchedule.getMessage());

    }
    
}
