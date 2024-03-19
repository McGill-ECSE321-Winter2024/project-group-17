package ca.mcgill.ecse321.SportCenterManager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.exception.ServiceException;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Session;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class EventServiceTests {
	@Mock
	InstructorAccountService instructorService;
	@Mock
	SessionRepository sessionRepo;
	@InjectMocks
	@Spy
	EventService service;
	
	@Test
	public void testFindInstructorSessions() {
		// setup
		InstructorAccount instructorOne = new InstructorAccount();
		int instructorId = 1;
		instructorOne.setId(instructorId);
		Iterable<Session> sessions = getExistingSessions();
		lenient().when(sessionRepo.findAll()).thenReturn((Iterable<Session>)sessions);
		
		// execution
		List<Session> returnedSessions = service.findInstructorSessions(instructorId);
		
		// assertions
		List<Session> expectedSessions = new ArrayList<Session>();
		for (Session session: sessions) {
			if (session.getInstructorAccount() != null && session.getInstructorAccount().getId() == instructorId) {
				expectedSessions.add(session);
			}
		}
		
		assertNotNull(returnedSessions);
		assertEquals(expectedSessions, returnedSessions);
		verify(sessionRepo, times(1)).findAll();
	}
	
	@Test
	public void testSuperviseSessionDuplicate() {
		// setup
		InstructorAccount instructorOne = new InstructorAccount();
		int instructorId = 1;
		instructorOne.setId(instructorId);
		List<Session> sessions = getExistingSessions();
		
		lenient().when(instructorService.findInstructorById(instructorId)).thenReturn(instructorOne);
		lenient().when(service.findSessionById(3)).thenReturn(sessions.get(2));
		
		// execution
		String msg = "";
		try {
			service.superviseSession(instructorId, 3);
		} catch (ServiceException e) {
			msg = e.getMessage();
		}
		
		// assertions
		assertEquals("An instructor is already supervising this session!", msg);
		verify(service, times(1)).findSessionById(3);
	}
	
	@Test
	public void testSuperviseSessionConflict() {
		// setup
		InstructorAccount instructorOne = new InstructorAccount();
		int instructorId = 1;
		instructorOne.setId(instructorId);
		List<Session> sessions = getExistingSessions();
		
		Session newSession = new Session();
		int sessionId = 5;
		newSession.setId(sessionId);
		newSession.setStartTime(Time.valueOf("20:30:00"));
		newSession.setEndTime(Time.valueOf("21:30:00"));
		newSession.setDate(Date.valueOf("2024-05-05"));
		
		lenient().when(sessionRepo.findAll()).thenReturn((Iterable<Session>) sessions);
		lenient().when(instructorService.findInstructorById(instructorId)).thenReturn(instructorOne);
		lenient().when(service.findSessionById(5)).thenReturn(newSession);
		
		// execution
		String msg = "";
		try {
			service.superviseSession(instructorId, 5);
		} catch (ServiceException e) {
			msg = e.getMessage();
		}
		
		// assertions
		assertEquals("Session overlaps with another that is already supervised by the instructor!", msg);
		verify(service, times(1)).findSessionById(5);
		verify(sessionRepo, times(1)).findAll();
		verify(instructorService).findInstructorById(instructorId);
	}
	
	@Test
	public void testSuperviseSessionValid() {
		// setup
		InstructorAccount instructorOne = new InstructorAccount();
		int instructorId = 1;
		instructorOne.setId(instructorId);
		List<Session> sessions = getExistingSessions();
		
		lenient().when(sessionRepo.findAll()).thenReturn((Iterable<Session>) sessions);
		lenient().when(instructorService.findInstructorById(instructorId)).thenReturn(instructorOne);
		lenient().when(service.findSessionById(4)).thenReturn(sessions.get(3));
		lenient().when(sessionRepo.save(sessions.get(3))).thenReturn(sessions.get(3));
		
		// execution
		Session resultSession = service.superviseSession(instructorId, 4);
		
		// assertions
		assertNotNull(resultSession);
		assertEquals(instructorId, resultSession.getInstructorAccount().getId());
		verify(service, times(1)).findSessionById(4);
		verify(sessionRepo, times(1)).findAll();
		verify(instructorService).findInstructorById(instructorId);
	}
	
	private List<Session> getExistingSessions(){
		// Assigns sessionOne and sessionTwo to instructorOne, sessionThree to instructorTwo, and sessionFour is unassigned
		InstructorAccount instructorOne = new InstructorAccount();
		instructorOne.setId(1);
		InstructorAccount instructorTwo = new InstructorAccount();
		instructorTwo.setId(2);
		List<Session> sessions = new ArrayList<Session>();
		
		Session sessionOne = new Session();
		sessionOne.setInstructorAccount(instructorOne);
		sessionOne.setStartTime(Time.valueOf("20:00:00"));
		sessionOne.setEndTime(Time.valueOf("21:00:00"));
		sessionOne.setDate(Date.valueOf("2024-05-05"));
		sessionOne.setId(1);
		sessions.add(sessionOne);
		
		Session sessionTwo = new Session();
		sessionTwo.setInstructorAccount(instructorOne);
		sessionTwo.setStartTime(Time.valueOf("20:00:00"));
		sessionTwo.setEndTime(Time.valueOf("21:00:00"));
		sessionTwo.setDate(Date.valueOf("2024-05-06"));
		sessionTwo.setId(2);
		sessions.add(sessionTwo);
		
		Session sessionThree = new Session();
		sessionThree.setStartTime(Time.valueOf("12:00:00"));
		sessionThree.setEndTime(Time.valueOf("13:00:00"));
		sessionThree.setDate(Date.valueOf("2024-05-05"));
		sessionThree.setInstructorAccount(instructorTwo);
		sessionThree.setId(3);
		sessions.add(sessionThree);
		
		Session sessionFour = new Session();
		sessionFour.setStartTime(Time.valueOf("8:00:00"));
		sessionFour.setEndTime(Time.valueOf("9:00:00"));
		sessionFour.setDate(Date.valueOf("2024-05-05"));
		sessionFour.setId(4);
		sessions.add(sessionFour);
		
		return sessions;
		
	}
}
