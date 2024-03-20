package ca.mcgill.ecse321.SportCenterManager.dto;

import java.sql.Time;

import ca.mcgill.ecse321.SportCenterManager.model.Schedule;

public class ScheduleResponseDto {
	Time openingHours;
	Time closingHours;
	
	public ScheduleResponseDto() {
		
	}
	
	public ScheduleResponseDto(Schedule schedule) {
		this.openingHours = schedule.getOpeningHours();
		this.closingHours = schedule.getClosingHours();
	}
	
	public Time getOpeningHours() {
		return openingHours;
	}
	
	public Time getClosingHours() {
		return closingHours;
	}
}
