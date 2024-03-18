package ca.mcgill.ecse321.SportCenterManager.service;

import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.SportCenterManager.dao.ScheduleRepository;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import jakarta.transaction.Transactional;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepo;

    @Transactional
    //assuming always valid id from frontend config 
    public Schedule updateSchedule(int id, Time startTime, Time endTime){
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Cannot have an empty time");
         }
        else if(endTime.after(startTime)){
            throw new IllegalArgumentException("Cannot have closing hour occur before opening hour");
        }
        Schedule schedule = scheduleRepo.findScheduleById(id);
        schedule.setOpeningHours(startTime);
        schedule.setClosingHours(endTime);
        return scheduleRepo.save(schedule);
    }
}
