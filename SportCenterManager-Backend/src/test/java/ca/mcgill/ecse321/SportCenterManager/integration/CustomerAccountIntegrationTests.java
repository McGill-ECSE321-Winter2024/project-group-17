package ca.mcgill.ecse321.SportCenterManager.integration;

import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dto.CourseResponseDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CustomerListDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CustomerRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.CustomerResponseDto;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
        System.out.println(response);
        System.out.println(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(this.validId, response.getBody().getId());

    }

    @Test
    @Order(3)
    public void testFindCustomerByInvalidId() {
        // act
        ResponseEntity<IllegalArgumentException> response = client.getForEntity("/customerAccounts/"+ this.invalidId, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("There is no course with ID" + invalidId, response.getBody().getMessage());
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
    public void testCreateCustomerWithEmptyEmail() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, "", password);

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/customerAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Email is empty", response.getBody().getMessage());
    }

    @Test
    @Order(5)
    public void testCreateCustomerWithSpace() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, email+" ", password);

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/customerAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Email cannot contain any space", response.getBody().getMessage());
    }

    @Test
    @Order(6)
    public void testCreateCustomerWithInvalidEmail() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, "Namir@gmail.co", password);

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/customerAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Invalid Email", response.getBody().getMessage());
    }

    @Test
    @Order(7)
    public void testCreateCustomerWithEmptyPassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, "m"+email, "");

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/customerAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Password is empty", response.getBody().getMessage());
    }

    @Test
    @Order(8)
    public void testCreateCustomerWithShortPassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, "m"+email, "Pass$");

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/customerAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Password must be at least four characters long", response.getBody().getMessage());
    }

    @Test
    @Order(9)
    public void testCreateCustomerWithoutUppercasePassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, "m"+email, "password$");

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/customerAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Password must contain one upper-case character", response.getBody().getMessage());
    }

    @Test
    @Order(10)
    public void testCreateCustomerWithoutLowercasePassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, "m"+email, "PASSWORD$");

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/customerAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Password must contain one lower-case character", response.getBody().getMessage());
    }

    @Test
    @Order(11)
    public void testCreateCustomerWithoutSpecialCharPassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, "m"+email, "Passworddd");

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/customerAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Password must contain at least one special character", response.getBody().getMessage());
    }

    @Test
    @Order(12)
    public void testCreateCustomerWithSameEmail() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(name, email, password);

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/customerAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Customer with this email already exists", response.getBody().getMessage());
    }

    @Test
    @Order(13)
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
    @Order(14)
    public void testUpdateCustomerByInvalidId() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, newEmail1, newPassword1);

        //act
        client.put("/customerAccounts/" + this.invalidId, requestDto);
        ResponseEntity<IllegalArgumentException> response = client.getForEntity("/customerAccounts/" + this.invalidId, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("The customer account was not found", response.getBody().getMessage());
    }

    @Test
    @Order(15)
    public void testUpdateCustomerWithEmptyEmail() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, "", newPassword1);

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Email is empty", response.getBody().getMessage());
    }

    @Test
    @Order(16)
    public void testUpdateCustomerWithSpaceEmail() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, newEmail1+" ", newPassword1);

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Email cannot contain any space", response.getBody().getMessage());
    }

    @Test
    @Order(17)
    public void testUpdateCustomerWithInvalidEmail() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, "Mahmoud@gmail", newPassword1);

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Invalid Email", response.getBody().getMessage());
    }

    @Test
    @Order(18)
    public void testUpdateCustomerWithEmptyPassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, newEmail1, "");

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Password is empty", response.getBody().getMessage());
    }

    @Test
    @Order(19)
    public void testUpdateCustomerWithShortPassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, newEmail1, "Pass$");

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Password must be at least four characters long", response.getBody().getMessage());
    }

    @Test
    @Order(20)
    public void testUpdateCustomerWithoutUppercasePassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, newEmail1, "password$$");

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Password must contain one upper-case character", response.getBody().getMessage());
    }

    @Test
    @Order(21)
    public void testUpdateCustomerWithoutLowercasePassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, newEmail1, "PASSWORD$$");

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Password must contain one lower-case character", response.getBody().getMessage());
    }

    @Test
    @Order(22)
    public void testUpdateCustomerWithoutSpecialCharPassword() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName1, newEmail1, "Passworddddd");

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Password must contain at least one special character", response.getBody().getMessage());
    }

    @Test
    @Order(23)
    public void testUpdateCustomerWithSameEmail() {
        // setup
        CustomerRequestDto requestDto = new CustomerRequestDto(newName, newEmail, newPassword);

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/customerAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Customer with this email already exists", response.getBody().getMessage());
    }

    @Test
    @Order(24)
    public void testDeleteCustomerByInvalidId() {
        // act
        ResponseEntity<CustomerListDto> response = client.exchange("/customerAccounts/" + this.invalidId, HttpMethod.DELETE, null, CustomerListDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @Order(25)
    public void testDeleteCustomerByValidId() {
        // act
        client.delete("/customerAccounts/" + this.validId, CourseResponseDto.class);
        ResponseEntity<CustomerListDto> response = client.getForEntity("/customerAccounts", CustomerListDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().getCustomers().size());

    }

}
