package ca.mcgill.ecse321.SportCenterManager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Course
{
  @Id
  @GeneratedValue
  private String name;
  private String description;
  private int costPerSession;
  private int id;

  public Course(String aName, String aDescription, int aCostPerSession)
  {
    name = aName;
    description = aDescription;
    costPerSession = aCostPerSession;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setCostPerSession(int aCostPerSession)
  {
    boolean wasSet = false;
    costPerSession = aCostPerSession;
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

  public String getDescription()
  {
    return description;
  }

  public int getCostPerSession()
  {
    return costPerSession;
  }

  public int getId()
  {
    return id;
  }
}