package ca.mcgill.ecse321.SportCenterManager.service;

import ca.mcgill.ecse321.SportCenterManager.dao.BillingInformationRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.model.BillingInformation;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CustomerAccountServiceTests {
    @Mock
    private CustomerAccountRepository customerRepo;
    @Mock
    private BillingInformationRepository billingRepo;
    @Mock
    private BillingInformationService billingService;

    @InjectMocks
    private CustomerAccountService service;

    @Test
    public void testFindCustomerByValidId() {
        // setup
        int id = 30;
        CustomerAccount customerAccount = new CustomerAccount("validName", "validEmail@gmail.com", "validPassword$");
        customerAccount.setId(30);
        lenient().when(customerRepo.findCustomerAccountById(id)).thenReturn(customerAccount);

        // act
        CustomerAccount foundCustomerAccount = service.findCustomerById(id);

        // assertions
        assertNotNull(foundCustomerAccount);
        assertEquals(foundCustomerAccount.getName(), customerAccount.getName());
        assertEquals(foundCustomerAccount.getEmail(), customerAccount.getEmail());
        assertEquals(foundCustomerAccount.getPassword(), customerAccount.getPassword());
        verify(customerRepo, times(1)).findCustomerAccountById(id);
    }

    @Test
    public void testFindCustomerByInvalidId() {
        // setup
        int id = 33;
        lenient().when(customerRepo.findCustomerAccountById(id)).thenReturn(null);
        String error = "";

        // act
        try {
            CustomerAccount foundCustomerAccount = service.findCustomerById(id);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("There is no course with ID" + id, error);
        verify(customerRepo, times(1)).findCustomerAccountById(id);
    }

    @Test
    public void testFindAllCustomers() {
        // setup
        List<CustomerAccount> listCustomerAccounts = new ArrayList<CustomerAccount>();

        CustomerAccount customerAccount1 = new CustomerAccount("validName1", "validEmail1@gmail.com", "validPassword$1");
        customerAccount1.setId(5);
        listCustomerAccounts.add(customerAccount1);

        CustomerAccount customerAccount2 = new CustomerAccount("validName2", "validEmail1@gmail.com", "validPassword$2");
        customerAccount1.setId(10);
        listCustomerAccounts.add(customerAccount2);

        CustomerAccount customerAccount3 = new CustomerAccount("validName3", "validEmail1@gmail.com", "validPassword$3");
        customerAccount1.setId(15);
        listCustomerAccounts.add(customerAccount3);

        CustomerAccount customerAccount4 = new CustomerAccount("validName4", "validEmail1@gmail.com", "validPassword$4");
        customerAccount1.setId(20);
        listCustomerAccounts.add(customerAccount4);

        CustomerAccount customerAccount5 = new CustomerAccount("validName5", "validEmail1@gmail.com", "validPassword$5");
        customerAccount1.setId(25);
        listCustomerAccounts.add(customerAccount5);

        lenient().when(customerRepo.findAll()).thenReturn((Iterable<CustomerAccount>) listCustomerAccounts);

        // act
        List<CustomerAccount> foundCustomerAccounts = (List<CustomerAccount>) service.findAllCustomers();

        // assertions
        assertNotNull(foundCustomerAccounts);
        assertEquals(foundCustomerAccounts.size(), 5);
        verify(customerRepo, times(1)).findAll();
    }

    // CREATE CUSTOMER ACCOUNT
    @Test
    public void testCreateValidCustomer() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        int id = 30;
        customerAccount.setId(id);
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(customerAccount);
        // don't need billing information class, not focus of test
        when(billingService.createBillingInformation("address", "postalCode", "country", "name", "cardNumber", 123, null, id)).thenReturn(null);
        
        // act
        CustomerAccount createdCustomerAccount = service.createCustomer(name, email, password);

        // assertions
        assertNotNull(createdCustomerAccount);
        assertEquals(createdCustomerAccount.getName(), name);
        assertEquals(createdCustomerAccount.getEmail(), email);
        assertEquals(createdCustomerAccount.getPassword(), password);
        verify(customerRepo, times(1)).save(any(CustomerAccount.class));
    }

    @Test
    public void testCreateCustomerByEmptyEmail() {
        // setup
        String name = "validName6";
        String email = "";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            CustomerAccount createdCustomerAccount = service.createCustomer(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Email is empty", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
    }
    @Test
    public void testCreateCustomerByEmailWithSpace() {
        // setup
        String name = "validName6";
        String email = "invalidEmail @gmail.com";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            CustomerAccount createdCustomerAccount = service.createCustomer(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Email cannot contain any space", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
    }

    @Test
    public void testCreateCustomerByInvalidEmail() {
        // setup
        String name = "validName6";
        String email = "invalidEmail6@gmail.co";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            CustomerAccount createdCustomerAccount = service.createCustomer(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Invalid Email", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
    }

    @Test
    public void testCreateCustomerByEmptyPassword() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            CustomerAccount createdCustomerAccount = service.createCustomer(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password is empty", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
    }

    @Test
    public void testCreateCustomerByShortPassword() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "P$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            CustomerAccount createdCustomerAccount = service.createCustomer(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must be at least eight characters long", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
    }

    @Test
    public void testCreateCustomerByPasswordWithoutUppercaseChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "invalidpassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            CustomerAccount createdCustomerAccount = service.createCustomer(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain one upper-case character", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
    }

    @Test
    public void testCreateCustomerByPasswordWithoutLowercaseChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "PASSWORD$";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            CustomerAccount createdCustomerAccount = service.createCustomer(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain one lower-case character", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
    }

    @Test
    public void testCreateCustomerByPasswordWithoutSpecialChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "invalidPassword6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(null);
        String error = "";

        // act
        try {
            CustomerAccount createdCustomerAccount = service.createCustomer(name, email, password);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain at least one special character", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
    }
    @Test
    public void testCreateDuplicateCustomer() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(customerAccount);
        lenient().when(customerRepo.existsCustomerAccountByEmail(email)).thenReturn(true);

        String sameName = "validName6";
        String sameEmail = "validEmail6@gmail.com";
        String samePassword = "validPassword$6";

        String error = "";

        // act
        try {
            CustomerAccount createdCustomerAccount = service.createCustomer(sameName, sameEmail, samePassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Customer with this email already exists", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
        verify(customerRepo, times(1)).existsCustomerAccountByEmail(email);
    }

    // UPDATE CUSTOMER ACCOUNT
    @Test
    public void testUpdateValidCustomer() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        int id = customerAccount.getId();
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(customerAccount);
        lenient().when(customerRepo.findCustomerAccountById(id)).thenReturn(customerAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "validPassword7$";

        // act
        CustomerAccount updatedCustomerAccount = service.updateCustomerAccount(id, newName, newEmail, newPassword);

        // assertions
        assertNotNull(updatedCustomerAccount);
        assertEquals(updatedCustomerAccount.getName(), newName);
        assertEquals(updatedCustomerAccount.getEmail(), newEmail);
        assertEquals(updatedCustomerAccount.getPassword(), newPassword);
        verify(customerRepo, times(1)).save(any(CustomerAccount.class));
        verify(customerRepo, times(1)).findCustomerAccountById(id);
    }

    @Test
    public void testUpdateCustomerWithInvalidId() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        int id = 50;
        customerAccount.setId(id);
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(customerAccount);
        lenient().when(customerRepo.findCustomerAccountById(id)).thenReturn(customerAccount);

        int invalidId = 60;
        String newName = "validName8";
        String newEmail = "validEmail8@gmail.com";
        String newPassword = "validPassword8$";
        String error = "";

        // act
        try {
            CustomerAccount updatedCustomerAccount = service.updateCustomerAccount(invalidId, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("The customer account was not found", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
        verify(customerRepo, times(1)).findCustomerAccountById(invalidId);

    }

    @Test
    public void testUpdateCustomerByEmptyEmail() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        int id = customerAccount.getId();
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(customerAccount);
        lenient().when(customerRepo.findCustomerAccountById(id)).thenReturn(customerAccount);

        String newName = "validName7";
        String newEmail = "";
        String newPassword = "validPassword7$";
        String error = "";

        // act
        try {
            CustomerAccount updatedCustomerAccount = service.updateCustomerAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Email is empty", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
        verify(customerRepo, times(1)).findCustomerAccountById(id);
    }
    @Test
    public void testUpdateCustomerByEmailWithSpace() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        int id = customerAccount.getId();
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(customerAccount);
        lenient().when(customerRepo.findCustomerAccountById(id)).thenReturn(customerAccount);

        String newName = "validName7";
        String newEmail = "validEmail @gmail.com";
        String newPassword = "validPassword7$";
        String error = "";

        // act
        try {
            CustomerAccount updatedCustomerAccount = service.updateCustomerAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Email cannot contain any space", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
        verify(customerRepo, times(1)).findCustomerAccountById(id);
    }

    @Test
    public void testUpdateCustomerByInvalidEmail() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        int id = customerAccount.getId();
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(customerAccount);
        lenient().when(customerRepo.findCustomerAccountById(id)).thenReturn(customerAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.co";
        String newPassword = "validPassword7$";
        String error = "";

        // act
        try {
            CustomerAccount updatedCustomerAccount = service.updateCustomerAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Invalid Email", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
        verify(customerRepo, times(1)).findCustomerAccountById(id);
    }

    @Test
    public void testUpdateCustomerByEmptyPassword() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        int id = customerAccount.getId();
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(customerAccount);
        lenient().when(customerRepo.findCustomerAccountById(id)).thenReturn(customerAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "";
        String error = "";

        // act
        try {
            CustomerAccount updatedCustomerAccount = service.updateCustomerAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password is empty", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
        verify(customerRepo, times(1)).findCustomerAccountById(id);
    }

    @Test
    public void testUpdateCustomerByShortPassword() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        int id = customerAccount.getId();
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(customerAccount);
        lenient().when(customerRepo.findCustomerAccountById(id)).thenReturn(customerAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "valP$";
        String error = "";

        // act
        try {
            CustomerAccount updatedCustomerAccount = service.updateCustomerAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must be at least eight characters long", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
        verify(customerRepo, times(1)).findCustomerAccountById(id);
    }

    @Test
    public void testUpdateCustomerByPasswordWithoutUppercaseChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        int id = customerAccount.getId();
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(customerAccount);
        lenient().when(customerRepo.findCustomerAccountById(id)).thenReturn(customerAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "validpassword7$";
        String error = "";

        // act
        try {
            CustomerAccount updatedCustomerAccount = service.updateCustomerAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain one upper-case character", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
        verify(customerRepo, times(1)).findCustomerAccountById(id);
    }

    @Test
    public void testUpdateCustomerByPasswordWithoutLowercaseChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        int id = customerAccount.getId();
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(customerAccount);
        lenient().when(customerRepo.findCustomerAccountById(id)).thenReturn(customerAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "PASSWORD7$";
        String error = "";

        // act
        try {
            CustomerAccount updatedCustomerAccount = service.updateCustomerAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain one lower-case character", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
        verify(customerRepo, times(1)).findCustomerAccountById(id);
    }

    @Test
    public void testUpdateCustomerByPasswordWithoutSpecialChar() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        int id = customerAccount.getId();
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(customerAccount);
        lenient().when(customerRepo.findCustomerAccountById(id)).thenReturn(customerAccount);

        String newName = "validName7";
        String newEmail = "validEmail7@gmail.com";
        String newPassword = "validPassword7";
        String error = "";

        // act
        try {
            CustomerAccount updatedCustomerAccount = service.updateCustomerAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Password must contain at least one special character", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
        verify(customerRepo, times(1)).findCustomerAccountById(id);
    }

    @Test
    public void testUpdateDuplicateCustomer() {
        // setup
        String name = "validName6";
        String email = "validEmail6@gmail.com";
        String password = "validPassword$6";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        int id = 50;
        customerAccount.setId(id);
        lenient().when(customerRepo.save(any(CustomerAccount.class))).thenReturn(customerAccount);
        lenient().when(customerRepo.findCustomerAccountById(id)).thenReturn(customerAccount);
        lenient().when(customerRepo.existsCustomerAccountByEmail(email)).thenReturn(true);

        String newName = "validName7";
        String newEmail = "validEmail6@gmail.com";
        String newPassword = "validPassword$7";
        String error = "";

        // act
        try {
            CustomerAccount updatedCustomerAccount = service.updateCustomerAccount(id, newName, newEmail, newPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("Customer with this email already exists", error);
        verify(customerRepo, times(0)).save(any(CustomerAccount.class));
        verify(customerRepo, times(1)).findCustomerAccountById(id);
        verify(customerRepo, times(1)).existsCustomerAccountByEmail(email);
    }

    // DELETE CUSTOMER ACCOUNT
    @Test
    public void testDeleteCustomerByValidId() {
        // setup
        String name = "validName8";
        String email = "validEmail8@gmail.com";
        String password = "validPassword$8";
        CustomerAccount customerAccount = new CustomerAccount(name, email, password);
        int id = 30;
        customerAccount.setId(id);
        lenient().when(customerRepo.findCustomerAccountById(id)).thenReturn(customerAccount);

        // act
        boolean deletedCustomerAccount = service.deleteCustomer(id);

        // assertions
        assertTrue(deletedCustomerAccount, "The customer account is deleted");
        verify(customerRepo, times(1)).findCustomerAccountById(id);
    }

    @Test
    public void testDeleteCustomerByInvalidId() {
        // setup
        int id = 33;
        lenient().when(customerRepo.findById(id)).thenReturn(null);
        String error = "";

        // act
        try {
            boolean deletedCustomerAccount = service.deleteCustomer(id);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        // assertions
        assertEquals("There is no course with ID" + id, error);
        verify(customerRepo, times(1)).findCustomerAccountById(id);
    }
}
