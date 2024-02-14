package ca.mcgill.ecse321.SportCenterManager.dao;

import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
  private InstructorAccountRepository instructorAccountRepository;
  private CourseRepository courseRepository;

  @AfterEach
  public void clearDatabase(){
    sessionRepository.deleteAll();
    //instructorAccountRepository.deleteAll();
    courseRepository.deleteAll();
  }

  public void testPersistAndLoadSession(){
    // Create InstructorAccount
    String instructorName = "Mahmoud";
    String email = "mahmoud@gmail.com";
    String password = "123";
    int instructorId = 0; //might change

    InstructorAccount instructorAccount = new InstructorAccount(instructorName, email, password, instructorId);
    //instructorAccount = instructorAccountRepository.save(instructorAccount);

    // Create Course
    String courseName = "Soccer";
    String description = "kick";
    int costPerSession = 20;
    int courseId = 0; //might change

    Course course = new Course(courseName, description, costPerSession, courseId);
    course = courseRepository.save(course);

    // Create Session
    Time startTime = Time.valueOf("08:00:00");
    Time endTime = Time.valueOf("10:00:00");
    Date date = Date.valueOf("2024-02-13");
    int sessionId = 0; //might change

    Session session = new Session(startTime, endTime, date, sessionId, instructorAccount, course);
    session = sessionRepository.save(session);
    sessionId = session.getId();

    // Read session from database
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
