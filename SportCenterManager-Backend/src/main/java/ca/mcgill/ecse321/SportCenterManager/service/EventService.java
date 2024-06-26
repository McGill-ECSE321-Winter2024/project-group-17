package ca.mcgill.ecse321.SportCenterManager.service;

import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.RegistrationRepository;

import ca.mcgill.ecse321.SportCenterManager.exception.ServiceException;

import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.model.Session;
import ca.mcgill.ecse321.SportCenterManager.model.Registration;

import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
public class EventService {
    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private SessionRepository sessionRepo;
    @Autowired
    private InstructorAccountService instructorService;
    @Autowired
    private RegistrationRepository registrationRepo;
    @Autowired
    private ScheduleService scheduleService;

    @Transactional 
    public Iterable<Course> findAllCourses(){
        return courseRepo.findAll();
    }

    @Transactional 
    public Course findCourseById(int course_id ){
        Course course = courseRepo.findCourseById(course_id);
        if (course == null){
          throw new ServiceException(HttpStatus.NOT_FOUND, "There is no course with ID " + course_id + ".");
        }
        return course;
    }

    @Transactional
    public List<Course> findAllApprovedCourses(){
        Iterable<Course> courses = findAllCourses();
        List<Course> approvedCourses = new ArrayList<>();

        for (Course course : courses){
            if (course.getIsApproved()){
                approvedCourses.add(course);
            }
        }
        return approvedCourses;
    }

    @Transactional
    public List<Course> findAllNonApprovedCourses(){
        Iterable<Course> courses = findAllCourses();
        List<Course> nonApprovedCourses = new ArrayList<>();

        for (Course course : courses){
            if (!course.getIsApproved()){
                nonApprovedCourses.add(course);
            }
        }
        return nonApprovedCourses;
    }

    @Transactional 
    public boolean deleteCourseById(int course_id){
        if (courseRepo.findCourseById(course_id) == null){
          throw new ServiceException(HttpStatus.NOT_FOUND, "There is no course with ID " + course_id + ".");
        }
        deleteAllSessionsOfCourse(course_id);
        courseRepo.deleteById(course_id);
        return true;
	}

    @Transactional
    public Course modifyCourseById(int course_id, String description, double costPerSession){
        Course courseToModify = courseRepo.findCourseById(course_id);

        if (courseToModify == null){
            throw new ServiceException(HttpStatus.NOT_FOUND, "There is no course with ID " + course_id + ".");
        }

        if (costPerSession <= 0){
            throw new ServiceException(HttpStatus.FORBIDDEN, "Failed to modify course: Cost per session must be greater than 0.");
        }

        courseToModify.setDescription(description);
        courseToModify.setCostPerSession(costPerSession);

        return courseRepo.save(courseToModify);
	}

    @Transactional
    public Course createCourse(String name, String description, double costPerSession){
        Iterable<Course> courses = findAllCourses();

        for (Course course : courses){
            if (course.getName().equals(name)){
                throw new ServiceException(HttpStatus.CONFLICT, "Failed to create course: Course with name " + name + " already exists.");
            }
        }

        if (costPerSession <= 0){
            throw new ServiceException(HttpStatus.FORBIDDEN, "Failed to create course: Cost per session must be greater than 0.");
        }

        Course courseToCreate = new Course(name, description, costPerSession);
        return courseRepo.save(courseToCreate);
    }

    @Transactional
    public Course approveCourseById(int course_id){
        Course courseToApprove = courseRepo.findCourseById(course_id);
        if (courseToApprove == null){
            throw new ServiceException(HttpStatus.NOT_FOUND, "There is no course with ID " + course_id + ".");
        }
        courseToApprove.setIsApproved(true);
        return courseRepo.save(courseToApprove);
    }
  
    @Transactional
    public List<Session> findAllSessionsOfCourse(int course_id){
        Iterable<Session> sessions = sessionRepo.findAll();

        List<Session> sessionsOfCourse = new ArrayList<>();

        for (Session session: sessions){
            if (session.getCourse().getId() == course_id){
                sessionsOfCourse.add(session);
            }
        }

        return sessionsOfCourse;
    }
    
   @Transactional
    public Session findSessionById(int session_id){
        if(sessionRepo.findSessionById(session_id) == null){
            throw new ServiceException(HttpStatus.NOT_FOUND, "Session with inputted id is not found.");
        }
        return sessionRepo.findSessionById(session_id);
		}
    
