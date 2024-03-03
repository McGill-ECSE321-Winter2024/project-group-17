package ca.mcgill.ecse321.SportCenterManager.dto;
import java.sql.Time;

import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;

import java.sql.Date;

public class SessionRequestDto {
    private Time startTime;
    private Time endTime; 
    private Date date;
    private Course course;
    private InstructorAccount instructor;
    private Schedule schedule;
    
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
