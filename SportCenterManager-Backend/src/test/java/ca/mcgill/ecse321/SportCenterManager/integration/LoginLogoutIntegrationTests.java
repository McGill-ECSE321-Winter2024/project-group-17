package ca.mcgill.ecse321.SportCenterManager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dto.ErrorDto;
import ca.mcgill.ecse321.SportCenterManager.dto.LoginDto;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class LoginLogoutIntegrationTests {
    @Autowired
    private CustomerAccountRepository customerRepo;
    @Autowired
    private TestRestTemplate client;

    private String customerOneName = "bob";
    private String customerTwoName="jane";
    private String customerOneEmail = "bob@gmail.ca";
    private String customerTwoEmail = "jane@gmail.ca";
    private String customerOnePass = "ecse321";
    private String customerTwoPass = "ecse223";
    private String invalidEmail = "jack@gmail.ca";
    private String invalidPass= "comp250"; 
    
    @BeforeEach
    public void setUpDatabase(){
        populateDatabase();
    }

    @AfterEach
    public void cleanDatabase(){
        clearDatabase();
    }

    private void populateDatabase(){
        CustomerAccount customerOne = new CustomerAccount(customerOneName,customerOneEmail,customerOnePass);

        CustomerAccount customerTwo = new CustomerAccount(customerTwoName, customerTwoEmail, customerTwoPass);
    }

    private void clearDatabase(){
        customerRepo.deleteAll();
    }

    @Test
    public void testLoginValid(){
        LoginDto request = new LoginDto(customerOneEmail,customerOnePass);

        ResponseEntity<LoginDto> response = client.postForEntity("/login", request, LoginDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        LoginDto logged_in_account = response.getBody();
        assertNotNull(logged_in_account);
        assertEquals(customerOneEmail,logged_in_account.getEmail());
        assertEquals(customerOnePass,logged_in_account.getPassword());
    }
    
    @Test
    public void testLoginInvalidEmail(){
        
        LoginDto request = new LoginDto(invalidEmail,customerOnePass);
        ResponseEntity<ErrorDto> response = client.postForEntity("/login", request, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid email or password",response.getBody().getMessage());
    }

    @Test
    public void testLoginInvalidPassword(){
        LoginDto request = new LoginDto(customerOnePass,invalidPass);
        ResponseEntity<ErrorDto> response = client.postForEntity("/login", request, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid email or password",response.getBody().getMessage());

    }

    @Test
    public void testLoginBothInvalid(){
        LoginDto request = new LoginDto(invalidEmail,invalidPass);
        ResponseEntity<ErrorDto> response = client.postForEntity("/login", request, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid email or password",response.getBody().getMessage());

    }

    @Test
    public void testLogoutValid(){
        
        Object request = null;
        ResponseEntity<String> response = client.postForEntity("/logout",request, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());

        String logged_out_account = response.getBody();
        assertNotNull(logged_out_account);
        assertEquals("logged out",logged_out_account);
    }

    //no invalid logout => if not logged in, frontend won't display logout button
}
