package ca.mcgill.ecse321.SportCenterManager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.SessionRepository;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Registration;
import ca.mcgill.ecse321.SportCenterManager.model.Session;

@Service
public class RegistrationService {
    
	@Autowired
	SessionRepository sessionRepository;
	@Autowired
	CustomerAccountRepository customerAccountRepository;
	@Autowired
	RegistrationRepository registrationRepository;
	
	@Transactional
	public Registration register(int customerId, int sessionId) {
		// Retrieve customer and session by ID
		CustomerAccount customer = customerAccountRepository.findCustomerAccountById(customerId);
		Session session = sessionRepository.findSessionById(sessionId);
		
		// Create new Registration
		Registration registration = new Registration();
		Registration.Key key = new Registration.Key(session, customer);
		
		// Check if registration already exists
		Registration existingRegistration = registrationRepository.findRegistrationByKey(key);
		if (existingRegistration != null) {
			throw new IllegalArgumentException("Failed to Register: You are already registered to this session!");
		}
		
		// Set fields and check if there are registration conflicts
		registration.setKey(key);
		if (hasConflict(registration)) {
			throw new IllegalArgumentException("Failed to Register: Session overlaps with an existing registration!");
		}
		
		// Save and return registration
		registrationRepository.save(registration);
		return registration;
	}
	
	@Transactional
	public Registration findRegistration(int customerId, int sessionId) {
		// Retrieve customer and session by ID
		CustomerAccount customer = customerAccountRepository.findCustomerAccountById(customerId);
		Session session = sessionRepository.findSessionById(sessionId);
		
		// Find registration by customer/session key
		Registration registration = registrationRepository.findRegistrationByKey(new Registration.Key(session, customer));
		return registration;
	}

	
	@Transactional
	public List<Registration> findCustomerRegistrations(int customerId){
		// Retrieve customer account by ID and all registration in DB
		CustomerAccount customer = customerAccountRepository.findCustomerAccountById(customerId);
		List<Registration> allRegistrations = getAllRegistrations();
		
		// Instantiate new arrayList and populate with registrations belonging to customer
		List<Registration> customerRegistrations = new ArrayList<Registration>();
		for (Registration registration: allRegistrations) {
			if (registration.getKey().getCustomerAccount().getId() == customer.getId()) {
				customerRegistrations.add(registration);
			}
		}
		return customerRegistrations;
	}
	
	@Transactional
	public List<CustomerAccount> findSessionRegistrants(int sessionId){
		// Retrieve session by ID and all registration in DB
		Session session = sessionRepository.findSessionById(sessionId);
		
		// Instantiate new arrayList and populate with registrations involving the session
		List<Registration> allRegistrations = getAllRegistrations();
		List<CustomerAccount> sessionRegistrants = new ArrayList<CustomerAccount>();
		for (Registration registration: allRegistrations) {
			if (registration.getKey().getSession().getId() == session.getId()) {
				sessionRegistrants.add(registration.getKey().getCustomerAccount());
			}
		}
		return sessionRegistrants;
	}
	
	public boolean cancelRegistration(int customerId, int sessionId) {
		// Search for registration
		CustomerAccount customer = customerAccountRepository.findCustomerAccountById(customerId);
		Session session = sessionRepository.findSessionById(sessionId);
		Registration registration = registrationRepository.findRegistrationByKey(new Registration.Key(session, customer));
		// Delete registration for DB and return whether it has been successfully deleted
		registrationRepository.delete(registration);
		return registrationRepository.findRegistrationByKey(new Registration.Key(session, customer)) == null;
	}
	
	private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t: iterable) {
			resultList.add(t);
		}
		return resultList;
	}
	
	@Transactional
	private List<Registration> getAllRegistrations() {
		Iterable<Registration> registrations = registrationRepository.findAll();
		return toList(registrations);
	}
	
	private boolean hasConflict(Registration registration) {
		// Retrieve all registrations of the customer
		List<Registration> registrations = findCustomerRegistrations(registration.getKey().getCustomerAccount().getId());
		Session newRegSession = registration.getKey().getSession();
		
		// Check for conflict, i.e. if there are any existing registrations where the sessions overlap with the new one
		for (Registration reg: registrations) {
			Session existingRegSession = reg.getKey().getSession();
			if (newRegSession.getDate().equals(existingRegSession.getDate()) &&(
				newRegSession.getStartTime().before(existingRegSession.getEndTime()) &&
				newRegSession.getStartTime().after(existingRegSession.getStartTime()) || 
				newRegSession.getEndTime().before(existingRegSession.getEndTime()) &&
				newRegSession.getEndTime().after(existingRegSession.getStartTime()) ||
				newRegSession.getStartTime().equals(existingRegSession.getStartTime()) &&
				newRegSession.getEndTime().equals(existingRegSession.getEndTime()))) {
				return true;
			}
		}
		return false;
	}
}
