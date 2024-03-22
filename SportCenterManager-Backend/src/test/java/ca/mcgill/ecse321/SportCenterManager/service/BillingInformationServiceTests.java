package ca.mcgill.ecse321.SportCenterManager.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportCenterManager.dao.BillingInformationRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.exception.ServiceException;
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

    private String NAME = "testName";
    private String EMAIL = "testEmail@email.com";
    private String PASSWORD = "testPassword!";
    private CustomerAccount customer = new CustomerAccount(NAME, EMAIL, PASSWORD);
    private int customer_id = 9;

    private String address = "testAddress";
    private String postalCode = "testPostalCode";
    private String country = "testCountry";
    private String cardNumber = "testCardNumber";
    private int cvc = 123;
    private BillingInformation billingInformation = new BillingInformation(address, postalCode, country, NAME, cardNumber, cvc, Date.valueOf(LocalDate.now()), customer);

    @Test
    public void testCreateBillingInformationForValidCustomer() {
        // Setup
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);

        // Act
        when(billingRepo.save(any(BillingInformation.class))).thenReturn(billingInformation);
        BillingInformation createdBillingInformation = billingService.createBillingInformation(address, postalCode, country, NAME, cardNumber, cvc, Date.valueOf(LocalDate.now()), customer_id);
        
        // Assert
        assertNotNull(createdBillingInformation);
        assertEquals(billingInformation, createdBillingInformation);
        verify(billingRepo, times(1)).save(any(BillingInformation.class));
    }

    @Test
    public void testCreateBillingInformationForInvalidCustomer() {
        // Setup
        when(customerRepo.existsById(customer_id)).thenReturn(false);
        
        // Act & Assert
        ServiceException e = assertThrows(ServiceException.class, () -> billingService.createBillingInformation("TestAddress", "TestPostalCode", "TestCountry", "TestName", "TestCardNumber", 123, Date.valueOf(LocalDate.now()), customer_id));
        assertEquals("There is no customer with ID " + customer_id + " in the system.", e.getMessage());
    }

    @Test
    public void testGetBillingInformationForValidCustomer() {
        // Setup
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);
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
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(null);
        
        // Act & Assert
        ServiceException e = assertThrows(ServiceException.class, () -> billingService.getBillingInformation(customer_id));
        assertEquals("There is no customer with ID " + customer_id + " in the system.", e.getMessage());
    }

    @Test
    public void testUpdateBillingInformationForValidCustomer() {
        // Setup
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);
        when(billingRepo.existsByKeyCustomerAccount(customer)).thenReturn(true);
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
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(null);
        
        // Act & Assert
        ServiceException e = assertThrows(ServiceException.class, () -> billingService.getBillingInformation(customer_id));
        assertEquals("There is no customer with ID " + customer_id + " in the system.", e.getMessage());
    }

    @Test
    public void testUpdateBillingInformationForInvalidBillingInformation() {
        // Setup
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);
        when(billingRepo.existsByKeyCustomerAccount(customer)).thenReturn(false);
        
        // Act & Assert
        ServiceException e = assertThrows(ServiceException.class, () -> billingService.updateBillingInformation(customer_id, "testAddress", "testPostalCode", "testCountry", "testName", "testCardNumber", 123, Date.valueOf(LocalDate.now())));
        assertEquals("There is no billing information for customer with ID " + customer_id + " in the system.", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {99, 1000})
    public void testUpdateBillingInformationForInvalidCvc(int invalid_cvc) {
        // Setup
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);
        when(billingRepo.existsByKeyCustomerAccount(customer)).thenReturn(true);
        when(billingRepo.findBillingInformationByKeyCustomerAccount(customer)).thenReturn(billingInformation);

        // Act & Assert
        ServiceException e = assertThrows(ServiceException.class, () -> billingService.updateBillingInformation(customer_id, "testAddress", "testPostalCode", "testCountry", "testName", "testCardNumber", invalid_cvc, Date.valueOf(LocalDate.now())));
        assertEquals("CVC must be a 3-digit number.", e.getMessage());
    }

    @Test
    public void testUpdateBillingInformationForInvalidExpirationDate() {
        // Setup
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);
        when(billingRepo.existsByKeyCustomerAccount(customer)).thenReturn(true);
        when(billingRepo.findBillingInformationByKeyCustomerAccount(customer)).thenReturn(billingInformation);

        // Act & Assert
        ServiceException e = assertThrows(ServiceException.class, () -> billingService.updateBillingInformation(customer_id, "testAddress", "testPostalCode", "testCountry", "testName", "testCardNumber", 123, Date.valueOf(LocalDate.now().minusDays(1))));
        assertEquals("Expiration date cannot be in the past.", e.getMessage());
    }

    @Test
    public void testDeleteBillingInformationForValidCustomer() {
        // Setup
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);
        when(billingRepo.existsByKeyCustomerAccount(customer)).thenReturn(true, false);
        when(billingRepo.findBillingInformationByKeyCustomerAccount(customer)).thenReturn(billingInformation);

        // Act
        billingService.deleteBillingInformation(customer_id);

        // Assert
        verify(billingRepo, times(1)).delete(billingInformation);
    }

    @Test
    public void testDeleteBillingInformationForInvalidCustomer() {
        // Setup
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(null);
        
        // Act & Assert
        ServiceException e = assertThrows(ServiceException.class, () -> billingService.deleteBillingInformation(customer_id));
        assertEquals("There is no customer with ID " + customer_id + " in the system.", e.getMessage());
    }

    @Test
    public void testDeleteBillingInformationForInvalidBillingInformation() {
        // Setup
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);
        when(billingRepo.existsByKeyCustomerAccount(customer)).thenReturn(false);
        
        // Act & Assert
        ServiceException e = assertThrows(ServiceException.class, () -> billingService.deleteBillingInformation(customer_id));
        assertEquals("There is no billing information for customer with ID " + customer_id + " in the system.", e.getMessage());
    }

    @Test
    public void testDeleteBillingInformationFailure() {
        // Setup
        when(customerRepo.existsById(customer_id)).thenReturn(true);
        when(customerRepo.findCustomerAccountById(customer_id)).thenReturn(customer);
        when(billingRepo.existsByKeyCustomerAccount(customer)).thenReturn(true, true);
        when(billingRepo.findBillingInformationByKeyCustomerAccount(customer)).thenReturn(billingInformation);

        // Act & Assert
        ServiceException e = assertThrows(ServiceException.class, () -> billingService.deleteBillingInformation(customer_id));
        assertEquals("There was an error deleting the billing information for customer with ID " + customer_id + " in the system.", e.getMessage());
    }
}
