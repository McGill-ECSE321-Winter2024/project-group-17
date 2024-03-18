package ca.mcgill.ecse321.SportCenterManager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.service.ScheduleService;

@RestController
public class ScheduleController {

    @PutMapping("/schedule/{schedule_id}")
    public Schedule updateSchedule(@PathVariable int schedule_id, @RequestBody ScheduleRequestDto schedule){
        Schedule modifiedschedule = ScheduleService.updateSchedule(schedule_id, schedule.getOpeningHour(), schedule.getClosingHour());
        return modifiedschedule;
    }

}