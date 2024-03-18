package ca.mcgill.ecse321.SportCenterManager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.sql.Time;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.ScheduleRepository;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;

@SpringBootTest
public class ScheduleTests {
    @Mock 
    private ScheduleRepository scheduleRepo;
    @InjectMocks
    private ScheduleService scheduleService;
    
    @Test
    public void testValidScheduleUpdate(){
        Time start = new Time(0);
        Time end = new Time(3600000);
        int id = 1;
    
        Schedule test_schedule = new Schedule(start,end);

        when(scheduleRepo.findScheduleById(id)).thenReturn(test_schedule);

        Schedule schedule = scheduleService.updateSchedule(id,start,end);

        assertNotNull(schedule);
        assertEquals(test_schedule.getOpeningHours(),schedule.getOpeningHours());
        assertEquals(test_schedule.getClosingHours(),test_schedule.getClosingHours());
    }

    @Test
    public void testEmptyOpening(){
        Time start = null;
        Time end = new Time(3600000);
        int id = 1;
    
        Schedule test_schedule = new Schedule(start,end);

        when(scheduleRepo.findScheduleById(id)).thenReturn(test_schedule);

        Schedule schedule = scheduleService.updateSchedule(id,start,end);

        assertNotNull(schedule);
        assertEquals(test_schedule.getOpeningHours(),schedule.getOpeningHours());
        assertEquals(test_schedule.getClosingHours(),test_schedule.getClosingHours());

    }
    
    @Test
    public void testEmptyClosing(){

    }
    @Test
    public void testBothEmpty(){

    }
    @Test
    public void testInvalidCombo(){

    }
    
}
