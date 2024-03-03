package ca.mcgill.ecse321.SportCenterManager.dto;

import java.util.List;

public class CourseListDto {

    private List<CourseResponseDto> courses;
    
    public CourseListDto(List<CourseResponseDto> courses){
        this.courses = courses;
    }

    public List<CourseResponseDto> getCourses(){
        return courses;
    }

    public void setCourses(List<CourseResponseDto> courses){
        this.courses = courses;
    }
}
