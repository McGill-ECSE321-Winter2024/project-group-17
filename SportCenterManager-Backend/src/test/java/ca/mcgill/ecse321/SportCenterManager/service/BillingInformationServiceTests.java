package ca.mcgill.ecse321.SportCenterManager.service;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

    @Test
    public void testCreateBillingInformationForValidCustomer() {
        // Setup
        String name = "testName";
        String email = "testEmail";
        String password = "testPassword";
        CustomerAccount customer = new CustomerAccount(name, email, password);
        int customer_id = 9;
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);

        // Act
        String address = "testAddress";
        String postalCode = "testPostalCode";
        String country = "testCountry";
        String cardNumber = "testCardNumber";
        int cvc = 123;
        BillingInformation billingInformation = new BillingInformation(address, postalCode, country, name, cardNumber, cvc, Date.valueOf(LocalDate.now()), customer);
        when(billingRepo.save(any(BillingInformation.class))).thenReturn(billingInformation);
        BillingInformation createdBillingInformation = billingService.createBillingInformation(address, postalCode, country, name, cardNumber, cvc, Date.valueOf(LocalDate.now()), customer_id);
        
        // Assert
        assertNotNull(createdBillingInformation);
        assertEquals(billingInformation, createdBillingInformation);
        verify(billingRepo, times(1)).save(any(BillingInformation.class));
    }

    @Test
    public void testCreateBillingInformationForInvalidCustomer() {
        // Setup
        int customer_id = 9;
        when(customerRepo.existsById(customer_id)).thenReturn(false);
        
        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> billingService.createBillingInformation("TestAddress", "TestPostalCode", "TestCountry", "TestName", "TestCardNumber", 123, Date.valueOf(LocalDate.now()), customer_id));
        assertEquals("There is no customer with ID " + customer_id + " in the system.", e.getMessage());
    }

    @Test
    public void testGetBillingInformationForValidCustomer() {
        // Setup
        String name = "testName";
        String email = "testEmail@email.com";
        String password = "testPassword";
        CustomerAccount customer = new CustomerAccount(name, email, password);
        int customer_id = 9;
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);

        String address = "testAddress";
        String postalCode = "testPostalCode";
        String country = "testCountry";
        String cardNumber = "testCardNumber";
        int cvc = 123;
        BillingInformation billingInformation = new BillingInformation(address, postalCode, country, name, cardNumber, cvc, Date.valueOf(LocalDate.now()), customer);
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
     * Failure case:
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
        String name = "testName";
        String email = "testEmail";
        String password = "testPassword";
        int customer_id = 9;
        CustomerAccount customer = new CustomerAccount(name, email, password);
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);
        when(billingRepo.existsByKeyCustomerAccount(customer)).thenReturn(true);
        
        String address = "testAddress";
        String postalCode = "testPostalCode";
        String country = "testCountry";
        String cardNumber = "testCardNumber";
        int cvc = 123;
        BillingInformation billingInformation = new BillingInformation(address, postalCode, country, name, cardNumber, cvc, Date.valueOf(LocalDate.now()), customer);
        when(billingRepo.findBillingInformationByKeyCustomerAccount(customer)).thenReturn(billingInformation);

        String newAddress = "newTestAddress";
        String newPostalCode = "newTestPostalCode";
        String newCountry = "newTestCountry";
        String newName = "newTestName";
        String newCardNumber = "newTestCardNumber";
        int newCvc = 456;
        Date newExpirationDate = Date.valueOf(LocalDate.now().plusDays(1));
        when(billingRepo.save(billingInformation)).thenReturn(new BillingInformation(newAddress, newPostalCode, newCountry, newName, newCardNumber, newCvc, newExpirationDate, customer));

        // Act
        BillingInformation updatedBillingInformation = billingService.updateBillingInformation(customer_id, newAddress, newPostalCode, newCountry, newName, newCardNumber, newCvc, newExpirationDate);

        // Assert
        assertNotNull(updatedBillingInformation);
        assertEquals(billingInformation, updatedBillingInformation);
        verify(billingRepo, times(1)).save(billingInformation);
    }

    @Test
    public void testUpdateBillingInformationForInvalidCustomer() {
        // Setup
        int customer_id = 9;
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(null);
        
        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> billingService.getBillingInformation(customer_id));
        assertEquals("There is no customer with ID " + customer_id + " in the system.", e.getMessage());
    }

    @Test
    public void testUpdateBillingInformationForInvalidBillingInformation() {
        // Setup
        String name = "testName";
        String email = "testEmail";
        String password = "testPassword";
        int customer_id = 9;
        CustomerAccount customer = new CustomerAccount(name, email, password);
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);
        when(billingRepo.existsByKeyCustomerAccount(customer)).thenReturn(false);
        
        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> billingService.updateBillingInformation(customer_id, "testAddress", "testPostalCode", "testCountry", "testName", "testCardNumber", 123, Date.valueOf(LocalDate.now())));
        assertEquals("There is no billing information for customer with ID " + customer_id + " in the system.", e.getMessage());
    }

    @Test
    public void updateBillingInformationWithNullParameters() {
        // Setup
        String name = "testName";
        String email = "testEmail";
        String password = "testPassword";
        int customer_id = 9;
        CustomerAccount customer = new CustomerAccount(name, email, password);
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);
        when(billingRepo.existsByKeyCustomerAccount(customer)).thenReturn(true);

        String address = "testAddress";
        String postalCode = "testPostalCode";
        String country = "testCountry";
        String cardNumber = "testCardNumber";
        int cvc = 123;
        BillingInformation billingInformation = new BillingInformation(address, postalCode, country, name, cardNumber, cvc, Date.valueOf(LocalDate.now()), customer);
        when(billingRepo.findBillingInformationByKeyCustomerAccount(customer)).thenReturn(billingInformation);

        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> billingService.updateBillingInformation(customer_id, null, "testPostalCode", "testCountry", "testName", "testCardNumber", 123, Date.valueOf(LocalDate.now())));
        assertEquals("Address cannot be empty.", e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () -> billingService.updateBillingInformation(customer_id, "testAddress", null, "testCountry", "testName", "testCardNumber", 123, Date.valueOf(LocalDate.now())));
        assertEquals("Postal code cannot be empty.", e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () -> billingService.updateBillingInformation(customer_id, "testAddress", "testPostalCode", null, "testName", "testCardNumber", 123, Date.valueOf(LocalDate.now())));
        assertEquals("Country cannot be empty.", e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () -> billingService.updateBillingInformation(customer_id, "testAddress", "testPostalCode", "testCountry", null, "testCardNumber", 123, Date.valueOf(LocalDate.now())));
        assertEquals("Name cannot be empty.", e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () -> billingService.updateBillingInformation(customer_id, "testAddress", "testPostalCode", "testCountry", "testName", null, 123, Date.valueOf(LocalDate.now())));
        assertEquals("Card number cannot be empty.", e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () -> billingService.updateBillingInformation(customer_id, "testAddress", "testPostalCode", "testCountry", "testName", "testCardNumber", 123, null));
        assertEquals("Expiration date cannot be empty.", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {99, 1000})
    public void testUpdateBillingInformationForInvalidCvc(int invalid_cvc) {
        // Setup
        String name = "testName";
        String email = "testEmail";
        String password = "testPassword";
        int customer_id = 9;
        CustomerAccount customer = new CustomerAccount(name, email, password);
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);
        when(billingRepo.existsByKeyCustomerAccount(customer)).thenReturn(true);

        String address = "testAddress";
        String postalCode = "testPostalCode";
        String country = "testCountry";
        String cardNumber = "testCardNumber";
        int cvc = 123;
        BillingInformation billingInformation = new BillingInformation(address, postalCode, country, name, cardNumber, cvc, Date.valueOf(LocalDate.now()), customer);
        when(billingRepo.findBillingInformationByKeyCustomerAccount(customer)).thenReturn(billingInformation);

        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> billingService.updateBillingInformation(customer_id, "testAddress", "testPostalCode", "testCountry", "testName", "testCardNumber", invalid_cvc, Date.valueOf(LocalDate.now())));
        assertEquals("CVC must be a 3-digit number.", e.getMessage());

    }
}