    @Transactional
    public boolean deleteAllSessionsOfCourse(int course_id){
        Course course = courseRepo.findCourseById(course_id);
        if (course == null){
            throw new ServiceException(HttpStatus.NOT_FOUND, "There is no course with ID " + course_id + ".");
        }

        Iterable<Registration> registrations = registrationRepo.findAll();
        List<Session> sessionsToDelete = findAllSessionsOfCourse(course_id);

        for (Registration registration : registrations){
            for (Session session : sessionsToDelete){
                if (registration.getKey().getSession().getId() == session.getId()){
                    registrationRepo.delete(registration);
                }
            }
        }

        for (Session session : sessionsToDelete){
            deleteSessionById(session.getId());
        }

        return true;
    }

    @Transactional
    public boolean deleteSessionById(int session_id){

    	//Check if session exists
        findSessionById(session_id);
        sessionRepo.deleteById(session_id);
        return true;
    }
    
    @Transactional
    public Session modifySessionById(int session_id, Time startTime, Time endTime, Date date, int courseId, int instructorId){
        if(endTime.before(startTime)){
            throw new ServiceException(HttpStatus.FORBIDDEN, "End time must be be after the start time.");
        }

        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        if(date.before(currentDate)){
            throw new ServiceException(HttpStatus.FORBIDDEN, "Cannot create a session on date that has passed.");
        }

        Course course = findCourseById(courseId);
        InstructorAccount instructor = instructorService.findInstructorById(instructorId);
        Schedule schedule = scheduleService.findSchedule();
        
        Session session = findSessionById(session_id);
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        session.setDate(date);
        session.setCourse(course);
        session.setInstructorAccount(instructor);
        session.setSchedule(schedule);
        return sessionRepo.save(session);
    }
    
    @Transactional
    public Session createSession(Time start_time, Time end_time, Date date, int instructorId, int courseId){
        if(end_time.before(start_time)){
            throw new ServiceException(HttpStatus.FORBIDDEN, "End time must be be after the start time.");
        }
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        if(date.before(currentDate)){
            throw new ServiceException(HttpStatus.FORBIDDEN, "Cannot create a session on date that has passed.");
        }
        
        InstructorAccount aInstructorAccount;
        // If instructorId == -1, then instructor will be assigned later on
        if (instructorId == -1) {
        	aInstructorAccount = null;
        } else {
        	aInstructorAccount = instructorService.findInstructorById(instructorId);
        }
        
        Course aCourse = findCourseById(courseId);
        Schedule aSchedule = scheduleService.findSchedule();
        
        if (start_time.before(aSchedule.getOpeningHours()) || end_time.after(aSchedule.getClosingHours())) {
        	throw new ServiceException(HttpStatus.FORBIDDEN, "Cannot create a session outside of the center's open hours.");
        }
        
        Session sessionToCreate = new Session(start_time, end_time, date,aInstructorAccount,aCourse,aSchedule);
        return sessionRepo.save(sessionToCreate);
	}
    
    @Transactional
    public Session superviseSession(int instructorId, int sessionId) {
    	InstructorAccount instructor = instructorService.findInstructorById(instructorId);
    	Session session = findSessionById(sessionId);
    	if (session.getInstructorAccount() != null) {
    		throw new ServiceException(HttpStatus.CONFLICT, "An instructor is already supervising this session!");
    	}
    	if (hasConflict(instructor, session)) {
    		throw new ServiceException(HttpStatus.FORBIDDEN, "Session overlaps with another that is already supervised by the instructor!");
    	}
    	session.setInstructorAccount(instructor);
    	sessionRepo.save(session);
    	return session;
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
			if (newSession.getDate().equals(session.getDate()) && (
				newSession.getStartTime().before(session.getEndTime()) &&
				newSession.getStartTime().after(session.getStartTime()) || 
				newSession.getEndTime().before(session.getEndTime()) &&
				newSession.getEndTime().after(session.getStartTime()) ||
				newSession.getStartTime().equals(session.getStartTime()) &&
				newSession.getEndTime().equals(session.getEndTime())||
				newSession.getStartTime().before(session.getStartTime()) &&
				newSession.getEndTime().after(session.getEndTime()))) {
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
