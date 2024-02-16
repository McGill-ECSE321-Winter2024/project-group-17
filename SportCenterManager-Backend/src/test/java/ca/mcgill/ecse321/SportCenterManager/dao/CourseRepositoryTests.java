package ca.mcgill.ecse321.SportCenterManager.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.SportCenterManager.model.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CourseRepositoryTests {

    @Autowired
    private CourseRepository courseRepository;

    @AfterEach
    public void clearDatabase() {
        courseRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadCourse() {
        //Create a course
        String name = "Tennis";
        String description = "One on one coaching";
        int costPerSession = 30;

        Course course = new Course(name, description, costPerSession);
        course = courseRepository.save(course);

        int id = course.getId();

        //Read the course from database
        Course courseDatabase = courseRepository.findCourseById(id);

        //Assertions
        assertNotNull(courseDatabase);
        assertEquals(name, courseDatabase.getName());
        assertEquals(description, courseDatabase.getDescription());
        assertEquals(costPerSession, courseDatabase.getCostPerSession());
    }
}
