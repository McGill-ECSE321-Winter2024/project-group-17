package ca.mcgill.ecse321.SportCenterManager.dto;
import java.time.LocalTime;
import java.time.LocalDate;

public class SessionRequestDto {
    private LocalTime startTime;
    private LocalTime endTime; 
    private LocalDate date;
    //recurring dates? can date just be a string?
    
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime(){
        return endTime;
    }

    public void setEndTime(LocalTime endTime){
        this.endTime = endTime;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }
}
