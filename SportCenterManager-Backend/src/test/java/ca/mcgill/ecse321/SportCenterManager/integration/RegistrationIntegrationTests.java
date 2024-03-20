package ca.mcgill.ecse321.SportCenterManager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportCenterManager.dao.BillingInformationRepository;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
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
	@Autowired
	private BillingInformationRepository billingRepo;
	
	private int customerOneId;
	private int customerTwoId;
	private int sessionOneId;
	private int sessionTwoId;
	private int sessionThreeId;
	private int sessionFourId;
	private int sessionFiveId;
	private int sessionSixId;
	private int sessionSevenId;
	private int courseOneId;
	private int courseTwoId;
	
	@BeforeAll
	public void setupDatabase() {
		clearDatabase();
		populateDatabase();
	}
	
	@AfterAll
	public void cleanDatabase() {
		clearDatabase();
	}
	
	private void clearDatabase() {
		registrationRepo.deleteAll();
		billingRepo.deleteAll();
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
		
		InstructorAccount instructorOne = new InstructorAccount("Haoyuan", "5g@gmail.com", "bigPassword4@");
		instructorRepo.save(instructorOne);
		InstructorAccount instructorTwo = new InstructorAccount("Namir", "6g@gmail.com", "midPassword3@");
		instructorRepo.save(instructorTwo);
		
		Course courseOne = new Course();
		courseOne.setName("course1");
		courseOneId = courseRepo.save(courseOne).getId();
		
		
		Course courseTwo = new Course();
		courseTwo.setName("course2");
		courseTwoId = courseRepo.save(courseTwo).getId();
		
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
		Session sessionFive = new Session(Time.valueOf("20:30:00"), Time.valueOf("21:30:00"), Date.valueOf("2024-05-05"), 
				instructorOne, courseOne, schedule);
		sessionFiveId = sessionRepo.save(sessionFive).getId();
		Session sessionSix = new Session(Time.valueOf("19:30:00"), Time.valueOf("20:30:00"), Date.valueOf("2024-05-05"), 
				instructorOne, courseOne, schedule);
		sessionSixId = sessionRepo.save(sessionSix).getId();
		Session sessionSeven = new Session(Time.valueOf("19:00:00"), Time.valueOf("22:00:00"), Date.valueOf("2024-05-05"), 
				instructorOne, courseOne, schedule);
		sessionSevenId = sessionRepo.save(sessionSeven).getId();
	}
	
	@Test
	@Order(1)
	public void testRegisterValid() {
		registerCustomerValid(customerOneId, sessionOneId, courseOneId);
	}
	

	
	@Test
	@Order(2)
	public void testRegisterWithDuplicate() {
		//Execution
		ResponseEntity<ErrorDto> response = client.exchange("/courses/" + Integer.toString(courseOneId) + "/sessions/"
		+ Integer.toString(sessionOneId) + "/registrations/" + Integer.toString(customerOneId) , 
				HttpMethod.PUT, null, ErrorDto.class);
		
		//Assertions
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals("Failed to Register: You are already registered to this session!", response.getBody().getMessage());
	}
	
	@Test
	@Order(3)
	public void testRegisterWithSameTimeConflict() {
		//Execution
		ResponseEntity<ErrorDto> response = client.exchange("/courses/" + Integer.toString(courseTwoId) + "/sessions/"
				+ Integer.toString(sessionFourId) + "/registrations/" + Integer.toString(customerOneId), 
				HttpMethod.PUT, null, ErrorDto.class);
		//Assertions
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals("Failed to Register: Session overlaps with an existing registration!", response.getBody().getMessage());
	}
	
	@Test
	@Order(4)
	public void testRegisterWithEndTimeConflict() {
		//Execution
		ResponseEntity<ErrorDto> response = client.exchange("/courses/" + Integer.toString(courseOneId) + "/sessions/"
				+ Integer.toString(sessionFiveId) + "/registrations/" + Integer.toString(customerOneId), 
				HttpMethod.PUT, null, ErrorDto.class);
		//Assertions
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals("Failed to Register: Session overlaps with an existing registration!", response.getBody().getMessage());
	}
	
	@Test
	@Order(5)
	public void testRegisterWithStartTimeConflict() {
		//Execution
		ResponseEntity<ErrorDto> response = client.exchange("/courses/" + Integer.toString(courseOneId) + "/sessions/"
				+ Integer.toString(sessionSixId) + "/registrations/" + Integer.toString(customerOneId), 
				HttpMethod.PUT, null, ErrorDto.class);
		//Assertions
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals("Failed to Register: Session overlaps with an existing registration!", response.getBody().getMessage());
	}
	
	@Test
	@Order(6)
	public void testRegisterWithCompleteOverlapConflict() {
		//Execution
		ResponseEntity<ErrorDto> response = client.exchange("/courses/" + Integer.toString(courseOneId) + "/sessions/"
				+ Integer.toString(sessionSevenId) + "/registrations/" + Integer.toString(customerOneId), 
				HttpMethod.PUT, null, ErrorDto.class);
		//Assertions
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals("Failed to Register: Session overlaps with an existing registration!", response.getBody().getMessage());
	}
	
	@Test
	@Order(7)
	public void testFindAllRegistrationsByCustomer() {
		//Setup
		registerCustomerValid(customerOneId, sessionTwoId, courseOneId);
		registerCustomerValid(customerOneId, sessionThreeId, courseOneId);
		registerCustomerValid(customerTwoId, sessionOneId, courseOneId);
		
		//Execution
		ResponseEntity<RegistrationListDto> response = client.getForEntity("/customerAccounts/" + Integer.toString(customerOneId) + "/registrations", 
				RegistrationListDto.class);
		RegistrationListDto responseBody = response.getBody();
		List<RegistrationResponseDto> registrationDtos = responseBody.getRegistrations();
		
		//Assertions
		assertNotNull(response);
		assertNotNull(responseBody);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(registrationDtos.size(), 3);
		assertTrue(containsRegistration(sessionOneId, registrationDtos));
		assertTrue(containsRegistration(sessionTwoId, registrationDtos));
		assertTrue(containsRegistration(sessionThreeId, registrationDtos));
	}
	
	@Test
	@Order(8)
	public void testFindAllSessionRegistrants() {
		//Execution
		ResponseEntity<CustomerListDto> response = client.getForEntity("/courses/" + Integer.toString(courseOneId) + "/sessions/"
				+ Integer.toString(sessionOneId) + "/registrations/customers", CustomerListDto.class);
		CustomerListDto responseBody = response.getBody();
		List<CustomerResponseDto> customerDtos = responseBody.getCustomers();
		
		//Assertions
		assertNotNull(response);
		assertNotNull(responseBody);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, customerDtos.size());
		assertTrue(containsCustomer(customerOneId, customerDtos));
		assertTrue(containsCustomer(customerTwoId, customerDtos));
		
	}
	
	@Test
	@Order(9)
	public void testCancelRegistration() {
		//Setup
		ResponseEntity<CustomerListDto> responseBeforeDelete = client.getForEntity("/courses/" + Integer.toString(courseOneId) + "/sessions/"
				+ Integer.toString(sessionOneId) + "/registrations/customers", CustomerListDto.class);
		CustomerListDto preDeleteBody = responseBeforeDelete.getBody();
		List<CustomerResponseDto> initialCustomerDtos = preDeleteBody.getCustomers();
		
		//Execution
		client.delete("/courses/" + Integer.toString(courseOneId) + "/sessions/"
				+ Integer.toString(sessionOneId) + "/registrations/" + Integer.toString(customerOneId));
		
		//Assertions
		ResponseEntity<CustomerListDto> responseAfterDelete = client.getForEntity("/courses/" + Integer.toString(courseOneId) + "/sessions/"
				+ Integer.toString(sessionOneId) + "/registrations/customers", CustomerListDto.class);
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
	
	/*------------------------HELPER METHODS-------------------------*/
	private void registerCustomerValid(int customerId, int sessionId, int courseId) {
		ResponseEntity<RegistrationResponseDto> response = client.exchange("/courses/" + Integer.toString(courseId) + "/sessions/"
				+ Integer.toString(sessionId) + "/registrations/" + Integer.toString(customerId), 
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
