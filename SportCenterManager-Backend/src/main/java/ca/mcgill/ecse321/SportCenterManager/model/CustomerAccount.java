package ca.mcgill.ecse321.SportCenterManager.model;

import jakarta.persistence.Entity;

@Entity
public class CustomerAccount extends Account
{
  // Default constructor for Hibernate
  public CustomerAccount(){
  }
  public CustomerAccount(String aName, String aEmail, String aPassword)
  {
    super(aName, aEmail, aPassword);
  }
}