package ca.mcgill.ecse321.SportCenterManager.dto;

import java.util.List;

public class CustomerListDto {
    private List<CustomerResponseDto> customers;
    
    public CustomerListDto() {
    	
    }

    public CustomerListDto(List<CustomerResponseDto> customers) {
        this.customers = customers;
    }

    public List<CustomerResponseDto> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerResponseDto> customers) {
        this.customers = customers;
    }
}
