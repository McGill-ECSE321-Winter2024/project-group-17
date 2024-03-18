package ca.mcgill.ecse321.SportCenterManager.service;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
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
    @InjectMocks
    private EventService service;
    @Test
    public void testDeleteSession(){
        //Setup
        int sessionId = 1234;

        doNothing().when(sessionRepo).deleteById(sessionId);

        boolean deleted = service.deleteSessionById(sessionId);

        //Is it called the correct number of times?
        verify(sessionRepo, times(1)).findById(sessionId);
        verify(sessionRepo, times(1)).deleteById(sessionId);

        assertTrue(deleted,"The session got deleted successfully!");
    }

    @Test
    public void testDeleteInvalidSession(){
        //Setup
        int wrongId = 34;
        when(sessionRepo.findById(wrongId)).thenReturn(null);

        boolean deleted = service.deleteSessionById(wrongId);

        //Is it called the correct number of times?
        verify(sessionRepo, times(1)).findById(wrongId);
        verify(sessionRepo, times(0)).deleteById(wrongId);
        verifyNoMoreInteractions(sessionRepo);

        assertFalse(deleted, "The session was not found!");
    }

    @Test
    public void testModifyValidSession(){
        //Setup
        Course course = new Course("yoga", "Beginner class", 44 );
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        Schedule schedule = new Schedule(Time.valueOf("8:00:00"), Time.valueOf("13:00:00"));
        Time startTime = Time.valueOf("10:00:00");
        Time endTime = Time.valueOf("12:00:00");
        Date date = Date.valueOf("2024-03-15");
        Session session = new Session(startTime, endTime, date, instructor, course, schedule);
        int id = session.getId();

        Time newStartTime = Time.valueOf("11:00:00");
        Time newEndTime = Time.valueOf("13:00:00");
        Date newDate = Date.valueOf("2024-04-16");
        Course newCourse = new Course("Zumba", "Intermediate class", 50 );
        InstructorAccount newInstructor = new InstructorAccount("Jannett", "jannett@gmail.com", "abcd");
        Schedule newSchedule = new Schedule(Time.valueOf("9:00:00"), Time.valueOf("16:00:00"));

        when(sessionRepo.save(any(Session.class))).thenAnswer((InvocationOnMock i) -> i.getArgument(0));
        when(sessionRepo.findSessionById(id)).thenReturn(session);

        Session modifiedSession = service.modifySessionById(id, newStartTime,newEndTime,newDate,newCourse, newInstructor,newSchedule);

        assertNotNull(modifiedSession);
        assertEquals(newStartTime, modifiedSession.getStartTime());
        assertEquals(newEndTime, modifiedSession.getEndTime());
        assertEquals(newDate, modifiedSession.getDate());
        assertEquals(newInstructor, modifiedSession.getInstructorAccount());
        assertEquals(newCourse, modifiedSession.getCourse());
        assertEquals(newSchedule, modifiedSession.getSchedule());
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
        Date newDate = Date.valueOf("2024-04-16");
        Course newCourse = new Course("Zumba", "Intermediate class", 50 );
        InstructorAccount newInstructor = new InstructorAccount("Jannett", "jannett@gmail.com", "abcd");
        Schedule newSchedule = new Schedule(Time.valueOf("9:00:00"), Time.valueOf("16:00:00"));

        lenient().when(sessionRepo.save(any(Session.class))).thenReturn(null);
        lenient().when(sessionRepo.findSessionById(id)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.modifySessionById(id, newStartTime,newEndTime,newDate,newCourse, newInstructor,newSchedule)
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
        InstructorAccount newInstructor = new InstructorAccount("Jannett", "jannett@gmail.com", "abcd");
        Schedule newSchedule = new Schedule(Time.valueOf("9:00:00"), Time.valueOf("16:00:00"));

        lenient().when(sessionRepo.save(any(Session.class))).thenReturn(null);
        lenient().when(sessionRepo.findSessionById(id)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.modifySessionById(id, newStartTime,newEndTime,newDate,newCourse, newInstructor,newSchedule)
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
        Date newDate = Date.valueOf("2024-04-15");
        Course newCourse = new Course("Zumba", "Intermediate class", 50 );
        InstructorAccount newInstructor = new InstructorAccount("Jannett", "jannett@gmail.com", "abcd");
        Schedule newSchedule = new Schedule(Time.valueOf("9:00:00"), Time.valueOf("16:00:00"));

        lenient().when(sessionRepo.save(any(Session.class))).thenReturn(null);
        lenient().when(sessionRepo.findSessionById(id)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.modifySessionById(id, newStartTime,newEndTime,newDate,newCourse, newInstructor,newSchedule)
        );

        assertEquals("Session with inputted id is not found.", exception.getMessage());
        verify(sessionRepo, never()).save(any(Session.class));

    }

    @Test
    public void testCreateValidSession(){
        //Set up
        Course course = new Course("yoga", "Beginner class", 44);
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        Schedule schedule = new Schedule(Time.valueOf("8:00:00"), Time.valueOf("13:00:00"));
        Time startTime = Time.valueOf("10:00:00");
        Time endTime = Time.valueOf("12:00:00");
        Date date = Date.valueOf("2024-04-15");
        Session session = new Session(startTime, endTime,date,instructor,course, schedule);

        when(sessionRepo.save(any(Session.class))).thenReturn(session);

        //Act
        Session createdSession = service.createSession(startTime,endTime,date,instructor,course,schedule);

        //Assert
        assertNotNull(createdSession);
        assertEquals(startTime, createdSession.getStartTime());
        assertEquals(endTime, createdSession.getEndTime());
        assertEquals(date, createdSession.getDate());
        assertEquals(instructor, createdSession.getInstructorAccount());
        assertEquals(course, createdSession.getCourse());
        assertEquals(schedule, createdSession.getSchedule());

        verify(sessionRepo, times(0)).save(createdSession);

    }
    @Test
    public void testCreateInvalidSessionTime(){
        //Setup
        Course course = new Course("yoga", "Beginner class", 44 );
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        Schedule schedule = new Schedule(Time.valueOf("8:00:00"), Time.valueOf("13:00:00"));
        Time startTime = Time.valueOf("13:00:00");
        Time endTime = Time.valueOf("12:00:00");
        Date date = Date.valueOf("2024-03-15");
        Session session = new Session(startTime,endTime,date,instructor,course, schedule);

        lenient().when(sessionRepo.save(any(Session.class))).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.createSession(startTime, endTime, date, instructor, course, schedule)
        );

        assertEquals("End time must be be after the start time.", exception.getMessage());
        verify(sessionRepo, never()).save(any(Session.class));
    }

    @Test
    public void testCreateInvalidSessionDate(){
        //Setup
        Course course = new Course("yoga", "Beginner class", 44 );
        InstructorAccount instructor = new InstructorAccount("Bob", "bob@gmail.com", "1234");
        Schedule schedule = new Schedule(Time.valueOf("8:00:00"), Time.valueOf("13:00:00"));
        Time startTime = Time.valueOf("10:00:00");
        Time endTime = Time.valueOf("12:00:00");
        Date date = Date.valueOf("2024-03-15");
        Session session = new Session(startTime,endTime,date,instructor,course, schedule);

        lenient().when(sessionRepo.save(any(Session.class))).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.createSession(startTime, endTime, date, instructor, course, schedule)
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

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.findSessionById(id)
        );
        assertEquals("Session with inputted id is not found.", exception.getMessage());
        verify(sessionRepo, never()).save(any(Session.class));
    }
}
