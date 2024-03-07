package ca.mcgill.ecse321.SportCenterManager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportCenterManager.dao.CourseRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.InstructorAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.ScheduleRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.dto.RegistrationResponseDto;
import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.model.Session;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RegistrationIntegrationTests {
	@Autowired
	private TestRestTemplate client;
	@Autowired
	private RegistrationRepository registrationRepo;
	@Autowired
	private CustomerAccountRepository customerRepo;
	@Autowired
	private SessionRepository sessionRepo;
	@Autowired
	private CourseRepository courseRepo;
	@Autowired
	private InstructorAccountRepository instructorRepo;
	@Autowired
	private ScheduleRepository scheduleRepo;
	
	private int customerId;
	private int sessionOneId;
	private int sessionTwoId;
	private int sessionThreeId;
	private int sessionFourId;
	
	@BeforeEach
	public void setupDatabase() {
		clearDatabase();
		populateDatabase();
	}
	
	@AfterEach
	public void cleanDatabase() {
//		clearDatabase();
	}
	
	private void clearDatabase() {
		registrationRepo.deleteAll();
		customerRepo.deleteAll();
		sessionRepo.deleteAll();
		instructorRepo.deleteAll();
		scheduleRepo.deleteAll();
		courseRepo.deleteAll();
	}
	
	private void populateDatabase() {
		// Prefills the database with information that would've been in there at the point of registration
		CustomerAccount customer = new CustomerAccount("Tibo", "tibo123@gmail.com", "password#123");
		this.customerId = customerRepo.save(customer).getId();
		CustomerAccount otherCustomer = new CustomerAccount("Mahmoud", "MahmoudA2@gmail.com", "smolPassword2@");
		customerRepo.save(otherCustomer);
		
		InstructorAccount instructorOne = new InstructorAccount("Haoyuan", "5g@gmail.com", "bigPassword4@");
		instructorRepo.save(instructorOne);
		InstructorAccount instructorTwo = new InstructorAccount("Namir", "6g@gmail.com", "midPassword3@");
		instructorRepo.save(instructorTwo);
		
		Course courseOne = new Course();
		courseOne.setName("course1");
		courseRepo.save(courseOne);
		Course courseTwo = new Course();
		courseTwo.setName("course2");
		courseRepo.save(courseTwo);
		
		Schedule schedule = new Schedule();
		scheduleRepo.save(schedule);
		
		Session sessionOne = new Session(Time.valueOf("20:00:00"), Time.valueOf("21:00:00"), Date.valueOf("2024-05-05"), 
				instructorOne, courseOne, schedule);
		sessionOneId = sessionRepo.save(sessionOne).getId();
		Session sessionTwo = new Session(Time.valueOf("20:00:00"), Time.valueOf("21:00:00"), Date.valueOf("2024-05-06"), 
				instructorOne, courseOne, schedule);
		sessionTwoId = sessionRepo.save(sessionTwo).getId();
		Session sessionThree = new Session(Time.valueOf("12:00:00"), Time.valueOf("13:00:00"), Date.valueOf("2024-05-05"), 
				instructorOne, courseOne, schedule);
		sessionThreeId = sessionRepo.save(sessionThree).getId();
		Session sessionFour = new Session(Time.valueOf("19:30:00"), Time.valueOf("20:30:00"), Date.valueOf("2024-05-05"), 
				instructorTwo, courseTwo, schedule);
		sessionFourId = sessionRepo.save(sessionFour).getId();
	}
	
	@Test
	public void testRegister() {
		ResponseEntity<RegistrationResponseDto> response = client.exchange("/registrations/" + Integer.toString(customerId) + "/" + Integer.toString(sessionOneId), 
				HttpMethod.PUT, null, RegistrationResponseDto.class);
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(customerId, response.getBody().getCustomer().getId());
		assertEquals(sessionOneId, response.getBody().getSession().getId());
	}
	
}
