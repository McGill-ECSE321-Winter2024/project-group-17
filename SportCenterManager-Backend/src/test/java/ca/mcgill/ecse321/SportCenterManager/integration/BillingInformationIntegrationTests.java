package ca.mcgill.ecse321.SportCenterManager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportCenterManager.dto.BillingInformationRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.BillingInformationResponseDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CustomerRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CustomerResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class BillingInformationIntegrationTests {
    @Autowired
    private TestRestTemplate client;

    private String NAME = "testName";
    private String EMAIL = "testEmail@gmail.com";
    private String PASSWORD = "testPassword";
    private int validId;

    private String DEFAULT_ADDRESS = "address";
    private String DEFAULT_POSTAL_CODE = "postalCode";
    private String DEFAULT_COUNTRY = "country";
    private String DEFAULT_NAME = "name";
    private String DEFAULT_CARD_NUMBER = "cardNumber";
    private int DEFAULT_CVC = 123;

    private String UPDATED_ADDRESS = "updatedAddress";
    private String UPDATED_POSTAL_CODE = "updatedPostalCode";
    private String UPDATED_COUNTRY = "updatedCountry";
    private String UPDATED_NAME = "updatedName";
    private String UPDATED_CARD_NUMBER = "updatedCardNumber";
    private int UPDATED_CVC = 456;
    private Date UPDATED_EXPIRATION_DATE = Date.valueOf(LocalDate.now().plusYears(1));

    @Test
    @Order(1)
    public void createBillingInformation() {
        // Setup
        String url = "/customerAccounts";
        CustomerRequestDto request = new CustomerRequestDto(NAME, EMAIL, PASSWORD);

        // Act
        ResponseEntity<CustomerResponseDto> response = client.postForEntity(url, request, CustomerResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CustomerResponseDto customer = response.getBody();
        assertNotNull(customer);
        assertEquals(NAME, customer.getName());
        assertEquals(EMAIL, customer.getEmail());
        assertNotNull(customer.getId());
        assertNotNull(customer.getId() > 0, "Customer ID must be greater than 0.");

        this.validId = customer.getId();
    }   

    @Test
    @Order(2)
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
    @Order(3)
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
    @Order(4)
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
}
