package ca.mcgill.ecse321.SportCenterManager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportCenterManager.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportCenterManager.exception.ServiceException;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Registration;
import ca.mcgill.ecse321.SportCenterManager.model.Session;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class RegistrationServiceTests {
	@Mock
	private CustomerAccountService customerService;
	@Mock
	private EventService eventService;
	@Mock
	private RegistrationRepository registrationRepo;
	@InjectMocks
	@Spy
	private RegistrationService service;

	/*------------ CRUD WRAPPERS UNIT TESTS  -----------*/
	@Test
	public void testFindExistingRegistration() {
		// setup
		CustomerAccount customer = createCustomer();
		int customerId = 20;
		Session session = new Session();
		int sessionId = 25;
		session.setId(sessionId);
		Registration registration = new Registration(new Registration.Key(session, customer));
		lenient().when(eventService.findSessionById(sessionId)).thenReturn(session);
		lenient().when(customerService.findCustomerById(customerId)).thenReturn(customer);
		lenient().when(registrationRepo.findRegistrationByKey(new Registration.Key(session, customer))).thenReturn(registration);
		
		// execution
		Registration result = service.findRegistration(customerId, sessionId);
		
		// assertions
		assertNotNull(result);
		assertEquals(registration, result);
		verify(registrationRepo, times(1)).findRegistrationByKey(new Registration.Key(session, customer));
		verify(eventService, times(1)).findSessionById(sessionId);
		verify(customerService, times(1)).findCustomerById(customerId);
	}

	@Test
	public void testFindNonExistingRegistration() {
		// setup
		CustomerAccount customer = createCustomer();
		int customerId = 20;
		Session session = new Session();
		int sessionId = 25;
		session.setId(sessionId);
		lenient().when(eventService.findSessionById(sessionId)).thenReturn(session);
		lenient().when(customerService.findCustomerById(customerId)).thenReturn(customer);
		lenient().when(registrationRepo.findRegistrationByKey(new Registration.Key(session, customer))).thenReturn(null);
		
		// execution
		Registration result = service.findRegistration(customerId, sessionId);
		
		// assertions
		assertNull(result);
		verify(registrationRepo, times(1)).findRegistrationByKey(new Registration.Key(session, customer));
		verify(eventService, times(1)).findSessionById(sessionId);
		verify(customerService, times(1)).findCustomerById(customerId);
	}
	
	@Test
	public void testFindAllRegistrations() {
		// setup
		Iterable<Registration> registrations = getExistingRegistrations(new CustomerAccount());
		lenient().when(registrationRepo.findAll()).thenReturn(registrations);
		
		// execution
		List<Registration> result = service.findAllRegistrations();
		
		// assertions
		assertNotNull(result);
		assertEquals(4, result.size());
		verify(registrationRepo, times(1)).findAll();
	}

	/*------------ HELPER METHOD UNIT TESTS  -----------*/

	@Test
	public void testHasConflictFalse() {
		// setup
		CustomerAccount customer = createCustomer();
		int customerId = 20;
		Iterable<Registration> registrationIterable = getExistingRegistrations(customer);
		List<Registration> customerRegistrations = new ArrayList<Registration>();
		
		for (Registration reg : registrationIterable) {
			if (reg.getKey().getCustomerAccount().getId() == customerId) {
				customerRegistrations.add(reg);
			}
		}
		lenient().when(service.findCustomerRegistrations(customerId)).thenReturn(customerRegistrations);
		
		Session newSession = new Session();
		newSession.setStartTime(Time.valueOf("8:00:00"));
		newSession.setEndTime(Time.valueOf("9:00:00"));
		newSession.setDate(Date.valueOf("2024-05-05"));
		Registration newRegistration = new Registration(new Registration.Key(newSession, customer));
		
		// execution
		boolean result = service.hasConflict(newRegistration);
		
		// assertions
		assertFalse(result);
	}
	
	@Test
	public void testHasConflictTrue() {
		// setup
		CustomerAccount customer = createCustomer();
		int customerId = 20;
		Iterable<Registration> registrationIterable = getExistingRegistrations(customer);
		List<Registration> customerRegistrations = new ArrayList<Registration>();
		
		for (Registration reg : registrationIterable) {
			if (reg.getKey().getCustomerAccount().getId() == customerId) {
				customerRegistrations.add(reg);
			}
		}
		lenient().when(service.findCustomerRegistrations(customerId)).thenReturn(customerRegistrations);
		
		Session newSession = new Session();
		newSession.setStartTime(Time.valueOf("20:30:00"));
		newSession.setEndTime(Time.valueOf("21:30:00"));
		newSession.setDate(Date.valueOf("2024-05-05"));
		Registration newRegistration = new Registration(new Registration.Key(newSession, customer));
		
		// execution
		boolean result = service.hasConflict(newRegistration);
		
		// assertions
		assertTrue(result);
	}
	
	/*------------ FIND CUSTOMER'S REGISTRATIONS -- ENDPOINT SERVICE UNIT TESTS  -----------*/
	@Test
	public void testFindCustomerRegistrationsNonEmpty() {
		// setup
		CustomerAccount customer = createCustomer();
		int customerId = 20;
		Iterable<Registration> existingRegistrations = getExistingRegistrations(customer);
		lenient().when(customerService.findCustomerById(customerId)).thenReturn(customer);
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
		CustomerAccount existingCustomer = createCustomer();
		CustomerAccount newCustomer = new CustomerAccount("customerTwo", "validEmail2@gmail.com", "pas0-2");
		int newCustomerId = 21;
		newCustomer.setId(21);
		Iterable<Registration> existingRegistrations = getExistingRegistrations(existingCustomer);
		lenient().when(customerService.findCustomerById(newCustomerId)).thenReturn(newCustomer);
		lenient().when(registrationRepo.findAll()).thenReturn(existingRegistrations);
		
		// execution
		List<Registration> newCustomerRegistrations = service.findCustomerRegistrations(newCustomerId);	
		
		// assertions
		assertEquals(new ArrayList<Registration>(), newCustomerRegistrations);
		verify(registrationRepo, times(1)).findAll();
	}
	

	/*------------ REGISTER -- ENDPOINT SERVICE UNIT TESTS  -----------*/
	@Test
	public void testRegisterValidSession() {
		// setup
		CustomerAccount customer = createCustomer();
		int customerId = customer.getId();
		lenient().when(customerService.findCustomerById(customerId)).thenReturn(customer);
		
		Session session = new Session();
		session.setStartTime(Time.valueOf("8:00:00"));
		session.setEndTime(Time.valueOf("9:00:00"));
		session.setDate(Date.valueOf("2024-05-05"));
		int sessionId = 25;
		lenient().when(eventService.findSessionById(sessionId)).thenReturn(session);
		
		lenient().when(registrationRepo.findAll()).thenReturn(getExistingRegistrations(customer));
		lenient().when(registrationRepo.findRegistrationByKey(any(Registration.Key.class))).thenReturn(null);
		
		// execution
		Registration registration = service.register(customerId, sessionId);
		lenient().when(registrationRepo.save(any(Registration.class))).thenReturn(registration);
		
		// assertions
		assertNotNull(registration);
		assertEquals(customer, registration.getKey().getCustomerAccount());
		assertEquals(session, registration.getKey().getSession());
		verify(customerService, times(3)).findCustomerById(customerId);
		verify(eventService, times(2)).findSessionById(sessionId);
		verify(registrationRepo, times(1)).findAll();
		verify(registrationRepo, times(1)).findRegistrationByKey(any(Registration.Key.class));
		verify(registrationRepo, times(1)).save(any(Registration.class));
	}
	
	@Test
	public void testRegisterConflictingSession() {
		CustomerAccount customer = createCustomer();
		int customerId = customer.getId();
		lenient().when(customerService.findCustomerById(customerId)).thenReturn(customer);
		
		Session session = new Session();
		session.setStartTime(Time.valueOf("20:30:00"));
		session.setEndTime(Time.valueOf("21:30:00"));
		session.setDate(Date.valueOf("2024-05-05"));
		int sessionId = 25;
		lenient().when(eventService.findSessionById(sessionId)).thenReturn(session);
		
		lenient().when(registrationRepo.findAll()).thenReturn(getExistingRegistrations(customer));
		lenient().when(registrationRepo.findRegistrationByKey(any(Registration.Key.class))).thenReturn(null);
		String msg = "";
		try {
			Registration registration = service.register(customerId, sessionId);
			lenient().when(registrationRepo.save(any(Registration.class))).thenReturn(registration);
		} catch (ServiceException e) {
			msg = e.getMessage();
		}
		
		assertEquals("Failed to Register: Session overlaps with an existing registration!", msg);
		verify(registrationRepo, times(0)).save(any(Registration.class));
	}
	
	@Test
	public void testRegisterDuplicate() {
		CustomerAccount customer = createCustomer();
		int customerId = customer.getId();
		lenient().when(customerService.findCustomerById(customerId)).thenReturn(customer);
		
		Session session = new Session();
		session.setStartTime(Time.valueOf("20:00:00"));
		session.setEndTime(Time.valueOf("21:00:00"));
		session.setDate(Date.valueOf("2024-05-05"));
		int sessionId = 25;
		lenient().when(eventService.findSessionById(sessionId)).thenReturn(session);
		
		lenient().when(registrationRepo.findAll()).thenReturn(getExistingRegistrations(customer));
		Registration existingRegistration = new Registration(new Registration.Key(session, customer));
		lenient().when(registrationRepo.findRegistrationByKey(new Registration.Key(session, customer))).thenReturn(existingRegistration);
		String msg = "";
		try {
			Registration registration = service.register(customerId, sessionId);
			lenient().when(registrationRepo.save(any(Registration.class))).thenReturn(registration);
		} catch (ServiceException e) {
			msg = e.getMessage();
		}
		
		assertEquals("Failed to Register: You are already registered to this session!", msg);
		verify(registrationRepo, times(0)).save(any(Registration.class));
	}
	
	/*------------ FIND SESSION'S REGISTRANTS -- ENDPOINT SERVICE UNIT TESTS  -----------*/
	@Test
	public void testSessionRegistrantsNonEmpty() {
		// setup
		List<Registration> registrations = new ArrayList<Registration>();
		Session sessionOne = new Session();
		int sessionOneId = 4;
		sessionOne.setId(sessionOneId);
		Session sessionTwo = new Session();
		int sessionTwoId = 5;
		sessionTwo.setId(sessionTwoId);
		CustomerAccount firstCustomer = createCustomer();
		CustomerAccount secondCustomer = new CustomerAccount("customerTwo", "validEmail2@gmail.com", "pas0-2");
		CustomerAccount thirdCustomer = new CustomerAccount("customerThree", "validEmail2@gmail.com", "pas0-3");
		CustomerAccount fourthCustomer = new CustomerAccount("customerFour", "validEmail3@gmail.com", "pas0-4");
		
		registrations.add(new Registration(new Registration.Key(sessionOne, firstCustomer)));
		registrations.add(new Registration(new Registration.Key(sessionOne, secondCustomer)));
		registrations.add(new Registration(new Registration.Key(sessionOne, thirdCustomer)));
		registrations.add(new Registration(new Registration.Key(sessionTwo, fourthCustomer)));
		
		lenient().when(registrationRepo.findAll()).thenReturn((Iterable<Registration>) registrations);
		lenient().when(eventService.findSessionById(sessionOneId)).thenReturn(sessionOne);
		
		// execution
		List<CustomerAccount> registrants = service.findSessionRegistrants(sessionOneId);
		
		// assertions
		List<CustomerAccount> expectedRegistrants = new ArrayList<CustomerAccount>();
		expectedRegistrants.add(firstCustomer);
		expectedRegistrants.add(secondCustomer);
		expectedRegistrants.add(thirdCustomer);
		
		assertEquals(3, registrants.size());
		assertEquals(expectedRegistrants, registrants);
		verify(eventService, times(1)).findSessionById(sessionOneId);
		verify(registrationRepo, times(1)).findAll();
		
	}
	
	@Test
	public void testSessionRegistrantsEmpty() {
		// setup
		List<Registration> registrations = new ArrayList<Registration>();
		Session sessionOne = new Session();
		int sessionOneId = 4;
		sessionOne.setId(sessionOneId);
		Session sessionTwo = new Session();
		int sessionTwoId = 5;
		sessionTwo.setId(sessionTwoId);
		CustomerAccount firstCustomer = createCustomer();
		CustomerAccount secondCustomer = new CustomerAccount("customerTwo", "validEmail2@gmail.com", "pas0-2");
		CustomerAccount thirdCustomer = new CustomerAccount("customerThree", "validEmail2@gmail.com", "pas0-3");
		
		registrations.add(new Registration(new Registration.Key(sessionOne, firstCustomer)));
		registrations.add(new Registration(new Registration.Key(sessionOne, secondCustomer)));
		registrations.add(new Registration(new Registration.Key(sessionOne, thirdCustomer)));
		
		lenient().when(registrationRepo.findAll()).thenReturn((Iterable<Registration>) registrations);
		lenient().when(eventService.findSessionById(sessionTwoId)).thenReturn(sessionTwo);
		
		// execution
		List<CustomerAccount> registrants = service.findSessionRegistrants(sessionTwoId);
		
		// assertions
		assertEquals(0, registrants.size());
		assertEquals(new ArrayList<CustomerAccount>(), registrants);
		verify(eventService, times(1)).findSessionById(sessionTwoId);
		verify(registrationRepo, times(1)).findAll();
		
	}

	/*------------ CANCEL REGISTRATION -- ENDPOINT SERVICE METHOD TESTS  -----------*/
	@Test
	public void testCancelRegistration() {
		// setup
		CustomerAccount customer = createCustomer();
		int customerId = customer.getId();
		Session session = new Session();
		int sessionId = 5;
		session.setId(sessionId);
		Registration.Key registrationKey = new Registration.Key(session, customer);
		Registration registration = new Registration(registrationKey);
		
		lenient().when(customerService.findCustomerById(customerId)).thenReturn(customer);
		lenient().when(eventService.findSessionById(sessionId)).thenReturn(session);
		lenient().when(registrationRepo.findRegistrationByKey(registrationKey)).thenReturn(registration).thenReturn(null);
		lenient().doNothing().when(registrationRepo).delete(registration);
		
		// execution
		boolean result = service.cancelRegistration(customerId, sessionId);
		
		// assertion
		assertNotNull(result);
		assertTrue(result);
		verify(customerService, times(2)).findCustomerById(customerId);
		verify(eventService, times(2)).findSessionById(sessionId);
		verify(registrationRepo, times(2)).findRegistrationByKey(registrationKey);
		verify(registrationRepo, times(1)).delete(registration);
	}
	
	/*------------ SETUP HELPER METHODS  -----------*/
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
