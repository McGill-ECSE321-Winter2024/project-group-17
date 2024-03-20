package ca.mcgill.ecse321.SportCenterManager.dto;
import java.sql.Time;

import java.sql.Date;

public class SessionRequestDto {
    private Time startTime;
    private Time endTime; 
    private Date date;
    private int courseId;
    private int instructorId;
    public SessionRequestDto(Time startTime, Time endTime, Date date, int instructorId, int courseId){
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.courseId = courseId;
        this.instructorId = instructorId;
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

    public int getCourseId() {
        return courseId;
    }

    public void setCourse(int courseId) {
        this.courseId = courseId;
    }

    public int getInstructorId() {
         return instructorId;
    }

    public void setInstructorId(int instructorId) {
         this.instructorId = instructorId;
    }
    
}
