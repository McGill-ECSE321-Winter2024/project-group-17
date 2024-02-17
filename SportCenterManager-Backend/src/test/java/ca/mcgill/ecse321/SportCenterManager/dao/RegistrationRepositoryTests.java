package ca.mcgill.ecse321.SportCenterManager.dao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Registration;
import ca.mcgill.ecse321.SportCenterManager.model.Session;

@SpringBootTest
public class RegistrationRepositoryTests {
	@Autowired
	private RegistrationRepository registrationRepo;
	@Autowired
	private SessionRepository sessionRepo;
	@Autowired
	private CustomerAccountRepository customerAccountRepo;
	@Autowired
	private InstructorAccountRepository instructorAccountRepo;
	@Autowired
	private CourseRepository courseRepo;
	
	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		registrationRepo.deleteAll();
		sessionRepo.deleteAll();
		customerAccountRepo.deleteAll();
		instructorAccountRepo.deleteAll();
		courseRepo.deleteAll();
	}
	
	@Test
	public void testCreateAndReadRegistration() {
		// Create & Persist an Instructor Account
        String insName = "Tibo";
        String insEmail = "Tibo@gmail.com";
        String insPassword = "ee-ar";
		InstructorAccount instructor = new InstructorAccount(insName, insEmail, insPassword);
		instructor = instructorAccountRepo.save(instructor);
		
		// Create & persist a Course
		String courseName = "course1";
		String description = "A new course";
		int cost = 25;
		Course course = new Course(courseName, description, cost);
		course = courseRepo.save(course);
		
		// Create & persist a Session
		Time startTime = Time.valueOf("08:00:00");
		Time endTime = Time.valueOf("16:30:00");
		Date date = Date.valueOf("2024-02-20");
		Session session = new Session(startTime, endTime, date, instructor, course);
		session = sessionRepo.save(session);
		
		// Create & persist a CustomerAccount
		String name = "Tibo";
		String email = "Tibo@gmail.com";
		String password = "djivede123";
		CustomerAccount customer = new CustomerAccount(name, email, password);
		customer = customerAccountRepo.save(customer);
		
		// Create a Registration
		Registration.Key key = new Registration.Key(session, customer);
		Registration registration = new Registration(key);
		
		// Write registration into db
		registration = registrationRepo.save(registration);
		
		// Read registration from db
		Registration persistedRegistration = registrationRepo.findRegistrationByKey(key);
	
		// Validating result against expectations
		// Validating if the key is persisted
		assertNotNull(persistedRegistration);
		assertNotNull(persistedRegistration.getKey());
		
		// Validating if the session is persisted
		assertEquals(session.getId(), persistedRegistration.getKey().getSession().getId());
		assertEquals(startTime ,persistedRegistration.getKey().getSession().getStartTime());
		assertEquals(endTime, persistedRegistration.getKey().getSession().getEndTime());
		assertEquals(date, persistedRegistration.getKey().getSession().getDate());
		
		// Validating if the customerAccount is persisted
		assertEquals(customer.getId(), persistedRegistration.getKey().getCustomerAccount().getId());
		assertEquals(name, persistedRegistration.getKey().getCustomerAccount().getName());
		assertEquals(email, persistedRegistration.getKey().getCustomerAccount().getEmail());
		assertEquals(password, persistedRegistration.getKey().getCustomerAccount().getPassword());
		
	}
}