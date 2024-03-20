package ca.mcgill.ecse321.SportCenterManager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.dto.CustomerListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CustomerResponseDto;
import ca.mcgill.ecse321.SportCenterManager.dto.RegistrationListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.RegistrationResponseDto;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Registration;
import ca.mcgill.ecse321.SportCenterManager.service.RegistrationService;

@CrossOrigin(origins = "*")
@RestController
public class RegistrationController {
    @Autowired
    private RegistrationService service;
    
    @PutMapping("/courses/{course_id}/sessions/{session_id}/registrations/{customerAccount_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponseDto register(@PathVariable(name="customerAccount_id") int customerId, @PathVariable(name="session_id") int sessionId) {
    	Registration registration = service.register(customerId, sessionId);
    	return new RegistrationResponseDto(registration);
    }
    
    @GetMapping("/customerAccounts/{customerAccount_id}/registrations")
    public RegistrationListDto findAllRegistrationsByCustomer(@PathVariable(name="customerAccount_id") int customerId) {
    	List<Registration> registrations = service.findCustomerRegistrations(customerId);
    	return new RegistrationListDto(registrations);
    }
    
    @GetMapping("/courses/{course_id}/sessions/{session_id}/registrations/customers")
    public CustomerListDto findAllRegistrantsForSession(@PathVariable(name="session_id") int sessionId) {
    	List<CustomerResponseDto> registrants = new ArrayList<CustomerResponseDto>();
    	for (CustomerAccount customer : service.findSessionRegistrants(sessionId)) {
    		registrants.add(new CustomerResponseDto(customer));
    	}
    	return new CustomerListDto(registrants);
    }
    
    @DeleteMapping("/courses/{course_id}/sessions/{session_id}/registrations/{customerAccount_id}")
    public void cancelRegistration(@PathVariable(name="customerAccount_id") int customerId, @PathVariable(name="session_id") int sessionId) {
    	service.cancelRegistration(customerId, sessionId);
    }
}
