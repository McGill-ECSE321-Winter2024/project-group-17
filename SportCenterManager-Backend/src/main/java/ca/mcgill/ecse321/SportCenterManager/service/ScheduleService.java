package ca.mcgill.ecse321.SportCenterManager.service;

import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.SportCenterManager.dao.ScheduleRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.exception.ServiceException;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.model.Session;
import jakarta.transaction.Transactional;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepo;
    @Autowired
    private SessionRepository sessionRepo;

    @Transactional
    //assuming valid time format due to fronted dropdown config
    public Schedule updateSchedule(Time startTime, Time endTime){
        if (startTime == null || endTime == null) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "Cannot have an empty time");
        }
        else if(endTime.before(startTime)){
            throw new ServiceException(HttpStatus.FORBIDDEN, "Cannot have closing hour occur before opening hour");
        }else{
            if(scheduleRepo.count() == 0L){
                Schedule schedule = new Schedule(startTime, endTime);
                return scheduleRepo.save(schedule);
            } else {
            	Schedule oldSchedule = ((List<Schedule>)scheduleRepo.findAll()).get(0);
                Schedule schedule = new Schedule(startTime, endTime);
                scheduleRepo.save(schedule);
            	List<Session> allSessions = (List<Session>)sessionRepo.findAll();
            	for (Session session: allSessions) {
            		session.setSchedule(schedule);
            		sessionRepo.save(session);
                }
                scheduleRepo.delete(oldSchedule);
                return scheduleRepo.save(schedule);
            }
        }
    }
    
    @Transactional
    public Schedule findSchedule() {
    	if (scheduleRepo.count() == 0) {
    		throw new ServiceException(HttpStatus.NOT_FOUND, "The opening hours have not been set!");
    	} else {
    		return ((List<Schedule>) scheduleRepo.findAll()).get(0);
    	}
    }
}
