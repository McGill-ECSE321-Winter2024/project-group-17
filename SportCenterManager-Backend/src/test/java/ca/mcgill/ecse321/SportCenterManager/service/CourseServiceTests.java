package ca.mcgill.ecse321.SportCenterManager.service;

import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.model.Course;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CourseServiceTests {
  @Mock
  private CourseRepository courseRepo;
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
      assertEquals("There is no course with ID" + id + ".", e.getMessage());
    }
  }

  @Test
  public void testCreateValidCourse(){
    // setup
    String name = "valid name";
    String description = "valid description";
    double costPerSession = 10;
    Course course = new Course(name, description, costPerSession);
    lenient().when(courseRepo.save(any(Course.class))).thenReturn(course);

    // execution
    Course createdCourse = service.createCourse(name, description, costPerSession);

    // assertions
    assertNotNull(createdCourse);
    assertEquals(name, createdCourse.getName());
    assertEquals(description, createdCourse.getDescription());
    assertEquals(costPerSession, createdCourse.getCostPerSession());
    verify(courseRepo, times(1)).save(course);
  }

  @Test
  public void testDeleteCourseByValidId(){
    // setup
    int id = 15;
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
    lenient().doNothing().when(courseRepo).deleteById(id);

    try{
      // execution
      service.deleteCourseById(id);
      fail("No exception was thrown.");
    }
    catch (IllegalArgumentException e){
      // assertions
      assertEquals("There is no course with ID" + id + ".", e.getMessage());
      verify(courseRepo, times(1)).findCourseById(id);
      verify(courseRepo, times(1)).deleteById(id);
    }
  }
}
