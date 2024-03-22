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
    public void testFindOwner() {
        // setup
        String name = "validName";
        String email = "owner@sportcenter.com";
        String password = "validPassword$";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        lenient().when(ownerRepo.findOwnerAccountByEmail(email)).thenReturn(ownerAccount);

        // act
        OwnerAccount foundOwnerAccount = service.findOwner();

        // assertions
        assertNotNull(foundOwnerAccount);
        assertEquals(foundOwnerAccount.getName(), ownerAccount.getName());
        assertEquals(foundOwnerAccount.getEmail(), ownerAccount.getEmail());
        assertEquals(foundOwnerAccount.getPassword(), ownerAccount.getPassword());
        verify(ownerRepo, times(1)).findOwnerAccountByEmail(email);
    }

    @Test
    public void testCannotFindOwner() {
        // setup
        String email = "owner@sportcenter.com";
        lenient().when(ownerRepo.findOwnerAccountByEmail(email)).thenReturn(null);
        String error = "";

        // act
        try {
            service.findOwner();
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("There is no owner", error);
        verify(ownerRepo, times(1)).findOwnerAccountByEmail(email);
    }

    // CREATE OWNER ACCOUNT
    @Test
    public void testCreateValidOwner() {
        // setup
        String name = "validName6";
        String email = "owner@sportcenter.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);

        // act
        OwnerAccount createdOwnerAccount = service.createOwner(name,password);

        // assertions
        assertNotNull(createdOwnerAccount);
        assertEquals(createdOwnerAccount.getName(), name);
        assertEquals(createdOwnerAccount.getEmail(), email);
        assertEquals(createdOwnerAccount.getPassword(), password);
        verify(ownerRepo, times(1)).save(any(OwnerAccount.class));
    }

    @Test
    public void testCreateOwnerByEmptyName() {
        // setup
        String name = "";
        String email = "owner@sportcenter.com";
        String password = "validPassword$6";
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            service.createOwner(name, password);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Name is empty", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
    }
    @Test
    public void testCreateOwnerByEmptyPassword() {
        // setup
        String name = "validName6";
        String email = "owner@sportcenter.com";
        String password = "";
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            service.createOwner(name, password);
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
        String email = "owner@sportcenter.com";
        String password = "P$6";
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            service.createOwner(name, password);
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
        String email = "owner@sportcenter.com";
        String password = "invalidpassword$6";
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            service.createOwner(name, password);
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
        String email = "owner@sportcenter.com";
        String password = "PASSWORD$";
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            service.createOwner(name, password);
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
        String email = "owner@sportcenter.com";
        String password = "invalidPassword6";
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            service.createOwner(name, password);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain at least one special character", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
    }
    @Test
    public void testCreateSecondOwner() {
        // setup
        String name = "validName6";
        String email = "owner@sportcenter.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.existsOwnerAccountByEmail(email)).thenReturn(true);

        String newName = "validName7";
        String newPassword = "validPassword$7";

        String error = "";

        // act
        try {
            service.createOwner(newName, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Only one owner can exist", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).existsOwnerAccountByEmail(email);
    }

    // UPDATE OWNER ACCOUNT
    @Test
    public void testUpdateValidOwner() {
        // setup
        String name = "validName6";
        String email = "owner@sportcenter.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountByEmail(email)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newPassword = "validPassword7$";

        // act
        OwnerAccount updatedOwnerAccount = service.updateOwnerAccount(newName, newPassword);

        // assertions
        assertNotNull(updatedOwnerAccount);
        assertEquals(updatedOwnerAccount.getName(), newName);
        assertEquals(updatedOwnerAccount.getPassword(), newPassword);
        verify(ownerRepo, times(1)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountByEmail(email);
    }

    @Test
    public void testUpdateOwnerByEmptyName() {
        // setup
        String name = "validName6";
        String email = "owner@sportcenter.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountByEmail(email)).thenReturn(ownerAccount);

        String newName = "";
        String newPassword = "validPassword$7";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(newName, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Name is empty", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountByEmail(email);
    }

    @Test
    public void testUpdateOwnerByEmptyPassword() {
        // setup
        String name = "validName6";
        String email = "owner@sportcenter.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountByEmail(email)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newPassword = "";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(newName, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password is empty", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountByEmail(email);
    }

    @Test
    public void testUpdateOwnerByShortPassword() {
        // setup
        String name = "validName6";
        String email = "owner@sportcenter.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountByEmail(email)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newPassword = "valP$";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(newName, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must be at least eight characters long", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountByEmail(email);
    }

    @Test
    public void testUpdateOwnerByPasswordWithoutUppercaseChar() {
        // setup
        String name = "validName6";
        String email = "owner@sportcenter.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountByEmail(email)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newPassword = "validpassword7$";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(newName, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain one upper-case character", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountByEmail(email);
    }

    @Test
    public void testUpdateOwnerByPasswordWithoutLowercaseChar() {
        // setup
        String name = "validName6";
        String email = "owner@sportcenter.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountByEmail(email)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newPassword = "PASSWORD7$";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(newName, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain one lower-case character", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountByEmail(email);
    }

    @Test
    public void testUpdateOwnerByPasswordWithoutSpecialChar() {
        // setup
        String name = "validName6";
        String email = "owner@sportcenter.com";
        String password = "validPassword$6";
        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        lenient().when(ownerRepo.save(any(OwnerAccount.class))).thenReturn(ownerAccount);
        lenient().when(ownerRepo.findOwnerAccountByEmail(email)).thenReturn(ownerAccount);

        String newName = "validName7";
        String newPassword = "validPassword7";
        String error = "";

        // act
        try {
            service.updateOwnerAccount(newName, newPassword);
        } catch (ServiceException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain at least one special character", error);
        verify(ownerRepo, times(0)).save(any(OwnerAccount.class));
        verify(ownerRepo, times(1)).findOwnerAccountByEmail(email);
    }
}
