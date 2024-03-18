package ca.mcgill.ecse321.SportCenterManager.dto;

import java.sql.Time;


public class ScheduleRequestDto {
    private Time openingHour;
    private Time closingHour;

    public ScheduleRequestDto(){}

    public Time getOpeningHour(){
        return this.openingHour;
    }
    public Time getClosingHour(){
        return this.closingHour;
    }
}
