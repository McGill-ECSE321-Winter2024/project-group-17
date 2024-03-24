package ca.mcgill.ecse321.SportCenterManager.dto;

import ca.mcgill.ecse321.SportCenterManager.model.Course;

public class CourseResponseDto {
    private String name;
    private String description; 
    private double costPerSession;
    private boolean isApproved;
    private int id;

    @SuppressWarnings("unused")
    private CourseResponseDto(){
    }

    public CourseResponseDto(Course model){
        this.id = model.getId();
        this.name = model.getName();
        this.description = model.getDescription();
        this.costPerSession = model.getCostPerSession();
        this.isApproved = model.getIsApproved();
    }
    
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
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
