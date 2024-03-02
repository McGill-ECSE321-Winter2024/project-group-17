package ca.mcgill.ecse321.SportCenterManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.SportCenterManager.dao.OwnerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.model.OwnerAccount;
import jakarta.transaction.Transactional;

@Service
public class OwnerAccountService {
    @Autowired
    private OwnerAccountRepository ownerRepo;

     @Transactional
     public OwnerAccount findOwnerById(int id) {
        return ownerRepo.findOwnerAccountById(id);
     }

    @Transactional
    public OwnerAccount createOwner(String name, String email, String password) {
        OwnerAccount owner = new OwnerAccount(name, email, password);
        return ownerRepo.save(owner);
    }

    @Transactional
    public OwnerAccount updateOwnerAccount(int id, String name, String email, String password) {
        OwnerAccount owner = ownerRepo.findOwnerAccountById(id);
        owner.setName(name);
        owner.setEmail(email);
        owner.setPassword(password);
        return ownerRepo.save(owner);
    }
}
