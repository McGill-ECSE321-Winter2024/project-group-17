package ca.mcgill.ecse321.SportCenterManager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportCenterManager.dao.ScheduleRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.exception.ServiceException;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.model.Session;

@SpringBootTest
public class ScheduleTests {
    @Mock 
    private ScheduleRepository scheduleRepo;
    @Mock
    private SessionRepository sessionRepo;
    @InjectMocks
    private ScheduleService scheduleService;

    @SuppressWarnings("null")
    @Test
    public void testValidFirstScheduleUpdate(){
        Time start = new Time(0);
        Time end = new Time(3600000);
    
        Schedule test_schedule = new Schedule(start,end);

        when(scheduleRepo.count()).thenReturn(0L);
        when(scheduleRepo.save(any(Schedule.class))).thenReturn(test_schedule);

        Schedule schedule = scheduleService.updateSchedule(start,end);
        
        assertNotNull(schedule);
        assertEquals(test_schedule.getOpeningHours(),schedule.getOpeningHours());
        assertEquals(test_schedule.getClosingHours(),test_schedule.getClosingHours());
        verify(scheduleRepo, times(1)).save(any(Schedule.class));
    }
    @SuppressWarnings("null")
    @Test
    public void testValidScheduleUpdate(){
        Time start = new Time(0);
        Time end = new Time(3600000);
        
        Schedule oldSchedule = new Schedule(new Time(0), new Time(2000000));
        Schedule test_schedule = new Schedule(start,end);
        List<Schedule> schedules = new ArrayList<Schedule>();
        schedules.add(oldSchedule);
        
        when(scheduleRepo.count()).thenReturn(1L);
        when(scheduleRepo.findAll()).thenReturn((Iterable<Schedule>) schedules);
        when(scheduleRepo.save(any(Schedule.class))).thenReturn(test_schedule);
        when(sessionRepo.save(any(Session.class))).thenReturn(null);

        Schedule schedule = scheduleService.updateSchedule(start,end);
        

        assertNotNull(schedule);
        assertEquals(test_schedule.getOpeningHours(),schedule.getOpeningHours());
        assertEquals(test_schedule.getClosingHours(),test_schedule.getClosingHours());
        verify(scheduleRepo, times(2)).save(any(Schedule.class));
    }

    @Test
    public void testEmptyOpening(){
        Time start = null;
        Time end = new Time(3600000);


        ServiceException e = assertThrows(ServiceException.class,()->scheduleService.updateSchedule(start, end));
        assertEquals("Cannot have an empty time",e.getMessage());

    }
    
    @Test
    public void testEmptyClosing(){
        Time start = new Time(1);
        Time end = null;
        
        ServiceException e = assertThrows(ServiceException.class,()->scheduleService.updateSchedule(start, end));
        assertEquals("Cannot have an empty time",e.getMessage());

    }
    @Test
    public void testBothEmpty(){
        Time start = null;
        Time end = null;
        
        ServiceException e = assertThrows(ServiceException.class,()->scheduleService.updateSchedule(start, end));
        assertEquals("Cannot have an empty time",e.getMessage());

    }
    @Test
    public void testInvalidCombo(){
        Time start = new Time(3600000);
        Time end = new Time(1);
        
        ServiceException e = assertThrows(ServiceException.class,()->scheduleService.updateSchedule(start, end));
        assertEquals("Cannot have closing hour occur before opening hour",e.getMessage());

    }

    @Test
    public void getExistingSchedule(){
        Time start = new Time(0);
        Time end = new Time(3600000);
    
        Schedule test_schedule = new Schedule(start,end);
        Schedule[] test_schedules = {test_schedule};
        Iterable<Schedule> schedules = Arrays.asList(test_schedules);

        when(scheduleRepo.count()).thenReturn(1L);
        when((scheduleRepo.findAll())).thenReturn(schedules);

        Schedule schedule = scheduleService.findSchedule();
        
        assertNotNull(schedule);
        assertEquals(test_schedule.getOpeningHours(),schedule.getOpeningHours());
        assertEquals(test_schedule.getClosingHours(),schedule.getClosingHours());
    }
    @Test
    public void getNonexistingSchedule(){
        when(scheduleRepo.count()).thenReturn(0L);
        ServiceException e = assertThrows(ServiceException.class,()->scheduleService.findSchedule());
        assertEquals("The opening hours have not been set!",e.getMessage());
       
    }
    
}
