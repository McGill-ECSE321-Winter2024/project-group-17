package ca.mcgill.ecse321.SportCenterManager.integration;
import java.sql.Time;
import java.sql.Date;

import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.InstructorAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.ScheduleRepository;
import ca.mcgill.ecse321.SportCenterManager.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;

import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.model.Course;

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
    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private ScheduleRepository scheduleRepo;
    @Autowired
    private InstructorAccountRepository instructorRepo;
    private  Time validStartTime = Time.valueOf("10:00:00");
    private  Time inValidStartTime = Time.valueOf("16:00:00");
    private  Time ModifiedStartTime = Time.valueOf("12:00:00");
    private  Time validEndTime = Time.valueOf("12:00:00");
    private  Time ModifiedEndTime = Time.valueOf("14:00:00");
    private  Date validDate = Date.valueOf("2024-04-13");
    private  Date inValidDate = Date.valueOf("2024-03-13");
    private  Date ModifiedDate = Date.valueOf("2024-05-13");
    private final Time VALID_START_TIME = new Time(1);
    private final Time VALID_END_TIME = new Time(3600000);
    private int validCourseId;
    private Course validCourse;
    private int validScheduleId;
    private Schedule validSchedule;
    private int validInstructorId;
    private InstructorAccount validInstructor;
    private int validSessionId;


    @BeforeAll
    @AfterAll
    public void clearDatabase(){
        sessionRepo.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateValidCourse(){
        CourseRequestDto request = new CourseRequestDto("yoga", "Beginner class", 44 );
        String url = "/courses";

        ResponseEntity<CourseResponseDto> response = client.postForEntity(url, request, CourseResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CourseResponseDto createdCourse = response.getBody();
        assertNotNull(createdCourse);
        assertEquals("yoga", createdCourse.getName());
        assertEquals("Beginner class", createdCourse.getDescription());
        assertEquals(44, createdCourse.getCostPerSession());
        assertNotNull(createdCourse.getId());
        assertTrue(createdCourse.getId() > 0, "Response should have a positive ID.");
        this.validCourseId = createdCourse.getId();
        this.validCourse = courseRepo.findCourseById(validCourseId);
    }

    @Test
    @Order(2)
    public void testCreateValidSchedule(){
        ScheduleRequestDto request = new ScheduleRequestDto(VALID_START_TIME, VALID_END_TIME);
        String url = "/schedule";
        //Act
        ResponseEntity<Schedule> response = client.postForEntity(url,request,Schedule.class);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        Schedule createdSchedule = response.getBody();
        assertNotNull(createdSchedule);
        assertEquals(VALID_START_TIME.toString(),createdSchedule.getOpeningHours().toString());
        assertEquals(VALID_END_TIME.toString(),createdSchedule.getClosingHours().toString());
        this.validScheduleId = createdSchedule.getId();
        this.validSchedule = scheduleRepo.findScheduleById(validScheduleId);
    }

    @Test
    @Order(3)
    public void testCreateValidInstructor(){
        InstructorRequestDto request = new InstructorRequestDto("Mahmoud", "mahmoud@gmail.com", "abcd");
        String url = "/instructorAccounts";

        ResponseEntity<InstructorResponseDto> response = client.postForEntity(url, request, InstructorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        InstructorResponseDto createdInstructor = response.getBody();
        assertNotNull(createdInstructor);
        assertEquals("Mahmoud", createdInstructor.getName());
        assertEquals("mahmoud@gmail.com", createdInstructor.getEmail());
        assertNotNull(createdInstructor.getId());
        assertTrue(createdInstructor.getId() > 0, "Response should have a positive ID.");
        this.validInstructorId = createdInstructor.getId();
        this.validInstructor = instructorRepo.findInstructorAccountById(validInstructorId);
    }


    @Test
    @Order(4)
    public void testCreateValidSession(){
        SessionRequestDto request = new SessionRequestDto(validStartTime,validEndTime,validDate,validInstructor,validCourse,validSchedule);
        String url="/courses/"+validCourseId+"/sessions";

        //Execution
        ResponseEntity<SessionResponseDto> response = client.postForEntity(url, request, SessionResponseDto.class);

        //Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SessionResponseDto createdSession = response.getBody();
        assertNotNull(createdSession);
        assertEquals(validStartTime, createdSession.getStartTime());
        assertEquals(validEndTime, createdSession.getEndTime());
        assertEquals(validDate, createdSession.getDate());
        assertEquals(validInstructorId, createdSession.getInstructor().getId());
        assertEquals(validCourseId, createdSession.getCourse().getId());
        assertEquals(validScheduleId, createdSession.getSchedule().getId());

        assertNotNull(createdSession.getId());
        assertTrue(createdSession.getId() > 0, "Response should have a positive id.");
        //Check the id
        this.validSessionId = createdSession.getId();
    }

    @Test
    @Order(5)
    public void testGetSpecificSession(){
        //Setup
        String url ="/courses/"+validCourseId+"/sessions/"+validSessionId;

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
        //assertEquals(validInstructor, session.getInstructor());
        assertEquals(validCourseId, session.getCourse().getId());
        assertEquals(validScheduleId, session.getSchedule().getId());
        assertEquals(this.validSessionId, session.getId());
    }

    @Test
    @Order(6)
    public void testGetSpecificInvalidSession(){
        //Setup
        int invalidSessionId = this.validSessionId + 5;
        String url = "/courses/"+validCourseId+"/sessions/"+invalidSessionId;

        //Execution
        ResponseEntity<IllegalArgumentException> response = client.getForEntity(url, IllegalArgumentException.class);

        //assertions
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);
    }

    @Test
    @Order(7)
    public void testCreateInvalidSessionTime(){
        //setup
        SessionRequestDto request = new SessionRequestDto(inValidStartTime,validEndTime,validDate,validInstructor,validCourse,validSchedule);
        String url="/courses/"+validCourseId+"/sessions";

        //Execution
        ResponseEntity<IllegalArgumentException> response = client.postForEntity(url, request, IllegalArgumentException.class);

        //Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);
    }

    @Test
    @Order(8)
    public void testCreateInvalidSessionDate(){
        //setup
        SessionRequestDto request = new SessionRequestDto(validStartTime,validEndTime,inValidDate,validInstructor,validCourse,validSchedule);
        String url="/courses/"+validCourseId+"/sessions";

        //Execution
        ResponseEntity<IllegalArgumentException> response = client.postForEntity(url, request, IllegalArgumentException.class);

        //Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);;
    }

    @Test
    @Order(9)
    public void testModifyValidSession(){
        //setup
        SessionRequestDto request = new SessionRequestDto(ModifiedStartTime, ModifiedEndTime, ModifiedDate, validInstructor, validCourse, validSchedule);
        String url = "/courses/" + validCourseId + "/sessions/" + validSessionId;

        //execution
        client.put(url, request);
        ResponseEntity<SessionResponseDto> responseForUpdate = client.getForEntity(url, SessionResponseDto.class);

        //assertions
        assertNotNull(responseForUpdate);
        assertEquals(HttpStatus.OK, responseForUpdate.getStatusCode());
        SessionResponseDto session = responseForUpdate.getBody();
        assertNotNull(session);
        assertEquals(ModifiedStartTime, session.getStartTime());
        assertEquals(ModifiedEndTime, session.getEndTime());
        assertEquals(ModifiedDate, session.getDate());
        //assertEquals(validInstructor.getId(), session.getInstructor());
        assertEquals(validCourse.getId(), session.getCourse().getId());
        assertEquals(validSchedule.getId(), session.getSchedule().getId());
        assertEquals(this.validSessionId, session.getId());
    }

    @Test
    @Order(10)
    public void testModifyCourseByInvalidId(){
        //setup
        SessionRequestDto request = new SessionRequestDto(ModifiedStartTime, ModifiedEndTime, ModifiedDate, validInstructor, validCourse, validSchedule);
        int invalidSessionId = this.validSessionId + 5;
        String url = "/courses/" + validCourseId + "/sessions/" + invalidSessionId;

        ResponseEntity<IllegalArgumentException> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), IllegalArgumentException.class);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);
    }

    @Test
    @Order(11)
    public void testModifyCourseByInvalidTime(){
        //setup
        SessionRequestDto request = new SessionRequestDto(inValidStartTime, ModifiedEndTime, ModifiedDate, validInstructor, validCourse, validSchedule);
        String url = "/courses/" + validCourseId + "/sessions/" + validSessionId;

        ResponseEntity<IllegalArgumentException> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), IllegalArgumentException.class);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);
    }
    @Test
    @Order(12)
    public void testModifyCourseByInvalidDate(){
        //setup
        SessionRequestDto request = new SessionRequestDto(ModifiedStartTime, ModifiedEndTime, inValidDate, validInstructor, validCourse, validSchedule);
        String url = "/courses/" + validCourseId + "/sessions/" + validSessionId;

        ResponseEntity<IllegalArgumentException> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), IllegalArgumentException.class);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);
    }
    @Test
    @Order(13)
    public void testDeleteSessionByInvalidId(){
        int invalidSessionId = validSessionId + 1000000000;
        String url = "/courses/" + validCourseId + "/sessions/" + invalidSessionId;

        ResponseEntity<IllegalArgumentException> response = client.exchange(url, HttpMethod.DELETE, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);
    }
    @Test
    @Order(14)
    public void testDeleteSessionByValidId(){
        String url = "/courses/" + validCourseId + "/sessions/" + validSessionId;

        client.delete(url, SessionResponseDto.class);
        ResponseEntity<SessionListDto> responseAfterDelete = client.getForEntity("/courses/" + validCourseId + "/sessions", SessionListDto.class);

        assertNotNull(responseAfterDelete);
        assertEquals(HttpStatus.OK, responseAfterDelete.getStatusCode());
        SessionListDto sessions = responseAfterDelete.getBody();
        assertNotNull(sessions);
        assertEquals(0, sessions.getSessions().size());
    }
}
