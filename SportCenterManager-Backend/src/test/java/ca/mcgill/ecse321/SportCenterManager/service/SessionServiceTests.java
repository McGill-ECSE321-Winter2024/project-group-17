package ca.mcgill.ecse321.SportCenterManager.service;
import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.exception.ServiceException;
import ca.mcgill.ecse321.SportCenterManager.model.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;


@ExtendWith(MockitoExtension.class)
public class SessionServiceTests {
    @Mock
    private SessionRepository sessionRepo;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private InstructorAccountService instructorService;
    @Mock
    private CourseRepository courseRepo;
    @InjectMocks
    private EventService service;
    @Test
    public void testDeleteSession(){
        //Setup
        int sessionId = 1234;

        doNothing().when(sessionRepo).deleteById(sessionId);
        lenient().when(sessionRepo.findSessionById(sessionId)).thenReturn(new Session());

        boolean deleted = service.deleteSessionById(sessionId);

        //Is it called the correct number of times?
        verify(sessionRepo, times(2)).findSessionById(sessionId);
        verify(sessionRepo, times(1)).deleteById(sessionId);

        assertTrue(deleted,"The session got deleted successfully!");
    }

    @Test
    public void testDeleteInvalidSession(){
        //Setup
        int wrongId = 34;
        when(sessionRepo.findSessionById(wrongId)).thenReturn(null);


        try {
            service.deleteSessionById(wrongId);
        }
        catch(Exception e) {

            //Is it called the correct number of times?
            verify(sessionRepo, times(1)).findSessionById(wrongId);
            verify(sessionRepo, times(0)).deleteById(wrongId);

            lenient().when(sessionRepo.save(any(Session.class))).thenReturn(null);
            lenient().when(sessionRepo.findSessionById(wrongId)).thenReturn(null);

            lenient().doNothing().when(sessionRepo).deleteById(wrongId);

            ServiceException exception = assertThrows(ServiceException.class, () ->
                    service.deleteSessionById(wrongId)
            );

            assertEquals("Session with inputted id is not found.", exception.getMessage());
            //verify(sessionRepo, never()).save(any(Session.class));
        }
    }

    @Test
    public void testModifyValidSession(){
        //Setup
        Course course = new Course("yoga", "Beginner class", 44 );
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        Schedule schedule = new Schedule(Time.valueOf("8:00:00"), Time.valueOf("13:00:00"));
        Time startTime = Time.valueOf("10:00:00");
        Time endTime = Time.valueOf("12:00:00");
        Date date = Date.valueOf("2026-03-15");
        Session session = new Session(startTime, endTime, date, instructor, course, schedule);
        int id = session.getId();

        Time newStartTime = Time.valueOf("11:00:00");
        Time newEndTime = Time.valueOf("13:00:00");
        Date newDate = Date.valueOf("2026-04-16");
        Course newCourse = new Course("Zumba", "Intermediate class", 50 );
        int courseId = 1;
        InstructorAccount newInstructor = new InstructorAccount("Jannett", "jannett@gmail.com", "abcd");
        int instructorId = 1;

        when(sessionRepo.save(any(Session.class))).thenAnswer((InvocationOnMock i) -> i.getArgument(0));
        when(sessionRepo.findSessionById(id)).thenReturn(session);
        lenient().when(instructorService.findInstructorById(instructorId)).thenReturn(newInstructor);
        lenient().when(courseRepo.findCourseById(courseId)).thenReturn(newCourse);

        Session modifiedSession = service.modifySessionById(id, newStartTime,newEndTime,newDate,courseId, instructorId);

        assertNotNull(modifiedSession);
        assertEquals(newStartTime, modifiedSession.getStartTime());
        assertEquals(newEndTime, modifiedSession.getEndTime());
        assertEquals(newDate, modifiedSession.getDate());
        assertEquals(newInstructor, modifiedSession.getInstructorAccount());
        assertEquals(newCourse, modifiedSession.getCourse());
    }

