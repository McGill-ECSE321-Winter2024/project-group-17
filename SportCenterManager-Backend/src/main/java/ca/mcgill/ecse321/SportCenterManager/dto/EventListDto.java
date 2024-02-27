package ca.mcgill.ecse321.SportCenterManager.dto;

import java.util.List;

public class EventListDto {

    private List<EventListDto> events;
    
    public EventListDto(List<EventListDto> events){
        this.events = events;
    }

    public List<EventListDto> getEvents(){
        return events;
    }

    public void setEvents(List<EventListDto> events){
        this.events = events;
    }
}
