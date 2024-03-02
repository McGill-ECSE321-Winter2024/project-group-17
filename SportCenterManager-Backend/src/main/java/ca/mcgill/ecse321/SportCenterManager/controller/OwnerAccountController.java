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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/ownerAccount")
public class OwnerAccountController {
    @Autowired
    private OwnerAccountService ownerAccountService;

    @GetMapping("/{ownerAccount_id}")
    public OwnerResponseDto findOwnerById(@PathVariable int ownerAccount_id) {
        OwnerAccount owner = ownerAccountService.findOwnerById(ownerAccount_id);
        return new OwnerResponseDto(owner);
    }

    @PutMapping("/{ownerAccount_id}")
    public OwnerResponseDto modifyOwnerAccount(@PathVariable int ownerAccount_id, @RequestBody OwnerRequestDto owner) {
        OwnerAccount modifiedOwner = ownerAccountService.modifyOwnerAccount(ownerAccount_id, owner.getName(), owner.getEmail(), owner.getPassword());
        return new OwnerResponseDto(modifiedOwner);
    }

    @PostMapping()
    public OwnerResponseDto createOwner(@RequestBody OwnerRequestDto owner) {
        OwnerAccount ownerToCreate = ownerAccountService.createOwner(owner.getName(), owner.getEmail(), owner.getPassword());
        return new OwnerResponseDto(ownerToCreate);
    }
}
