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
      return customerRepo.findCustomerAccountById(id);
   }

     @Transactional
     public CustomerAccount updateCustomerAccount(int id, String name, String email, String password) {
        CustomerAccount customerToModify = customerRepo.findCustomerAccountById(id);
        customerToModify.setName(name);
        customerToModify.setEmail(email);
        customerToModify.setPassword(password);
        return customerRepo.save(customerToModify);
     }

   @Transactional
   public CustomerAccount createCustomer(String name, String email, String password) {
      if (customerRepo.existsCustomerAccountByEmail(email)) {
         throw new IllegalArgumentException("Customer with this email already exists");
      }
      CustomerAccount customerToCreate = new CustomerAccount(name, email, password);
      CustomerAccount createdCustomer = customerRepo.save(customerToCreate);
        billingService.createBillingInformation("address", "postalCode", "country", "name", "cardNumber", 123, null, createdCustomer);
        return createdCustomer;
   }

   @Transactional
   public CustomerAccount login(String email, String password) {
      if (!customerRepo.existsCustomerAccountByEmailAndPassword(email, password)) {
         throw new IllegalArgumentException("Invalid email or password");
      }
      return customerRepo.findCustomerAccountByEmailAndPassword(email, password);
   }

   @Transactional
   public void deleteCustomer(int id) {
      customerRepo.deleteById(id);
   }
}
