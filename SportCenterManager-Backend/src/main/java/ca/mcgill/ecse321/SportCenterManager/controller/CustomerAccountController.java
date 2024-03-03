package ca.mcgill.ecse321.SportCenterManager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.dto.CustomerListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CustomerRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CustomerResponseDto;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import ca.mcgill.ecse321.SportCenterManager.service.CustomerAccountService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/customerAccounts")
public class CustomerAccountController {
    @Autowired
    private CustomerAccountService customerAccountService;

    @GetMapping("/{customerAccount_id}")
    public CustomerResponseDto findCustomerById(@PathVariable int customerAccount_id) {
        CustomerAccount customer = customerAccountService.findCustomerById(customerAccount_id);
        return new CustomerResponseDto(customer);
    }

    @GetMapping()
    public CustomerListDto findAllCustomers() {
        List<CustomerResponseDto> customers = new ArrayList<CustomerResponseDto>();
        for (CustomerAccount customer : customerAccountService.findAllCustomers()) {
            customers.add(new CustomerResponseDto(customer));
        }
        return new CustomerListDto(customers);
    }

    @PutMapping("/{customerAccount_id}")
    public CustomerResponseDto updateCustomerAccount(@PathVariable int customerAccount_id, @RequestBody CustomerRequestDto customer) {
        CustomerAccount modifiedCustomer = customerAccountService.updateCustomerAccount(customerAccount_id, customer.getName(), customer.getEmail(), customer.getPassword());
        return new CustomerResponseDto(modifiedCustomer);
    }

    @PostMapping()
    public CustomerResponseDto createCustomer(@RequestBody CustomerRequestDto customer) {
        CustomerAccount customerToCreate = customerAccountService.createCustomer(customer.getName(), customer.getEmail(), customer.getPassword());
        return new CustomerResponseDto(customerToCreate);
    }   

    @DeleteMapping("/{customerAccount_id}")
    public void deleteCustomer(@PathVariable int customerAccount_id) {
        customerAccountService.deleteCustomer(customerAccount_id);
    }
}
