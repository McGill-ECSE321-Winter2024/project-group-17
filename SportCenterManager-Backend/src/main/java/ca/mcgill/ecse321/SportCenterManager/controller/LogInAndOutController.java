package ca.mcgill.ecse321.SportCenterManager.controller;

import ca.mcgill.ecse321.SportCenterManager.dto.AccountResponseDto;
import ca.mcgill.ecse321.SportCenterManager.dto.ErrorDto;
import ca.mcgill.ecse321.SportCenterManager.model.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.dto.LoginDto;
import ca.mcgill.ecse321.SportCenterManager.service.CustomerAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class LogInAndOutController {
    @Autowired
    private CustomerAccountService customerService;

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully logged in",
                            content = @Content(
                                    schema = @Schema(implementation = AccountResponseDto.class),
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
    public ResponseEntity<AccountResponseDto> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = AccountResponseDto.class),
                            examples = {
                                    @ExampleObject(value = "{" + "\"name\" : \"Bob\"," + "\"email\" : \"bob@gmail.com\", " + "\"password\" : \"Password321\"}")
                            }
                    )
            )
            @RequestBody LoginDto client) {
        Account account = customerService.login(client.getEmail(), client.getPassword());
        return ResponseEntity.ok(new AccountResponseDto(account));
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully logged out",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
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
