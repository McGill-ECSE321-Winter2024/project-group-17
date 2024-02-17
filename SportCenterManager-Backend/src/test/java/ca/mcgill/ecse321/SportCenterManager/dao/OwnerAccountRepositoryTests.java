package ca.mcgill.ecse321.SportCenterManager.dao;

import ca.mcgill.ecse321.SportCenterManager.model.OwnerAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class OwnerAccountRepositoryTests {
    @Autowired
    private OwnerAccountRepository ownerAccountRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase(){
        ownerAccountRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadOwnerAccount(){
        // Create OwnerAccount
        String name = "Namir";
        String email = "namir@gmail.com";
        String password = "789";

        OwnerAccount ownerAccount = new OwnerAccount(name, email, password);
        ownerAccount = ownerAccountRepository.save(ownerAccount);
        int accountId = ownerAccount.getId();

        // Read OwnerAccount from database
        OwnerAccount ownerAccountFromDb = ownerAccountRepository.findOwnerAccountById(accountId);

        // Assertions
        assertNotNull(ownerAccountFromDb);
        assertEquals(name, ownerAccountFromDb.getName());
        assertEquals(email, ownerAccountFromDb.getEmail());
        assertEquals(password, ownerAccountFromDb.getPassword());
    }
}
