package ca.mcgill.ecse321.SportCenterManager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.*;
@Entity
public class Schedule
{
  @Id
  @GeneratedValue
  private int id;

  private List<Session> sessions;

  // Default constructor for Hibernate
  private Schedule(){
  }

  public Schedule(Session... allSessions)
  {
    sessions = new ArrayList<Session>();
    boolean didAddSessions = setSessions(allSessions);
    if (!didAddSessions)
    {
      throw new RuntimeException("Unable to create Schedule, must have at least 1 sessions. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template association_GetMany */
  public Session getSession(int index)
  {
    Session aSession = sessions.get(index);
    return aSession;
  }

  public List<Session> getSessions()
  {
    List<Session> newSessions = Collections.unmodifiableList(sessions);
    return newSessions;
  }

  public int numberOfSessions()
  {
    int number = sessions.size();
    return number;
  }

  public boolean hasSessions()
  {
    boolean has = sessions.size() > 0;
    return has;
  }

  public int indexOfSession(Session aSession)
  {
    int index = sessions.indexOf(aSession);
    return index;
  }
  
  public static int minimumNumberOfSessions()
  {
    return 1;
  }
  
  public boolean addSession(Session aSession)
  {
    boolean wasAdded = false;
    if (sessions.contains(aSession)) { return false; }
    sessions.add(aSession);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSession(Session aSession)
  {
    boolean wasRemoved = false;
    if (!sessions.contains(aSession))
    {
      return wasRemoved;
    }

    if (numberOfSessions() <= minimumNumberOfSessions())
    {
      return wasRemoved;
    }

    sessions.remove(aSession);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_SetUnidirectionalMStar */
  public boolean setSessions(Session... newSessions)
  {
    boolean wasSet = false;
    ArrayList<Session> verifiedSessions = new ArrayList<Session>();
    for (Session aSession : newSessions)
    {
      if (verifiedSessions.contains(aSession))
      {
        continue;
      }
      verifiedSessions.add(aSession);
    }

    if (verifiedSessions.size() != newSessions.length || verifiedSessions.size() < minimumNumberOfSessions())
    {
      return wasSet;
    }

    sessions.clear();
    sessions.addAll(verifiedSessions);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addSessionAt(Session aSession, int index)
  {  
    boolean wasAdded = false;
    if(addSession(aSession))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSessions()) { index = numberOfSessions() - 1; }
      sessions.remove(aSession);
      sessions.add(index, aSession);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSessionAt(Session aSession, int index)
  {
    boolean wasAdded = false;
    if(sessions.contains(aSession))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSessions()) { index = numberOfSessions() - 1; }
      sessions.remove(aSession);
      sessions.add(index, aSession);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSessionAt(aSession, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    sessions.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]";
  }
}