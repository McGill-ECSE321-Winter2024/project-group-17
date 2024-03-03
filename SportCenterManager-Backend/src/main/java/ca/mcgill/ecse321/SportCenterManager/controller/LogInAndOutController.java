package ca.mcgill.ecse321.SportCenterManager.controller;

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
    public ResponseEntity<CustomerResponseDto> login(@RequestBody LoginDto client) {
        CustomerAccount customer = customerService.login(client.getEmail(), client.getPassword());
        return ResponseEntity.ok(new CustomerResponseDto(customer));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out");
    }
}
