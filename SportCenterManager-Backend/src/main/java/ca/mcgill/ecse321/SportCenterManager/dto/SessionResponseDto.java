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
    private Course course;
    private InstructorAccount instructor;
    private Schedule schedule;
    //recurring dates? can date just be a string?

    public SessionResponseDto(Session model){
        this.startTime = model.getStartTime();
        this.endTime=model.getEndTime();
        this.date=model.getDate();
        this.course=model.getCourse();
        this.instructor=model.getInstructorAccount();
        this.schedule=model.getSchedule();
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public InstructorAccount getInstructor() {
        return instructor;
    }

    public void setInstructor(InstructorAccount instructor) {
        this.instructor = instructor;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
    
}
