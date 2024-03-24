package ca.mcgill.ecse321.SportCenterManager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportCenterManager.dao.BillingInformationRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dto.BillingInformationRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.BillingInformationResponseDto;
import ca.mcgill.ecse321.SportCenterManager.dto.ErrorDto;
import ca.mcgill.ecse321.SportCenterManager.model.BillingInformation;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class BillingInformationIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private BillingInformationRepository billingRepo;
    @Autowired
    private CustomerAccountRepository customerRepo;

    private String NAME = "testName";
    private String EMAIL = "testEmail@email.com";
    private String PASSWORD = "testPassword!";
    private int validId;

    private String DEFAULT_ADDRESS = "address";
    private String DEFAULT_POSTAL_CODE = "postalCode";
    private String DEFAULT_COUNTRY = "country";
    private String DEFAULT_NAME = "name";
    private String DEFAULT_CARD_NUMBER = "cardNumber";
    private int DEFAULT_CVC = 123;

    private static String UPDATED_ADDRESS = "updatedAddress";
    private static String UPDATED_POSTAL_CODE = "updatedPostalCode";
    private static String UPDATED_COUNTRY = "updatedCountry";
    private static String UPDATED_NAME = "updatedName";
    private static String UPDATED_CARD_NUMBER = "updatedCardNumber";
    private static int UPDATED_CVC = 456;
    private static Date UPDATED_EXPIRATION_DATE = Date.valueOf(LocalDate.now().plusYears(1));

    @AfterAll
    public void clearDatabase() {
        billingRepo.deleteAll();
        customerRepo.deleteAll();
    }

    @BeforeAll
    public void setUp() {
        // Clear databases
        billingRepo.deleteAll();
        customerRepo.deleteAll();
        
        // Create a customer account with default billing information
        CustomerAccount customer = new CustomerAccount(NAME, EMAIL, PASSWORD);
        CustomerAccount createdCustomer = customerRepo.save(customer);
        this.validId = createdCustomer.getId();

        // Don't test billing since there is no endpoint for it; created automatically with customer in the service class
        BillingInformation billing = new BillingInformation(DEFAULT_ADDRESS, DEFAULT_POSTAL_CODE, DEFAULT_COUNTRY, DEFAULT_NAME, DEFAULT_CARD_NUMBER, DEFAULT_CVC, null, createdCustomer);
        billingRepo.save(billing);
    }

    @Test
    @Order(1)
    public void getDefaultBillingInformation() {
        // Setup
        String url = "/customerAccounts/" + validId + "/billingInformation";

        // Act
        ResponseEntity<BillingInformationResponseDto> response = client.getForEntity(url, BillingInformationResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BillingInformationResponseDto billingInformation = response.getBody();
        assertNotNull(billingInformation);
        assertEquals(DEFAULT_ADDRESS, billingInformation.getAddress());
        assertEquals(DEFAULT_POSTAL_CODE, billingInformation.getPostalCode());
        assertEquals(DEFAULT_COUNTRY, billingInformation.getCountry());
        assertEquals(DEFAULT_NAME, billingInformation.getName());
        assertEquals(DEFAULT_CARD_NUMBER, billingInformation.getCardNumber());
        assertEquals(DEFAULT_CVC, billingInformation.getCvc());
        assertNull(billingInformation.getExpirationDate());
    }

    @Test
    @Order(2)
    public void updateBillingInformation() {
        // Setup
        String url = "/customerAccounts/" + validId + "/billingInformation";
        BillingInformationRequestDto request = new BillingInformationRequestDto(UPDATED_ADDRESS, UPDATED_POSTAL_CODE, UPDATED_COUNTRY, UPDATED_NAME, UPDATED_CARD_NUMBER, UPDATED_CVC, UPDATED_EXPIRATION_DATE);

        // Act
        HttpEntity<BillingInformationRequestDto> requestEntity = new HttpEntity<BillingInformationRequestDto>(request);
        ResponseEntity<BillingInformationResponseDto> response = client.exchange(url, HttpMethod.PUT, requestEntity, BillingInformationResponseDto.class);

        // Assert
        assertNotNull(response, "Response is null.");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Incorrect status code.");
        BillingInformationResponseDto billingInformation = response.getBody();
        assertNotNull(billingInformation);
        assertEquals(UPDATED_ADDRESS, billingInformation.getAddress(), "Address not updated.");
        assertEquals(UPDATED_POSTAL_CODE, billingInformation.getPostalCode(), "Postal code not updated.");
        assertEquals(UPDATED_COUNTRY, billingInformation.getCountry(), "Country not updated.");
        assertEquals(UPDATED_NAME, billingInformation.getName(), "Name not updated.");
        assertEquals(UPDATED_CARD_NUMBER, billingInformation.getCardNumber(), "Card number not updated.");
        assertEquals(UPDATED_CVC, billingInformation.getCvc(), "CVC not updated.");
        // Casting to LocalDate to compare
        LocalDate local_UPDATED_EXPIRATION_DATE = UPDATED_EXPIRATION_DATE.toLocalDate();
        LocalDate local_test = billingInformation.getExpirationDate().toLocalDate();
        assertEquals(local_UPDATED_EXPIRATION_DATE, local_test, "Expiration date not updated.");
    }

    @Test
    @Order(3)
    public void getUpdatedBillingInformation() {
        // Setup
        String url = "/customerAccounts/" + validId + "/billingInformation";
        
        // Act
        ResponseEntity<BillingInformationResponseDto> response = client.getForEntity(url, BillingInformationResponseDto.class);

        // Assert
        assertNotNull(response, "Response is null.");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Incorrect status code.");
        BillingInformationResponseDto billingInformation = response.getBody();
        assertNotNull(billingInformation, "Billing information is null.");
        assertEquals(UPDATED_ADDRESS, billingInformation.getAddress(), "Address not updated.");
        assertEquals(UPDATED_POSTAL_CODE, billingInformation.getPostalCode(), "Postal code not updated.");
        assertEquals(UPDATED_COUNTRY, billingInformation.getCountry(), "Country not updated.");
        assertEquals(UPDATED_NAME, billingInformation.getName(), "Name not updated.");
        assertEquals(UPDATED_CARD_NUMBER, billingInformation.getCardNumber(), "Card number not updated.");
        assertEquals(UPDATED_CVC, billingInformation.getCvc(), "CVC not updated.");
        // Casting to LocalDate to compare
        LocalDate local_UPDATED_EXPIRATION_DATE = UPDATED_EXPIRATION_DATE.toLocalDate();
        LocalDate local_test = billingInformation.getExpirationDate().toLocalDate();
        assertEquals(local_UPDATED_EXPIRATION_DATE, local_test, "Expiration date not updated.");
    }

    @Test
    @Order(4)
    public void getBillingInformationForInvalidCustomer() {
        // Setup
        String url = "/customerAccounts/" + (validId + 1) + "/billingInformation";

        // Act
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.GET, null, ErrorDto.class);

        // Assert
        assertNotNull(response, "Response is null.");
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Incorrect status code.");
        assertEquals("There is no customer with ID " + (validId + 1) + " in the system.", response.getBody().getMessage());
    }

    @Test
    @Order(5)
    public void updateBillingInformationForInvalidCustomer() {
        // Setup
        String url = "/customerAccounts/" + (validId + 1) + "/billingInformation";
        BillingInformationRequestDto request = new BillingInformationRequestDto(UPDATED_ADDRESS, UPDATED_POSTAL_CODE, UPDATED_COUNTRY, UPDATED_NAME, UPDATED_CARD_NUMBER, UPDATED_CVC, UPDATED_EXPIRATION_DATE);

        // Act
        HttpEntity<BillingInformationRequestDto> requestEntity = new HttpEntity<BillingInformationRequestDto>(request);
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // Assert
        assertNotNull(response, "Response is null.");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Incorrect status code.");
        assertNotNull(response.getBody());
        assertEquals("There is no customer with ID " + (validId + 1) + " in the system.", response.getBody().getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {99, 1000})
    @Order(6)
    public void updateBillingInformationWithInvalidCvc(int invalid_cvc) {
        // Setup
        String url = "/customerAccounts/" + validId + "/billingInformation";
        BillingInformationRequestDto request = new BillingInformationRequestDto(UPDATED_ADDRESS, UPDATED_POSTAL_CODE, UPDATED_COUNTRY, UPDATED_NAME, UPDATED_CARD_NUMBER, invalid_cvc, UPDATED_EXPIRATION_DATE);

        // Act
        HttpEntity<BillingInformationRequestDto> requestEntity = new HttpEntity<BillingInformationRequestDto>(request);
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // Assert
        assertNotNull(response, "Response is null.");
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Incorrect status code.");
        assertNotNull(response.getBody());
        assertEquals("CVC must be a 3-digit number.", response.getBody().getMessage());
    }

    @Test
    @Order(7)
    public void updateBillingInformationWithInvalidExpirationDate() {
        // Setup
        String url = "/customerAccounts/" + validId + "/billingInformation";
        BillingInformationRequestDto request = new BillingInformationRequestDto(UPDATED_ADDRESS, UPDATED_POSTAL_CODE, UPDATED_COUNTRY, UPDATED_NAME, UPDATED_CARD_NUMBER, UPDATED_CVC, Date.valueOf(LocalDate.now().minusDays(1)));

        // Act
        HttpEntity<BillingInformationRequestDto> requestEntity = new HttpEntity<BillingInformationRequestDto>(request);
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // Assert
        assertNotNull(response, "Response is null.");
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Incorrect status code.");
        assertNotNull(response.getBody());
        assertEquals("Expiration date cannot be in the past.", response.getBody().getMessage());
    }
}
