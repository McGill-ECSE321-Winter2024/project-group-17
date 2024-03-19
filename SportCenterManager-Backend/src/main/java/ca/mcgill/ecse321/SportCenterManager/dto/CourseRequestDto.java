package ca.mcgill.ecse321.SportCenterManager.dto;

import ca.mcgill.ecse321.SportCenterManager.model.Course;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;

import java.sql.Date;
import java.sql.Time;

public class CourseRequestDto {
    private String name;
    private String description; 
    private double costPerSession;

    private boolean isApproved;

    public CourseRequestDto(String name, String description, double costPerSession){
        this.name = name;
        this.description = description;
        this.costPerSession = costPerSession;
        this.isApproved = false;
    }
  
    public String getName() {
        return name;
    }
  
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public double getCostPerSession(){
        return costPerSession;
    }

    public void setCostPerSession(double costPerSession){
        this.costPerSession = costPerSession;
    }

    public boolean getIsApproved(){
        return isApproved;
    }

    public void setIsApproved(boolean isApproved){
        this.isApproved = isApproved;
    }
}
