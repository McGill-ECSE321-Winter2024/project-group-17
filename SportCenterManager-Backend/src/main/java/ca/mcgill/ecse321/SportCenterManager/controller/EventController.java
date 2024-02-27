package ca.mcgill.ecse321.SportCenterManager.controller;

@RestController
public class EventController {
    @Autowired 
    private EventService eventService

    //courses
    //TODO change void to appropriate dto 
    @GetMapping("/courses")
    public void findAllCourses(){
			//return 
		}

    @GetMapping("/courses/{course_id}")
    public void findCourseById(@PathVariable int course_id){
			//return 
		}
    @DeleteMapping("/courses/{course_id}")
    public void deleteCourseById(@PathVariable int course_id){
			//return 
		}
    @PutMapping("/courses/{course_id}")
    public void modifyCourseById(@PathVariable int course_id){
			//return 
		}
    @PostMapping("/courses")
    public void createCourse(){
			//return 
		}
    
    //sessions
    @GetMapping("/courses/{course_id}/sessions")
    public void findAllSessionsOfCourse(@PathVariable int course_id){
			//return 
		}
    @GetMapping("/courses/{course_id}/sessions/{session_id}")
    public void findSessionById(@PathVariable int course_id int session_id){
			//return 
		}
    @DeleteMapping("/courses/{course_id}/sessions")
    public void deleteAllSessionsOfCourse(@PathVariable int course_id){
			//return 
		}
    @DeleteMapping("/courses/{course_id}/sessions/{session_id}")
    public void deleteSessionById(@PathVariable int course_id int session_id){
			//return 
		}
    @PutMapping("/courses/{course_id}/sessions/{session_id}")
    public void modifySessionById(){
			//return 
		}
    @PostMapping("/courses/{course_id}/sessions")
    public void createSession(){
			//return 
		}
}
