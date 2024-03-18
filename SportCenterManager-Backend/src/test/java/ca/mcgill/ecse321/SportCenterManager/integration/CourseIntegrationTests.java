package ca.mcgill.ecse321.SportCenterManager.integration;

import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.dto.CourseListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CourseRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CourseResponseDto;
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

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private CourseRepository courseRepo;

    private final String VALID_NAME = "name";
    private final String VALID_DESCRIPTION = "description";
    private final String MODIFIED_DESCRIPTION = "description1";
    private final double VALID_COST_PER_SESSION = 10;
    private final double INVALID_COST_PER_SESSION = -1;
    private final double MODIFIED_COST_PER_SESSION = 20;
    private int validId;

    @BeforeAll
    @AfterAll
    public void clearDatabase(){
        courseRepo.deleteAll();
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
        System.out.println(createdCourse.getId());
        this.validId = createdCourse.getId();
        System.out.println(this.validId);
    }

    @Test
    @Order(2)
    public void testFindCourseByValidId(){
        // setup
        String url = "/courses/" + this.validId;

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
        assertEquals(this.validId, course.getId());
    }

    @Test
    @Order(3)
    public void testFindCourseByInvalidId(){
        // setup
        int invalidId = this.validId + 1;
        String url = "/courses/" + invalidId;

        // execution
        ResponseEntity<IllegalArgumentException> response = client.getForEntity(url, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);
        //assertEquals("There is no course with ID " + invalidId + ".", error.getMessage());
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
        assertTrue(containsCourse(this.validId, courses.getCourses()));
    }

    @Test
    @Order(5)
    public void testCreateCourseWithConflictingName(){
        // setup
        CourseRequestDto request = new CourseRequestDto(VALID_NAME, VALID_DESCRIPTION, VALID_COST_PER_SESSION);
        String url = "/courses";

        // execution
        ResponseEntity<IllegalArgumentException> response = client.postForEntity(url, request, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);
        //assertEquals("Failed to create course: Course with name " + VALID_NAME + " already exists.", error.getMessage());
    }

    @Test
    @Order(6)
    public void testCreateCourseWithInvalidCostPerSession(){
        // setup
        CourseRequestDto request = new CourseRequestDto(VALID_NAME, VALID_DESCRIPTION, INVALID_COST_PER_SESSION);
        String url = "/courses";

        // execution
        ResponseEntity<IllegalArgumentException> response = client.postForEntity(url, request, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);
        //assertEquals("Failed to create course: Cost per session must be greater than 0.", error.getMessage());
    }

    @Test
    @Order(7)
    public void testModifyCourseByValidId(){
        // setup
        CourseRequestDto request = new CourseRequestDto(VALID_NAME, MODIFIED_DESCRIPTION, MODIFIED_COST_PER_SESSION);
        String url = "/courses/" + this.validId;

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
        assertEquals(this.validId, course.getId());
    }

    @Test
    @Order(8)
    public void testModifyCourseByInvalidId(){
        // setup
        CourseRequestDto request = new CourseRequestDto(VALID_NAME, MODIFIED_DESCRIPTION, MODIFIED_COST_PER_SESSION);
        int invalidId = this.validId + 1;
        String url = "/courses/" + invalidId;

        // execution
        ResponseEntity<IllegalArgumentException> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);
        //assertEquals("There is no course with ID " + this.validId + ".", error.getMessage());
    }

    @Test
    @Order(9)
    public void testModifyCourseWithInvalidCostPerSession(){
        // setup
        CourseRequestDto request = new CourseRequestDto(VALID_NAME, MODIFIED_DESCRIPTION, INVALID_COST_PER_SESSION);
        String url = "/courses/" + this.validId;

        // execution
        ResponseEntity<IllegalArgumentException> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);
        //assertEquals("Failed to modify course: Cost per session must be greater than 0.", error.getMessage());
    }

    @Test
    @Order(10)
    public void testApproveCourseByValidId(){
        // setup
        CourseRequestDto request = new CourseRequestDto(VALID_NAME, MODIFIED_DESCRIPTION, MODIFIED_COST_PER_SESSION);
        request.setIsApproved(true);
        String url = "/courses/" + this.validId + "/approve";

        // execution
        client.put(url, request);
        ResponseEntity<CourseResponseDto> responseAfterApproval = client.getForEntity("/courses/" + this.validId, CourseResponseDto.class);

        // assertions
        assertNotNull(responseAfterApproval);
        assertEquals(HttpStatus.OK, responseAfterApproval.getStatusCode());
        CourseResponseDto course = responseAfterApproval.getBody();
        assertNotNull(course);
        assertEquals(VALID_NAME, course.getName());
        assertEquals(MODIFIED_DESCRIPTION, course.getDescription());
        assertEquals(MODIFIED_COST_PER_SESSION, course.getCostPerSession());
        assertTrue(course.getIsApproved());
        assertEquals(this.validId, course.getId());
    }

    @Test
    @Order(11)
    public void testApproveCourseByInvalidId(){
        // setup
        CourseRequestDto request = new CourseRequestDto(VALID_NAME, VALID_DESCRIPTION, VALID_COST_PER_SESSION);
        request.setIsApproved(true);
        int invalidId = this.validId + 1;
        String url = "/courses/" + invalidId;

        // execution
        ResponseEntity<IllegalArgumentException> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);
        //assertEquals("There is no course with ID " + invalidId + ".", error.getMessage());
    }

    @Test
    @Order(10)
    public void testDeleteCourseByValidId(){
        // setup
        String url = "/courses/" + this.validId;

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
    @Order(11)
    public void testDeleteCourseByInvalidId(){
        // setup
        String url = "/courses/" + this.validId;

        // execution
        ResponseEntity<IllegalArgumentException> response = client.exchange(url, HttpMethod.DELETE, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        IllegalArgumentException error = response.getBody();
        assertNotNull(error);
        //assertEquals("There is no course with ID " + this.validId + ".", error.getMessage());
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
