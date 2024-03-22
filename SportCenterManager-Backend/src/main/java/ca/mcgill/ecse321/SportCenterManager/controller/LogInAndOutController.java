package ca.mcgill.ecse321.SportCenterManager.controller;

import ca.mcgill.ecse321.SportCenterManager.dto.ErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.dto.CustomerResponseDto;
import ca.mcgill.ecse321.SportCenterManager.dto.LoginDto;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import ca.mcgill.ecse321.SportCenterManager.service.CustomerAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class LogInAndOutController {
    @Autowired
    private CustomerAccountService customerService;

    @PostMapping("/login")
    @Operation(
            summary = "Create the Owner Account",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully logged in",
                            content = @Content(
                                    schema = @Schema(implementation = CustomerResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" + "\"name\" : \"Bob\"," + "\"email\" : \"bob@gmail.com\", " + "\"password\" : \"Password321\"}")
                                    }
                            )

                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: invalid email or password.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Invalid email or password\"}")
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<CustomerResponseDto> login(@RequestBody LoginDto client) {
        CustomerAccount customer = customerService.login(client.getEmail(), client.getPassword());
        return ResponseEntity.ok(new CustomerResponseDto(customer));
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully logged out",
                            content = @Content(
                                    schema = @Schema(implementation = CustomerResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" + "\"message\" : \"\"Logged out\"\"," + "}")
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out");
    }
}