    @Test
    public void testModifyInvalidSessionTime(){
        //Setup
        Course course = new Course("yoga", "Beginner class", 44 );
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        Schedule schedule = new Schedule(Time.valueOf("8:00:00"), Time.valueOf("13:00:00"));
        Time startTime = Time.valueOf("10:00:00");
        Time endTime = Time.valueOf("12:00:00");
        Date date = Date.valueOf("2024-03-15");
        Session session = new Session(startTime, endTime, date, instructor, course, schedule);
        int id = session.getId();

        Time newStartTime = Time.valueOf("13:00:00");
        Time newEndTime = Time.valueOf("11:00:00");
        Date newDate = Date.valueOf("2024-06-16");
        Course newCourse = new Course("Zumba", "Intermediate class", 50 );
        int courseId = 1;
        InstructorAccount newInstructor = new InstructorAccount("Jannett", "jannett@gmail.com", "abcd");
        int instructorId = 1;

        lenient().when(sessionRepo.save(any(Session.class))).thenReturn(null);
        lenient().when(sessionRepo.findSessionById(id)).thenReturn(null);
        lenient().when(instructorService.findInstructorById(instructorId)).thenReturn(newInstructor);
        lenient().when(courseRepo.findCourseById(courseId)).thenReturn(newCourse);

        ServiceException exception = assertThrows(ServiceException.class, () ->
                service.modifySessionById(id, newStartTime,newEndTime,newDate,courseId, instructorId)
        );

        assertEquals("End time must be be after the start time.", exception.getMessage());
        verify(sessionRepo, never()).save(any(Session.class));

    }
    @Test
    public void testModifyInvalidSessionDate(){
        //Setup
        Course course = new Course("yoga", "Beginner class", 44 );
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        Schedule schedule = new Schedule(Time.valueOf("8:00:00"), Time.valueOf("13:00:00"));
        Time startTime = Time.valueOf("10:00:00");
        Time endTime = Time.valueOf("12:00:00");
        Date date = Date.valueOf("2024-03-15");
        Session session = new Session(startTime, endTime, date, instructor, course, schedule);
        int id = session.getId();

        Time newStartTime = Time.valueOf("10:00:00");
        Time newEndTime = Time.valueOf("11:00:00");
        Date newDate = Date.valueOf("2024-03-15");
        Course newCourse = new Course("Zumba", "Intermediate class", 50 );
        int courseId = 1;
        InstructorAccount newInstructor = new InstructorAccount("Jannett", "jannett@gmail.com", "abcd");
        int instructorId = 1;

        lenient().when(sessionRepo.save(any(Session.class))).thenReturn(null);
        lenient().when(sessionRepo.findSessionById(id)).thenReturn(null);
        lenient().when(instructorService.findInstructorById(instructorId)).thenReturn(newInstructor);
        lenient().when(courseRepo.findCourseById(courseId)).thenReturn(newCourse);

        ServiceException exception = assertThrows(ServiceException.class, () ->
                service.modifySessionById(id, newStartTime,newEndTime,newDate,courseId, instructorId)
        );

        assertEquals("Cannot create a session on date that has passed.", exception.getMessage());
        verify(sessionRepo, never()).save(any(Session.class));

    }

    @Test
    public void testModifyInvalidSessionId(){
        //Setup
        Course course = new Course("yoga", "Beginner class", 44 );
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        Schedule schedule = new Schedule(Time.valueOf("8:00:00"), Time.valueOf("13:00:00"));
        Time startTime = Time.valueOf("10:00:00");
        Time endTime = Time.valueOf("12:00:00");
        Date date = Date.valueOf("2024-03-15");
        Session session = new Session(startTime, endTime, date, instructor, course, schedule);
        int id = session.getId();

        Time newStartTime = Time.valueOf("10:00:00");
        Time newEndTime = Time.valueOf("11:00:00");
        Date newDate = Date.valueOf("2026-04-15");
        Course newCourse = new Course("Zumba", "Intermediate class", 50 );
        int courseId = 1;
        InstructorAccount newInstructor = new InstructorAccount("Jannett", "jannett@gmail.com", "abcd");
        int instructorId = 1;

        lenient().when(sessionRepo.save(any(Session.class))).thenReturn(null);
        lenient().when(sessionRepo.findSessionById(id)).thenReturn(null);
        lenient().when(instructorService.findInstructorById(instructorId)).thenReturn(newInstructor);
        lenient().when(courseRepo.findCourseById(courseId)).thenReturn(newCourse);

        ServiceException exception = assertThrows(ServiceException.class, () ->
                service.modifySessionById(id, newStartTime,newEndTime,newDate,courseId, instructorId)
        );

        assertEquals("Session with inputted id is not found.", exception.getMessage());
        verify(sessionRepo, never()).save(any(Session.class));

    }

