package ca.mcgill.ecse321.SportCenterManager.dto;

public class CourseRequestDto {
    private String name;
    private String description; 
    private double costPerSession;
    //no id 
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

    public double costPerSession(){
        return costPerSession;
    }

    public void setCostPerSession(double costPerSession){
        this.costPerSession = costPerSession;
    }
}
