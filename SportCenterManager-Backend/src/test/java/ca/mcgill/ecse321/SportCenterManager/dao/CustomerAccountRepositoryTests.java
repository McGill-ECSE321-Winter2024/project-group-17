package ca.mcgill.ecse321.SportCenterManager.dao;

import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CustomerAccountRepositoryTests {

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        customerAccountRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadCustomerAccount() {
        // Create customer account
        String name = "Thibaut";
        String email = "thibaut@gmail.com" ;
        String password = "12345";

        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        customerAccount = customerAccountRepository.save(customerAccount);
        int id = customerAccount.getId();

        //Read customer account from database
        CustomerAccount customerAccountDatabase = customerAccountRepository.findCustomerAccountById(id);

        //Assertions
        assertNotNull(customerAccountDatabase);
        assertEquals(name, customerAccountDatabase.getName());
        assertEquals(email, customerAccountDatabase.getEmail());
        assertEquals(password, customerAccountDatabase.getPassword());
    }

}
