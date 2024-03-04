package ca.mcgill.ecse321.SportCenterManager.service;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportCenterManager.dao.BillingInformationRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.model.BillingInformation;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;

@SpringBootTest
public class BillingInformationServiceTests {
    @Mock
    private BillingInformationRepository billingRepo;
    @Mock
    private CustomerAccountRepository customerRepo;
    @InjectMocks
    private BillingInformationService billingService;

    /*
     * Create BillingInformation
     *  Success case:
     *    Create billing information with default parameters
     *  Failure case:
     *    Customer does not exist
     */
    
    @Test
    public void testCreateBillingInformation() {
        
    }

    @Test
    public void testGetBillingInformationForValidCustomer() {
        // Setup
        String name = "TestName";
        String email = "testEmail@email.com";
        String password = "testPassword";
        CustomerAccount customer = new CustomerAccount(name, email, password);
        int customer_id = 9;
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);

        BillingInformation billingInformation = new BillingInformation("TestAddress", "TestPostalCode", "TestCountry", "TestName", "TestCardNumber", 123, Date.valueOf(LocalDate.now()), customer);
        when(billingRepo.findBillingInformationByKeyCustomerAccount(customer)).thenReturn(billingInformation);
        
        // Act
        BillingInformation billingInformationFromDb = billingService.getBillingInformation(customer_id);

        // Assert
        assertNotNull(billingInformationFromDb);
        assertEquals(billingInformation, billingInformationFromDb);
    }

    @Test
    public void testGetBillingInformationForInvalidCustomer() {
        // Setup
        int customer_id = 9;
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(null);
        
        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> billingService.getBillingInformation(customer_id));
        assertEquals("There is no customer with ID " + customer_id + " in the system.", e.getMessage());
    }

    /*
     * Update BillingInformation
     * Success case:
     *  Update billing information for an existing customer
     * Failure case:
     *  Billing information does not exist
     *  Customer does not exist
     *  Invalid parameters
     *      - Invalid address
     *      - Invalid postal code
     *      - Invalid country
     *      - Invalid name
     *      - Invalid card number
     *      - Invalid CVC
     *      - Invalid expiration date
     */

    @Test
    public void testUpdateBillingInformationForValidCustomer() {
        // Setup
        
        
        // Act


        // Assert
    }
}
