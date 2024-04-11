package ca.mcgill.ecse321.SportCenterManager.service;

import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.SportCenterManager.dao.OwnerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.exception.ServiceException;
import ca.mcgill.ecse321.SportCenterManager.model.OwnerAccount;
import jakarta.transaction.Transactional;

@Service
public class OwnerAccountService {
    @Autowired
    private OwnerAccountRepository ownerRepo;

     @Transactional
     public OwnerAccount findOwner() {
         String email = "owner@sportcenter.com";
         OwnerAccount ownerAccount = ownerRepo.findOwnerAccountByEmail(email);
         if (ownerAccount == null) {
             throw new ServiceException(HttpStatus.NOT_FOUND, "There is no owner");
         }
         return ownerAccount;
     }

    @Transactional
    public OwnerAccount createOwner(String name, String password) {
         String email = "owner@sportcenter.com";
         if (ownerRepo.existsOwnerAccountByEmail(email)) {
             throw new ServiceException(HttpStatus.CONFLICT, "Only one owner can exist");
         }
         else {
             // Check if name and password are invalid
             String nameError = isNameEmpty(name);
             String passwordError = isPasswordValid(password);

             // Error messages are thrown if name or password is invalid. If not create, save and return
             if (!nameError.isEmpty()) {
                 throw new ServiceException(HttpStatus.FORBIDDEN, nameError);
             }
             if (!passwordError.isEmpty()) {
                 throw new ServiceException(HttpStatus.FORBIDDEN, passwordError);
             } else {
                 OwnerAccount ownerToCreate = new OwnerAccount(name, email, password);
                 return ownerRepo.save(ownerToCreate);
             }
         }
    }

    @Transactional
    public OwnerAccount updateOwnerAccount(String name, String password) {
        String email = "owner@sportcenter.com";

        // Retrieve ownerAccount with email
        OwnerAccount ownerToModify = ownerRepo.findOwnerAccountByEmail(email);

        if (ownerToModify == null) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "The owner account was not found");
        }
        else {
            // Check if name and password are invalid
            String nameError = isNameEmpty(name);
            String passwordError = isPasswordValid(password);

            // Error messages are thrown if name or password is invalid. If not create, save and return
            if (!nameError.isEmpty()) {
                throw new ServiceException(HttpStatus.FORBIDDEN, nameError);
            }
            if (!passwordError.isEmpty()) {
                throw new ServiceException(HttpStatus.FORBIDDEN, passwordError);
            }
            else {
                ownerToModify.setName(name);
                ownerToModify.setPassword(password);
                return ownerRepo.save(ownerToModify);
            }
        }
    }

    private String isPasswordValid(String password) {
        String error = "";
        String specialCharacters = "!@#$%^&*()-_+={}[]|;:'<>,./";

        // Check if password is empty
        if (password == null || password.isEmpty()) {
            error = "Password is empty";
            return error;
        }
        else {
            // Check if password is at least 8 character long
            if (password.length() < 8) {
                error = "Password must be at least eight characters long";
                return error;
            }

            // Check if password contains an upper-case character
            else if (password.toLowerCase().equals(password)) {
                error = "Password must contain one upper-case character";
                return error;
            }

            // Check if password contains a lower-case character
            else if (password.toUpperCase().equals(password)) {
                error = "Password must contain one lower-case character";
                return error;
            }

            // Check if there is at least one special character
            boolean passwordValid = true;
            for (char c : password.toLowerCase().toCharArray()) {
                if (specialCharacters.indexOf(c) >= 0) {
                    passwordValid = false;
                }
            }
            if (passwordValid) {
                error = "Password must contain at least one special character";
            }
        }
        return error;
    }

    private String isNameEmpty(String name) {
        String error = "";

        // Check if name is empty
        if (name == null || name.isEmpty()) {
            error = "Name is empty";
        }

        return error;
    }
}
