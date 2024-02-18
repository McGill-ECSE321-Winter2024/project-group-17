package ca.mcgill.ecse321.SportCenterManager.model;

import java.sql.Time;

import jakarta.persistence.Entity;

@Entity
public class Schedule
{

 
  private int id;
  private Time openingHours;
  private Time closingHours;

  // Default Constructor
  private Schedule() {
	  
  }
  
  public Schedule(int aId, Time aOpeningHours, Time aClosingHours)
  {
    id = aId;
    openingHours = aOpeningHours;
    closingHours = aClosingHours;
  }

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setOpeningHours(Time aOpeningHours)
  {
    boolean wasSet = false;
    openingHours = aOpeningHours;
    wasSet = true;
    return wasSet;
  }

  public boolean setClosingHours(Time aClosingHours)
  {
    boolean wasSet = false;
    closingHours = aClosingHours;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }

  public Time getOpeningHours()
  {
    return openingHours;
  }

  public Time getClosingHours()
  {
    return closingHours;
  }
}