package ca.mcgill.ecse321.SportCenterManager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
    
    @PutMapping("/registrations/{customerId}/{sessionId}")
    public RegistrationResponseDto register(@PathVariable int customerId, @PathVariable int sessionId) {
    	Registration registration = service.register(customerId, sessionId);
    	return new RegistrationResponseDto(registration);
    }
    
    @GetMapping("/registrations/customers/{customerId}")
    public RegistrationListDto findAllRegistrationsByCustomer(@PathVariable int customerId) {
    	List<Registration> registrations = service.findCustomerRegistrations(customerId);
    	return new RegistrationListDto(registrations);
    }
    
    @GetMapping("/registrations/{sessionId}/customers")
    public CustomerListDto findAllRegistrantsForSession(@PathVariable int sessionId) {
    	List<CustomerResponseDto> registrants = new ArrayList<CustomerResponseDto>();
    	for (CustomerAccount customer : service.findSessionRegistrants(sessionId)) {
    		registrants.add(new CustomerResponseDto(customer));
    	}
    	return new CustomerListDto(registrants);
    }
    
    @DeleteMapping("/registrations/{customerId}/{sessionId}")
    public void deleteRegistration(@PathVariable int customerId, @PathVariable int sessionId) {
    	service.cancelRegistration(customerId, sessionId);
    }
}
