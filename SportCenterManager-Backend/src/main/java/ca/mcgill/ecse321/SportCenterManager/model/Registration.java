package ca.mcgill.ecse321.SportCenterManager.model;

public class Registration
{
  private Session session;
  private CustomerAccount customerAccount;

  public Registration(Session aSession, CustomerAccount aCustomerAccount)
  {
    if (!setSession(aSession))
    {
      throw new RuntimeException("Unable to create Registration due to aSession. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setCustomerAccount(aCustomerAccount))
    {
      throw new RuntimeException("Unable to create Registration due to aCustomerAccount. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Session getSession()
  {
    return session;
  }
  
  public CustomerAccount getCustomerAccount()
  {
    return customerAccount;
  }
  
  public boolean setSession(Session aNewSession)
  {
    boolean wasSet = false;
    if (aNewSession != null)
    {
      session = aNewSession;
      wasSet = true;
    }
    return wasSet;
  }
  
  public boolean setCustomerAccount(CustomerAccount aNewCustomerAccount)
  {
    boolean wasSet = false;
    if (aNewCustomerAccount != null)
    {
      customerAccount = aNewCustomerAccount;
      wasSet = true;
    }
    return wasSet;
  }
}