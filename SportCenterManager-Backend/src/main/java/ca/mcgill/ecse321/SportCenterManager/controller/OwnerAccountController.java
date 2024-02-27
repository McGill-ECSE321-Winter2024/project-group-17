package ca.mcgill.ecse321.SportCenterManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.dto.OwnerRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.OwnerResponseDto;
import ca.mcgill.ecse321.SportCenterManager.model.OwnerAccount;
import ca.mcgill.ecse321.SportCenterManager.service.OwnerAccountService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class OwnerAccountController {
    @Autowired
    private OwnerAccountService ownerAccountService;

    /*
     * PUT /ownerAccounts/{ownerAccount_id}
     */

    @GetMapping("/ownerAccount/{ownerAccount_id}")
    public OwnerResponseDto findOwnerById(@PathVariable int ownerAccount_id) {
        OwnerAccount owner = ownerAccountService.findOwnerById(ownerAccount_id);
        return new OwnerResponseDto(owner);
    }

    @PostMapping("/ownerAccount")
    public OwnerResponseDto createOwner(@RequestBody OwnerRequestDto owner) {
        OwnerAccount ownerToCreate = ownerAccountService.createOwner(owner.getName(), owner.getEmail(), owner.getPassword());
        return new OwnerResponseDto(ownerToCreate);
    }  
}
