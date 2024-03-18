package ca.mcgill.ecse321.SportCenterManager.controller;

import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.service.ScheduleService;

@RestController
public class ScheduleController {
    @Autowired
    ScheduleService service;

    @PutMapping("/schedule/{schedule_id}")
    public Schedule updateSchedule(@PathVariable int schedule_id, @RequestBody ScheduleRequestDto schedule){
        Time opening = schedule.getOpeningHour();
        Time closing = schedule.getClosingHour();
        Schedule modifiedschedule = service.updateSchedule(schedule_id,opening,closing);
        return modifiedschedule;
    }

}