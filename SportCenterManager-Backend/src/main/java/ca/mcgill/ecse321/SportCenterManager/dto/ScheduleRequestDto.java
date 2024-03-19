package ca.mcgill.ecse321.SportCenterManager.dto;
import java.sql.Time;
public class ScheduleRequestDto {
    private Time openingHour;
    private Time closingHour;

    public ScheduleRequestDto(Time start, Time end){
        this.openingHour = start;
        this.closingHour = end;
    }

    public Time getOpeningHour(){
        return this.openingHour;
    }
    public Time getClosingHour(){
        return this.closingHour;
    }
}
