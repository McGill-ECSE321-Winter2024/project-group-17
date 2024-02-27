package ca.mcgill.ecse321.SportCenterManager.dto;

public class SessionRequestDto {
    private Time startTime;
    private Time endTime; 
    private Date date;
    //no id 
    //recurring dates? 
    
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
}
