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

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorAccountRepository instructorAccountRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @AfterEach
    public void clearDatabase() {
        scheduleRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadSchedule() {
        //Create Course
        String name = "Badminton";
        String description = "One on one coaching";
        int costPerSession = 20;

        Course course = new Course(name, description, costPerSession);
        course = courseRepository.save(course);

        //Create Instructor Account
        String instructorName = "Mahmoud";
        String email = "mahmoud@gmail.com";
        String password = "123";

        InstructorAccount instructorAccount = new InstructorAccount(instructorName, email, password);
        instructorAccount = instructorAccountRepository.save(instructorAccount);

        //Create Session
        Time startTime = Time.valueOf("09:00:00");
        Time endTime = Time.valueOf("11:00:00");
        Date date = Date.valueOf("2024-02-14");

        Session session = new Session(startTime, endTime, date, instructorAccount, course);
        session = sessionRepository.save(session);

        //Create Schedule
        Schedule schedule = new Schedule(session);
        schedule = scheduleRepository.save(schedule);
        int id = schedule.getId();

        //Read customer account from database
        Schedule scheduleDatabase = scheduleRepository.findScheduleById(id);

        //Assertions
        assertNotNull(scheduleDatabase);
        assertEquals(schedule.getSessions(), scheduleDatabase.getSessions());



    }

}
