package ca.mcgill.ecse321.SportCenterManager.model;

public abstract class StaffAccount extends Account
{
  public StaffAccount(String aName, String aEmail, String aPassword, int aId)
  {
    super(aName, aEmail, aPassword, aId);
  }
}