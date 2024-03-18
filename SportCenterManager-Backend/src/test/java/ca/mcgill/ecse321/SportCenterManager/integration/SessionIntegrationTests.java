package ca.mcgill.ecse321.SportCenterManager.integration;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import ca.mcgill.ecse321.SportCenterManager.dto.*;
import org.hibernate.Session;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.InstructorAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.ScheduleRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SessionIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private SessionRepository sessionRepo;


    private final Time validStartTime = Time.valueOf("10:00:00");
    private final Time validEndTime = Time.valueOf("12:00:00");
    private final Time invalidEndTime = Time.valueOf("09:00:00");
    private final Date validDate = Date.valueOf("2024-04-15");
    private final Date invalidDate = Date.valueOf("2024-03-15");
    private final Course validCourse = new Course("yoga", "Beginner class", 44 );
    private final InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
    private final Schedule schedule = new Schedule(Time.valueOf("8:00:00"), Time.valueOf("13:00:00"));

    private final Time modifiedStartTime = Time.valueOf("11:00:00");
    private final Time modifiedEndTime = Time.valueOf("14:00:00");

    private final Date modifiedDate = Date.valueOf("2024-05-15");
    private final Course modifiedCourse = new Course("zumba", "Intermediate class", 54 );

    private final InstructorAccount modifiedInstructor = new InstructorAccount("James", "James@gmail.com", "abcd");
    private final Schedule modifiedSchedule = new Schedule(Time.valueOf("10:00:00"), Time.valueOf("15:00:00"));
    private int validId;

    @BeforeAll
    @AfterAll
    public void clearDatabase(){
        sessionRepo.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateValidSession(){
        SessionRequestDto request = new SessionRequestDto(validStartTime,validEndTime,validDate,instructor,validCourse,schedule);
        String url="/sessions";

        //Execution
        ResponseEntity<SessionResponseDto> response = client.postForEntity(url, request, SessionResponseDto.class);

        //Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        SessionResponseDto createdSession = response.getBody();
        assertNotNull(createdSession);
        assertEquals(validStartTime, createdSession.getStartTime());
        assertEquals(validEndTime, createdSession.getEndTime());
        assertEquals(validDate, createdSession.getDate());
        assertEquals(instructor, createdSession.getInstructor());
        assertEquals(validCourse, createdSession.getCourse());
        assertEquals(schedule, createdSession.getSchedule());

        assertNotNull(createdSession.getId());
        assertTrue(createdSession.getId() > 0, "Response should have a positive id.");
        //Check the id

        this.validId= createdSession.getId();
    }

    @Test
    @Order(2)
    public void testGetSpecificSession(){
        //Setup
        String url = "/sessions/" + this.validId;

        //Execution
        ResponseEntity<SessionResponseDto> response = client.getForEntity(url, SessionResponseDto.class);

        //Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SessionResponseDto session = response.getBody();
        assertNotNull(session);
        assertEquals(validStartTime, session.getStartTime());
        assertEquals(validEndTime, session.getEndTime());
        assertEquals(validDate, session.getDate());
        assertEquals(instructor, session.getInstructor());
        assertEquals(validCourse, session.getCourse());
        assertEquals(schedule, session.getSchedule());
        assertEquals(this.validId, session.getId());
    }

    @Test
    @Order(3)
    public void testGetSpecificInvalidSession(){
        //Setup
        int invalidId = this.validId + 1;
        String url = "/sessions/" + invalidId;

        //Execution
        ResponseEntity<IllegalArgumentException> response = client.getForEntity(url, IllegalArgumentException.class);

        //assertions
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);
        assertEquals("Session with inputted id is not found.", error.getMessage());
    }

    @Test
    @Order(4)
    public void testCreateInvalidSessionTime(){
        //setup
        SessionRequestDto request = new SessionRequestDto(validStartTime,invalidEndTime,validDate,instructor,validCourse,schedule);
        String url = "/sessions";

        //Execution
        ResponseEntity<IllegalArgumentException> response = client.postForEntity(url, request, IllegalArgumentException.class);

        //Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        IllegalArgumentException error = response.getBody();//What does this line do?
        assertNotNull(error);
        assertEquals("End time must be be after the start time.", error.getMessage());
    }

    @Test
    @Order(5)
    public void testCreateInvalidSessionDate(){
        //setup
        SessionRequestDto request = new SessionRequestDto(validStartTime,validEndTime,invalidDate,instructor,validCourse,schedule);
        String url = "/sessions";

        //Execution
        ResponseEntity<IllegalArgumentException> response = client.postForEntity(url, request, IllegalArgumentException.class);

        //Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        IllegalArgumentException error = response.getBody();//What does this line do?
        assertNotNull(error);
        assertEquals("Cannot create a session on date that has passed.", error.getMessage());
    }

    @Test
    @Order(6)
    public void testModifyValidSession(){
        //setup
        SessionRequestDto request = new SessionRequestDto(modifiedStartTime, modifiedEndTime, modifiedDate,modifiedInstructor,modifiedCourse,modifiedSchedule);
        String url = "/sessions/" + this.validId;

        //execution
        client.put(url, request);
        ResponseEntity<SessionResponseDto> responseForUpdate = client.getForEntity(url, SessionResponseDto.class);

        //assertions
        assertNotNull(responseForUpdate);
        assertEquals(HttpStatus.OK, responseForUpdate.getStatusCode());
        SessionResponseDto session = responseForUpdate.getBody();
        assertNotNull(session);
        assertEquals(modifiedStartTime, session.getStartTime());
        assertEquals(modifiedEndTime, session.getEndTime());
        assertEquals(modifiedDate, session.getDate());
        assertEquals(modifiedInstructor, session.getInstructor());
        assertEquals(modifiedCourse, session.getCourse());
        assertEquals(modifiedSchedule, session.getSchedule());
        assertEquals(this.validId, session.getId());
    }

    /*@Test
    @Order(7)
    public void testModifyCourseByInvalidId(){
        //setup
        SessionRequestDto request = new SessionRequestDto(modifiedStartTime, modifiedEndTime, modifiedDate,modifiedInstructor,modifiedCourse,modifiedSchedule);
        int invalidId = this.validId + 1;
        String url = "/sessions/" + invalidId;

        client.put(url, request);
        //ResponseEntity<SessionResponseDto> responseForUpdate = client.getForEntity(url, SessionResponseDto.class);

    }

    @Test
    @Order(8)
    public void testModifyCourseByInvalid(){
        //setup
        SessionRequestDto request = new SessionRequestDto(modifiedStartTime, modifiedEndTime, modifiedDate,modifiedInstructor,modifiedCourse,modifiedSchedule);
        int invalidId = this.validId + 1;
        String url = "/sessions/" + invalidId;

        client.put(url, request);
        //ResponseEntity<SessionResponseDto> responseForUpdate = client.getForEntity(url, SessionResponseDto.class);

    }*/

}
