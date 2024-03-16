package ca.mcgill.ecse321.SportCenterManager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Course
{
  @Id
  @GeneratedValue
  private int id;
  private String name;
  private String description;
  private double costPerSession;
  //private boolean isApproved;

  // Default constructor for Hibernate
  private Course(){
  }

  public Course(String aName, String aDescription, double aCostPerSession)
  {
    name = aName;
    description = aDescription;
    costPerSession = aCostPerSession;
    //isApproved = false;
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

  public boolean setCostPerSession(double aCostPerSession)
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

  // public boolean setIsApproved(boolean aIsApproved)
  // {
  //   boolean wasSet = false;
  //   isApproved = aIsApproved;
  //   wasSet = true;
  //   return wasSet;
  // }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return description;
  }

  public double getCostPerSession()
  {
    return costPerSession;
  }

  public int getId()
  {
    return id;
  }

  // public boolean getIsApproved()
  // {
  //   return isApproved;
  // }
}