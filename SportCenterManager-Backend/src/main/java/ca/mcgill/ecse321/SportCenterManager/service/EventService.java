package ca.mcgill.ecse321.SportCenterManager.service;

import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.model.Course;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;

@Service
public class EventService {
    @Autowired
    private CourseRepository courseRepo;

    @Transactional 
    public Iterable<Course> findAllCourses(){
        return courseRepo.findAll();
    }

    @Transactional 
    public Course findCourseById(int course_id ){
        return courseRepo.findCourseById(course_id);
    }

    @Transactional 
    public void deleteCourseById(int course_id){
        courseRepo.deleteById(course_id);
	}
    
        //TODO 
    @Transactional
    public void modifyCourseById(int course_id){
			//return 
	}

    @Transactional
    //is this not dto
    public Course createCourse(String name, String description, Double costPerSession ){
        Course courseToCreate = new Course(name, description, costPerSession);
	    return courseRepo.save(courseToCreate); 
    }
}