    @Test
    public void testCreateValidSession(){
        //Set up
        Course course = new Course("yoga", "Beginner class", 44);
        int courseId = 1;
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        int instructorId = 1;
        Time startTime = Time.valueOf("10:00:00");
        Time endTime = Time.valueOf("12:00:00");
        Date date = Date.valueOf("2026-04-15");
        Schedule schedule = new Schedule(Time.valueOf("9:00:00"), Time.valueOf("16:00:00"));
        Session session = new Session(startTime, endTime, date, instructor, course, schedule);
        		
        when(sessionRepo.save(any(Session.class))).thenReturn(session);
        lenient().when(instructorService.findInstructorById(instructorId)).thenReturn(instructor);
        lenient().when(courseRepo.findCourseById(courseId)).thenReturn(course);
        lenient().when(scheduleService.findSchedule()).thenReturn(schedule);

        //Act
        Session createdSession = service.createSession(startTime,endTime, date,instructorId, courseId);

        //Assert
        assertNotNull(createdSession);
        assertEquals(startTime, createdSession.getStartTime());
        assertEquals(endTime, createdSession.getEndTime());
        assertEquals(date, createdSession.getDate());
        assertEquals(instructor, createdSession.getInstructorAccount());
        assertEquals(course, createdSession.getCourse());

        verify(sessionRepo, times(0)).save(createdSession);

    }
    @Test
    public void testCreateInvalidSessionTime(){
        //Setup
        Course course = new Course("yoga", "Beginner class", 44 );
        int courseId = 1;
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        int instructorId = 1;
        Time startTime = Time.valueOf("13:00:00");
        Time endTime = Time.valueOf("12:00:00");
        Date date = Date.valueOf("2026-03-15");
        Schedule schedule = new Schedule(Time.valueOf("9:00:00"), Time.valueOf("16:00:00"));

        lenient().when(sessionRepo.save(any(Session.class))).thenReturn(null);
        lenient().when(instructorService.findInstructorById(instructorId)).thenReturn(instructor);
        lenient().when(courseRepo.findCourseById(courseId)).thenReturn(course);
        lenient().when(scheduleService.findSchedule()).thenReturn(schedule);

        ServiceException exception = assertThrows(ServiceException.class, () ->
                service.createSession(startTime, endTime, date, instructorId, courseId)
        );

        assertEquals("End time must be be after the start time.", exception.getMessage());
        verify(sessionRepo, never()).save(any(Session.class));
    }

    @Test
    public void testCreateInvalidSessionAfterClosing(){
        //Setup
        Course course = new Course("yoga", "Beginner class", 44 );
        int courseId = 1;
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        int instructorId = 1;
        Time startTime = Time.valueOf("12:00:00");
        Time endTime = Time.valueOf("13:00:00");
        Date date = Date.valueOf("2026-03-15");
        Schedule schedule = new Schedule(Time.valueOf("9:00:00"), Time.valueOf("12:30:00"));

        lenient().when(sessionRepo.save(any(Session.class))).thenReturn(null);
        lenient().when(instructorService.findInstructorById(instructorId)).thenReturn(instructor);
        lenient().when(courseRepo.findCourseById(courseId)).thenReturn(course);
        lenient().when(scheduleService.findSchedule()).thenReturn(schedule);

        ServiceException exception = assertThrows(ServiceException.class, () ->
                service.createSession(startTime, endTime, date, instructorId, courseId)
        );

        assertEquals("Cannot create a session outside of the center's open hours.", exception.getMessage());
        verify(sessionRepo, never()).save(any(Session.class));
    }
    
