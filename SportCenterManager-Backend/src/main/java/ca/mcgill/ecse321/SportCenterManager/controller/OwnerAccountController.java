package ca.mcgill.ecse321.SportCenterManager.controller;

import ca.mcgill.ecse321.SportCenterManager.dto.ErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.SportCenterManager.dto.OwnerRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.OwnerResponseDto;
import ca.mcgill.ecse321.SportCenterManager.model.OwnerAccount;
import ca.mcgill.ecse321.SportCenterManager.service.OwnerAccountService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ownerAccount")
public class OwnerAccountController {
    @Autowired
    private OwnerAccountService ownerAccountService;

    @GetMapping()
    @Operation(
            summary = "Find the Owner Account",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully found owner account",
                            content = @Content(
                                    schema = @Schema(implementation = OwnerResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" + "\"name\" : \"Namir\"," + "\"email\" : \"owner@sportcenter.com\", " + "\"password\" : \"Password$\"}")
                                    }
                            )

                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: There is no owner account.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"There is no owner.\"}")
                                    }
                            )
                    )
            }
    )
    public OwnerResponseDto findOwner() {
        OwnerAccount owner = ownerAccountService.findOwner();
        return new OwnerResponseDto(owner);
    }

    @PutMapping()
    @Operation(
            summary = "Update the Owner Account",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully updated owner account",
                            content = @Content(
                                    schema = @Schema(implementation = OwnerResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" + "\"name\" : \"Eric\"," + "\"email\" : \"owner@sportcenter.com\", "
                                                    + "\"password\" : \"Password$1\"}")
                                    }
                            )

                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: The owner account was not found.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"The owner account was not found\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: invalid parameters.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Password must contain one upper-case character\"}")
                                    }
                            )
                    )
            }
    )
    public OwnerResponseDto updateOwnerAccount(@RequestBody OwnerRequestDto owner) {
        OwnerAccount modifiedOwner = ownerAccountService.updateOwnerAccount(owner.getName(), owner.getPassword());
        return new OwnerResponseDto(modifiedOwner);
    }

    @PostMapping()
    @Operation(
            summary = "Create the Owner Account",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully created Owner account",
                            content = @Content(
                                    schema = @Schema(implementation = OwnerResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" + "\"name\" : \"Namir\"," + "\"email\" : \"owner@sportcenter.com\", " + "\"password\" : \"Password$1\"}")
                                    }
                            )

                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict: Only one owner can be created",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Only one owner can exist\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: invalid parameters.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Password must be at least eight characters long\"}")
                                    }
                            )
                    )
            }
    )
    public OwnerResponseDto createOwner(@RequestBody OwnerRequestDto owner) {
        OwnerAccount ownerToCreate = ownerAccountService.createOwner(owner.getName(), owner.getPassword());
        return new OwnerResponseDto(ownerToCreate);
    }
}
