package ca.mcgill.ecse321.SportCenterManager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.sql.Time;
import java.sql.Date;
@Entity
public class Session
{
  @Id
  @GeneratedValue
  private int id;
  private Time startTime;
  private Time endTime;
  private Date date;
  private InstructorAccount instructorAccount;
  private Course course;

  // Default constructor for Hibernate
  private Session(){
  }
  public Session(Time aStartTime, Time aEndTime, Date aDate, InstructorAccount aInstructorAccount, Course aCourse)
  {
    startTime = aStartTime;
    endTime = aEndTime;
    date = aDate;
    if (!setInstructorAccount(aInstructorAccount))
    {
      throw new RuntimeException("Unable to create Session due to aInstructorAccount. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setCourse(aCourse))
    {
      throw new RuntimeException("Unable to create Session due to aCourse. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public boolean setStartTime(Time aStartTime)
  {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndTime(Time aEndTime)
  {
    boolean wasSet = false;
    endTime = aEndTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
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

  public Time getStartTime()
  {
    return startTime;
  }

  public Time getEndTime()
  {
    return endTime;
  }

  public Date getDate()
  {
    return date;
  }

  public int getId()
  {
    return id;
  }
  
  public InstructorAccount getInstructorAccount()
  {
    return instructorAccount;
  }
  
  public Course getCourse()
  {
    return course;
  }
  
  public boolean setInstructorAccount(InstructorAccount aNewInstructorAccount)
  {
    boolean wasSet = false;
    if (aNewInstructorAccount != null)
    {
      instructorAccount = aNewInstructorAccount;
      wasSet = true;
    }
    return wasSet;
  }
  
  public boolean setCourse(Course aNewCourse)
  {
    boolean wasSet = false;
    if (aNewCourse != null)
    {
      course = aNewCourse;
      wasSet = true;
    }
    return wasSet;
  }
}