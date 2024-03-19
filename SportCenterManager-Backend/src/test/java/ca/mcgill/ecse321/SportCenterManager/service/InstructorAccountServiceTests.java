package ca.mcgill.ecse321.SportCenterManager.service;

import ca.mcgill.ecse321.SportCenterManager.dao.InstructorAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class InstructorAccountServiceTests {
    @Mock
    private InstructorAccountRepository instructorRepo;

    @InjectMocks
    private InstructorAccountService service;

    @Test
    public void testFindInstructorByValidId() {
        // setup
        int id = 30;
        InstructorAccount instructorAccount = new InstructorAccount("validName", "validEmail@gmail.com", "validPassword$");
        instructorAccount.setId(30);
        lenient().when(instructorRepo.findInstructorAccountById(id)).thenReturn(instructorAccount);

        // act
        InstructorAccount foundCustomerAccount = service.findInstructorById(id);

        // assertions
        assertNotNull(foundCustomerAccount);
        assertEquals(foundCustomerAccount.getName(), instructorAccount.getName());
        assertEquals(foundCustomerAccount.getEmail(), instructorAccount.getEmail());
        assertEquals(foundCustomerAccount.getPassword(), instructorAccount.getPassword());
        verify(instructorRepo, times(1)).findInstructorAccountById(id);
    }

    @Test
    public void testFindInstructorByInvalidId() {
        // setup
        int id = 33;
        lenient().when(instructorRepo.findInstructorAccountById(id)).thenReturn(null);
        String error = "";

        // act
        try {
            InstructorAccount foundCustomerAccount = service.findInstructorById(id);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("There is no course with ID" + id, error);
        verify(instructorRepo, times(1)).findInstructorAccountById(id);
    }

    @Test
    public void testFindAllInstructors() {
        // setup
        List<InstructorAccount> listCustomerAccounts = new ArrayList<InstructorAccount>();

        InstructorAccount customerAccount1 = new InstructorAccount("validName1", "validEmail1@gmail.com", "validPassword$1");
        customerAccount1.setId(5);
        listCustomerAccounts.add(customerAccount1);

        InstructorAccount customerAccount2 = new InstructorAccount("validName2", "validEmail1@gmail.com", "validPassword$2");
        customerAccount1.setId(10);
        listCustomerAccounts.add(customerAccount2);

        InstructorAccount customerAccount3 = new InstructorAccount("validName3", "validEmail1@gmail.com", "validPassword$3");
        customerAccount1.setId(15);
        listCustomerAccounts.add(customerAccount3);

        InstructorAccount customerAccount4 = new InstructorAccount("validName4", "validEmail1@gmail.com", "validPassword$4");
        customerAccount1.setId(20);
        listCustomerAccounts.add(customerAccount4);

        InstructorAccount customerAccount5 = new InstructorAccount("validName5", "validEmail1@gmail.com", "validPassword$5");
        customerAccount1.setId(25);
        listCustomerAccounts.add(customerAccount5);

        lenient().when(instructorRepo.findAll()).thenReturn((Iterable<InstructorAccount>) listCustomerAccounts);

        // act
        List<InstructorAccount> foundCustomerAccounts = (List<InstructorAccount>) service.findAllInstructors();

        // assertions
        assertNotNull(foundCustomerAccounts);
        assertEquals(foundCustomerAccounts.size(), 5);
        verify(instructorRepo, times(1)).findAll();
    }

    // CREATE INSTRUCTOR ACCOUNT
    @Test
    public void testCreateValidInstructor() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        int id = 30;
        instructorAccount.setId(id);
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(instructorAccount);

        // act
        InstructorAccount createdInstructorAccount = service.createInstructor(name, email, password);

        // assertions
        assertNotNull(createdInstructorAccount);
        assertEquals(createdInstructorAccount.getName(), name);
        assertEquals(createdInstructorAccount.getEmail(), email);
        assertEquals(createdInstructorAccount.getPassword(), password);
        verify(instructorRepo, times(1)).save(any(InstructorAccount.class));
    }

    @Test
    public void testCreateInstructorByEmptyEmail() {
        // setup
        String name = "validName6";
        String email = "";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            InstructorAccount createdInstructorAccount = service.createInstructor(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Email is empty", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
    }
    @Test
    public void testCreateInstructorByEmailWithSpace() {
        // setup
        String name = "validName6";
        String email = "invalidEmail @gmail.com";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            InstructorAccount createdInstructorAccount = service.createInstructor(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Email cannot contain any space", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
    }

    @Test
    public void testCreateInstructorByInvalidEmail() {
        // setup
        String name = "validName6";
        String email = "invalidEmail6@gmail.co";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            InstructorAccount createdInstructorAccount = service.createInstructor(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Invalid Email", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
    }

    @Test
    public void testCreateInstructorByEmptyPassword() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            InstructorAccount createdInstructorAccount = service.createInstructor(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password is empty", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
    }

    @Test
    public void testCreateInstructorByShortPassword() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "P$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            InstructorAccount createdInstructorAccount = service.createInstructor(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must be at least four characters long", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
    }

    @Test
    public void testCreateInstructorByPasswordWithoutUppercaseChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "invalidpassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            InstructorAccount createdInstructorAccount = service.createInstructor(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain one upper-case character", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
    }

    @Test
    public void testCreateInstructorByPasswordWithoutLowercaseChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "PASSWORD$";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            InstructorAccount createdInstructorAccount = service.createInstructor(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain one lower-case character", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
    }

    @Test
    public void testCreateInstructorByPasswordWithoutSpecialChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "invalidPassword6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            InstructorAccount createdInstructorAccount = service.createInstructor(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain at least one special character", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
    }
    @Test
    public void testCreateDuplicateInstructor() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(instructorAccount);
        lenient().when(instructorRepo.existsInstructorAccountByEmail(email)).thenReturn(true);

        String sameName = "validName6";
        String sameEmail = "validEmail6@gmail.com";
        String samePassword = "validPassword$6";

        String error = "";

        // act
        try {
            InstructorAccount createdInstructorAccount = service.createInstructor(sameName, sameEmail, samePassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Instructor with this email already exists", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
        verify(instructorRepo, times(1)).existsInstructorAccountByEmail(email);
    }

    // UPDATE INSTRUCTOR ACCOUNT
    @Test
    public void testUpdateValidInstructor() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        int id = instructorAccount.getId();
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(instructorAccount);
        lenient().when(instructorRepo.findInstructorAccountById(id)).thenReturn(instructorAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "validPassword7$";

        // act
        InstructorAccount updatedInstructorAccount = service.updateInstructorAccount(id, newName, newEmail, newPassword);

        // assertions
        assertNotNull(updatedInstructorAccount);
        assertEquals(updatedInstructorAccount.getName(), newName);
        assertEquals(updatedInstructorAccount.getEmail(), newEmail);
        assertEquals(updatedInstructorAccount.getPassword(), newPassword);
        verify(instructorRepo, times(1)).save(any(InstructorAccount.class));
        verify(instructorRepo, times(1)).findInstructorAccountById(id);
    }

    @Test
    public void testUpdateInstructorWithInvalidId() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        int id = 50;
        instructorAccount.setId(id);
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(instructorAccount);
        lenient().when(instructorRepo.findInstructorAccountById(id)).thenReturn(instructorAccount);

        int invalidId = 60;
        String newName = "validName8";
        String newEmail = "validEmail8@gmail.com";
        String newPassword = "validPassword8$";
        String error = "";

        // act
        try {
            InstructorAccount updatedInstructorAccount = service.updateInstructorAccount(invalidId, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("The instructor account was not found", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
        verify(instructorRepo, times(1)).findInstructorAccountById(invalidId);

    }

    @Test
    public void testUpdateInstructorByEmptyEmail() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        int id = instructorAccount.getId();
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(instructorAccount);
        lenient().when(instructorRepo.findInstructorAccountById(id)).thenReturn(instructorAccount);

        String newName = "validName7";
        String newEmail = "";
        String newPassword = "validPassword7$";
        String error = "";

        // act
        try {
            InstructorAccount updatedInstructorAccount = service.updateInstructorAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Email is empty", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
        verify(instructorRepo, times(1)).findInstructorAccountById(id);
    }
    @Test
    public void testUpdateInstructorByEmailWithSpace() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        int id = instructorAccount.getId();
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(instructorAccount);
        lenient().when(instructorRepo.findInstructorAccountById(id)).thenReturn(instructorAccount);

        String newName = "validName7";
        String newEmail = "validEmail @gmail.com";
        String newPassword = "validPassword7$";
        String error = "";

        // act
        try {
            InstructorAccount updatedInstructorAccount = service.updateInstructorAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Email cannot contain any space", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
        verify(instructorRepo, times(1)).findInstructorAccountById(id);
    }

    @Test
    public void testUpdateInstructorByInvalidEmail() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        int id = instructorAccount.getId();
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(instructorAccount);
        lenient().when(instructorRepo.findInstructorAccountById(id)).thenReturn(instructorAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.co";
        String newPassword = "validPassword7$";
        String error = "";

        // act
        try {
            InstructorAccount updatedInstructorAccount = service.updateInstructorAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Invalid Email", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
        verify(instructorRepo, times(1)).findInstructorAccountById(id);
    }

    @Test
    public void testUpdateInstructorByEmptyPassword() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        int id = instructorAccount.getId();
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(instructorAccount);
        lenient().when(instructorRepo.findInstructorAccountById(id)).thenReturn(instructorAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "";
        String error = "";

        // act
        try {
            InstructorAccount updatedInstructorAccount = service.updateInstructorAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password is empty", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
        verify(instructorRepo, times(1)).findInstructorAccountById(id);
    }

    @Test
    public void testUpdateInstructorByShortPassword() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        int id = instructorAccount.getId();
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(instructorAccount);
        lenient().when(instructorRepo.findInstructorAccountById(id)).thenReturn(instructorAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "valP$";
        String error = "";

        // act
        try {
            InstructorAccount updatedInstructorAccount = service.updateInstructorAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must be at least four characters long", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
        verify(instructorRepo, times(1)).findInstructorAccountById(id);
    }

    @Test
    public void testUpdateInstructorByPasswordWithoutUppercaseChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        int id = instructorAccount.getId();
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(instructorAccount);
        lenient().when(instructorRepo.findInstructorAccountById(id)).thenReturn(instructorAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "validpassword7$";
        String error = "";

        // act
        try {
            InstructorAccount updatedInstructorAccount = service.updateInstructorAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain one upper-case character", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
        verify(instructorRepo, times(1)).findInstructorAccountById(id);
    }

    @Test
    public void testUpdateInstructorByPasswordWithoutLowercaseChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        int id = instructorAccount.getId();
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(instructorAccount);
        lenient().when(instructorRepo.findInstructorAccountById(id)).thenReturn(instructorAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "PASSWORD7$";
        String error = "";

        // act
        try {
            InstructorAccount updatedInstructorAccount = service.updateInstructorAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain one lower-case character", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
        verify(instructorRepo, times(1)).findInstructorAccountById(id);
    }

    @Test
    public void testUpdateInstructorByPasswordWithoutSpecialChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        int id = instructorAccount.getId();
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(instructorAccount);
        lenient().when(instructorRepo.findInstructorAccountById(id)).thenReturn(instructorAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "validPassword7";
        String error = "";

        // act
        try {
            InstructorAccount updatedInstructorAccount = service.updateInstructorAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain at least one special character", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
        verify(instructorRepo, times(1)).findInstructorAccountById(id);
    }

    @Test
    public void testUpdateDuplicateInstructor() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        int id = 50;
        instructorAccount.setId(id);
        lenient().when(instructorRepo.save(any(InstructorAccount.class))).thenReturn(instructorAccount);
        lenient().when(instructorRepo.findInstructorAccountById(id)).thenReturn(instructorAccount);
        lenient().when(instructorRepo.existsInstructorAccountByEmail(email)).thenReturn(true);

        String newName = "validName7";
        String newEmail = "validEmail6@gmail.com";
        String newPassword = "validPassword$7";
        String error = "";

        // act
        try {
            InstructorAccount updatedInstructorAccount = service.updateInstructorAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Instructor with this email already exists", error);
        verify(instructorRepo, times(0)).save(any(InstructorAccount.class));
        verify(instructorRepo, times(1)).findInstructorAccountById(id);
        verify(instructorRepo, times(1)).existsInstructorAccountByEmail(email);
    }

    // DELETE INSTRUCTOR ACCOUNT
    @Test
    public void testDeleteInstructorByValidId() {
        // setup
        String name = "validName8";
        String email = "validEmail8@gmail.com";
        String password = "validPassword$8";
        InstructorAccount instructorAccount = new InstructorAccount(name, email, password);
        int id = 30;
        instructorAccount.setId(id);
        lenient().when(instructorRepo.findInstructorAccountById(id)).thenReturn(instructorAccount);

        // act
        boolean deletedInstructorAccount = service.deleteInstructor(id);

        // assertions
        assertTrue(deletedInstructorAccount, "The instructor account is deleted");
        verify(instructorRepo, times(1)).findInstructorAccountById(id);
    }

    @Test
    public void testDeleteInstructorByInvalidId() {
        // setup
        int id = 33;
        lenient().when(instructorRepo.findById(id)).thenReturn(null);
        String error = "";

        // act
        try {
            boolean deletedInstructorAccount = service.deleteInstructor(id);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("There is no course with ID" + id, error);
        verify(instructorRepo, times(1)).findInstructorAccountById(id);
    }
}
