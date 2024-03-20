package ca.mcgill.ecse321.SportCenterManager.service;

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
     public OwnerAccount findOwnerById(int id) {
         OwnerAccount ownerAccount = ownerRepo.findOwnerAccountById(id);
         if (ownerAccount == null) {
             throw new ServiceException(HttpStatus.NOT_FOUND, "There is no owner with ID" + id);
         }
         return ownerAccount;
     }

    @Transactional
    public OwnerAccount createOwner(String name, String email, String password) {
        // Check if email and password are invalid
        String emailError = isEmailValid(email);
        String passwordError = isPasswordValid(password);

        // Error messages are thrown if email or password are is invalid. If not create, save and return
        if (!emailError.isEmpty()) {
            throw new ServiceException(HttpStatus.FORBIDDEN, emailError);
        }
        if (!passwordError.isEmpty()) {
            throw new ServiceException(HttpStatus.FORBIDDEN, passwordError);
        }
        else {
            OwnerAccount ownerToCreate = new OwnerAccount(name, email, password);
            return ownerRepo.save(ownerToCreate);
        }
    }

    @Transactional
    public OwnerAccount updateOwnerAccount(int id, String name, String email, String password) {
        // Retrieve ownerAccount with id
        OwnerAccount ownerToModify = ownerRepo.findOwnerAccountById(id);

        if (ownerToModify == null) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "The owner account was not found");
        }
        else {
            // Check if email and password are invalid
            String emailError = isEmailValid(email);
            String passwordError = isPasswordValid(password);

            // Error messages are thrown if email or password are invalid. If not update, save and return
            if (!emailError.isEmpty()) {
                throw new ServiceException(HttpStatus.FORBIDDEN, emailError);
            }
            if (!passwordError.isEmpty()) {
                throw new ServiceException(HttpStatus.FORBIDDEN, passwordError);
            }
            else {
                ownerToModify.setName(name);
                ownerToModify.setEmail(email);
                ownerToModify.setPassword(password);
                return ownerRepo.save(ownerToModify);
            }
        }
    }
    private String isEmailValid(String email) {
        String error = "";
        // Check if email is empty
        if (email == null || email.isEmpty()) {
            error = "Email is empty";
            return error;
        }
        else {
            // Check if email contains any spaces
            if (email.contains(" ")) {
                error = "Email cannot contain any space";
                return error;
            }

            int count = 0;
            String allowedCharacters = "abcdefghijklmnopqrstuvwxyz._-@1234567890";
            boolean validEmail = false;
            for (char c : email.toLowerCase().toCharArray()) {
                if (c == '@') {
                    count++;
                }
                if (allowedCharacters.indexOf(c) == -1) {
                    validEmail = true;
                }
            }

            // Check if email is valid
            if (count != 1 || (!(email.endsWith(".com")) && !(email.endsWith(".ca"))) || email.startsWith("@") || email.contains(".@") || email.contains("@.") || validEmail) {
                error = "Invalid Email";
                return error;
            }
        }
        // Check if email already exists
        if (ownerRepo.existsOwnerAccountByEmail(email)) {
            error = "Owner with this email already exists";
        }
        return error;
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
}
