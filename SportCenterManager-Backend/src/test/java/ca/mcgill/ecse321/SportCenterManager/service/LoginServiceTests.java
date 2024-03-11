import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;

@SpringBootTest
public class LoginServiceTests{
    @Mock 
    private CustomerAccountRepository customerRepo;
    @InjectMocks
    private CustomerAccountService customerService;
    

    @Test
    public void testValidCustomerLogin(){

    }

    //invalid meaning empty or DNE in database
    @Test
    public void testInvalidUsernameOnly(){

    }
    @Test
    public void testInvalidPasswordOnly(){

    }
    @Test
    public void testInvalidPasswordAndUsername(){

    }

}