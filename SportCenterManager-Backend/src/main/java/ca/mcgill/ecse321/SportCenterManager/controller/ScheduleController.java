package ca.mcgill.ecse321.SportCenterManager.controller;

import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.service.ScheduleService;

@RestController
public class ScheduleController {
    @Autowired
    ScheduleService service;

    @PostMapping("/schedule")
    public Schedule updateSchedule(@RequestBody ScheduleRequestDto schedule){
        Time opening = schedule.getOpeningHour();
        Time closing = schedule.getClosingHour();
        Schedule modifiedschedule = service.updateSchedule(opening,closing);
        return modifiedschedule;
    }

}