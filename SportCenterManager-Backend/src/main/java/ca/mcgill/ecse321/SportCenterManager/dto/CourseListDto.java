package ca.mcgill.ecse321.SportCenterManager.dto;

import java.util.List;
import ca.mcgill.ecse321.SportCenterManager.model.Course;

public class CourseListDto {

    private List<Course> courses;
    
    public CourseListDto(List<Course> courses){
        this.courses = courses;
    }

    public List<Course> getCourses(){
        return courses;
    }

    public void setCourses(List<Course> courses){
        this.courses = courses;
    }
}
