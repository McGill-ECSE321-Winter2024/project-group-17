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

    /*
     * Modify Account
     */

     @Transactional
     public Iterable<CustomerAccount> findAllCustomers() {
        return customerRepo.findAll();
     }

     @Transactional
     public CustomerAccount findCustomerById(int id) {
        return customerRepo.findCustomerAccountById(id);
     }

     @Transactional
     public CustomerAccount createCustomer(String name, String email, String password) {
        CustomerAccount customerToCreate = new CustomerAccount(name, email, password);
        return customerRepo.save(customerToCreate);
     }

     @Transactional
     public void deleteCustomer(int id) {
        customerRepo.deleteById(id);
     }
}
