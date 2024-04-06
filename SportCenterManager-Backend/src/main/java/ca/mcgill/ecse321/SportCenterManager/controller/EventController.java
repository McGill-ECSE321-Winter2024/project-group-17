package ca.mcgill.ecse321.SportCenterManager.controller;


import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.SportCenterManager.dto.*;
import ca.mcgill.ecse321.SportCenterManager.service.EventService;
import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.Session;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/courses")
    @Operation(
            summary = "Find all courses",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully found all courses.",
                            content = @Content(
                                    schema = @Schema(implementation = CourseListDto.class),
                                    examples = {@ExampleObject(value = "{" +
                                            "\"courses\": [" +
                                            "{" +
                                            "\"id\": 10, " +
                                            "\"name\": \"Yoga\", " +
                                            "\"description\": \"Intermediate\", " +
                                            "\"costPerSession\": 44, " +
                                            "\"isApproved\": true}]}"
                                    )}
                            )
                    )

            }
    )
    public CourseListDto findAllCourses(){
        List<CourseResponseDto> courses = new ArrayList<CourseResponseDto>();
        for (Course model : eventService.findAllCourses()){
            courses.add(new CourseResponseDto(model));
        }
        return new CourseListDto(courses);
    }

    @Operation(
            summary = "Find course by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully found course by id.",
                            content = @Content(
                                    schema = @Schema(implementation = CourseResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"id\" : 10, " +
                                                    "\"name\" : \"Yoga\", " +
                                                    "\"description\" : \"Intermediate\", " +
                                                    "\"costPerSession\" : 44, " +
                                                    "\"isApproved\" : false }")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: invalid course id.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"There is no course with ID 11.\"}")
                                    }
                            )
                    )

            }
    )
    @GetMapping("/courses/{course_id}")
    public CourseResponseDto findCourseById(@PathVariable int course_id){
        Course foundCourse = eventService.findCourseById(course_id);
        return new CourseResponseDto(foundCourse);
    }



    @Operation(
            summary = "Delete course by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully deleted course by id.",
                            content = @Content(
                                    schema = @Schema(implementation = CourseListDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"courses\": []}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: invalid customer id.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"There is no course with ID 11.\"}")
                                    }
                            )
                    )

            }
    )
    @DeleteMapping("/courses/{course_id}")
    public void deleteCourseById(@PathVariable int course_id){
        eventService.deleteCourseById(course_id);
    }

    @Operation(
            summary = "Update the information of a course by id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully updated course by id.",
                            content = @Content(
                                    schema = @Schema(implementation = CourseResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"id\" : 10, " +
                                                    "\"name\" : \"Yoga\", " +
                                                    "\"description\" : \"Advanced\", " +
                                                    "\"costPerSession\" : 50, " +
                                                    "\"isApproved\" : false }")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: invalid course id.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"There is no course with ID 11 .\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: invalid cost per session.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Failed to modify course: Cost per session must be greater than 0.\"}")
                                    }
                            )
                    )
            }
    )
    @PutMapping("/courses/{course_id}")
    public void modifyCourseById(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = CourseRequestDto.class),
                            examples = {
                                    @ExampleObject(value = "{" +
                                            "\"name\" : \"Yoga\", " +
                                            "\"description\" : \"Advanced\", " +
                                            "\"costPerSession\" : 50, " +
                                            "\"isApproved\" : false }"
                                    )
                            }
                    )
            )
            @RequestBody CourseRequestDto course, @PathVariable int course_id){
        eventService.modifyCourseById(course_id, course.getDescription(), course.getCostPerSession());
    }

    @Operation(
            summary = "Approve a course by id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully approved a course by id.",
                            content = @Content(
                                    schema = @Schema(implementation = CourseResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"id\" : 10, " +
                                                    "\"name\" : \"Yoga\", " +
                                                    "\"description\" : \"Advanced\", " +
                                                    "\"costPerSession\" : 50, " +
                                                    "\"isApproved\" : true }")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: invalid course id.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"There is no course with ID 10.\"}")
                                    }
                            )
                    )

            }
    )
    @PutMapping("/courses/{course_id}/approve")
    public void approveCourseById(@PathVariable int course_id){
        eventService.approveCourseById(course_id);
    }



    @Operation(
            summary = "Create a course.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully created a course.",
                            content = @Content(
                                    schema = @Schema(implementation = CourseResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"name\" : \"Yoga\", " +
                                                    "\"description\" : \"Intermediate\", " +
                                                    "\"costPerSession\" : 44}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict: invalid name.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Failed to create course: Course with name yoga already exists.\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: invalid course cost.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Failed to modify course: Cost per session must be greater than 0.\"}")
                                    }
                            )
                    )
            }
    )
    @PostMapping("/courses")
    public CourseResponseDto createCourse(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = CourseRequestDto.class),
                            examples = {
                                    @ExampleObject(value = "{" +
                                            "\"name\" : \"Name\", " +
                                            "\"description\" : \"Description\", " +
                                            "\"costPerSession\" : \"Cost\"}"
                                    )
                            }
                    )
            )
            @RequestBody CourseRequestDto course){
        Course createdCourse = eventService.createCourse(course.getName(), course.getDescription(), course.getCostPerSession());
        return new CourseResponseDto(createdCourse);
    }


    @Operation(
            summary = "Find all sessions of course",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully found all sessions of course.",
                            content = @Content(
                                    schema = @Schema(implementation = SessionListDto.class),
                                    examples = {@ExampleObject(value = "{" +
                                            "\"sessions\": [" +
                                            "{" +
                                            "\"startTime\": \"20:00:00\", " +
                                            "\"endTime\": \"21:00:00\", " +
                                            "\"date\":\"2024-05-05\", " +
                                            "\"course\": {" +
                                            "\"name\": \"string\", " +
                                            "\"description\": \"string\", " +
                                            "\"costPerSession\": 10, " +
                                            "\"isApproved\": true, " +
                                            "\"id\": 10 }," +
                                            "\"instructor\": {" +
                                            "\"id\": 20 ," +
                                            "\"name\": \"string\", " +
                                            "\"email\": \"string\", " +
                                            "\"password\": \"string\" + }, " +
                                            "\"schedule\": {" +
                                            "\"openingHours\": \"09:00:00\" , "+
                                            "\"closingHours\": \"23:00:00\" , }, "+
                                            "\"id\": 30 }]}"
                                    )}
                            )
                    )

            }
    )
    //sessions
    @GetMapping("/courses/{course_id}/sessions")
    public SessionListDto findAllSessionsOfCourse(@PathVariable int course_id){
        List<SessionResponseDto> sessions = new ArrayList<SessionResponseDto>();
        for (Session model : eventService.findAllSessionsOfCourse(course_id)){
            sessions.add(new SessionResponseDto(model));
        }
        return new SessionListDto(sessions);
    }

    @Operation(summary = "Find a session by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully found session by id.",
                            content = @Content(
                                    schema = @Schema(implementation = SessionResponseDto.class),
                                    examples = {@ExampleObject(value = "{" +
                                            "\"startTime\": \"20:00:00\", " +
                                            "\"endTime\": \"21:00:00\", " +
                                            "\"date\":\"2024-05-05\", " +
                                            "\"course\": {" +
                                            "\"name\": \"string\", " +
                                            "\"description\": \"string\", " +
                                            "\"costPerSession\": 10, " +
                                            "\"isApproved\": true, " +
                                            "\"id\": 10 }," +
                                            "\"instructor\": {" +
                                            "\"id\": 20 ," +
                                            "\"name\": \"string\", " +
                                            "\"email\": \"string\", " +
                                            "\"password\": \"string\" + }, " +
                                            "\"schedule\": {" +
                                            "\"openingHours\": \"09:00:00\" , "+
                                            "\"closingHours\": \"23:00:00\" , }, "+
                                            "\"id\": 30 }"
                                    )}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: invalid session id.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Session with inputted id is not found.\"}")
                                    }
                            )
                    )

            }
    )
    @GetMapping("/courses/{course_id}/sessions/{session_id}")
    public SessionResponseDto findSessionById(@PathVariable int course_id, @PathVariable int session_id){
        return new SessionResponseDto(eventService.findSessionById(session_id));
    }


    @Operation(
            summary = "Delete all sessions of course by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully deleted all sessions for a course by id.",
                            content = @Content(
                                    schema = @Schema(implementation = SessionListDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"sessions\": []}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: invalid session id.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"There is no course with ID 11\"}")
                                    }
                            )
                    )

            }
    )
    @DeleteMapping("/courses/{course_id}/sessions")
    public void deleteAllSessionsOfCourse(@PathVariable int course_id){
        eventService.deleteAllSessionsOfCourse(course_id);
    }

    @Operation(
            summary = "Delete session by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully deleted a session by id.",
                            content = @Content(
                                    schema = @Schema(implementation = SessionListDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"sessions\": []}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: invalid session id.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Session with inputted id is not found.\"}")
                                    }
                            )
                    )
            }
    )
    @DeleteMapping("/courses/{course_id}/sessions/{session_id}")
    public void deleteSessionById(@PathVariable int course_id, @PathVariable int session_id){
        eventService.deleteSessionById(session_id);
    }


    @Operation( summary = "Update the information of a session by id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully updated session by id.",
                            content = @Content(
                                    schema = @Schema(implementation = SessionResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"startTime\": \"14:00:00\", " +
                                                    "\"endTime\": \"15:00:00\", " +
                                                    "\"date\":\"2024-05-06\", " +
                                                    "\"course\": {" +
                                                    "\"name\": \"string\", " +
                                                    "\"description\": \"string\", " +
                                                    "\"costPerSession\": 10, " +
                                                    "\"isApproved\": true, " +
                                                    "\"id\": 10 }," +
                                                    "\"instructor\": {" +
                                                    "\"id\": 20 ," +
                                                    "\"name\": \"string\", " +
                                                    "\"email\": \"string\", " +
                                                    "\"password\": \"string\" + }, " +
                                                    "\"schedule\": {" +
                                                    "\"openingHours\": \"09:00:00\" , "+
                                                    "\"closingHours\": \"23:00:00\" , }, "+
                                                    "\"id\": 30 }")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: invalid start time.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"The start time must be before the end time.\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: invalid date.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Cannot modify session for a date that has passed.\"}")
                                    }
                            )
                    )

            })
    //TODO
    @PutMapping("/courses/{course_id}/sessions/{session_id}")
    public void modifySessionById(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = SessionRequestDto.class),
                            examples = {
                                    @ExampleObject(value = "{" +
                                            "\"startTime\": \"14:00:00\", " +
                                            "\"endTime\": \"15:00:00\", " +
                                            "\"date\": \"2024-05-06\", " +
                                            "\"courseId\": 14, " +
                                            "\"instructorId\": 43" +
                                            "}")
                            }
                    )
            )
            @RequestBody SessionRequestDto session, @PathVariable int session_id){
        eventService.modifySessionById(session_id,session.getStartTime(), session.getEndTime(), session.getDate(),  session.getCourseId(), session.getInstructorId());
    }

    @Operation(
            summary = "Create a session.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully created a session.",
                            content = @Content(
                                    schema = @Schema(implementation = SessionResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"startTime\": \"14:00:00\", " +
                                                    "\"endTime\": \"15:00:00\", " +
                                                    "\"date\":\"2024-05-06\", " +
                                                    "\"course\": {" +
                                                    "\"name\": \"string\", " +
                                                    "\"description\": \"string\", " +
                                                    "\"costPerSession\": 10, " +
                                                    "\"isApproved\": true, " +
                                                    "\"id\": 14 }," +
                                                    "\"instructor\": {" +
                                                    "\"id\": 43 ," +
                                                    "\"name\": \"string\", " +
                                                    "\"email\": \"string\", " +
                                                    "\"password\": \"string\" + }, " +
                                                    "\"schedule\": {" +
                                                    "\"openingHours\": \"09:00:00\" , "+
                                                    "\"closingHours\": \"23:00:00\" , }, "+
                                                    "\"id\": 30 }")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: invalid start time",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"End time must be be after the start time.\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: invalid date.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Cannot create a session on date that has passed.\"}")
                                    }
                            )
                    )
            }
    )
    //TODO
    @PostMapping("/courses/{course_id}/sessions")
    public SessionResponseDto createSession(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = SessionRequestDto.class),
                            examples = {
                                    @ExampleObject(value = "{" +
                                            "\"startTime\" : \"20:00:00\", " +
                                            "\"endTime\" : \"21:00:00\", " +
                                            "\"date\" : \"2024-05-05\", " +
                                            "\"courseId\" : \"14\", " +
                                            "\"instructorId\" : \"43\"}")
                            }
                    )
            )
            @RequestBody SessionRequestDto session, @PathVariable int course_id){
        Session createdSession = eventService.createSession(session.getStartTime(), session.getEndTime(), session.getDate(), session.getInstructorId(),session.getCourseId());
        return new SessionResponseDto(createdSession);
    }

    @Operation(
            summary = "Supervise a session.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully supervised a session.",
                            content = @Content(
                                    schema = @Schema(implementation = SessionResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"startTime\": \"14:00:00\", " +
                                                    "\"endTime\": \"15:00:00\", " +
                                                    "\"date\":\"2024-05-06\", " +
                                                    "\"course\": {" +
                                                    "\"name\": \"string\", " +
                                                    "\"description\": \"string\", " +
                                                    "\"costPerSession\": 10, " +
                                                    "\"isApproved\": true, " +
                                                    "\"id\": 14 }," +
                                                    "\"instructor\": {" +
                                                    "\"id\": 43 ," +
                                                    "\"name\": \"string\", " +
                                                    "\"email\": \"string\", " +
                                                    "\"password\": \"string\" + }, " +
                                                    "\"schedule\": {" +
                                                    "\"openingHours\": \"09:00:00\" , "+
                                                    "\"closingHours\": \"23:00:00\" , }, "+
                                                    "\"id\": 30 }")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict: An instructor is already supervising this session",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"An instructor is already supervising this session\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: Session overlaps with another that is already supervised by the instructor!.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \" Session overlaps with another that is already supervised by the instructor!\"}")
                                    }
                            )
                    )
            }
    )
    @PutMapping("/courses/{course_id}/sessions/{session_id}/instructor/{instructor_id}")
    public SessionResponseDto superviseSession(@PathVariable(name = "session_id") int sessionId, @PathVariable(name = "instructor_id") int instructorId) {
        Session session = eventService.superviseSession(instructorId, sessionId);
        return new SessionResponseDto(session);
    }

}