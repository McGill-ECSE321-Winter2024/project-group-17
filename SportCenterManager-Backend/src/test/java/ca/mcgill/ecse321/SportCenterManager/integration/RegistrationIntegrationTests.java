package ca.mcgill.ecse321.SportCenterManager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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
import ca.mcgill.ecse321.SportCenterManager.dto.CustomerListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CustomerResponseDto;
import ca.mcgill.ecse321.SportCenterManager.dto.ErrorDto;
import ca.mcgill.ecse321.SportCenterManager.dto.RegistrationListDto;
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
	
	private int customerOneId;
	private int customerTwoId;
	private int customerThreeId;
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
		clearDatabase();
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
		CustomerAccount customerOne = new CustomerAccount("Tibo", "tibo123@gmail.com", "password#123");
		this.customerOneId = customerRepo.save(customerOne).getId();
		CustomerAccount customerTwo = new CustomerAccount("Mahmoud", "MahmoudA2@gmail.com", "smolPassword2@");
		this.customerTwoId = customerRepo.save(customerTwo).getId();
		CustomerAccount customerThree = new CustomerAccount("Eric", "ez@gmail.com", "pass1#");
		this.customerThreeId = customerRepo.save(customerThree).getId();
		
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
	@Order(1)
	public void testRegisterValid() {
		registerCustomerValid(customerOneId, sessionOneId);
	}
	

	
	@Test
	@Order(2)
	public void testRegisterWithDuplicate() {
		registerCustomerValid(customerOneId, sessionOneId);
		ResponseEntity<ErrorDto> response = client.exchange("/registrations/" + Integer.toString(customerOneId) + "/" + Integer.toString(sessionOneId), 
				HttpMethod.PUT, null, ErrorDto.class);
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals("Failed to Register: You are already registered to this session!", response.getBody().getMessage());
	}
	
	@Test
	@Order(3)
	public void testRegisterWithConflict() {
		registerCustomerValid(customerOneId, sessionOneId);
		ResponseEntity<ErrorDto> response = client.exchange("/registrations/" + Integer.toString(customerOneId) + "/" + Integer.toString(sessionFourId), 
				HttpMethod.PUT, null, ErrorDto.class);
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals("Failed to Register: Session overlaps with an existing registration!", response.getBody().getMessage());
	}
	
	@Test
	@Order(4)
	public void testFindAllRegistrationsByCustomer() {
		registerCustomerValid(customerOneId, sessionOneId);
		registerCustomerValid(customerOneId, sessionTwoId);
		registerCustomerValid(customerOneId, sessionThreeId);
		registerCustomerValid(customerTwoId, sessionOneId);
		
		ResponseEntity<RegistrationListDto> response = client.getForEntity("/registrations/customers/" + Integer.toString(customerOneId), 
				RegistrationListDto.class);
		RegistrationListDto responseBody = response.getBody();
		List<RegistrationResponseDto> registrationDtos = responseBody.getRegistrations();
		
		assertNotNull(response);
		assertNotNull(responseBody);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(registrationDtos.size(), 3);
		assertTrue(containsRegistration(sessionOneId, registrationDtos));
		assertTrue(containsRegistration(sessionTwoId, registrationDtos));
		assertTrue(containsRegistration(sessionThreeId, registrationDtos));
	}
	
	@Test
	@Order(5)
	public void testFindAllSessionRegistrants() {
		registerCustomerValid(customerOneId, sessionOneId);
		registerCustomerValid(customerThreeId, sessionOneId);
		registerCustomerValid(customerTwoId, sessionTwoId);
		
		ResponseEntity<CustomerListDto> response = client.getForEntity("/registrations/" + Integer.toString(sessionOneId) + "/customers", 
				CustomerListDto.class);
		CustomerListDto responseBody = response.getBody();
		List<CustomerResponseDto> customerDtos = responseBody.getCustomers();
		
		assertNotNull(response);
		assertNotNull(responseBody);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, customerDtos.size());
		assertTrue(containsCustomer(customerOneId, customerDtos));
		assertTrue(containsCustomer(customerThreeId, customerDtos));
		
	}
	
	@Test
	@Order(6)
	public void testCancelRegistration() {
		registerCustomerValid(customerOneId, sessionOneId);
		registerCustomerValid(customerTwoId, sessionOneId);
		
		ResponseEntity<CustomerListDto> responseBeforeDelete = client.getForEntity("/registrations/" + Integer.toString(sessionOneId) + "/customers", 
				CustomerListDto.class);
		CustomerListDto preDeleteBody = responseBeforeDelete.getBody();
		List<CustomerResponseDto> initialCustomerDtos = preDeleteBody.getCustomers();
		
		client.delete("/registrations/" + Integer.toString(customerOneId) + "/" + Integer.toString(sessionOneId));
		
		ResponseEntity<CustomerListDto> responseAfterDelete = client.getForEntity("/registrations/" + Integer.toString(sessionOneId) + "/customers", 
				CustomerListDto.class);
		CustomerListDto postDeleteBody = responseAfterDelete.getBody();
		List<CustomerResponseDto> finalCustomerDtos = postDeleteBody.getCustomers();
		
		assertNotNull(responseBeforeDelete);
		assertNotNull(responseAfterDelete);
		assertNotNull(preDeleteBody);
		assertNotNull(postDeleteBody);
		assertEquals(HttpStatus.OK, responseBeforeDelete.getStatusCode());
		assertEquals(HttpStatus.OK, responseAfterDelete.getStatusCode());
		assertEquals(2, initialCustomerDtos.size());
		assertEquals(1, finalCustomerDtos.size());
		assertTrue(containsCustomer(customerTwoId, finalCustomerDtos));
	}
	
	private void registerCustomerValid(int customerId, int sessionId) {
		ResponseEntity<RegistrationResponseDto> response = client.exchange("/registrations/" + Integer.toString(customerId) + "/" + Integer.toString(sessionId), 
				HttpMethod.PUT, null, RegistrationResponseDto.class);
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(customerId, response.getBody().getCustomer().getId());
		assertEquals(sessionId, response.getBody().getSession().getId());
	}
	
	private boolean containsRegistration(int sessionId, List<RegistrationResponseDto> registrationDtos) {
		for (RegistrationResponseDto registration: registrationDtos) {
			if (registration.getSession().getId() == sessionId) {
				return true;
			}
		}
		return false;
	}
	
	private boolean containsCustomer(int customerId, List<CustomerResponseDto> customerDtos) {
		for (CustomerResponseDto customer : customerDtos) {
			if (customer.getId() == customerId) {
				return true;
			}
		}
		return false;
	}
	
}
