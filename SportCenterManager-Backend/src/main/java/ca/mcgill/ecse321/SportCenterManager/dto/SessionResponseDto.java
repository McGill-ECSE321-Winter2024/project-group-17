package ca.mcgill.ecse321.SportCenterManager.dto;

import java.sql.Date;
import java.sql.Time;

import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.model.Session;

public class SessionResponseDto {
    private Time startTime;
    private Time endTime; 
    private Date date;
    private CourseResponseDto course;
    private InstructorResponseDto instructor;
    private ScheduleResponseDto schedule;
    private int id;
    
    public SessionResponseDto() {
    	
    }
    
    public SessionResponseDto(Session model){
        this.startTime = model.getStartTime();
        this.endTime=model.getEndTime();
        this.date= model.getDate();
        this.course= new CourseResponseDto(model.getCourse());
        if (model.getInstructorAccount() != null) {
        	this.instructor= new InstructorResponseDto(model.getInstructorAccount());
        } else {
        	this.instructor = new InstructorResponseDto(new InstructorAccount("TBD", "N/A", "N/A"));
        }
        this.schedule= new ScheduleResponseDto(model.getSchedule());
        this.id= model.getId();
    }
  
    public int getId(){
        return id;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }
    
    public Time getEndTime(){
        return endTime;
    }

    public void setEndTime(Time endTime){
        this.endTime = endTime;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public CourseResponseDto getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = new CourseResponseDto(course);
    }

    public InstructorResponseDto getInstructor() {
        return instructor;
    }

    public void setInstructor(InstructorAccount instructor) {
        this.instructor = new InstructorResponseDto(instructor);
    }

    public ScheduleResponseDto getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = new ScheduleResponseDto(schedule);
    }
    
}
