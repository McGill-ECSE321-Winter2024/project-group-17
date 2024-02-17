package ca.mcgill.ecse321.SportCenterManager.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.InheritanceType;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account
{
  @Id
  @GeneratedValue
  private int id;
  private String name;
  private String email;
  private String password;

  // Default constructor for Hibernate
  protected Account(){
  }
  public Account(String aName, String aEmail, String aPassword)
  {
    name = aName;
    email = aEmail;
    password = aPassword;
  }

  
  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPassword()
  {
    return password;
  }

  public int getId()
  {
    return id;
  }

}