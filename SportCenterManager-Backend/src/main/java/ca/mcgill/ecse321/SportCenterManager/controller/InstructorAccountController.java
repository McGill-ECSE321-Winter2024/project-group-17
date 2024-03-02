package ca.mcgill.ecse321.SportCenterManager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.dto.InstructorListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.InstructorRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.InstructorResponseDto;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import ca.mcgill.ecse321.SportCenterManager.service.InstructorAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/instructorAccounts")
public class InstructorAccountController {
    @Autowired
    private InstructorAccountService instructorAccountService;

    @GetMapping()
    public InstructorListDto findAllInstructors() {
        List<InstructorResponseDto> instructors = new ArrayList<InstructorResponseDto>();
        for (InstructorAccount instructor : instructorAccountService.findAllInstructors()) {
            instructors.add(new InstructorResponseDto(instructor));
        }
        return new InstructorListDto(instructors);
    }

    @GetMapping("/{instructorAccount_id}")
    public InstructorResponseDto findInstructorById(@PathVariable int instructorAccount_id) {
        InstructorAccount instructor = instructorAccountService.findInstructorById(instructorAccount_id);
        return new InstructorResponseDto(instructor);
    }

    @PutMapping("/{instructorAccount_id}")
    public InstructorResponseDto updateInstructorAccount(@PathVariable int instructorAccount_id, @RequestBody InstructorRequestDto instructor) {
        InstructorAccount modifiedInstructor = instructorAccountService.updateInstructorAccount(instructorAccount_id, instructor.getName(), instructor.getEmail(), instructor.getPassword());
        return new InstructorResponseDto(modifiedInstructor);
    }
    
    @PostMapping()
    public InstructorResponseDto createInstructor(@RequestBody InstructorRequestDto instructor) {
        InstructorAccount instructorToCreate = instructorAccountService.createInstructor(instructor.getName(), instructor.getEmail(), instructor.getPassword());
        return new InstructorResponseDto(instructorToCreate);
    }

    @DeleteMapping("/{instructorAccount_id}")
    public void deleteInstructor(@PathVariable int instructorAccount_id) {
        instructorAccountService.deleteInstructor(instructorAccount_id);
    }
}
