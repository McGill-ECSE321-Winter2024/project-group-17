package ca.mcgill.ecse321.SportCenterManager.model;

import java.sql.Time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Schedule
{

  @Id
  @GeneratedValue
  private int id;
  private Time openingHours;
  private Time closingHours;

  // Default Constructor
  public Schedule() {
	  
  }
  
  public Schedule(Time aOpeningHours, Time aClosingHours)
  {
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