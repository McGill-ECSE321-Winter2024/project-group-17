package ca.mcgill.ecse321.SportCenterManager.dao;

import ca.mcgill.ecse321.SportCenterManager.model.BillingInformation;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

@SpringBootTest
public class BillingInformationRepositoryTests {
    @Autowired
    private BillingInformationRepository billingInformationRepository;
    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase(){
        billingInformationRepository.deleteAll();
        customerAccountRepository.deleteAll();
    }
    @Test
    public void testPersistAndLoadBillingInformation(){
        // Create and persist CustomerAccount
        String customerName = "Thibaut";
        String email = "thibaut@gmail.com";
        String password = "456";

        CustomerAccount customerAccount = new CustomerAccount(customerName, email, password);
        customerAccount = customerAccountRepository.save(customerAccount);

        // Create and persist BillingInformation
        String address = "845 Sherbrooke St W, Montreal, Quebec";
        String postalCode = "H3A 0G4";
        String country = "Canada";
        String billingName = "Thibaut";
        String cardNumber = "1234 5678 9012 3456";
        int cvc = 123;
        Date expirationDate = Date.valueOf("2027-01-25");

        BillingInformation billingInformation = new BillingInformation(address, postalCode, country, billingName, cardNumber, cvc, expirationDate, customerAccount);
        billingInformation = billingInformationRepository.save(billingInformation);
        CustomerAccount billingAccount = billingInformation.getKey().getCustomerAccount();

        // Read BillingInformation from database
        BillingInformation billingInformationFromDb = billingInformationRepository.findBillingInformationByKeyCustomerAccount(billingAccount);

        // Assertions
        assertNotNull(billingInformationFromDb);
        assertNotNull(billingInformationFromDb.getKey().getCustomerAccount());
        assertEquals(address, billingInformationFromDb.getAddress());
        assertEquals(postalCode, billingInformationFromDb.getPostalCode());
        assertEquals(country, billingInformationFromDb.getCountry());
        assertEquals(billingName, billingInformationFromDb.getName());
        assertEquals(cardNumber, billingInformation.getCardNumber());
        assertEquals(cvc, billingInformation.getCvc());
        assertEquals(expirationDate, billingInformation.getExpirationDate());
    }
}
