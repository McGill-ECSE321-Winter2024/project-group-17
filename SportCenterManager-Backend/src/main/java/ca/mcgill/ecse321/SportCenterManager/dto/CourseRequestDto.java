package ca.mcgill.ecse321.SportCenterManager.dto;

public class CourseRequestDto {
    private String name;
    private String description; 
    private double costPerSession;

    public CourseRequestDto(String name, String description, double costPerSession){
        this.name = name;
        this.description = description;
        this.costPerSession = costPerSession;
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
