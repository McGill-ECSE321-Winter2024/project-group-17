package ca.mcgill.ecse321.SportCenterManager.dto;

import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;

public class CustomerResponseDto {
    private int id;
    private String name;
    private String email;
    public CustomerResponseDto(){}

    public CustomerResponseDto(CustomerAccount model) {
        this.id = model.getId();
        this.name = model.getName();
        this.email = model.getEmail();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