    @Test
    public void testCreateInvalidSessionBeforeOpening() {
    	//Setup
        Course course = new Course("yoga", "Beginner class", 44 );
        int courseId = 1;
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        int instructorId = 1;
        Time startTime = Time.valueOf("12:00:00");
        Time endTime = Time.valueOf("13:00:00");
        Date date = Date.valueOf("2026-03-15");
        Schedule schedule = new Schedule(Time.valueOf("12:30:00"), Time.valueOf("15:30:00"));

        lenient().when(sessionRepo.save(any(Session.class))).thenReturn(null);
        lenient().when(instructorService.findInstructorById(instructorId)).thenReturn(instructor);
        lenient().when(courseRepo.findCourseById(courseId)).thenReturn(course);
        lenient().when(scheduleService.findSchedule()).thenReturn(schedule);

        ServiceException exception = assertThrows(ServiceException.class, () ->
                service.createSession(startTime, endTime, date, instructorId, courseId)
        );

        assertEquals("Cannot create a session outside of the center's open hours.", exception.getMessage());
        verify(sessionRepo, never()).save(any(Session.class));
    }
    
    @Test
    public void testCreateInvalidSessionDate(){
        //Setup
        Course course = new Course("yoga", "Beginner class", 44 );
        int courseId = 1;
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        int instructorId = 1;
        Time startTime = Time.valueOf("10:00:00");
        Time endTime = Time.valueOf("12:00:00");
        Date date = Date.valueOf("2024-03-15");
        Schedule schedule = new Schedule(Time.valueOf("9:00:00"), Time.valueOf("16:00:00"));

        lenient().when(sessionRepo.save(any(Session.class))).thenReturn(null);
        lenient().when(instructorService.findInstructorById(instructorId)).thenReturn(instructor);
        lenient().when(courseRepo.findCourseById(courseId)).thenReturn(course);
        lenient().when(scheduleService.findSchedule()).thenReturn(schedule);

        ServiceException exception = assertThrows(ServiceException.class, () ->
                service.createSession(startTime, endTime, date, instructorId, courseId)
        );
        assertEquals("Cannot create a session on date that has passed.", exception.getMessage());
        verify(sessionRepo, never()).save(any(Session.class));
    }
    @Test
    public void testGetSpecificSession(){
        Course course = new Course("yoga", "Beginner class", 44 );
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        Schedule schedule = new Schedule(Time.valueOf("8:00:00"), Time.valueOf("13:00:00"));
        Time startTime = Time.valueOf("10:00:00");
        Time endTime = Time.valueOf("12:00:00");
        Date date = Date.valueOf("2024-03-15");
        Session session = new Session(startTime, endTime, date, instructor, course, schedule);
        int id = session.getId();

        lenient().when(sessionRepo.save(any(Session.class))).thenAnswer((InvocationOnMock i) -> i.getArgument(0));
        lenient().when(sessionRepo.findSessionById(id)).thenReturn(session);

        Session searchSession = service.findSessionById(id);

        assertEquals(searchSession, session);
        verify(sessionRepo, times(2)).findSessionById(id);
    }
    @Test
    public void testGetSpecificInvalidSession(){
        Course course = new Course("yoga", "Beginner class", 44 );
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        Schedule schedule = new Schedule(Time.valueOf("8:00:00"), Time.valueOf("13:00:00"));
        Time startTime = Time.valueOf("10:00:00");
        Time endTime = Time.valueOf("12:00:00");
        Date date = Date.valueOf("2024-03-15");
        Session session = new Session(startTime, endTime, date, instructor, course, schedule);
        int id = session.getId()+1;

        lenient().when(sessionRepo.save(any(Session.class))).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class, () ->
                service.findSessionById(id)
        );
        assertEquals("Session with inputted id is not found.", exception.getMessage());
        verify(sessionRepo, never()).save(any(Session.class));
    }
}
