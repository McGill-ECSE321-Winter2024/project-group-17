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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import ca.mcgill.ecse321.SportCenterManager.model.Registration;
import ca.mcgill.ecse321.SportCenterManager.service.RegistrationService;

@CrossOrigin(origins = "*")
@RestController
public class RegistrationController {
    @Autowired
    private RegistrationService service;
    
    @PutMapping("/courses/{course_id}/sessions/{session_id}/registrations/{customerAccount_id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Register customer to a session",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "CREATED: Successfully registered",
                            content = @Content(
                                    schema = @Schema(implementation = RegistrationResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"Registration\": [" +
                                                    "{" +
                                                    "\"customer\": {" +
                                                    "\"name\" : \"Tibo\"," +
                                                    "\"email\" : \"tibo123@gmail.com\"," +
                                                    "\"password\" : \"Password#123\"" +
                                                    "}," +
                                                    "\"session\" : {" +
                                                    "\"startTime\" : \"20:00:00\"," +
                                                    "\"endTime\" : \"21:00:00\"," +
                                                    "\"date\" : \"2024-05-05\"," +
                                                    "\"instructor\" : \"instructorOne\"," +
                                                    "\"course\" : \"courseOne\"," +
                                                    "\"schedule\" : \"schedule\"" +
                                                    "}"+
                                                    "}"+
                                                    "]"+
                                                    "}")
                                    }
                            )

                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "CONFLICT: You are already registered to this session",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Failed to Register: You are already registered to this session!\"}")
                                    }
                            )
                    )
            }
    )
    public RegistrationResponseDto register(@PathVariable(name="customerAccount_id") int customerId, @PathVariable(name="session_id") int sessionId) {
    	Registration registration = service.register(customerId, sessionId);
    	return new RegistrationResponseDto(registration);
    }
    
    @GetMapping("/customerAccounts/{customerAccount_id}/registrations")
    @Operation(
            summary = "Find all the registrations by customer",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully found all registrations by customer",
                            content = @Content(
                                    schema = @Schema(implementation = RegistrationResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"Registration1\": [" +
                                                    "{" +
                                                    "\"customer\": {" +
                                                    "\"name\": \"Tibo\"," +
                                                    "\"email\": \"tibo123@gmail.com\"," +
                                                    "\"password\": \"Password#123\"" +
                                                    "}," +
                                                    "\"session\": {" +
                                                    "\"startTime\": \"20:00:00\"," +
                                                    "\"endTime\": \"21:00:00\"," +
                                                    "\"date\": \"2024-05-06\"," +
                                                    "\"instructor\": \"instructorOne\"," +
                                                    "\"course\": \"courseOne\"," +
                                                    "\"schedule\": \"schedule\"" +
                                                    "}" +
                                                    "}" +
                                                    "]," +
                                                    "\"Registration2\": [" +
                                                    "{" +
                                                    "\"customer\": {" +
                                                    "\"name\": \"Tibo\"," +
                                                    "\"email\": \"tibo123@gmail.com\"," +
                                                    "\"password\": \"Password#123\"" +
                                                    "}," +
                                                    "\"session\": {" +
                                                    "\"startTime\": \"12:00:00\"," +
                                                    "\"endTime\": \"13:00:00\"," +
                                                    "\"date\": \"2024-05-05\"," +
                                                    "\"instructor\": \"instructorOne\"," +
                                                    "\"course\": \"courseOne\"," +
                                                    "\"schedule\": \"schedule\"" +
                                                    "}" +
                                                    "}" +
                                                    "]," +
                                                    "\"Registration3\": [" +
                                                    "{" +
                                                    "\"customer\": {" +
                                                    "\"name\": \"Mahmoud\"," +
                                                    "\"email\": \"MahmoudA2@gmail.com\"," +
                                                    "\"password\": \"smolPassword2@\"" +
                                                    "}," +
                                                    "\"session\": {" +
                                                    "\"startTime\": \"20:00:00\"," +
                                                    "\"endTime\": \"21:00:00\"," +
                                                    "\"date\": \"2024-05-05\"," +
                                                    "\"instructor\": \"instructorOne\"," +
                                                    "\"course\": \"courseOne\"," +
                                                    "\"schedule\": \"schedule\"" +
                                                    "}" +
                                                    "}" +
                                                    "]" +
                                                    "}")
                                    }
                            )

                    )
            }
    )
    public RegistrationListDto findAllRegistrationsByCustomer(@PathVariable(name="customerAccount_id") int customerId) {
    	List<Registration> registrations = service.findCustomerRegistrations(customerId);
    	return new RegistrationListDto(registrations);
    }
    
    @GetMapping("/courses/{course_id}/sessions/{session_id}/registrations/customers")
    @Operation(
            summary = "Find all the registrations for a session",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully found all registration for a session",
                            content = @Content(
                                    schema = @Schema(implementation = RegistrationResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"Registration\": [" +
                                                    "{" +
                                                    "\"customer\": {" +
                                                    "\"name\" : \"Tibo\"," +
                                                    "\"email\" : \"tibo123@gmail.com\"," +
                                                    "\"password\" : \"Password#123\"" +
                                                    "}" +
                                                    "}," +
                                                    "{" +
                                                    "\"customer2\": {" +
                                                    "\"name\": \"Mahmoud\"," +
                                                    "\"email\": \"MahmoudA2@gmail.com\"," +
                                                    "\"password\": \"smolPassword2@\"" +
                                                    "}" +
                                                    "}" +
                                                    "]" +
                                                    "}")
                                    }
                            )

                    )
            }
    )
    public CustomerListDto findAllRegistrantsForSession(@PathVariable(name="session_id") int sessionId) {
    	List<CustomerResponseDto> registrants = new ArrayList<CustomerResponseDto>();
    	for (CustomerAccount customer : service.findSessionRegistrants(sessionId)) {
    		registrants.add(new CustomerResponseDto(customer));
    	}
    	return new CustomerListDto(registrants);
    }
    
    @DeleteMapping("/courses/{course_id}/sessions/{session_id}/registrations/{customerAccount_id}")
    @Operation(
            summary = "Cancel a registration",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully canceled the registration",
                            content = @Content(
                                    schema = @Schema(implementation = RegistrationResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"Registration\": [" +
                                                    "{" +
                                                    "\"customer\": {" +
                                                    "\"name\" : \"Tibo\"," +
                                                    "\"email\" : \"tibo123@gmail.com\"," +
                                                    "\"password\" : \"Password#123\"" +
                                                    "}," +
                                                    "\"session\" : {" +
                                                    "\"startTime\" : \"20:00:00\"," +
                                                    "\"endTime\" : \"21:00:00\"," +
                                                    "\"date\" : \"2024-05-05\"," +
                                                    "\"instructor\" : \"instructorOne\"," +
                                                    "\"course\" : \"courseOne\"," +
                                                    "\"schedule\" : \"schedule\"" +
                                                    "}"+
                                                    "}"+
                                                    "]"+
                                                    "}")
                                    }
                            )

                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "INTERNAL_SERVER_ERROR: Error occurred while canceling the registration",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"An error has occurred while canceling the registration.\"}")
                                    }
                            )
                    )
            }
    )
    public void cancelRegistration(@PathVariable(name="customerAccount_id") int customerId, @PathVariable(name="session_id") int sessionId) {
    	service.cancelRegistration(customerId, sessionId);
    }
}
