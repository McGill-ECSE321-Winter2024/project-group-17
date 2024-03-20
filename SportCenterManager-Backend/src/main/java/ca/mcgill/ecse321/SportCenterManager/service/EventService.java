package ca.mcgill.ecse321.SportCenterManager.service;

import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.InstructorAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.RegistrationRepository;

import ca.mcgill.ecse321.SportCenterManager.exception.ServiceException;

import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.model.Session;
import ca.mcgill.ecse321.SportCenterManager.model.Registration;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import jakarta.transaction.Transactional;

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
    //@Autowired
    //private RegistrationService registrationService;

    @Transactional 
    public Iterable<Course> findAllCourses(){
        return courseRepo.findAll();
    }

    @Transactional 
    public Course findCourseById(int course_id ){
        Course course = courseRepo.findCourseById(course_id);
        if (course == null){
          throw new IllegalArgumentException("There is no course with ID " + course_id + ".");
        }
        return course;
    }

    @Transactional 
    public boolean deleteCourseById(int course_id){
        if (courseRepo.findCourseById(course_id) == null){
          throw new IllegalArgumentException("There is no course with ID " + course_id + ".");
        }
        deleteAllSessionsOfCourse(course_id);
        courseRepo.deleteById(course_id);
        return true;
	}

    @Transactional
    public Course modifyCourseById(int course_id, String description, double costPerSession){
        Course courseToModify = courseRepo.findCourseById(course_id);

        if (courseToModify == null){
            throw new IllegalArgumentException("There is no course with ID " + course_id + ".");
        }

        if (costPerSession <= 0){
            throw new IllegalArgumentException("Failed to modify course: Cost per session must be greater than 0.");
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
                throw new IllegalArgumentException("Failed to create course: Course with name " + name + " already exists.");
            }
        }

        if (costPerSession <= 0){
            throw new IllegalArgumentException("Failed to create course: Cost per session must be greater than 0.");
        }

        Course courseToCreate = new Course(name, description, costPerSession);
        return courseRepo.save(courseToCreate);
    }

    @Transactional
    public Course approveCourseById(int course_id){
        Course courseToApprove = courseRepo.findCourseById(course_id);
        if (courseToApprove == null){
            throw new IllegalArgumentException("There is no course with ID " + course_id + ".");
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
            throw new IllegalArgumentException("Session with inputted id is not found.");
        }
        return sessionRepo.findSessionById(session_id);
		}
    
    @Transactional
    public boolean deleteAllSessionsOfCourse(int course_id){
        Course course = courseRepo.findCourseById(course_id);
        if (course == null){
            throw new IllegalArgumentException("There is no course with ID " + course_id + ".");
        }

        //List<Registration> registrations = registrationService.findAllRegistrations();
        Iterable<Registration> registrations = registrationRepo.findAll();
        List<Session> sessionsToDelete = findAllSessionsOfCourse(course_id);

        for (Registration registration : registrations){
            for (Session session : sessionsToDelete){
                if (registration.getKey().getSession().getId() == session.getId()){
                    registrationRepo.delete(registration);
                    //registrationService.cancelRegistration(registration.getKey().getCustomerAccount().getId(), session.getId());
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

        if(sessionRepo.findById(session_id)==null) {
            throw new IllegalArgumentException("Session with inputted id is not found"); // Session with the given ID does not exist
        }
        sessionRepo.deleteById(session_id);
        return true;
    }
    
    @Transactional
    public Session modifySessionById(int session_id, Time startTime, Time endTime, Date date, Course course, InstructorAccount instructor, Schedule schedule){
        if(endTime.before(startTime)){
            throw new IllegalArgumentException("End time must be be after the start time.");
        }

        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        if(date.before(currentDate)){
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
    public Session createSession(Time start_time, Time end_time, Date date, InstructorAccount aInstructorAccount,@NonNull Course aCourse, Schedule aSchedule){
        if(end_time.before(start_time)){
            throw new IllegalArgumentException("End time must be be after the start time.");
        }
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        if(date.before(currentDate)){
            throw new IllegalArgumentException("Cannot create a session on date that has passed.");
        }
        Session sessionToCreate = new Session(start_time, end_time, date,aInstructorAccount,aCourse,aSchedule);
        return sessionRepo.save(sessionToCreate);
	}
    
    @Transactional
    public Session superviseSession(int instructorId, int sessionId) {
    	InstructorAccount instructor = instructorService.findInstructorById(instructorId);
    	Session session = findSessionById(sessionId);
    	if (session.getInstructorAccount() != null) {
    		throw new ServiceException(HttpStatus.BAD_REQUEST, "An instructor is already supervising this session!");
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
				newSession.getEndTime().equals(session.getEndTime()))) {
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
