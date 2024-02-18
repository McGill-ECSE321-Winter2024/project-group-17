package ca.mcgill.ecse321.SportCenterManager.dao;

import ca.mcgill.ecse321.SportCenterManager.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest
public class ScheduleRepositoryTests {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @AfterEach
    public void clearDatabase() {
        scheduleRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadSchedule() {
        //Create Schedule
        Time openingHours = Time.valueOf("08:00:00");
        Time closingHours = Time.valueOf("20:00:00");

        Schedule schedule = new Schedule(openingHours, closingHours);
        schedule = scheduleRepository.save(schedule);
        int id = schedule.getId();

        //Read customer account from database
        Schedule scheduleDatabase = scheduleRepository.findScheduleById(id);

        //Assertions
        assertNotNull(scheduleDatabase);
        assertEquals(openingHours, scheduleDatabase.getOpeningHours());
        assertEquals(closingHours, scheduleDatabase.getClosingHours());
    }
}
