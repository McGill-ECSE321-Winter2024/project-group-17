package ca.mcgill.ecse321.SportCenterManager.model;

import jakarta.persistence.*;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
public class BillingInformation
{

  private String address;
  private String postalCode;
  private String country;
  private String name;
  private String cardNumber;
  private int cvc;
  private Date expirationDate;

  @EmbeddedId
  private BillingInformation.Key key;

  // Default Constructor for Hibernate

  private BillingInformation(){
  }

  public BillingInformation(String aAddress, String aPostalCode, String aCountry, String aName, String aCardNumber, int aCvc, Date aExpirationDate, CustomerAccount aCustomerAccount)
  {
    address = aAddress;
    postalCode = aPostalCode;
    country = aCountry;
    name = aName;
    cardNumber = aCardNumber;
    cvc = aCvc;
    expirationDate = aExpirationDate;
  }


  public Key getKey(){
    return key;
  }

  public void setKey(Key key) {
    this.key=key;
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

  @Embeddable
  public static class Key implements Serializable{
    @OneToOne
    private CustomerAccount customerAccount;
    public Key(){
    }
    public Key(CustomerAccount customerAccount) {
      this.customerAccount = customerAccount;
    }
    public CustomerAccount getCustomerAccount(){
      return customerAccount;
    }

    public void setCustomerAccount(CustomerAccount customerAccount) {
      this.customerAccount = customerAccount;
    }

    @Override
    public boolean equals(Object obj){
      if (!(obj instanceof Key)) {
        return false;
      }
      Key other = (Key) obj;
      return this.getCustomerAccount().getId() == other.getCustomerAccount().getId();
    }
    @Override
    public int hashCode() {
      return Objects.hash(this.getCustomerAccount().getId());
    }
  }
}