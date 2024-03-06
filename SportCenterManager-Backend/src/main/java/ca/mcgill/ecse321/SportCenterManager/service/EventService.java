package ca.mcgill.ecse321.SportCenterManager.service;

import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.InstructorAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.model.Session;

import org.springframework.stereotype.Service;


import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;

@Service
public class EventService {
    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private SessionRepository sessionRepo;
    @Autowired
    private InstructorAccountRepository instructorRepo;
    
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
    
//    @Transactional
//    public void deleteAllSessionsOfCourse(int course_id){
// 			//need to find all sessions that have foreign key course_id
//             sessionRepo.deleteByCourse(course_id); //added a method in repository...but it does not work... 
// 		}

    @Transactional
    public void deleteSessionById(int session_id){
			//return 
            sessionRepo.deleteById(session_id);
		}

    //TODO
    @Transactional
    public void modifySessionById(){
			//return 
		}
    
    @Transactional
    public Session createSession(Time start_time, Time end_time, Date date, InstructorAccount aInstructorAccount, Course aCourse, Schedule aSchedule){
        Session sessionToCreate = new Session(start_time, end_time, date,aInstructorAccount,aCourse,aSchedule);
	    return sessionRepo.save(sessionToCreate); 
	}
    
    @Transactional
    public void superviseSession(int instructorId, int sessionId) {
    	InstructorAccount instructor = instructorRepo.findInstructorAccountById(instructorId);
    	Session session = sessionRepo.findSessionById(sessionId);
    	if (session.getInstructorAccount() != null) {
    		throw new IllegalArgumentException("An instructor is already supervising this session!");
    	}
    	if (hasConflict(instructor, session)) {
    		throw new IllegalArgumentException("Session overlaps with another that is already supervised by the instructor!");
    	}
    	session.setInstructorAccount(instructor);
    	sessionRepo.save(session);
    }
    
    @Transactional
    public List<Session> findInstructorSessions(int instructorId){
    	List<Session> allSessions = toList(sessionRepo.findAll());
    	List<Session> instructorSessions = new ArrayList<Session>();
    	for (Session session : allSessions) {
    		if (session.getInstructorAccount() != null && session.getInstructorAccount().getId() == instructorId) {
    			instructorSessions.add(session);
    		}
    	}
    	return instructorSessions;
    }

    private boolean hasConflict(InstructorAccount instructor, Session newSession) {
    	List<Session> sessions = findInstructorSessions(instructor.getId());
		for (Session session: sessions) {
			if (newSession.getStartTime().before(session.getEndTime()) &&
				newSession.getStartTime().after(session.getStartTime()) || 
				newSession.getEndTime().before(session.getEndTime()) &&
				newSession.getEndTime().after(session.getStartTime()) ||
				newSession.getStartTime().equals(session.getStartTime()) &&
				newSession.getEndTime().equals(session.getEndTime())) {
				return true;
			}
		}
		return false;
    }
    
    private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t: iterable) {
			resultList.add(t);
		}
		return resultList;
	}
	
}
