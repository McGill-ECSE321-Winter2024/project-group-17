package ca.mcgill.ecse321.SportCenterManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
            summary = "Retrieve the billing information of a specific customer.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved billing information.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK.\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Failed to retrieve billing information: invalid customer id.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(value = "{\"code\" : 404, \"Status\" : \"Not Found.\"}")
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
            summary = "Update the billing information of a specific customer.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully updated billing information.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK.\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Failed to update billing information: invalid customer id.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(value = "{\"code\" : 404, \"Status\" : \"Not Found.\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Failed to update billing information: invalid parameters.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(value = "{\"code\" : 403, \"Status\" : \"Forbidden.\"}")
                                    }
                            )
                    )
            }
    )
    public BillingInformationResponseDto updateBillingInformation(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "/application/json",
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
            @PathVariable int customerAccount_id, @RequestBody BillingInformationRequestDto billingInfo) {
        BillingInformation billingInformation = billingService.updateBillingInformation(customerAccount_id, billingInfo.getAddress(), billingInfo.getPostalCode(), billingInfo.getCountry(), billingInfo.getName(), billingInfo.getCardNumber(), billingInfo.getCvc(), billingInfo.getExpirationDate());
        return new BillingInformationResponseDto(billingInformation);
    }
}
