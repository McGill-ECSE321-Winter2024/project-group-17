package ca.mcgill.ecse321.SportCenterManager.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.SportCenterManager.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    @Operation(
            summary = "Find all instructor accounts.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully found all instructor accounts.",
                            content = @Content(
                                    schema = @Schema(implementation = InstructorListDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"instructors\": [" +
                                                    "{" +
                                                    "\"id\": 10, " +
                                                    "\"name\": \"Namir\", " +
                                                    "\"email\": \"Namir@gmail.com\", " +
                                                    "\"password\": \"Password$\"" +
                                                    "}" +
                                                    "]}"
                                            )
                                    }
                            )
                    )
            }
    )
    public InstructorListDto findAllInstructors() {
        List<InstructorResponseDto> instructors = new ArrayList<InstructorResponseDto>();
        for (InstructorAccount instructor : instructorAccountService.findAllInstructors()) {
            instructors.add(new InstructorResponseDto(instructor));
        }
        return new InstructorListDto(instructors);
    }

    @GetMapping("/{instructorAccount_id}")
    @Operation(
            summary = "Find instructor account by id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully found instructor account by id.",
                            content = @Content(
                                    schema = @Schema(implementation = InstructorResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"id\" : \"10\", " +
                                                    "\"name\" : \"Namir\", " +
                                                    "\"email\" : \"Namir@gmail.com\", " +
                                                    "\"password\" : \"Password$\"}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: invalid instructor id.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"There is no instructor with ID 11 in the system.\"}")
                                    }
                            )
                    )
            }
    )
    public InstructorResponseDto findInstructorById(@PathVariable int instructorAccount_id) {
        InstructorAccount instructor = instructorAccountService.findInstructorById(instructorAccount_id);
        return new InstructorResponseDto(instructor);
    }

    @PutMapping("/{instructorAccount_id}")
    @Operation(
            summary = "Update instructor account by id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully updated instructor account.",
                            content = @Content(
                                    schema = @Schema(implementation = InstructorResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"id\" : \"10\", " +
                                                    "\"name\" : \"Eric\", " +
                                                    "\"email\" : \"Eric@gmail.com\", " +
                                                    "\"password\" : \"Password$1\"}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: invalid instructor id.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"There is no instructor with ID 11 in the system.\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: invalid parameters.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Password must be at least eight characters long.\"}")
                                    }
                            )
                    )
            }
    )
    public InstructorResponseDto updateInstructorAccount(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = InstructorRequestDto.class),
                            examples = {
                                    @ExampleObject(value = "{" +
                                            "\"name\" : \"Eric\", " +
                                            "\"email\" : \"Eric@gmail.com\", " +
                                            "\"password\" : \"Password$1\"}"
                                    )
                            }
                    )
            )
            @RequestBody InstructorRequestDto instructor, @PathVariable int instructorAccount_id) {
        InstructorAccount modifiedInstructor = instructorAccountService.updateInstructorAccount(instructorAccount_id, instructor.getName(), instructor.getEmail(), instructor.getPassword());
        return new InstructorResponseDto(modifiedInstructor);
    }
    
    @PostMapping()
    @Operation(
            summary = "Create instructor account.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully created instructor account.",
                            content = @Content(
                                    schema = @Schema(implementation = InstructorResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"id\" : \"10\", " +
                                                    "\"name\" : \"Namir\", " +
                                                    "\"email\" : \"Namir@gmail.com\", " +
                                                    "\"password\" : \"Password$\"}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: invalid parameters.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Password must be at least eight characters long.\"}")
                                    }
                            )
                    )
            }
    )
    public InstructorResponseDto createInstructor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = InstructorRequestDto.class),
                            examples = {
                                    @ExampleObject(value = "{" +
                                            "\"name\" : \"Namir\", " +
                                            "\"email\" : \"Namir@gmail.com\", " +
                                            "\"password\" : \"Password$\"}"
                                    )
                            }
                    )
            )
            @RequestBody InstructorRequestDto instructor) {
        InstructorAccount instructorToCreate = instructorAccountService.createInstructor(instructor.getName(), instructor.getEmail(), instructor.getPassword());
        return new InstructorResponseDto(instructorToCreate);
    }

    @DeleteMapping("/{instructorAccount_id}")
    @Operation(
            summary = "Delete instructor account by id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully deleted instructor account by id.",
                            content = @Content(
                                    schema = @Schema(implementation = InstructorListDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"instructors\": [" +
                                                    "]}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: invalid instructor id.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"There is no instructor with ID 11 in the system.\"}")
                                    }
                            )
                    )
            }
    )
    public void deleteInstructor(@PathVariable int instructorAccount_id) {
        instructorAccountService.deleteInstructor(instructorAccount_id);
    }
}
