package ca.mcgill.ecse321.SportCenterManager.model;

import java.sql.Date;

public class BillingInformation
{

  private String address;
  private String postalCode;
  private String country;
  private String name;
  private String cardNumber;
  private int cvc;
  private Date expirationDate;

  private CustomerAccount customerAccount;

  public BillingInformation(String aAddress, String aPostalCode, String aCountry, String aName, String aCardNumber, int aCvc, Date aExpirationDate, CustomerAccount aCustomerAccount)
  {
    address = aAddress;
    postalCode = aPostalCode;
    country = aCountry;
    name = aName;
    cardNumber = aCardNumber;
    cvc = aCvc;
    expirationDate = aExpirationDate;
    if (!setCustomerAccount(aCustomerAccount))
    {
      throw new RuntimeException("Unable to create BillingInformation due to aCustomerAccount. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public boolean setAddress(String aAddress)
  {
    boolean wasSet = false;
    address = aAddress;
    wasSet = true;
    return wasSet;
  }

  public boolean setPostalCode(String aPostalCode)
  {
    boolean wasSet = false;
    postalCode = aPostalCode;
    wasSet = true;
    return wasSet;
  }

  public boolean setCountry(String aCountry)
  {
    boolean wasSet = false;
    country = aCountry;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setCardNumber(String aCardNumber)
  {
    boolean wasSet = false;
    cardNumber = aCardNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setCvc(int aCvc)
  {
    boolean wasSet = false;
    cvc = aCvc;
    wasSet = true;
    return wasSet;
  }

  public boolean setExpirationDate(Date aExpirationDate)
  {
    boolean wasSet = false;
    expirationDate = aExpirationDate;
    wasSet = true;
    return wasSet;
  }

  public String getAddress()
  {
    return address;
  }

  public String getPostalCode()
  {
    return postalCode;
  }

  public String getCountry()
  {
    return country;
  }

  public String getName()
  {
    return name;
  }

  public String getCardNumber()
  {
    return cardNumber;
  }

  public int getCvc()
  {
    return cvc;
  }

  public Date getExpirationDate()
  {
    return expirationDate;
  }
  /* Code from template association_GetOne */
  public CustomerAccount getCustomerAccount()
  {
    return customerAccount;
  }
  /* Code from template association_SetUnidirectionalOne */
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