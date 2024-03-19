package ca.mcgill.ecse321.SportCenterManager.dto;
import java.sql.Time;

import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;

import java.sql.Date;
import java.time.LocalDate;

public class SessionRequestDto {
    private Time startTime;
    private Time endTime; 
    private LocalDate date;
    private Course course;
    private InstructorAccount instructor;
    private Schedule schedule;
    public SessionRequestDto(Time startTime, Time endTime, LocalDate date, InstructorAccount instructor, Course courseSchedule, Schedule schedule){
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.course = course;
        this.instructor = instructor;
        this.schedule = schedule;
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

    public LocalDate getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date.toLocalDate();
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
