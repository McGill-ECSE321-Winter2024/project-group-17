package ca.mcgill.ecse321.SportCenterManager.service;

import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.model.*;

import org.springframework.stereotype.Service;


import java.sql.Time;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;

@Service
public class EventService {
    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private SessionRepository sessionRepo;
    @Autowired
    private RegistrationRepository registrationRepo;

    @Transactional 
    public Iterable<Course> findAllCourses(){
        return courseRepo.findAll();
    }

    @Transactional 
    public Course findCourseById(int course_id ){
        Course course = courseRepo.findCourseById(course_id);
        if (course == null){
          throw new IllegalArgumentException("There is no course with ID" + course_id + ".");
        }
        return course;
    }

    @Transactional 
    public boolean deleteCourseById(int course_id){
        if (courseRepo.findCourseById(course_id) == null){
          throw new IllegalArgumentException("There is no course with ID" + course_id + ".");
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
                throw new IllegalArgumentException("Failed to create course: Course with name " + name + "already exists.");
            }
        }

        if (costPerSession <= 0){
            throw new IllegalArgumentException("Failed to create course: Cost per session must be greater than 0.");
        }

        Course courseToCreate = new Course(name, description, costPerSession);
        return courseRepo.save(courseToCreate);
    }

    //sessions

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
			return sessionRepo.findSessionById(session_id);
		}
    
    @Transactional
    public boolean deleteAllSessionsOfCourse(int course_id){
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
            sessionRepo.delete(session);
        }

        return true;
    }

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
}
