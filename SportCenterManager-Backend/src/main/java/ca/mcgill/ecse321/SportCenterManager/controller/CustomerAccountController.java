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
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import ca.mcgill.ecse321.SportCenterManager.service.CustomerAccountService;

@RestController
@RequestMapping("/customerAccounts")
public class CustomerAccountController {
    @Autowired
    private CustomerAccountService customerAccountService;

    @GetMapping("/{customerAccount_id}")
    @Operation(
            summary = "Find customer account by id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: successfully found customer account by id.",
                            content = @Content(
                                    schema = @Schema(implementation = CustomerResponseDto.class),
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
    public CustomerResponseDto findCustomerById(@PathVariable int customerAccount_id) {
        CustomerAccount customer = customerAccountService.findCustomerById(customerAccount_id);
        return new CustomerResponseDto(customer);
    }

    @GetMapping()
    @Operation(
            summary = "Find all customer accounts.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: successfully found all customer accounts.",
                            content = @Content(
                                    schema = @Schema(implementation = CustomerListDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"customers\": [" +
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
    public CustomerListDto findAllCustomers() {
        List<CustomerResponseDto> customers = new ArrayList<CustomerResponseDto>();
        for (CustomerAccount customer : customerAccountService.findAllCustomers()) {
            customers.add(new CustomerResponseDto(customer));
        }
        return new CustomerListDto(customers);
    }

    @PutMapping("/{customerAccount_id}")
    @Operation(
            summary = "Update customer account by id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: successfully updated customer account.",
                            content = @Content(
                                    schema = @Schema(implementation = CustomerResponseDto.class),
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
                            description = "Not Found: invalid customer id.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"There is no customer with ID 11 in the system.\"}")
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
    public CustomerResponseDto updateCustomerAccount(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = CustomerRequestDto.class),
                            examples = {
                                    @ExampleObject(value = "{" +
                                            "\"name\" : \"Eric\", " +
                                            "\"email\" : \"Eric@gmail.com\", " +
                                            "\"password\" : \"Password$1\"}"
                                    )
                            }
                    )
            )
            @RequestBody CustomerRequestDto customer, @PathVariable int customerAccount_id) {
        CustomerAccount modifiedCustomer = customerAccountService.updateCustomerAccount(customerAccount_id, customer.getName(), customer.getEmail(), customer.getPassword());
        return new CustomerResponseDto(modifiedCustomer);
    }

    @PostMapping()
    @Operation(
            summary = "Create customer account.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: successfully updated customer.",
                            content = @Content(
                                    schema = @Schema(implementation = CustomerResponseDto.class),
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
    public CustomerResponseDto createCustomer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = CustomerRequestDto.class),
                            examples = {
                                    @ExampleObject(value = "{" +
                                            "\"name\" : \"Namir\", " +
                                            "\"email\" : \"Namir@gmail.com\", " +
                                            "\"password\" : \"Password$\"}"
                                    )
                            }
                    )
            )
            @RequestBody CustomerRequestDto customer) {
        CustomerAccount customerToCreate = customerAccountService.createCustomer(customer.getName(), customer.getEmail(), customer.getPassword());
        return new CustomerResponseDto(customerToCreate);
    }   

    @DeleteMapping("/{customerAccount_id}")
    @Operation(
            summary = "Delete customer account by id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: successfully deleted customer account by id.",
                            content = @Content(
                                    schema = @Schema(implementation = CustomerListDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"customers\": [" +
                                                    "]}"
                                            )
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
    public void deleteCustomer(@PathVariable int customerAccount_id) {
        customerAccountService.deleteCustomer(customerAccount_id);
    }
}
