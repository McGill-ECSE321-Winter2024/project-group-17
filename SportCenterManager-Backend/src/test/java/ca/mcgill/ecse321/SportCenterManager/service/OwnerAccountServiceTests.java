package ca.mcgill.ecse321.SportCenterManager.service;

import ca.mcgill.ecse321.SportCenterManager.dao.OwnerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.exception.ServiceException;
import ca.mcgill.ecse321.SportCenterManager.model.OwnerAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class OwnerAccountServiceTests {

    @Mock
    private OwnerAccountRepository ownerRepo;

    @InjectMocks
    private OwnerAccountService service;

    @Test
    public void testFindOwnerByValidId() {
        // setup
        int id = 30;
        OwnerAccount ownerAccount = new OwnerAccount("validName", "validEmail@gmail.com", "validPassword$");
        ownerAccount.setId(30);
        lenient().when(ownerRepo.findOwnerAccountById(id)).thenReturn(ownerAccount);

        // act
        OwnerAccount foundOwnerAccount = service.findOwnerById(id);

        // assertions
        assertNotNull(foundOwnerAccount);
        assertEquals(foundOwnerAccount.getName(), ownerAccount.getName());
        assertEquals(foundOwnerAccount.getEmail(), ownerAccount.getEmail());
        assertEquals(foundOwnerAccount.getPassword(), ownerAccount.getPassword());
        verify(ownerRepo, times(1)).findOwnerAccountById(id);
    }

    @Test
    public void testFindOwnerByInvalidId() {
        // setup
        int id = 33;
        lenient().when(ownerRepo.findOwnerAccountById(id)).thenReturn(null);
        String error = "";

        // act
        try {
            service.findOwnerById(id);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("There is no owner with ID" + id, error);
        verify(ownerRepo, times(1)).findOwnerAccountById(id);
    }

    // CREATE OWNER ACCOUNT
    @Test
    public void testCreateValidOwner() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        int id = 30;
        ownerAccount.setId(id);
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);

        // act
        OwnerAccount createdOwnerAccount = service.createOwner(name, email, password);

        // assertions
        assertNotNull(createdOwnerAccount);
        assertEquals(createdOwnerAccount.getName(), name);
        assertEquals(createdOwnerAccount.getEmail(), email);
        assertEquals(createdOwnerAccount.getPassword(), password);
        verify(ownerRepo, times(1)).save(any(OwnerAccount.class));
    }

    @Test
    public void testCreateOwnerByEmptyEmail() {
        // setup
        String name = "validName6";
        String email = "";
        String password = "validPassword$6";
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            service.createOwner(name, email, password);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Email is empty", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
    }
    @Test
    public void testCreateOwnerByEmailWithSpace() {
        // setup
        String name = "validName6";
        String email = "invalidEmail @gmail.com";
        String password = "validPassword$6";
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            service.createOwner(name, email, password);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Email cannot contain any space", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
    }

    @Test
    public void testCreateOwnerByInvalidEmail() {
        // setup
        String name = "validName6";
        String email = "invalidEmail6@gmail.co";
        String password = "validPassword$6";
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            service.createOwner(name, email, password);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Invalid Email", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
    }

    @Test
    public void testCreateOwnerByEmptyPassword() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "";
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            service.createOwner(name, email, password);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password is empty", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
    }

    @Test
    public void testCreateOwnerByShortPassword() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "P$6";
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            service.createOwner(name, email, password);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must be at least eight characters long", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
    }

    @Test
    public void testCreateOwnerByPasswordWithoutUppercaseChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "invalidpassword$6";
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            service.createOwner(name, email, password);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain one upper-case character", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
    }

    @Test
    public void testCreateOwnerByPasswordWithoutLowercaseChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "PASSWORD$";
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            service.createOwner(name, email, password);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain one lower-case character", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
    }

    @Test
    public void testCreateOwnerByPasswordWithoutSpecialChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "invalidPassword6";
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            service.createOwner(name, email, password);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain at least one special character", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
    }
    @Test
    public void testCreateDuplicateOwner() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.existsOwnerAccountByEmail(email)).thenReturn(true);

        String sameName = "validName6";
        String sameEmail = "validEmail6@gmail.com";
        String samePassword = "validPassword$6";

        String error = "";

        // act
        try {
            service.createOwner(sameName, sameEmail, samePassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Owner with this email already exists", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).existsOwnerAccountByEmail(email);
    }

    // UPDATE OWNER ACCOUNT
    @Test
    public void testUpdateValidOwner() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        int id = ownerAccount.getId();
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountById(id)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "validPassword7$";

        // act
        OwnerAccount updatedOwnerAccount = service.updateOwnerAccount(id, newName, newEmail, newPassword);

        // assertions
        assertNotNull(updatedOwnerAccount);
        assertEquals(updatedOwnerAccount.getName(), newName);
        assertEquals(updatedOwnerAccount.getEmail(), newEmail);
        assertEquals(updatedOwnerAccount.getPassword(), newPassword);
        verify(ownerRepo, times(1)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)). findOwnerAccountById(id);
    }

    @Test
    public void testUpdateOwnerWithInvalidId() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        int id = 50;
        ownerAccount.setId(id);
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountById(id)).thenReturn(ownerAccount);

        int invalidId = 60;
        String newName = "validName8";
        String newEmail = "validEmail8@gmail.com";
        String newPassword = "validPassword8$";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(invalidId, newName, newEmail, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("The owner account was not found", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountById(invalidId);

    }

    @Test
    public void testUpdateOwnerByEmptyEmail() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        int id = ownerAccount.getId();
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountById(id)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newEmail = "";
        String newPassword = "validPassword7$";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(id, newName, newEmail, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Email is empty", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountById(id);
    }
    @Test
    public void testUpdateOwnerByEmailWithSpace() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        int id = ownerAccount.getId();
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountById(id)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newEmail = "validEmail @gmail.com";
        String newPassword = "validPassword7$";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(id, newName, newEmail, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Email cannot contain any space", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountById(id);
    }

    @Test
    public void testUpdateOwnerByInvalidEmail() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        int id = ownerAccount.getId();
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountById(id)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.co";
        String newPassword = "validPassword7$";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(id, newName, newEmail, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Invalid Email", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountById(id);
    }

    @Test
    public void testUpdateOwnerByEmptyPassword() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        int id = ownerAccount.getId();
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountById(id)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(id, newName, newEmail, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password is empty", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountById(id);
    }

    @Test
    public void testUpdateOwnerByShortPassword() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        int id = ownerAccount.getId();
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountById(id)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "valP$";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(id, newName, newEmail, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must be at least eight characters long", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountById(id);
    }

    @Test
    public void testUpdateOwnerByPasswordWithoutUppercaseChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        int id = ownerAccount.getId();
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountById(id)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "validpassword7$";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(id, newName, newEmail, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain one upper-case character", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountById(id);
    }

    @Test
    public void testUpdateOwnerByPasswordWithoutLowercaseChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        int id = ownerAccount.getId();
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountById(id)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "PASSWORD7$";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(id, newName, newEmail, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain one lower-case character", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountById(id);
    }

    @Test
    public void testUpdateOwnerByPasswordWithoutSpecialChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        int id = ownerAccount.getId();
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountById(id)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "validPassword7";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(id, newName, newEmail, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain at least one special character", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountById(id);
    }

    @Test
    public void testUpdateDuplicateOwner() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        int id = 50;
        ownerAccount.setId(id);
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountById(id)).thenReturn(ownerAccount);
        lenient().when(ownerRepo.existsOwnerAccountByEmail(email)).thenReturn(true);

        String newName = "validName7";
        String newEmail = "validEmail6@gmail.com";
        String newPassword = "validPassword$7";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(id, newName, newEmail, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Owner with this email already exists", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountById(id);
        verify(ownerRepo, times(1)).existsOwnerAccountByEmail(email);
    }
}
