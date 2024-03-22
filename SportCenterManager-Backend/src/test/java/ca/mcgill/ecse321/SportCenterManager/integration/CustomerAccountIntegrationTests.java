package ca.mcgill.ecse321.SportCenterManager.integration;

import ca.mcgill.ecse321.SportCenterManager.dao.BillingInformationRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dto.CustomerListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CustomerRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CustomerResponseDto;
import ca.mcgill.ecse321.SportCenterManager.dto.ErrorDto;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerAccountIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private CustomerAccountRepository customerRepo;
    @Autowired
    private BillingInformationRepository billingRepo;

    private String name = "Namir";
    private String email = "Namir@gmail.com";
    private String password = "Password$";
    private String newName = "Eric";
    private String newEmail = "Eric@gmail.com";
    private String newPassword = "Password$1";

    private String newName1 = "Mahmoud";
    private String newEmail1 = "Mahmoud@gmail.com";
    private String newPassword1 = "Password$2";

    private int validId;
    private int invalidId = -1;


    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        billingRepo.deleteAll();
        customerRepo.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateValidCustomer() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, email, password);

        //act
        ResponseEntity<CustomerResponseDto> response = client.postForEntity("/customerAccounts", requestDto, CustomerResponseDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Namir", response.getBody().getName());
        assertEquals("Namir@gmail.com", response.getBody().getEmail());
        assertEquals("Password$", response.getBody().getPassword());
        assertTrue(response.getBody().getId() > 0, "ID is not positive" );

        this.validId = response.getBody().getId();
    }
    @Test
    @Order(2)
    public void testFindCustomerByValidId() {
        // act
        ResponseEntity<CustomerResponseDto> response = client.getForEntity("/customerAccounts/" + this.validId, CustomerResponseDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(this.validId, response.getBody().getId());

    }

    @Test
    @Order(3)
    public void testFindCustomerByInvalidId() {
        // act
        ResponseEntity<ErrorDto> response = client.getForEntity("/customerAccounts/"+ this.invalidId, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("There is no customer with ID" + invalidId, response.getBody().getMessage());
    }

    @Test
    @Order(4)
    public void testFindAllCustomers() {
        // act
        ResponseEntity<CustomerListDto> response = client.getForEntity("/customerAccounts", CustomerListDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getCustomers().size());
    }

    @Test
    @Order(5)
    public void testCreateCustomerWithEmptyName() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto("", "m"+this.email, password);

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customerAccounts", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Name is empty", response.getBody().getMessage());
    }

    @Test
    @Order(6)
    public void testCreateCustomerWithEmptyEmail() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, "", password);

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customerAccounts", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Email is empty", response.getBody().getMessage());
    }

    @Test
    @Order(7)
    public void testCreateCustomerWithSpace() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, email+" ", password);

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customerAccounts", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Email cannot contain any space", response.getBody().getMessage());
    }

    @Test
    @Order(8)
    public void testCreateCustomerWithInvalidEmail() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, "Namir@gmail.co", password);

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customerAccounts", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Invalid Email", response.getBody().getMessage());
    }

    @Test
    @Order(9)
    public void testCreateCustomerWithEmptyPassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, "m"+email, "");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customerAccounts", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password is empty", response.getBody().getMessage());
    }

    @Test
    @Order(10)
    public void testCreateCustomerWithShortPassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, "m"+email, "Pass$");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customerAccounts", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must be at least eight characters long", response.getBody().getMessage());
    }

    @Test
    @Order(11)
    public void testCreateCustomerWithoutUppercasePassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, "m"+email, "password$");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customerAccounts", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain one upper-case character", response.getBody().getMessage());
    }

    @Test
    @Order(12)
    public void testCreateCustomerWithoutLowercasePassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, "m"+email, "PASSWORD$");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customerAccounts", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain one lower-case character", response.getBody().getMessage());
    }

    @Test
    @Order(13)
    public void testCreateCustomerWithoutSpecialCharPassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, "m"+email, "Passworddd");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customerAccounts", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain at least one special character", response.getBody().getMessage());
    }

    @Test
    @Order(14)
    public void testCreateCustomerWithSameEmail() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, email, password);

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customerAccounts", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Customer with this email already exists", response.getBody().getMessage());
    }

    @Test
    @Order(15)
    public void testUpdateCustomerByValidId() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName, newEmail, newPassword);

        //act
        client.put("/customerAccounts/" + this.validId, requestDto);
        ResponseEntity<CustomerResponseDto> response = client.getForEntity("/customerAccounts/" + this.validId, CustomerResponseDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Eric", response.getBody().getName());
        assertEquals("Eric@gmail.com", response.getBody().getEmail());
        assertEquals("Password$1", response.getBody().getPassword());
        assertEquals(this.validId, response.getBody().getId());

    }

    @Test
    @Order(16)
    public void testUpdateCustomerByInvalidId() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, newEmail1, newPassword1);

        //act
        client.put("/customerAccounts/" + this.invalidId, requestDto);
        ResponseEntity<ErrorDto> response = client.getForEntity("/customerAccounts/" + this.invalidId, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("There is no customer with ID" + this.invalidId, response.getBody().getMessage());
    }

    @Test
    @Order(17)
    public void testUpdateCustomerWithEmptyName() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto("", newEmail1, newPassword1);
        HttpEntity<CustomerRequestDto> requestEntity = new HttpEntity<CustomerRequestDto>(requestDto);

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Name is empty", response.getBody().getMessage());
    }

    @Test
    @Order(18)
    public void testUpdateCustomerWithEmptyEmail() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, "", newPassword1);
        HttpEntity<CustomerRequestDto> requestEntity = new HttpEntity<CustomerRequestDto>(requestDto);
        
        //act
        ResponseEntity<ErrorDto> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Email is empty", response.getBody().getMessage());
    }

    @Test
    @Order(19)
    public void testUpdateCustomerWithSpaceEmail() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, newEmail1+" ", newPassword1);
        HttpEntity<CustomerRequestDto> requestEntity = new HttpEntity<CustomerRequestDto>(requestDto);

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Email cannot contain any space", response.getBody().getMessage());
    }

    @Test
    @Order(20)
    public void testUpdateCustomerWithInvalidEmail() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, "Mahmoud@gmail", newPassword1);
        HttpEntity<CustomerRequestDto> requestEntity = new HttpEntity<CustomerRequestDto>(requestDto);
        
        //act
        ResponseEntity<ErrorDto> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Invalid Email", response.getBody().getMessage());
    }

    @Test
    @Order(21)
    public void testUpdateCustomerWithEmptyPassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, newEmail1, "");
        HttpEntity<CustomerRequestDto> requestEntity = new HttpEntity<CustomerRequestDto>(requestDto);

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password is empty", response.getBody().getMessage());
    }

    @Test
    @Order(22)
    public void testUpdateCustomerWithShortPassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, newEmail1, "Pass$");
        HttpEntity<CustomerRequestDto> requestEntity = new HttpEntity<CustomerRequestDto>(requestDto);

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must be at least eight characters long", response.getBody().getMessage());
    }

    @Test
    @Order(23)
    public void testUpdateCustomerWithoutUppercasePassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, newEmail1, "password$$");
        HttpEntity<CustomerRequestDto> requestEntity = new HttpEntity<CustomerRequestDto>(requestDto);

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain one upper-case character", response.getBody().getMessage());
    }

    @Test
    @Order(24)
    public void testUpdateCustomerWithoutLowercasePassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, newEmail1, "PASSWORD$$");
        HttpEntity<CustomerRequestDto> requestEntity = new HttpEntity<CustomerRequestDto>(requestDto);

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain one lower-case character", response.getBody().getMessage());
    }

    @Test
    @Order(25)
    public void testUpdateCustomerWithoutSpecialCharPassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, newEmail1, "Passworddddd");
        HttpEntity<CustomerRequestDto> requestEntity = new HttpEntity<CustomerRequestDto>(requestDto);
        
        //act
        ResponseEntity<ErrorDto> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain at least one special character", response.getBody().getMessage());
    }

    @Test
    @Order(26)
    public void testUpdateCustomerWithSameEmail() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName, newEmail, newPassword);
        HttpEntity<CustomerRequestDto> requestEntity = new HttpEntity<CustomerRequestDto>(requestDto);

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Customer with this email already exists", response.getBody().getMessage());
    }

    @Test
    @Order(27)
    public void testDeleteCustomerByInvalidId() {
        // act
        ResponseEntity<ErrorDto> response = client.exchange("/customerAccounts/" + this.invalidId, HttpMethod.DELETE, null, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("There is no customer with ID" + this.invalidId, response.getBody().getMessage());
    }

    @Test
    @Order(28)
    public void testDeleteCustomerByValidId() {
        // act
        client.delete("/customerAccounts/" + this.validId, CustomerResponseDto.class);
        ResponseEntity<CustomerListDto> response = client.getForEntity("/customerAccounts", CustomerListDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().getCustomers().size());

    }

}
