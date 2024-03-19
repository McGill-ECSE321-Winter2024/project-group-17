package ca.mcgill.ecse321.SportCenterManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import jakarta.transaction.Transactional;

@Service
public class CustomerAccountService {
   @Autowired
   private CustomerAccountRepository customerRepo;
   @Autowired
   private BillingInformationService billingService;

    @Transactional
    public Iterable<CustomerAccount> findAllCustomers() {
      return customerRepo.findAll();
   }
   @Transactional
   public CustomerAccount findCustomerById(int id) {
        CustomerAccount customerAccount = customerRepo.findCustomerAccountById(id);
        if (customerAccount == null) {
            throw new IllegalArgumentException("There is no course with ID" + id);
        }
        return customerAccount;
   }

   @Transactional
   public CustomerAccount updateCustomerAccount(int id, String name, String email, String password) {
       // Retrieve customerAccount with id
       CustomerAccount customerToModify = customerRepo.findCustomerAccountById(id);

       if (customerToModify == null) {
            throw new IllegalArgumentException("The customer account was not found");
       }
       else {
           // Check if email and password are invalid
           String emailError = isEmailValid(email);
           String passwordError = isPasswordValid(password);

           // Error messages are thrown if email or password are invalid. If not update, save and return
           if (!emailError.isEmpty()) {
                throw new IllegalArgumentException(emailError);
           }
           if (!passwordError.isEmpty()) {
               throw new IllegalArgumentException(passwordError);
           }
           else {
               customerToModify.setName(name);
               customerToModify.setEmail(email);
               customerToModify.setPassword(password);
               return customerRepo.save(customerToModify);
           }
       }
    }

   @Transactional
   public CustomerAccount createCustomer(String name, String email, String password) {
        // Check if email and password are invalid
        String emailError = isEmailValid(email);
        String passwordError = isPasswordValid(password);

        // Error messages are thrown if email or password are is invalid. If not create, save and return
        if (!emailError.isEmpty()) {
            throw new IllegalArgumentException(emailError);
        }
        if (!passwordError.isEmpty()) {
            throw new IllegalArgumentException(passwordError);
        }
        else {
            CustomerAccount customerToCreate = new CustomerAccount(name, email, password);
            CustomerAccount createdCustomer = customerRepo.save(customerToCreate);
            billingService.createBillingInformation("address", "postalCode", "country", "name", "cardNumber", 123, null,
            createdCustomer.getId());
            return createdCustomer;
        }
   }
   @Transactional
   public CustomerAccount login(String email, String password) {
      if (!customerRepo.existsCustomerAccountByEmailAndPassword(email, password)) {
         //Throw 400 Status Code
         throw new IllegalArgumentException("Invalid email or password");
      }
      return customerRepo.findCustomerAccountByEmailAndPassword(email, password);
   }

    @Transactional
    public boolean deleteCustomer(int id) {
        CustomerAccount customerAccount = findCustomerById(id);
        if (customerAccount != null) {
            customerRepo.delete(customerAccount);
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
        if (customerRepo.existsCustomerAccountByEmail(email)) {
            error = "Customer with this email already exists";
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
