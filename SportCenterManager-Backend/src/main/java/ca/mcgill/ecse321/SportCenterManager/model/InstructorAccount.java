package ca.mcgill.ecse321.SportCenterManager.model;

import jakarta.persistence.Entity;

@Entity
public class InstructorAccount extends StaffAccount
{
  public InstructorAccount(String aName, String aEmail, String aPassword)
  {
    super(aName, aEmail, aPassword);
  }
}