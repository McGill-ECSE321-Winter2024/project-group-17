package ca.mcgill.ecse321.SportCenterManager.controller;


import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.SportCenterManager.dto.CourseRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CourseResponseDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CourseListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.SessionListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.SessionRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.SessionResponseDto;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.service.EventService;
import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.Session; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    @Autowired 
    private EventService eventService;

    @GetMapping("/courses")
    public CourseListDto findAllCourses(){
        List<CourseResponseDto> courses = new ArrayList<CourseResponseDto>();
        for (Course model : eventService.findAllCourses()){
          courses.add(new CourseResponseDto(model));
        }
        return new CourseListDto(courses);
		}

    @GetMapping("/courses/{course_id}")
    public CourseResponseDto findCourseById(@PathVariable int course_id){
        Course foundCourse = eventService.findCourseById(course_id);
        return new CourseResponseDto(foundCourse);
		}

    @DeleteMapping("/courses/{course_id}")
    public void deleteCourseById(@PathVariable int course_id){
        eventService.deleteCourseById(course_id);
		}
    //TODO
    @PutMapping("/courses/{course_id}")
    public void modifyCourseById(@RequestBody CourseRequestDto course, @PathVariable int course_id){
        eventService.modifyCourseById(course_id, course.getDescription(), course.getCostPerSession());
		}
    @PutMapping("/courses/{course_id}/approve")
    public void approveCourseById(@PathVariable int course_id){
        eventService.approveCourseById(course_id);
    }

    @PostMapping("/courses")
    public CourseResponseDto createCourse(@RequestBody CourseRequestDto course){
		  Course createdCourse = eventService.createCourse(course.getName(), course.getDescription(), course.getCostPerSession());	
      return new CourseResponseDto(createdCourse);
    }
    
    //sessions
    @GetMapping("/courses/{course_id}/sessions")
    public SessionListDto findAllSessionsOfCourse(@PathVariable int course_id){
        List<SessionResponseDto> sessions = new ArrayList<SessionResponseDto>();
        for (Session model : eventService.findAllSessionsOfCourse(course_id)){
          sessions.add(new SessionResponseDto(model));
        }
        return new SessionListDto(sessions);
		}
    @GetMapping("/courses/{course_id}/sessions/{session_id}")
    public Session findSessionById(@PathVariable int course_id, @PathVariable int session_id){
			return eventService.findSessionById(session_id);
		}

    // //TODO: DOES NOT WORK -- need to access sessions which have foreign key course_id... does not work yet, see event service 
    //  @DeleteMapping("/courses/{course_id}/sessions")
    //   public void deleteAllSessionsOfCourse(@PathVariable int course_id){
		//  	eventService.deleteAllSessionsOfCourse(course_id);
		//  }
    @DeleteMapping("/courses/{course_id}/sessions/{session_id}")
    public void deleteSessionById(@PathVariable int course_id, @PathVariable int session_id){
			eventService.deleteSessionById(session_id);
		}

    //TODO
    @PutMapping("/courses/{course_id}/sessions/{session_id}")
    public void modifySessionById(@RequestBody SessionRequestDto session, @PathVariable int session_id){
        eventService.modifySessionById(session_id,session.getStartTime(), session.getEndTime(), session.getDate(),  session.getCourse(), session.getInstructor(), session.getSchedule());
		}
  
    //TODO
    @PostMapping("/courses/{course_id}/sessions")
    public SessionResponseDto createSession(@RequestBody SessionRequestDto session, @PathVariable int course_id){
			Course course = eventService.findCourseById(course_id);
      //Did not figure out how to link an instructor and schedule when creating session without passing Instructor and Schedule objects in the request body...
      Session createdSession = eventService.createSession(session.getStartTime(), session.getEndTime(), session.getDate(), session.getInstructor(),course,session.getSchedule());	
      return new SessionResponseDto(createdSession);
		}
    
    @PutMapping("/courses/{course_id}/sessions/{session_id}/{instructor_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SessionResponseDto superviseSession(@PathVariable(name = "session_id") int sessionId, @PathVariable(name = "instructor_id") int instructorId) {
    	Session session = eventService.superviseSession(instructorId, sessionId);
    	return new SessionResponseDto(session);
    }
    
}
