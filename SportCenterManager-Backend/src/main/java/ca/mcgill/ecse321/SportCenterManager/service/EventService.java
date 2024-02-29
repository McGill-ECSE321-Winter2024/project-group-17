package ca.mcgill.ecse321.SportCenterManager.service;

import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.model.Session;

import org.springframework.stereotype.Service;


import java.sql.Time;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;

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

    //sessions

    @Transactional
    public Iterable<Session> findAllSessionsOfCourse(int course_id){
			return sessionRepo.findAll();
		}
    
    @Transactional
    public Session findSessionById(int session_id){
			return sessionRepo.findSessionById(session_id);
		}
    
   // @Transactional
   // public void deleteAllSessionsOfCourse(int course_id){
			//need to find all sessions that have foreign key course_id
     //       sessionRepo.deleteByCourse(course_id); //added a method in repository...
		//}

    @Transactional
    public void deleteSessionById(int session_id){
			//return 
            sessionRepo.deleteById(session_id);
		}

    @Transactional
    public void modifySessionById(){
			//return 
		}
    
    @Transactional
    public Session createSession(Time start_time, Time end_time, Date date, InstructorAccount aInstructorAccount, Course aCourse, Schedule aSchedule){
        //Time and Date in model, change? 
        Session sessionToCreate = new Session(start_time, end_time, date,aInstructorAccount,aCourse,aSchedule);
	    return sessionRepo.save(sessionToCreate); 
	}
}
