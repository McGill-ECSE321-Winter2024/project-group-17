package ca.mcgill.ecse321.SportCenterManager.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.exception.ServiceException;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;

@SpringBootTest
public class LoginServiceTests{
    @Mock 
    private CustomerAccountRepository customerRepo;
    @InjectMocks
    private CustomerAccountService customerService;
    

    @Test
    public void testValidCustomerLogin(){
        String name = "Bob";
        String email = "bob@gmail.com";
        String password = "Password321";
        CustomerAccount bob = new CustomerAccount(name,email,password);
        when(customerRepo.existsCustomerAccountByEmailAndPassword(email,password)).thenReturn(true);
        when(customerRepo.findCustomerAccountByEmailAndPassword(email,password)).thenReturn(bob);

        CustomerAccount customer = customerService.login(email, password);

        assertNotNull(customer);
        assertEquals(bob.getEmail(), customer.getEmail());
        assertEquals(bob.getName(),customer.getName());
        assertEquals(bob.getId(),customer.getId());
    }

    //invalid meaning empty or DNE in database
    @Test
    public void testInvalidUsernameOnly(){
        String email = "";
        String password = "Password321";
        when(customerRepo.existsCustomerAccountByEmailAndPassword(email,password)).thenReturn(false);
        when(customerRepo.findCustomerAccountByEmailAndPassword(email,password)).thenReturn(null);
        ServiceException e = assertThrows(ServiceException.class,()->customerService.login(email,password));
        assertEquals("Invalid email or password",e.getMessage());
    }
    @Test
    public void testInvalidPasswordOnly(){
         String email = "bob@gmail.com";
         String password = "";
         when(customerRepo.existsCustomerAccountByEmailAndPassword(email,password)).thenReturn(false);
         when(customerRepo.findCustomerAccountByEmailAndPassword(email,password)).thenReturn(null);   
         ServiceException e = assertThrows(ServiceException.class,()->customerService.login(email,password));
         assertEquals("Invalid email or password",e.getMessage());
    }

    @Test
    public void testInvalidPasswordAndUsername(){
        String email = "";
        String password = "";
        when(customerRepo.existsCustomerAccountByEmailAndPassword(email,password)).thenReturn(false);
        ServiceException e = assertThrows(ServiceException.class,()->customerService.login(email,password));
        assertEquals("Invalid email or password",e.getMessage());

    }

}