package ca.mcgill.ecse321.SportCenterManager.integration;

import ca.mcgill.ecse321.SportCenterManager.dao.OwnerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dto.ErrorDto;
import ca.mcgill.ecse321.SportCenterManager.dto.OwnerRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.OwnerResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OwnerAccountIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private OwnerAccountRepository ownerRepo;

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
        ownerRepo.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateValidOwner() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(name, email, password);

        //act
        ResponseEntity<OwnerResponseDto> response = client.postForEntity("/ownerAccount", requestDto, OwnerResponseDto.class);

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
    public void testFindOwnerByValidId() {
        // act
        ResponseEntity<OwnerResponseDto> response = client.getForEntity("/ownerAccount/" + this.validId, OwnerResponseDto.class);

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
    public void testFindOwnerByInvalidId() {
        // act
        ResponseEntity<ErrorDto> response = client.getForEntity("/ownerAccount/"+ this.invalidId, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("There is no owner with ID" + invalidId, response.getBody().getMessage());
    }

    @Test
    @Order(4)
    public void testCreateOwnerWithEmptyEmail() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(name, "", password);

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Email is empty", response.getBody().getMessage());
    }

    @Test
    @Order(5)
    public void testCreateOwnerWithSpace() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(name, email+" ", password);

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Email cannot contain any space", response.getBody().getMessage());
    }

    @Test
    @Order(6)
    public void testCreateOwnerWithInvalidEmail() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(name, "Namir@gmail.co", password);

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Invalid Email", response.getBody().getMessage());
    }

    @Test
    @Order(7)
    public void testCreateOwnerWithEmptyPassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(name, "m"+email, "");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password is empty", response.getBody().getMessage());
    }

    @Test
    @Order(8)
    public void testCreateOwnerWithShortPassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(name, "m"+email, "Pass$");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must be at least eight characters long", response.getBody().getMessage());
    }

    @Test
    @Order(9)
    public void testCreateOwnerWithoutUppercasePassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(name, "m"+email, "password$");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain one upper-case character", response.getBody().getMessage());
    }

    @Test
    @Order(10)
    public void testCreateOwnerWithoutLowercasePassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(name, "m"+email, "PASSWORD$");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain one lower-case character", response.getBody().getMessage());
    }

    @Test
    @Order(11)
    public void testCreateOwnerWithoutSpecialCharPassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(name, "m"+email, "Passworddd");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain at least one special character", response.getBody().getMessage());
    }

    @Test
    @Order(12)
    public void testCreateOwnerWithSameEmail() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(name, email, password);

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Owner with this email already exists", response.getBody().getMessage());
    }

    @Test
    @Order(13)
    public void testUpdateOwnerByValidId() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(newName, newEmail, newPassword);

        //act
        client.put("/ownerAccount/" + this.validId, requestDto);
        ResponseEntity<OwnerResponseDto> response = client.getForEntity("/ownerAccount/" + this.validId, OwnerResponseDto.class);

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
    public void testUpdateOwnerByInvalidId() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(newName1, newEmail1, newPassword1);

        //act
        client.put("/ownerAccount/" + this.invalidId, requestDto);
        ResponseEntity<ErrorDto> response = client.getForEntity("/ownerAccount/" + this.invalidId, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("There is no owner with ID" + invalidId, response.getBody().getMessage());
    }

    @Test
    @Order(15)
    public void testUpdateOwnerWithEmptyEmail() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(newName1, "", newPassword1);

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount/" + this.validId, HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Email is empty", response.getBody().getMessage());
    }

    @Test
    @Order(16)
    public void testUpdateOwnerWithSpaceEmail() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(newName1, newEmail1+" ", newPassword1);

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount/" + this.validId, HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Email cannot contain any space", response.getBody().getMessage());
    }

    @Test
    @Order(17)
    public void testUpdateOwnerWithInvalidEmail() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(newName1, "Mahmoud@gmail", newPassword1);

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount/" + this.validId, HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Invalid Email", response.getBody().getMessage());
    }

    @Test
    @Order(18)
    public void testUpdateOwnerWithEmptyPassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(newName1, newEmail1, "");

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount/" + this.validId, HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password is empty", response.getBody().getMessage());
    }

    @Test
    @Order(19)
    public void testUpdateOwnerWithShortPassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(newName1, newEmail1, "Pass$");

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount/" + this.validId, HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must be at least eight characters long", response.getBody().getMessage());
    }

    @Test
    @Order(20)
    public void testUpdateOwnerWithoutUppercasePassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(newName1, newEmail1, "password$$");

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount/" + this.validId, HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain one upper-case character", response.getBody().getMessage());
    }

    @Test
    @Order(21)
    public void testUpdateOwnerWithoutLowercasePassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(newName1, newEmail1, "PASSWORD$$");

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount/" + this.validId, HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain one lower-case character", response.getBody().getMessage());
    }

    @Test
    @Order(22)
    public void testUpdateOwnerWithoutSpecialCharPassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(newName1, newEmail1, "Passworddddd");

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount/" + this.validId, HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain at least one special character", response.getBody().getMessage());
    }

    @Test
    @Order(23)
    public void testUpdateOwnerWithSameEmail() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(newName, newEmail, newPassword);

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount/" + this.validId, HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Owner with this email already exists", response.getBody().getMessage());
    }

}
