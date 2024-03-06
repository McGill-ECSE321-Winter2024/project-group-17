package ca.mcgill.ecse321.SportCenterManager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.dto.RegistrationListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.RegistrationResponseDto;
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
    
    @GetMapping("/registrations/sessions/{sessionId}")
    public RegistrationListDto findAllRegistrationsBySession(@PathVariable int sessionId) {
    	List<Registration> registrations = service.findSessionRegistrations(sessionId);
    	return new RegistrationListDto(registrations);
    }
    
    @DeleteMapping("/registrations/{customerId}/{sessionId}")
    public void deleteRegistration(@PathVariable int customerId, @PathVariable int sessionId) {
    	service.cancelRegistration(customerId, sessionId);
    }
}
