package ca.mcgill.ecse321.SportCenterManager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Registration;
import ca.mcgill.ecse321.SportCenterManager.model.Session;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTests {
	@Mock
	private CustomerAccountRepository customerRepo;
	@Mock
	private SessionRepository sessionRepo;
	@Mock
	private RegistrationRepository registrationRepo;
	@InjectMocks
	private RegistrationService service;

	@Test
	public void testFindCustomerRegistrationsNonEmpty() {
		// setup
		CustomerAccount customer = createCustomer();
		int customerId = 20;
		Iterable<Registration> existingRegistrations = getExistingRegistrations(customer);
		lenient().when(customerRepo.findCustomerAccountById(customerId)).thenReturn(customer);
		lenient().when(registrationRepo.findAll()).thenReturn(existingRegistrations);
		
		// execution
		List<Registration> registrations = service.findCustomerRegistrations(customerId);	
		
		// assertions
		List<Registration> expectedRegistrations = new ArrayList<Registration>();
		for (Registration reg : existingRegistrations) {
			if (reg.getKey().getCustomerAccount().getId() == customerId) {
				expectedRegistrations.add(reg);
			}
		}
		assertEquals(expectedRegistrations, registrations);
		verify(registrationRepo, times(1)).findAll();
	}
	
	@Test
	public void testFindCustomerRegistrationsEmpty() {
		// setup
		CustomerAccount customer = createCustomer();
		int customerId = 20;
		lenient().when(customerRepo.findCustomerAccountById(customerId)).thenReturn(customer);
		lenient().when(registrationRepo.findAll()).thenReturn((Iterable<Registration>) new ArrayList<Registration>());
		
		// execution
		List<Registration> registrations = service.findCustomerRegistrations(customerId);	
		
		// assertions
		assertEquals(new ArrayList<Registration>(), registrations);
		verify(registrationRepo, times(1)).findAll();
	}
	
	@Test
	public void testRegisterValidSession() {
		// setup
		CustomerAccount customer = createCustomer();
		int customerId = 20;
		lenient().when(customerRepo.findCustomerAccountById(customerId)).thenReturn(customer);
		
		Session session = new Session();
		session.setStartTime(Time.valueOf("8:00:00"));
		session.setEndTime(Time.valueOf("9:00:00"));
		session.setDate(Date.valueOf("2024-05-05"));
		int sessionId = 25;
		lenient().when(sessionRepo.findSessionById(sessionId)).thenReturn(session);
		
		lenient().when(registrationRepo.findAll()).thenReturn(getExistingRegistrations(customer));
		lenient().when(registrationRepo.findRegistrationByKey(any(Registration.Key.class))).thenReturn(null);
		
		// execution
		Registration registration = service.register(customerId, sessionId);
		lenient().when(registrationRepo.save(any(Registration.class))).thenReturn(registration);
		
		// assertions
		assertNotNull(registration);
		assertEquals(customer, registration.getKey().getCustomerAccount());
		assertEquals(session, registration.getKey().getSession());
		verify(customerRepo, times(2)).findCustomerAccountById(customerId);
		verify(sessionRepo, times(1)).findSessionById(sessionId);
		verify(registrationRepo, times(1)).findAll();
		verify(registrationRepo, times(1)).findRegistrationByKey(any(Registration.Key.class));
		verify(registrationRepo, times(1)).save(any(Registration.class));
	}
	
	@Test
	public void testRegisterConflictingSession() {
		CustomerAccount customer = createCustomer();
		int customerId = 20;
		lenient().when(customerRepo.findCustomerAccountById(customerId)).thenReturn(customer);
		
		Session session = new Session();
		session.setStartTime(Time.valueOf("20:30:00"));
		session.setEndTime(Time.valueOf("21:30:00"));
		session.setDate(Date.valueOf("2024-05-05"));
		int sessionId = 25;
		lenient().when(sessionRepo.findSessionById(sessionId)).thenReturn(session);
		
		lenient().when(registrationRepo.findAll()).thenReturn(getExistingRegistrations(customer));
		lenient().when(registrationRepo.findRegistrationByKey(any(Registration.Key.class))).thenReturn(null);
		String msg = "";
		try {
			Registration registration = service.register(customerId, sessionId);
			lenient().when(registrationRepo.save(any(Registration.class))).thenReturn(registration);
		} catch (IllegalArgumentException e) {
			msg = e.getMessage();
		}
		
		assertEquals("Failed to Register: Session overlaps with an existing registration!", msg);
		verify(registrationRepo, times(0)).save(any(Registration.class));
	}
	
	@Test
	public void testRegisterDuplicate() {
		CustomerAccount customer = createCustomer();
		int customerId = 20;
		lenient().when(customerRepo.findCustomerAccountById(customerId)).thenReturn(customer);
		
		Session session = new Session();
		session.setStartTime(Time.valueOf("20:00:00"));
		session.setEndTime(Time.valueOf("21:00:00"));
		session.setDate(Date.valueOf("2024-05-05"));
		int sessionId = 25;
		lenient().when(sessionRepo.findSessionById(sessionId)).thenReturn(session);
		
		lenient().when(registrationRepo.findAll()).thenReturn(getExistingRegistrations(customer));
		Registration existingRegistration = new Registration(new Registration.Key(session, customer));
		lenient().when(registrationRepo.findRegistrationByKey(new Registration.Key(session, customer))).thenReturn(existingRegistration);
		String msg = "";
		try {
			Registration registration = service.register(customerId, sessionId);
			lenient().when(registrationRepo.save(any(Registration.class))).thenReturn(registration);
		} catch (IllegalArgumentException e) {
			msg = e.getMessage();
		}
		
		assertEquals("Failed to Register: You are already registered to this session!", msg);
		verify(registrationRepo, times(0)).save(any(Registration.class));
	}
	
	private CustomerAccount createCustomer() {
		CustomerAccount customer = new CustomerAccount();
		customer.setEmail("validEmail@gmail.com");
		customer.setPassword("validPassword-123");
		customer.setName("customer1");
		customer.setId(20);
		return customer;
	}
	
	private Iterable<Registration> getExistingRegistrations(CustomerAccount customer) {
		List<Registration> registrations = new ArrayList<Registration>();
		
		Session sessionOne = new Session();
		sessionOne.setStartTime(Time.valueOf("20:00:00"));
		sessionOne.setEndTime(Time.valueOf("21:00:00"));
		sessionOne.setDate(Date.valueOf("2024-05-05"));
		Registration registrationOne = new Registration(new Registration.Key(sessionOne, customer));
		registrations.add(registrationOne);
		
		Session sessionTwo = new Session();
		sessionTwo.setStartTime(Time.valueOf("20:00:00"));
		sessionTwo.setEndTime(Time.valueOf("21:00:00"));
		sessionTwo.setDate(Date.valueOf("2024-05-06"));
		Registration registrationTwo = new Registration(new Registration.Key(sessionTwo, customer));
		registrations.add(registrationTwo);

		Session sessionThree = new Session();
		sessionThree.setStartTime(Time.valueOf("12:00:00"));
		sessionThree.setEndTime(Time.valueOf("13:00:00"));
		sessionThree.setDate(Date.valueOf("2024-05-05"));
		Registration registrationThree = new Registration(new Registration.Key(sessionThree, customer));
		registrations.add(registrationThree);
		
		CustomerAccount otherCustomer = new CustomerAccount("otherCustomer", "validEmail2@gmail.com", "valid-pass20");
		otherCustomer.setId(10);
		
		Session sessionFour = new Session();
		sessionFour.setStartTime(Time.valueOf("8:00:00"));
		sessionFour.setEndTime(Time.valueOf("9:00:00"));
		sessionFour.setDate(Date.valueOf("2024-05-05"));
		Registration registrationFour = new Registration(new Registration.Key(sessionFour, otherCustomer));
		registrations.add(registrationFour);
		
		return (Iterable<Registration>) registrations;
	}
	
}
