package ca.mcgill.ecse321.SportCenterManager.model;

import jakarta.persistence.Entity;

@Entity
public class OwnerAccount extends StaffAccount
{
  // Default constructor for Hibernate
  private OwnerAccount(){
  }
  public OwnerAccount(String aName, String aEmail, String aPassword)
  {
    super(aName, aEmail, aPassword);
  }
}