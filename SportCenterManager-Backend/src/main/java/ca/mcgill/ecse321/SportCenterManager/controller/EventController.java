package ca.mcgill.ecse321.SportCenterManager.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.SportCenterManager.dto.CourseRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CourseResponseDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CourseListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.SessionListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.SessionRequestDto;
import ca.mcgill.ecse321.SportCenterManager.service.EventService;
import ca.mcgill.ecse321.SportCenterManager.model.Course; //is this legal
import ca.mcgill.ecse321.SportCenterManager.model.Session; //is this legal

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    public Course findCourseById(@PathVariable int course_id){
        return eventService.findCourseById(course_id);
		}

    @DeleteMapping("/courses/{course_id}")
    public void deleteCourseById(@PathVariable int course_id){
        eventService.deleteCourseById(course_id);
		}
    @PutMapping("/courses/{course_id}")
    public void modifyCourseById(@PathVariable int course_id){
			//return 
		}

    @PostMapping("/courses")
    public CourseResponseDto createCourse(@RequestBody CourseRequestDto course){
		  Course createdCourse = eventService.createCourse(course.getName(), course.getDescription(), course.getCostPerSession());	
      return new CourseResponseDto(createdCourse);
    }
    
    //sessions
    @GetMapping("/courses/{course_id}/sessions")
    public SessionListDto findAllSessionsOfCourse(@PathVariable int course_id){
        List<Session> sessions = new ArrayList<Session>();
        for (Session model : eventService.findAllSessionsOfCourse(course_id)){
          sessions.add(new Session(model.getStartTime(), model.getEndTime(), model.getDate(),model.getInstructorAccount(),model.getCourse(),model.getSchedule()));
        }
        return new SessionListDto(sessions);
		}
    @GetMapping("/courses/{course_id}/sessions/{session_id}")
    public Session findSessionById(@PathVariable int course_id, @PathVariable int session_id){
			return eventService.findSessionById(session_id);
		}
    // @DeleteMapping("/courses/{course_id}/sessions")
    // public void deleteAllSessionsOfCourse(@PathVariable int course_id){
		// 	eventService.deleteAllSessionsOfCourse(course_id);
		// }
    @DeleteMapping("/courses/{course_id}/sessions/{session_id}")
    public void deleteSessionById(@PathVariable int course_id, @PathVariable int session_id){
			eventService.deleteSessionById(session_id);
		}
    @PutMapping("/courses/{course_id}/sessions/{session_id}")
    public void modifySessionById(){
			//return 
		}
  
    @PostMapping("/courses/{course_id}/sessions")
    public Session createSession(@RequestBody SessionRequestDto session, @PathVariable int course_id){
			Course course = eventService.findCourseById(course_id);
      Session createdSession = eventService.createSession(session.getStartTime(), session.getEndTime(), session.getDate(), session.getInstructor(),course,session.getSchedule());	
      return createdSession;
		}
    
}
