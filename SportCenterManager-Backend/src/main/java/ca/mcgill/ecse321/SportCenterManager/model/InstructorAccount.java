package ca.mcgill.ecse321.SportCenterManager.model;

import jakarta.persistence.Entity;

@Entity
public class InstructorAccount extends StaffAccount
{
  // Default constructor for Hibernate
  private InstructorAccount(){
  }
  public InstructorAccount(String aName, String aEmail, String aPassword)
  {
    super(aName, aEmail, aPassword);
  }
}