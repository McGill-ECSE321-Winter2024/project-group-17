package ca.mcgill.ecse321.SportCenterManager.integration;

import ca.mcgill.ecse321.SportCenterManager.dao.InstructorAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dto.*;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InstructorAccountIntegrationTests {
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private InstructorAccountRepository instructorRepo;

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
    private int invalidId = -2;

    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        instructorRepo.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateValidInstructor() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(name, email, password);

        //act
        ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructorAccounts", requestDto, InstructorResponseDto.class);

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
    public void testFindInstructorByValidId() {
        // act
        ResponseEntity<InstructorResponseDto> response = client.getForEntity("/instructorAccounts/" + this.validId, InstructorResponseDto.class);

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
    public void testFindInstructorByInvalidId() {
        // act
        ResponseEntity<IllegalArgumentException> response = client.getForEntity("/instructorAccounts/"+ this.invalidId, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("There is no course with ID" + invalidId, response.getBody().getMessage());
    }

    @Test
    @Order(4)
    public void testFindAllInstructors() {
        // act
        ResponseEntity<InstructorListDto> response = client.getForEntity("/instructorAccounts", InstructorListDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getInstructors().size());
    }

    @Test
    @Order(5)
    public void testCreateInstructorWithEmptyEmail() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(name, "", password);

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/instructorAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Email is empty", response.getBody().getMessage());
    }

    @Test
    @Order(6)
    public void testCreateInstructorWithSpace() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(name, email+" ", password);

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/instructorAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Email cannot contain any space", response.getBody().getMessage());
    }

    @Test
    @Order(7)
    public void testCreateInstructorWithInvalidEmail() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(name, "Namir@gmail.co", password);

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/instructorAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Invalid Email", response.getBody().getMessage());
    }

    @Test
    @Order(8)
    public void testCreateInstructorWithEmptyPassword() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(name, "m"+email, "");

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/instructorAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Password is empty", response.getBody().getMessage());
    }

    @Test
    @Order(9)
    public void testCreateInstructorWithShortPassword() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(name, "m"+email, "Pass$");

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/instructorAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Password must be at least four characters long", response.getBody().getMessage());
    }

    @Test
    @Order(10)
    public void testCreateInstructorWithoutUppercasePassword() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(name, "m"+email, "password$");

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/instructorAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Password must contain one upper-case character", response.getBody().getMessage());
    }

    @Test
    @Order(11)
    public void testCreateInstructorWithoutLowercasePassword() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(name, "m"+email, "PASSWORD$");

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/instructorAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Password must contain one lower-case character", response.getBody().getMessage());
    }

    @Test
    @Order(12)
    public void testCreateInstructorWithoutSpecialCharPassword() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(name, "m"+email, "Passworddd");

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/instructorAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Password must contain at least one special character", response.getBody().getMessage());
    }

    @Test
    @Order(13)
    public void testCreateInstructorWithSameEmail() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(name, email, password);

        //act
        ResponseEntity<IllegalArgumentException> response = client.postForEntity("/instructorAccounts", requestDto, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Instructor with this email already exists", response.getBody().getMessage());
    }

    @Test
    @Order(14)
    public void testUpdateInstructorByValidId() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(newName, newEmail, newPassword);

        //act
        client.put("/instructorAccounts/" + this.validId, requestDto);
        ResponseEntity<InstructorResponseDto> response = client.getForEntity("/instructorAccounts/" + this.validId, InstructorResponseDto.class);

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
    @Order(15)
    public void testUpdateInstructorByInvalidId() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(newName1, newEmail1, newPassword1);

        //act
        client.put("/customerAccounts/" + this.invalidId, requestDto);
        ResponseEntity<IllegalArgumentException> response = client.getForEntity("/instructorAccounts/" + this.invalidId, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("The instructor account was not found", response.getBody().getMessage());
    }

    @Test
    @Order(16)
    public void testUpdateInstructorWithEmptyEmail() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(newName1, "", newPassword1);

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/instructorAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Email is empty", response.getBody().getMessage());
    }

    @Test
    @Order(17)
    public void testUpdateInstructorWithSpaceEmail() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(newName1, newEmail1+" ", newPassword1);

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/instructorAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Email cannot contain any space", response.getBody().getMessage());
    }

    @Test
    @Order(18)
    public void testUpdateInstructorWithInvalidEmail() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(newName1, "Mahmoud@gmail", newPassword1);

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/instructorAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Invalid Email", response.getBody().getMessage());
    }

    @Test
    @Order(19)
    public void testUpdateInstructorWithEmptyPassword() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(newName1, newEmail1, "");

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/instructorAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Password is empty", response.getBody().getMessage());
    }

    @Test
    @Order(20)
    public void testUpdateInstructorWithShortPassword() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(newName1, newEmail1, "Pass$");

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/instructorAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Password must be at least four characters long", response.getBody().getMessage());
    }

    @Test
    @Order(21)
    public void testUpdateInstructorWithoutUppercasePassword() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(newName1, newEmail1, "password$$");

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/instructorAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Password must contain one upper-case character", response.getBody().getMessage());
    }

    @Test
    @Order(22)
    public void testUpdateInstructorWithoutLowercasePassword() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(newName1, newEmail1, "PASSWORD$$");

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/instructorAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Password must contain one lower-case character", response.getBody().getMessage());
    }

    @Test
    @Order(23)
    public void testUpdateInstructorWithoutSpecialCharPassword() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(newName1, newEmail1, "Passworddddd");

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/instructorAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Password must contain at least one special character", response.getBody().getMessage());
    }

    @Test
    @Order(24)
    public void testUpdateInstructorWithSameEmail() {
        // setup
        InstructorRequestDto requestDto = new InstructorRequestDto(newName, newEmail, newPassword);

        //act
        ResponseEntity<IllegalArgumentException> response = client.exchange("/instructorAccounts/" + this.validId, HttpMethod.PUT, null, IllegalArgumentException.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals("Instructor with this email already exists", response.getBody().getMessage());
    }

    @Test
    @Order(25)
    public void testDeleteInstructorByInvalidId() {
        // act
        ResponseEntity<InstructorListDto> response = client.exchange("/instructorAccounts/" + this.invalidId, HttpMethod.DELETE, null, InstructorListDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @Order(26)
    public void testDeleteInstructorByValidId() {
        // act
        client.delete("/instructorAccounts/" + this.validId, InstructorResponseDto.class);
        ResponseEntity<InstructorListDto> response = client.getForEntity("/instructorAccounts", InstructorListDto.class);

        // assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().getInstructors().size());

    }
}
