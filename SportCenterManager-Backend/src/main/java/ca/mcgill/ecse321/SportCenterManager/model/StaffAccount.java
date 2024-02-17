package ca.mcgill.ecse321.SportCenterManager.model;

import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class StaffAccount extends Account
{
  // Default constructor for Hibernate
  protected StaffAccount(){
  }
  public StaffAccount(String aName, String aEmail, String aPassword)
  {
    super(aName, aEmail, aPassword);
  }
}