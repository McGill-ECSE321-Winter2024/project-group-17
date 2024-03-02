package ca.mcgill.ecse321.SportCenterManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.dto.BillingInformationResponseDto;
import ca.mcgill.ecse321.SportCenterManager.model.BillingInformation;
import ca.mcgill.ecse321.SportCenterManager.service.BillingInformationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/customerAccounts/{customerAccount_id}/billingInformation")
public class BillingInformationController {
    @Autowired
    private BillingInformationService billingService;

    // @GetMapping()
    // public BillingInformationResponseDto getBillingInformation(@PathVariable int customerAccount_id) {
    //     BillingInformation billingInformation = billingService.getBillingInformation(customerAccount_id);
    //     return new BillingInformationResponseDto(billingInformation);
    // }
}
