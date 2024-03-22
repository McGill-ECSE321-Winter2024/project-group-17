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
    private String email = "owner@sportcenter.com";
    private String password = "Password$";
    private String newName = "Eric";
    private String newPassword = "Password$1";
    private String newName1 = "Mahmoud";
    private String newPassword1 = "Password$2";


    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        ownerRepo.deleteAll();
    }


    @Test
    @Order(1)
    public void testCannotFindOwner() {
        // act
        ResponseEntity<ErrorDto> response = client.getForEntity("/ownerAccount", ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("There is no owner", response.getBody().getMessage());
    }

    @Test
    @Order(2)
    public void testCreateOwnerWithEmptyName() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto("", this.email, this.password);

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Name is empty", response.getBody().getMessage());
    }
    @Test
    @Order(3)
    public void testCreateOwnerWithEmptyPassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(this.name, this.email, "");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password is empty", response.getBody().getMessage());
    }

    @Test
    @Order(4)
    public void testCreateOwnerWithShortPassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(this.name, this.email, "Pass$");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must be at least eight characters long", response.getBody().getMessage());
    }

    @Test
    @Order(5)
    public void testCreateOwnerWithoutUppercasePassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(this.name, this.email, "password$");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain one upper-case character", response.getBody().getMessage());
    }

    @Test
    @Order(6)
    public void testCreateOwnerWithoutLowercasePassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(this.name, this.email, "PASSWORD$");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain one lower-case character", response.getBody().getMessage());
    }

    @Test
    @Order(7)
    public void testCreateOwnerWithoutSpecialCharPassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(this.name, this.email, "Passworddd");

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain at least one special character", response.getBody().getMessage());
    }

    @Test
    @Order(8)
    public void testCreateValidOwner() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(this.name, this.email, this.password);

        //act
        ResponseEntity<OwnerResponseDto> response = client.postForEntity("/ownerAccount", requestDto, OwnerResponseDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Namir", response.getBody().getName());
        assertEquals(this.email, response.getBody().getEmail());
        assertEquals("Password$", response.getBody().getPassword());
        assertTrue(response.getBody().getId() > 0, "ID is not positive" );
    }
    @Test
    @Order(9)
    public void testFindOwner() {
        // act
        ResponseEntity<OwnerResponseDto> response = client.getForEntity("/ownerAccount", OwnerResponseDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        System.out.println(response);
        System.out.println(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Namir", response.getBody().getName());
        assertEquals(this.email, response.getBody().getEmail());
        assertEquals("Password$", response.getBody().getPassword());
    }

    @Test
    @Order(10)
    public void testSecondCreateOwner() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(this.name, this.email, this.password);

        //act
        ResponseEntity<ErrorDto> response = client.postForEntity("/ownerAccount", requestDto, ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Only one owner can exist", response.getBody().getMessage());
    }

    @Test
    @Order(11)
    public void testUpdateValidOwner() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(this.newName, this.email, this.newPassword);

        //act
        ResponseEntity<OwnerResponseDto> response = client.exchange("/ownerAccount", HttpMethod.PUT, new HttpEntity<>(requestDto), OwnerResponseDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(this.newName, response.getBody().getName());
        assertEquals(this.email, response.getBody().getEmail());
        assertEquals(this.newPassword, response.getBody().getPassword());

    }
    @Test
    @Order(12)
    public void testUpdateOwnerWithEmptyName() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto("", this.email, this.newPassword1);

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount", HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Name is empty", response.getBody().getMessage());
    }

    @Test
    @Order(13)
    public void testUpdateOwnerWithEmptyPassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(this.newName1, this.email, "");

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount", HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password is empty", response.getBody().getMessage());
    }

    @Test
    @Order(14)
    public void testUpdateOwnerWithShortPassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(this.newName1, this.email, "Pass$");

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount", HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must be at least eight characters long", response.getBody().getMessage());
    }

    @Test
    @Order(15)
    public void testUpdateOwnerWithoutUppercasePassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(this.newName1, this.email, "password$$");

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount", HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain one upper-case character", response.getBody().getMessage());
    }

    @Test
    @Order(16)
    public void testUpdateOwnerWithoutLowercasePassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(this.newName1, this.email, "PASSWORD$$");

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount", HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain one lower-case character", response.getBody().getMessage());
    }

    @Test
    @Order(17)
    public void testUpdateOwnerWithoutSpecialCharPassword() {
        // setup
        OwnerRequestDto requestDto = new OwnerRequestDto(this.newName1, this.email, "Passworddddd");

        //act
        ResponseEntity<ErrorDto> response = client.exchange("/ownerAccount", HttpMethod.PUT, new HttpEntity<>(requestDto), ErrorDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Password must contain at least one special character", response.getBody().getMessage());
    }
}
