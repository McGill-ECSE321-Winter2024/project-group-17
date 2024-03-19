package ca.mcgill.ecse321.SportCenterManager.dto;

import java.util.List;

public class InstructorListDto {
    private List<InstructorResponseDto> instructors;

    public InstructorListDto() {
    }

    public InstructorListDto(List<InstructorResponseDto> instructors) {
        this.instructors = instructors;
    }

    public List<InstructorResponseDto> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<InstructorResponseDto> instructors) {
        this.instructors = instructors;
    }
}
