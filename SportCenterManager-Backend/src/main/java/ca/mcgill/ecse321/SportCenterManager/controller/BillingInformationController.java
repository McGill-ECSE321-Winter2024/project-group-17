package ca.mcgill.ecse321.SportCenterManager.controller;

import ca.mcgill.ecse321.SportCenterManager.dto.ErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.dto.BillingInformationRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.BillingInformationResponseDto;
import ca.mcgill.ecse321.SportCenterManager.model.BillingInformation;
import ca.mcgill.ecse321.SportCenterManager.service.BillingInformationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import java.sql.Date;
import java.time.LocalDate;

@RestController
@RequestMapping("/customerAccounts/{customerAccount_id}/billingInformation")
public class BillingInformationController {
    @Autowired
    private BillingInformationService billingService;

    @GetMapping()
    @Operation(
            summary = "Find the billing information of a customer by id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: successfully retrieved billing information.",
                            content = @Content(
                                    schema = @Schema(implementation = BillingInformationResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"address\" : \"address\", " +
                                                    "\"postalCode\" : \"postalCode\", " +
                                                    "\"country\" : \"country\", " +
                                                    "\"name\" : \"postalCode\", " +
                                                    "\"cardNumber\" : \"cardNumber\", " +
                                                    "\"cvc\" : 123, " +
                                                    "\"expirationDate\" : \"2024-03-21T03:30:07.329Z\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: invalid customer id.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"There is no customer with ID 11 in the system.\"}")
                                    }
                            )
                    )

            }
    )
    public BillingInformationResponseDto getBillingInformation(@PathVariable int customerAccount_id) {
        BillingInformation billingInformation = billingService.getBillingInformation(customerAccount_id);
        return new BillingInformationResponseDto(billingInformation);
    }

    @PutMapping()
    @Operation(
            summary = "Update the billing information of a customer by id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: successfully updated billing information.",
                            content = @Content(
                                    schema = @Schema(implementation = BillingInformationResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"address\" : \"updatedAddress\", " +
                                                    "\"postalCode\" : \"updatedPostalCode\", " +
                                                    "\"country\" : \"updatedCountry\", " +
                                                    "\"name\" : \"updatedPostalCode\", " +
                                                    "\"cardNumber\" : \"updatedCardNumber\", " +
                                                    "\"cvc\" : 456, " +
                                                    "\"expirationDate\" : \"2024-03-21T03:30:07.329Z\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: invalid customer id.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"There is no customer with ID 11 in the system.\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found: invalid billing information.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"There is no billing information for customer with ID 11 in the system.\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: invalid parameters.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Card number cannot be empty.\"}")
                                    }
                            )
                    )
            }
    )
    public BillingInformationResponseDto updateBillingInformation(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = BillingInformationRequestDto.class),
                            examples = {
                                    @ExampleObject(value = "{" +
                                            "\"address\" : \"updatedAddress\", " +
                                            "\"postalCode\" : \"updatedPostalCode\", " +
                                            "\"country\" : \"updatedCountry\", " +
                                            "\"name\" : \"updatedPostalCode\", " +
                                            "\"cardNumber\" : \"updatedCardNumber\", " +
                                            "\"cvc\" : 456, " +
                                            "\"expirationDate\" : \"2024-03-21T03:30:07.329Z\"}"
                                    )
                            }
                    )
            )
            @RequestBody BillingInformationRequestDto billingInfo, @PathVariable int customerAccount_id) {
        BillingInformation billingInformation = billingService.updateBillingInformation(customerAccount_id, billingInfo.getAddress(), billingInfo.getPostalCode(), billingInfo.getCountry(), billingInfo.getName(), billingInfo.getCardNumber(), billingInfo.getCvc(), billingInfo.getExpirationDate());
        return new BillingInformationResponseDto(billingInformation);
    }
}
