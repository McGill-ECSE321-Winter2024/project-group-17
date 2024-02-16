package ca.mcgill.ecse321.SportCenterManager.model;

public class CustomerAccount extends Account
{
  // Default constructor for Hibernate
  private CustomerAccount(){
  }
  public CustomerAccount(String aName, String aEmail, String aPassword)
  {
    super(aName, aEmail, aPassword);
  }
}