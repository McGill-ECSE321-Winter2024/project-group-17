package ca.mcgill.ecse321.SportCenterManager.model;

public abstract class Account
{

  private String name;
  private String email;
  private String password;
  private int id;

  public Account(String aName, String aEmail, String aPassword, int aId)
  {
    name = aName;
    email = aEmail;
    password = aPassword;
    id = aId;
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