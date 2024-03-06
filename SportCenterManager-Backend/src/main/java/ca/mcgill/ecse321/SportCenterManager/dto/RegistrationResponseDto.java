package ca.mcgill.ecse321.SportCenterManager.dto;

import ca.mcgill.ecse321.SportCenterManager.model.Registration;

public class RegistrationResponseDto {
    private CustomerResponseDto customer;
    private SessionResponseDto session;
    
    public RegistrationResponseDto(Registration registration) {
    	this.customer = new CustomerResponseDto(registration.getKey().getCustomerAccount());
    	this.session = new SessionResponseDto(registration.getKey().getSession());
    }
    
    public CustomerResponseDto getCustomer() {
    	return this.customer;
    }
    
    public SessionResponseDto getSession() {
    	return this.session;
    }
}
