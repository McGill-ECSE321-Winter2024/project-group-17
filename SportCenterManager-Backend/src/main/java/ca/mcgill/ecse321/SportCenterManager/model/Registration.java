package ca.mcgill.ecse321.SportCenterManager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;

@Entity
public class Registration
{

  @EmbeddedId
  private Key key;
  
  public Registration() {  
  }
  
  public Registration(Key aKey)
  {
	  this.key = aKey;
  }
  
  public Key getKey() {
	  return key;
  }
  
  public void setKey(Key aKey) {
	  this.key = aKey;
  }
  
  @SuppressWarnings("serial")
  @Embeddable
  public static class Key implements Serializable {
	  @ManyToOne
	  private Session session;
	  @ManyToOne
	  private CustomerAccount customerAccount;
	  
	  @SuppressWarnings("unused")
	  private Key() {
		  
	  }
	  
	  public Key(Session session, CustomerAccount customerAccount) {
		  this.session = session;
		  this.customerAccount = customerAccount;
	  }
	  
	  public Session getSession() {
		  return session;
	  }
	  
	  public void setSession(Session aSession) {
		  this.session = aSession;
	  }
	  
	  public CustomerAccount getCustomerAccount() {
		  return customerAccount;
	  }
	  
	  public void setCustomerAccount(CustomerAccount aCustomerAccount) {
		  this.customerAccount = aCustomerAccount;
	  }
	  
	  @Override
      public boolean equals(Object obj) {
          if (!(obj instanceof Key)) {
              return false;
          }
          Key other = (Key) obj;
          return this.getSession().getId() == other.getSession().getId()
                  && this.getCustomerAccount().getId() == other.getCustomerAccount().getId();
      }

      @Override
      public int hashCode() {
          return Objects.hash(this.getSession().getId(), this.getCustomerAccount().getId());
      }
	  
  }
}