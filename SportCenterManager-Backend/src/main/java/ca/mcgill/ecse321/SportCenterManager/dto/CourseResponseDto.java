package ca.mcgill.ecse321.SportCenterManager.dto;

import ca.mcgill.ecse321.SportCenterManager.model.Course;

public class CourseResponseDto {
    private String name;
    private String description; 
    private double costPerSession;
    private int id;

    public CourseResponseDto(Course model){
        this.id = model.getId();
        this.name = model.getName();
        this.description = model.getDescription();
        this.costPerSession=model.getCostPerSession();
    }
    
    public int getid(){
        return id;
    }

    public void setid(int id){
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
    
}
