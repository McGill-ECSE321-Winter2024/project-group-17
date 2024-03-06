package ca.mcgill.ecse321.SportCenterManager.dto;

import java.util.List;

import ca.mcgill.ecse321.SportCenterManager.model.Registration;

public class RegistrationListDto {
    private List<RegistrationResponseDto> registrations;
    
    public RegistrationListDto(List<Registration> registrations) {
    	for (Registration registration: registrations) {
    		this.registrations.add(new RegistrationResponseDto(registration));
    	}
    }
    
    public List<RegistrationResponseDto> getRegistrations(){
    	return this.registrations;
    }
    
}
