package ca.mcgill.ecse321.SportCenterManager.dao;

import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.model.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.sql.Time;

@SpringBootTest
public class SessionRepositoryTests {
  @Autowired
  private SessionRepository sessionRepository;
  @Autowired
  private CourseRepository courseRepository;
  @Autowired
  private InstructorAccountRepository instructorAccountRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @BeforeEach
  @AfterEach
  public void clearDatabase(){
    sessionRepository.deleteAll();
    instructorAccountRepository.deleteAll();
    courseRepository.deleteAll();

  }
  @Test
  public void testPersistAndLoadSession(){
    // Create InstructorAccount
    String instructorName = "Mahmoud";
    String email = "mahmoud@gmail.com";
    String password = "123";

    InstructorAccount instructorAccount = new InstructorAccount(instructorName, email, password);
    instructorAccount = instructorAccountRepository.save(instructorAccount);

    // Create Course
    String courseName = "Soccer";
    String description = "kick";
    int costPerSession = 20;

    Course course = new Course(courseName, description, costPerSession);
    course = courseRepository.save(course);

    //Create a Schedule
    Time openingHours = Time.valueOf("08:00:00");
    Time closingHours = Time.valueOf("21:00:00");
    Schedule schedule = new Schedule(openingHours,closingHours);
    schedule = scheduleRepository.save(schedule);

    // Create Session
    Time startTime = Time.valueOf("08:00:00");
    Time endTime = Time.valueOf("10:00:00");
    Date date = Date.valueOf("2024-02-13");

    Session session = new Session(startTime, endTime, date, instructorAccount, course, schedule);
    session = sessionRepository.save(session);
    int sessionId = session.getId();

    // Read Session from database
    Session sessionFromDb = sessionRepository.findSessionById(sessionId);

    // Assertions
    assertNotNull(sessionFromDb);
    assertNotNull(sessionFromDb.getInstructorAccount());
    assertNotNull(sessionFromDb.getCourse());
    assertEquals(startTime, sessionFromDb.getStartTime());
    assertEquals(endTime, sessionFromDb.getEndTime());
    assertEquals(date, sessionFromDb.getDate());
  }
}
