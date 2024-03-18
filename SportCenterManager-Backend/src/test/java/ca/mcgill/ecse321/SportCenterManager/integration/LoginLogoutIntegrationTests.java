package ca.mcgill.ecse321.SportCenterManager.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class LoginLogoutIntegrationTests {
    
    @BeforeAll
    public void setUpDatabase(){
        populateDatabase();
    }

    @AfterAll
    public void cleanDatabase(){
        clearDatabase();
    }

    private void populateDatabase(){

    }
    private void clearDatabase(){

    }

    @Test
    public void testLoginValid(){

    }
    
    @Test
    public void testLoginInvalidUsername(){

    }

    @Test
    public void testLoginInvalidPassword(){

    }

    @Test
    public void testLoginBothInvalid(){

    }

    @Test
    public void testLogoutValid(){

    }

    @Test
    public void testLogoutInvalid(){

    }
}
