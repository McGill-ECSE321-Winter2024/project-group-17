package ca.mcgill.ecse321.SportCenterManager.service;

import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.model.Session;
import java.util.Optional;
import org.springframework.stereotype.Service; // Assuming your service is annotated with @Service
import org.springframework.transaction.annotation.Transactional; // Assuming you are using Spring's @Transactional


import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class EventService {
    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private SessionRepository sessionRepo;

    @Transactional 
    public Iterable<Course> findAllCourses(){
        return courseRepo.findAll();
    }

    @Transactional 
    public Course findCourseById(int course_id ){
        if(!courseRepo.existsById(course_id)){
            throw new IllegalArgumentException("Course does not exist.");
        }
        return courseRepo.findCourseById(course_id);
    }

    @Transactional 
    public void deleteCourseById(int course_id){
        //sessionRepo.deleteByCourse(course_id);
        courseRepo.deleteById(course_id);
	}
    
        //TODO 
    @Transactional
    public void modifyCourseById(int course_id){
			//return 
	}

    @Transactional
    public Course createCourse(String name, String description, Double costPerSession ){
        Course courseToCreate = new Course(name, description, costPerSession);
	    return courseRepo.save(courseToCreate); 
    }
    @Transactional
    public Iterable<Session> findAllSessionsOfCourse(int course_id){
			return sessionRepo.findSessionsByCourseId(course_id);
    }
    @Transactional
    public Session findSessionById(int session_id){
        if(sessionRepo.findSessionById(session_id) == null){
            throw new IllegalArgumentException("Session with inputted id is not found.");
        }
        return sessionRepo.findSessionById(session_id);
    }

    @Transactional
    public boolean deleteSessionById(int session_id){

        if(sessionRepo.findById(session_id)==null) {
            throw new IllegalArgumentException("Session with inputted id is not found"); // Session with the given ID does not exist
        }
        sessionRepo.deleteById(session_id);
        return true;
    }
    //TODO
    @Transactional
    public Session modifySessionById(int session_id, Time startTime, Time endTime, LocalDate date, Course course, InstructorAccount instructor, Schedule schedule){
        if(endTime.before(startTime)){
            throw new IllegalArgumentException("End time must be be after the start time.");
        }

        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        if(date.isBefore(currentDate.toLocalDate())){
            throw new IllegalArgumentException("Cannot create a session on date that has passed.");
        }

        if(sessionRepo.findSessionById(session_id) == null){
            throw new IllegalArgumentException("Session with inputted id is not found.");
        }

        Session session = sessionRepo.findSessionById(session_id);
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        session.setDate(date);
        session.setCourse(course);
        session.setInstructorAccount(instructor);
        session.setSchedule(schedule);
        return sessionRepo.save(session);
    }
    
    @Transactional
    public Session createSession(Time start_time, Time end_time, LocalDate date, InstructorAccount aInstructorAccount,@NonNull Course aCourse, Schedule aSchedule){
        if(end_time.before(start_time)){
            throw new IllegalArgumentException("End time must be be after the start time.");
        }
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        if(date.isBefore(currentDate.toLocalDate())){
            throw new IllegalArgumentException("Cannot create a session on date that has passed.");
        }
        Session sessionToCreate = new Session(start_time, end_time, date,aInstructorAccount,aCourse,aSchedule);
        return sessionRepo.save(sessionToCreate);
	}
}
