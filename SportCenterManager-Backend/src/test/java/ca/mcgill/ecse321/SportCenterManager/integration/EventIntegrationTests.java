package ca.mcgill.ecse321.SportCenterManager.integration;

import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.InstructorAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.ScheduleRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.dto.CourseListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CourseRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CourseResponseDto;
import ca.mcgill.ecse321.SportCenterManager.dto.ErrorDto;
import ca.mcgill.ecse321.SportCenterManager.dto.SessionListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.SessionRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.SessionResponseDto;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private SessionRepository sessionRepo;
    @Autowired
    private ScheduleRepository scheduleRepo;
    @Autowired
    private InstructorAccountRepository instructorRepo;
    
    private final String VALID_NAME = "name";
    private final String VALID_DESCRIPTION = "description";
    private final String MODIFIED_DESCRIPTION = "description1";
    private final double VALID_COST_PER_SESSION = 10;
    private final double INVALID_COST_PER_SESSION = -1;
    private final double MODIFIED_COST_PER_SESSION = 20;
    private int validCourseId;


    private  Time validStartTime = Time.valueOf("10:00:00");
    private  Time inValidStartTime = Time.valueOf("16:00:00");
    private  Time ModifiedStartTime = Time.valueOf("12:00:00");
    private  Time validEndTime = Time.valueOf("12:00:00");
    private  Time ModifiedEndTime = Time.valueOf("14:00:00");
    private  Date validDate = Date.valueOf("2024-04-13");
    private  Date inValidDate = Date.valueOf("2024-03-13");
    private  Date ModifiedDate = Date.valueOf("2024-05-13");
    private int validInstructorId;
    private int validSessionId;
    
    @BeforeAll
    @AfterAll
    public void clearDatabase(){
        sessionRepo.deleteAll();
        scheduleRepo.deleteAll();
        courseRepo.deleteAll();
        instructorRepo.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateValidCourse(){
        // setup
        CourseRequestDto request = new CourseRequestDto(VALID_NAME, VALID_DESCRIPTION, VALID_COST_PER_SESSION);
        String url = "/courses";

        // execution
        ResponseEntity<CourseResponseDto> response = client.postForEntity(url, request, CourseResponseDto.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CourseResponseDto createdCourse = response.getBody();
        assertNotNull(createdCourse);
        assertEquals(VALID_NAME, createdCourse.getName());
        assertEquals(VALID_DESCRIPTION, createdCourse.getDescription());
        assertEquals(VALID_COST_PER_SESSION, createdCourse.getCostPerSession());
        assertNotNull(createdCourse.getId());
        assertTrue(createdCourse.getId() > 0, "Response should have a positive ID.");
        this.validCourseId = createdCourse.getId();
    }

    @Test
    @Order(2)
    public void testFindCourseByValidId(){
        // setup
        String url = "/courses/" + this.validCourseId;

        // execution
        ResponseEntity<CourseResponseDto> response = client.getForEntity(url, CourseResponseDto.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CourseResponseDto course = response.getBody();
        assertNotNull(course);
        assertEquals(VALID_NAME, course.getName());
        assertEquals(VALID_DESCRIPTION, course.getDescription());
        assertEquals(VALID_COST_PER_SESSION, course.getCostPerSession());
        assertEquals(this.validCourseId, course.getId());
    }

    @Test
    @Order(3)
    public void testFindCourseByInvalidId(){
        // setup
        int invalidId = this.validCourseId + 1;
        String url = "/courses/" + invalidId;

        // execution
        ResponseEntity<ErrorDto> response = client.getForEntity(url, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertEquals("There is no course with ID " + invalidId + ".", error.getMessage());
    }

    @Test
    @Order(4)
    public void testFindAllCourses(){
        // setup
        String url = "/courses";

        // execution
        ResponseEntity<CourseListDto> response = client.getForEntity(url, CourseListDto.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CourseListDto courses = response.getBody();
        assertNotNull(courses);
        assertEquals(1, courses.getCourses().size());
        assertTrue(containsCourse(this.validCourseId, courses.getCourses()));
    }

    @Test
    @Order(5)
    public void testCreateCourseWithConflictingName(){
        // setup
        CourseRequestDto request = new CourseRequestDto(VALID_NAME, VALID_DESCRIPTION, VALID_COST_PER_SESSION);
        String url = "/courses";

        // execution
        ResponseEntity<ErrorDto> response = client.postForEntity(url, request, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertEquals("Failed to create course: Course with name " + VALID_NAME + " already exists.", error.getMessage());
    }

    @Test
    @Order(6)
    public void testCreateCourseWithInvalidCostPerSession(){
        // setup
    	String newValidName = "name2";
        CourseRequestDto request = new CourseRequestDto(newValidName, VALID_DESCRIPTION, INVALID_COST_PER_SESSION);
        String url = "/courses";

        // execution
        ResponseEntity<ErrorDto> response = client.postForEntity(url, request, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertEquals("Failed to create course: Cost per session must be greater than 0.", error.getMessage());
    }

    @Test
    @Order(7)
    public void testModifyCourseByValidId(){
        // setup
        CourseRequestDto request = new CourseRequestDto(VALID_NAME, MODIFIED_DESCRIPTION, MODIFIED_COST_PER_SESSION);
        String url = "/courses/" + this.validCourseId;

        // execution
        client.put(url, request);
        ResponseEntity<CourseResponseDto> responseAfterUpdate = client.getForEntity(url, CourseResponseDto.class);

        // assertions
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        CourseResponseDto course = responseAfterUpdate.getBody();
        assertNotNull(course);
        assertEquals(VALID_NAME, course.getName());
        assertEquals(MODIFIED_DESCRIPTION, course.getDescription());
        assertEquals(MODIFIED_COST_PER_SESSION, course.getCostPerSession());
        assertEquals(this.validCourseId, course.getId());
    }

    @Test
    @Order(8)
    public void testModifyCourseByInvalidId(){
        // setup
        CourseRequestDto request = new CourseRequestDto(VALID_NAME, MODIFIED_DESCRIPTION, MODIFIED_COST_PER_SESSION);
        int invalidId = this.validCourseId + 1;
        String url = "/courses/" + invalidId;

        // execution
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertEquals("There is no course with ID " + invalidId + ".", error.getMessage());
    }

    @Test
    @Order(9)
    public void testModifyCourseWithInvalidCostPerSession(){
        // setup
        CourseRequestDto request = new CourseRequestDto(VALID_NAME, MODIFIED_DESCRIPTION, INVALID_COST_PER_SESSION);
        String url = "/courses/" + this.validCourseId;

        // execution
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertEquals("Failed to modify course: Cost per session must be greater than 0.", error.getMessage());
    }

    @Test
    @Order(10)
    public void testApproveCourseByValidId(){
        // setup
        CourseRequestDto request = new CourseRequestDto(VALID_NAME, MODIFIED_DESCRIPTION, MODIFIED_COST_PER_SESSION);
        request.setIsApproved(true);
        String url = "/courses/" + this.validCourseId + "/approve";

        // execution
        client.put(url, request);
        ResponseEntity<CourseResponseDto> responseAfterApproval = client.getForEntity("/courses/" + this.validCourseId, CourseResponseDto.class);

        // assertions
        assertNotNull(responseAfterApproval);
        assertEquals(HttpStatus.OK, responseAfterApproval.getStatusCode());
        CourseResponseDto course = responseAfterApproval.getBody();
        assertNotNull(course);
        assertEquals(VALID_NAME, course.getName());
        assertEquals(MODIFIED_DESCRIPTION, course.getDescription());
        assertEquals(MODIFIED_COST_PER_SESSION, course.getCostPerSession());
        assertTrue(course.getIsApproved());
        assertEquals(this.validCourseId, course.getId());
    }

    @Test
    @Order(11)
    public void testApproveCourseByInvalidId(){
        // setup
        CourseRequestDto request = new CourseRequestDto(VALID_NAME, VALID_DESCRIPTION, VALID_COST_PER_SESSION);
        request.setIsApproved(true);
        int invalidId = this.validCourseId + 1;
        String url = "/courses/" + invalidId;

        // execution
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertEquals("There is no course with ID " + invalidId + ".", error.getMessage());
    }

    @Test
    @Order(12)
    public void testCreateValidSession(){
    	//Setup
        Schedule schedule = new Schedule(Time.valueOf("8:00:00"), Time.valueOf("20:00:00"));
        scheduleRepo.save(schedule);
        SessionRequestDto request = new SessionRequestDto(validStartTime,validEndTime,validDate,-1,validCourseId);
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
        assertEquals(validDate.toString(), createdSession.getDate().toString());
        assertEquals(validCourseId, createdSession.getCourse().getId());

        assertNotNull(createdSession.getId());
        assertTrue(createdSession.getId() > 0, "Response should have a positive id.");
        
        this.validSessionId = createdSession.getId();
    }

    @Test
    @Order(13)
    public void testSuperviseSessionValid() {
    	//Setup
    	InstructorAccount instructor = new InstructorAccount("ValidInstructor", "ValidEmail3@gmail.com", "validPass-100");
    	validInstructorId = instructorRepo.save(instructor).getId();
    	String url = "/courses/" + validCourseId + "/sessions/" + validSessionId + "/instructor/" + validInstructorId;
    	
    	//Execution
    	ResponseEntity<SessionResponseDto> response = client.exchange(url, HttpMethod.PUT, null, SessionResponseDto.class);
    	SessionResponseDto responseDto = response.getBody();
    	
    	//Assertion
    	assertNotNull(response);
    	assertNotNull(responseDto);
    	assertEquals(HttpStatus.OK, response.getStatusCode());
    	assertEquals(validInstructorId, response.getBody().getInstructor().getId());
    }
    
    @Test
    @Order(14)
    public void testSuperviseSessionDuplicate() {
    	//Setup
    	InstructorAccount secondInstructor = new InstructorAccount("SecondValidInstructor", "ValidEmail4@gmail.com", "validPass-101");
    	int secondInstructorId = instructorRepo.save(secondInstructor).getId();
    	String url = "/courses/" + validCourseId + "/sessions/" + validSessionId + "/instructor/" + secondInstructorId;
    	
    	//Execution
    	ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, null, ErrorDto.class);
    	ErrorDto responseDto = response.getBody();
    	
    	//Assertion
    	assertNotNull(response);
    	assertNotNull(responseDto);
    	assertEquals("An instructor is already supervising this session!", responseDto.getMessage());
    	assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    	
    }
    
    @Test
    @Order(15)
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
        assertEquals(validDate.toString(), session.getDate().toString());
        assertEquals(validInstructorId, session.getInstructor().getId());
        assertEquals(validCourseId, session.getCourse().getId());
        assertEquals(this.validSessionId, session.getId());
    }

    @Test
    @Order(16)
    public void testGetSpecificInvalidSession(){
        //Setup
        int invalidSessionId = this.validSessionId + 5;
        String url = "/courses/"+validCourseId+"/sessions/"+invalidSessionId;

        //Execution
        ResponseEntity<ErrorDto> response = client.getForEntity(url, ErrorDto.class);

        //assertions
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertEquals("Session with inputted id is not found.", error.getMessage());
    }

    @Test
    @Order(17)
    public void testCreateInvalidSessionTime(){
        //setup
        SessionRequestDto request = new SessionRequestDto(inValidStartTime,validEndTime,validDate,validInstructorId,validCourseId);
        String url="/courses/"+validCourseId+"/sessions";

        //Execution
        ResponseEntity<ErrorDto> response = client.postForEntity(url, request, ErrorDto.class);

        //Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertEquals("End time must be be after the start time.", error.getMessage());
    }

    @Test
    @Order(18)
    public void testCreateInvalidSessionDate(){
        //setup
        SessionRequestDto request = new SessionRequestDto(validStartTime,validEndTime,inValidDate,validInstructorId,validCourseId);
        String url="/courses/"+validCourseId+"/sessions";

        //Execution
        ResponseEntity<ErrorDto> response = client.postForEntity(url, request, ErrorDto.class);

        //Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertEquals("Cannot create a session on date that has passed.", error.getMessage());
    }

    @Test
    @Order(19)
    public void testModifyValidSession(){
        //setup
        SessionRequestDto request = new SessionRequestDto(ModifiedStartTime, ModifiedEndTime, ModifiedDate, validInstructorId, validCourseId);
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
        assertEquals(ModifiedDate.toString(), session.getDate().toString());
        assertEquals(validInstructorId, session.getInstructor().getId());
        assertEquals(validCourseId, session.getCourse().getId());
        assertEquals(this.validSessionId, session.getId());
    }

    @Test
    @Order(20)
    public void testModifySessionByInvalidId(){
        //setup
        SessionRequestDto request = new SessionRequestDto(ModifiedStartTime, ModifiedEndTime, ModifiedDate, validInstructorId, validCourseId);
        int invalidSessionId = this.validSessionId + 5;
        String url = "/courses/" + validCourseId + "/sessions/" + invalidSessionId;

        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertEquals("Session with inputted id is not found.", error.getMessage());
    }

    @Test
    @Order(21)
    public void testModifySessionByInvalidTime(){
        //setup
        SessionRequestDto request = new SessionRequestDto(inValidStartTime, ModifiedEndTime, ModifiedDate, validInstructorId, validCourseId);
        String url = "/courses/" + validCourseId + "/sessions/" + validSessionId;

        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertEquals("End time must be be after the start time.", error.getMessage());
    }
    @Test
    @Order(22)
    public void testModifyCourseByInvalidDate(){
        //setup
        SessionRequestDto request = new SessionRequestDto(ModifiedStartTime, ModifiedEndTime, inValidDate, validInstructorId, validCourseId);
        String url = "/courses/" + validCourseId + "/sessions/" + validSessionId;

        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertEquals("Cannot create a session on date that has passed.", error.getMessage());
    }
    
    @Test
    @Order(23)
    public void testDeleteSessionByInvalidId(){
        int invalidSessionId = validSessionId + 1000000000;
        String url = "/courses/" + validCourseId + "/sessions/" + invalidSessionId;

        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.DELETE, null, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertEquals("Session with inputted id is not found.", error.getMessage());
    }
    
    @Test
    @Order(24)
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
    
    @Test
    @Order(25)
    public void testDeleteCourseByValidId(){
        // setup
        String url = "/courses/" + this.validCourseId;

        // execution
        client.delete(url, CourseResponseDto.class);
        ResponseEntity<CourseListDto> responseAfterDelete = client.getForEntity("/courses", CourseListDto.class);

        // assertions
        assertNotNull(responseAfterDelete);
        assertEquals(HttpStatus.OK, responseAfterDelete.getStatusCode());
        CourseListDto courses = responseAfterDelete.getBody();
        assertNotNull(courses);
        assertEquals(0, courses.getCourses().size());
    }

    @Test
    @Order(26)
    public void testDeleteCourseByInvalidId(){
        // setup
        String url = "/courses/" + this.validCourseId;

        // execution
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.DELETE, null, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertEquals("There is no course with ID " + this.validCourseId + ".", error.getMessage());
    }

    private boolean containsCourse(int course_id, List<CourseResponseDto> courses){
        for (CourseResponseDto course : courses){
            if (course_id == course.getId()){
                return true;
            }
        }
        return false;
    }

}
