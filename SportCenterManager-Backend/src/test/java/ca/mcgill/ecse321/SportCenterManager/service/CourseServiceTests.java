package ca.mcgill.ecse321.SportCenterManager.service;

import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.Registration;
import ca.mcgill.ecse321.SportCenterManager.model.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CourseServiceTests {
  @Mock
  private CourseRepository courseRepo;
  @Mock
  private SessionRepository sessionRepo;
  @Mock
  private RegistrationRepository registrationRepo;
  @Mock
  private RegistrationService registrationService;
  @InjectMocks
  private EventService service;

  @Test
  public void testFindCourseByValidId(){
    // setup
    int id = 15;
    Course course = new Course("valid name", "valid description", 10);
    lenient().when(courseRepo.findCourseById(id)).thenReturn(course);

    // execution
    Course foundCourse = service.findCourseById(id);

    // assertions
    assertNotNull(foundCourse);
    assertEquals(course.getName(), foundCourse.getName());
    assertEquals(course.getDescription(), foundCourse.getDescription());
    assertEquals(course.getCostPerSession(), foundCourse.getCostPerSession());
    verify(courseRepo, times(1)).findCourseById(id);
  }

  @Test
  public void testFindCourseByInvalidId(){
    // setup
    int id = 15;
    lenient().when(courseRepo.findCourseById(id)).thenReturn(null);

    try {
      // execution
      service.findCourseById(id);
      fail("No exception was thrown");
    }
    catch (IllegalArgumentException e){
      // assertions
      assertEquals("There is no course with ID " + id + ".", e.getMessage());
      verify(courseRepo, times(1)).findCourseById(id);
    }
  }

  @Test
  public void testFindAllCourses(){
    // setup
    Iterable<Course> courses = getExistingCourses();
    lenient().when(courseRepo.findAll()).thenReturn(courses);

    // execution
    List<Course> result = (List<Course>) service.findAllCourses();

    // assertions
    assertNotNull(result);
    assertEquals(4, result.size());
    verify(courseRepo, times(1)).findAll();
  }

  @Test
  public void testCreateValidCourse(){
    // setup
    String name = "valid name";
    String description = "valid description";
    double costPerSession = 10;
    Course course = new Course(name, description, costPerSession);
    Iterable<Course> courses = getExistingCourses();
    lenient().when(courseRepo.findAll()).thenReturn(courses);
    lenient().when(courseRepo.save(any(Course.class))).thenReturn(course);

    // execution
    Course createdCourse = service.createCourse(name, description, costPerSession);

    // assertions
    assertNotNull(createdCourse);
    assertEquals(name, createdCourse.getName());
    assertEquals(description, createdCourse.getDescription());
    assertEquals(costPerSession, createdCourse.getCostPerSession());
    verify(courseRepo, times(1)).findAll();
    verify(courseRepo, times(1)).save(any(Course.class));
  }

  @Test
  public void testCreateCourseWithConflictingName(){
    // setup
    String name = "name1";
    String description = "valid description";
    double costPerSession = 10;
    Course course = new Course(name, description, costPerSession);
    Iterable<Course> courses = getExistingCourses();
    lenient().when(courseRepo.findAll()).thenReturn(courses);

    try {
      // execution
      service.createCourse(name, description, costPerSession);
      fail("No exception was thrown.");
    }
    catch (IllegalArgumentException e){
      // assertions
      assertEquals("Failed to create course: Course with name " + name + " already exists.", e.getMessage());
      verify(courseRepo, times(1)).findAll();
      verify(courseRepo, times(0)).save(course);
    }
  }

  @Test
  public void testCreateCourseWithInvalidCostPerSession(){
    // setup
    String name = "valid name";
    String description = "valid description";
    double costPerSession1 = -10;
    double costPerSession2 = 0;
    Course course1 = new Course(name, description, costPerSession1);
    Course course2 = new Course(name, description, costPerSession2);
    Iterable<Course> courses = getExistingCourses();
    lenient().when(courseRepo.findAll()).thenReturn(courses);

    try {
      // execution
      service.createCourse(name, description, costPerSession1);
      service.createCourse(name, description, costPerSession2);
      fail("No exception was thrown.");
    }
    catch (IllegalArgumentException e){
      // assertions
      assertEquals("Failed to create course: Cost per session must be greater than 0.", e.getMessage());
      verify(courseRepo, atLeast(1)).findAll();
      verify(courseRepo, atMost(2)).findAll();
      verify(courseRepo, times(0)).save(course1);
      verify(courseRepo, times(0)).save(course2);
    }

  }

  @Test
  public void testModifyCourseByValidId(){
    // setup
    int id = 15;
    String name = "valid name";
    Course course = new Course(name, "valid description", 10);
    lenient().when(courseRepo.findCourseById(id)).thenReturn(course);

    String modifiedDescription = "another description";
    double modifiedCostPerSession = 20;
    course.setDescription(modifiedDescription);
    course.setCostPerSession(modifiedCostPerSession);
    lenient().when(courseRepo.save(any(Course.class))).thenReturn(course);

    // execution
    Course modifiedCourse = service.modifyCourseById(id, modifiedDescription, modifiedCostPerSession);

    // assertions
    assertNotNull(modifiedCourse);
    assertEquals(name, modifiedCourse.getName());
    assertEquals(modifiedDescription, modifiedCourse.getDescription());
    assertEquals(modifiedCostPerSession, modifiedCourse.getCostPerSession());
    verify(courseRepo, times(1)).save(course);
  }

  @Test
  public void testModifyCourseByInvalidId(){
    // setup
    int id = 15;
    String name = "valid name";
    String modifiedDescription = "another description";
    double modifiedCostPerSession = 20;
    Course course = new Course(name, modifiedDescription, modifiedCostPerSession);
    lenient().when(courseRepo.findCourseById(id)).thenReturn(null);

    try {
      // execution
      service.modifyCourseById(id, modifiedDescription, modifiedCostPerSession);
      fail("No exception was thrown.");
    }
    catch (IllegalArgumentException e) {
      // assertions
      assertEquals("There is no course with ID " + id + ".", e.getMessage());
      verify(courseRepo, times(1)).findCourseById(id);
      verify(courseRepo, times(0)).save(course);
    }
  }

  @Test
  public void testModifyCourseWithInvalidCostPerSession(){
    // setup
    int id = 15;
    String name = "valid name";
    String modifiedDescription = "another description";
    double modifiedCostPerSession1 = -10;
    double modifiedCostPerSession2 = 0;
    Course course = new Course(name, "valid description", 10);
    lenient().when(courseRepo.findCourseById(id)).thenReturn(course);

    try {
      // execution
      service.modifyCourseById(id, modifiedDescription, modifiedCostPerSession1);
      service.modifyCourseById(id, modifiedDescription, modifiedCostPerSession2);
      fail("No exception was thrown.");
    }
    catch (IllegalArgumentException e){
      // assertions
      assertEquals("Failed to modify course: Cost per session must be greater than 0.", e.getMessage());
      verify(courseRepo, atLeast(1)).findCourseById(id);
      verify(courseRepo, atMost(2)).findCourseById(id);
      verify(courseRepo, times(0)).save(course);
    }
  }

  @Test
  public void testDeleteCourseByValidId(){
    // setup
    int id = 15;
    Course course = new Course("valid name", "valid description", 10);
    List<Session> sessions = service.findAllSessionsOfCourse(id);
    List<Registration> registrations = registrationService.findAllRegistrations();
    lenient().when(sessionRepo.findAll()).thenReturn(sessions);
    lenient().when(registrationRepo.findAll()).thenReturn(registrations);
    lenient().when(courseRepo.findCourseById(id)).thenReturn(course);
    lenient().doNothing().when(courseRepo).deleteById(id);

    // execution
    boolean result = service.deleteCourseById(id);

    // assertions
    assertTrue(result);
    verify(courseRepo, times(1)).findCourseById(id);
    verify(courseRepo, times(1)).deleteById(id);
  }

  @Test
  public void testDeleteCourseByInvalidId(){
    // setup
    int id = 15;
    lenient().when(courseRepo.findCourseById(id)).thenReturn(null);
    lenient().doNothing().when(courseRepo).deleteById(id);

    try {
      // execution
      service.deleteCourseById(id);
      fail("No exception was thrown.");
    }
    catch (IllegalArgumentException e){
      // assertions
      assertEquals("There is no course with ID " + id + ".", e.getMessage());
      verify(courseRepo, times(1)).findCourseById(id);
    }
  }

  @Test
  public void testApproveCourseByValidId(){
    // setup
    int id = 15;
    String name = "valid name";
    String description = "valid description";
    double costPerSession = 10;
    Course course = new Course(name, description, costPerSession);
    lenient().when(courseRepo.findCourseById(id)).thenReturn(course);
    course.setIsApproved(true);
    lenient().when(courseRepo.save(any(Course.class))).thenReturn(course);

    // execution
    Course approvedCourse = service.approveCourseById(id);

    // assertions
    assertNotNull(approvedCourse);
    assertEquals(name, approvedCourse.getName());
    assertEquals(description, approvedCourse.getDescription());
    assertEquals(costPerSession, approvedCourse.getCostPerSession());
    assertTrue(approvedCourse.getIsApproved());
    verify(courseRepo, times(1)).findCourseById(id);
    verify(courseRepo, times(1)).save(course);
  }

  @Test
  public void testApproveCourseByInvalidId(){
    // setup
    int id = 15;
    String name = "valid name";
    String description = "valid description";
    double costPerSession = 10;
    Course course = new Course(name, description, costPerSession);
    course.setIsApproved(true);
    lenient().when(courseRepo.findCourseById(id)).thenReturn(null);

    try {
      // execution
      service.approveCourseById(id);
      fail("No exception was thrown.");
    }
    catch (IllegalArgumentException e){
      // assertions
      assertEquals("There is no course with ID " + id + ".", e.getMessage());
      verify(courseRepo, times(1)).findCourseById(id);
      verify(courseRepo, times(0)).save(course);
    }
  }

  /*------------ SETUP HELPER METHODS  -----------*/
  private Iterable<Course> getExistingCourses(){
    List<Course> courses = new ArrayList<>();

    Course courseOne = new Course("name1", "description1", 10);
    Course courseTwo = new Course("name2", "description2", 20);
    Course courseThree = new Course("name3", "description3", 30);
    Course courseFour = new Course("name4", "description4", 40);

    courses.add(courseOne);
    courses.add(courseTwo);
    courses.add(courseThree);
    courses.add(courseFour);

    return (Iterable<Course>) courses;
  }
}
