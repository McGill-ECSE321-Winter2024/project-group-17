package ca.mcgill.ecse321.SportCenterManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.SportCenterManager.dao.InstructorAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.exception.ServiceException;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import jakarta.transaction.Transactional;

@Service
public class InstructorAccountService {
    @Autowired
    private InstructorAccountRepository instructorRepo;

    @Transactional
    public Iterable<InstructorAccount> findAllInstructors() {
        return instructorRepo.findAll();
    }

    @Transactional
    public InstructorAccount findInstructorById(int id) {
        InstructorAccount instructorAccount = instructorRepo.findInstructorAccountById(id);
        if (instructorAccount == null) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "There is no instructor with ID" + id);
        }
        return instructorAccount;
    }

    @Transactional
    public InstructorAccount updateInstructorAccount(int id, String name, String email, String password) {
        // Retrieve instructorAccount with id
        InstructorAccount instructorToModify = instructorRepo.findInstructorAccountById(id);

        if (instructorToModify == null) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "The instructor account was not found");
        }
        else {
            // Check if name, email and password are invalid
            String nameError = isNameEmpty(name);
            String emailError = isEmailValid(email);
            String passwordError = isPasswordValid(password);

            // Error messages are thrown if name or email or password are invalid. If not update, save and return
            if (!nameError.isEmpty()) {
                throw new ServiceException(HttpStatus.FORBIDDEN, nameError);
            }
            if (!emailError.isEmpty()) {
                throw new ServiceException(HttpStatus.FORBIDDEN, emailError);
            }
            if (!passwordError.isEmpty()) {
                throw new ServiceException(HttpStatus.FORBIDDEN, passwordError);
            }
            else {
                instructorToModify.setName(name);
                instructorToModify.setEmail(email);
                instructorToModify.setPassword(password);
                return instructorRepo.save(instructorToModify);
            }
        }
    }

    @Transactional
    public InstructorAccount createInstructor(String name, String email, String password) {
        // Check if name, email and password are invalid
        String nameError = isNameEmpty(name);
        String emailError = isEmailValid(email);
        String passwordError = isPasswordValid(password);

        // Error messages are thrown if name or email or password are invalid. If not create, save and return
        if (!nameError.isEmpty()) {
            throw new ServiceException(HttpStatus.FORBIDDEN, nameError);
        }
        if (!emailError.isEmpty()) {
            throw new ServiceException(HttpStatus.FORBIDDEN, emailError);
        }
        if (!passwordError.isEmpty()) {
            throw new ServiceException(HttpStatus.FORBIDDEN, passwordError);
        }
        else {
            InstructorAccount instructorToCreate = new InstructorAccount(name, email, password);
            return instructorRepo.save(instructorToCreate);
        }
    }

    @Transactional
    public boolean deleteInstructor(int id) {
        InstructorAccount instructorAccount = findInstructorById(id);
        if (instructorAccount != null) {
            instructorRepo.delete(instructorAccount);
            return true;
        }
        else {
            return false;
        }
    }

    //HELPER METHODS
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

            if (email.equals("owner@sportcenter.com")) {
                error = "Email cannot be owner@sportcenter.com";
                return error;
            }

            if (!(email.endsWith("@sportcenter.com"))) {
                error = "Email does not end with @sportcenter.com";
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
            if (count != 1 || email.startsWith("@") || validEmail) {
                error = "Invalid Email";
                return error;
            }
        }
        // Check if email already exists
        if (instructorRepo.existsInstructorAccountByEmail(email)) {
            error = "Instructor with this email already exists";
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
    private String isNameEmpty(String name) {
        String error = "";

        // Check if name is empty
        if (name == null || name.isEmpty()) {
            error = "Name is empty";
        }

        return error;
    }

}
