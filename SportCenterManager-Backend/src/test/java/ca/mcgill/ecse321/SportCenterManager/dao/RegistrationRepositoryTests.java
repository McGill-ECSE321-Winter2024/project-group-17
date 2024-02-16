package ca.mcgill.ecse321.SportCenterManager.dao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
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
	
	
	@AfterEach
	public void clearDatabase() {
		registrationRepo.deleteAll();
		sessionRepo.deleteAll();
		customerAccountRepo.deleteAll();
	}
	
	@Test
	public void testCreateAndReadRegistration() {
		// Create a Session
		Time startTime = Time.valueOf("08:00:00");
		Time endTime = Time.valueOf("16:30:00");
		Date date = Date.valueOf("2024-02-20");
		Session session = new Session(startTime, endTime, date, null, null);
		
		// Create a CustomerAccount
		String name = "Tibo";
		String email = "Tibo@gmail.com";
		String password = "djivede123";
		CustomerAccount customer = new CustomerAccount(name, email, password);
		
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
		assertEquals(persistedRegistration.getKey().getSession().getId(), session.getId());
		assertEquals(persistedRegistration.getKey().getSession().getStartTime(), startTime);
		assertEquals(persistedRegistration.getKey().getSession().getEndTime(), endTime);
		assertEquals(persistedRegistration.getKey().getSession().getDate(), date);
		
		// Validating if the customerAccount is persisted
		assertEquals(persistedRegistration.getKey().getCustomerAccount().getId(), customer.getId());
		assertEquals(persistedRegistration.getKey().getCustomerAccount().getName(), name);
		assertEquals(persistedRegistration.getKey().getCustomerAccount().getEmail(), email);
		assertEquals(persistedRegistration.getKey().getCustomerAccount().getPassword(), password);
		
	}
}
