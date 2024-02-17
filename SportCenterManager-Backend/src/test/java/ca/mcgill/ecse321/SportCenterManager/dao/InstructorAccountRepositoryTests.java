package ca.mcgill.ecse321.SportCenterManager.dao;

import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class InstructorAccountRepositoryTests {
    @Autowired InstructorAccountRepository instructorAccountRepository;

    @AfterEach
    public void clearDatabase() {
        instructorAccountRepository.deleteAll();
    }

    @Test
    public void testPersistanceAndLoadInstructorAccount(){
        String name = "Mahmoud";
        String email = "mahmoud@gmail.com" ;
        String password = "abcdefg";

        InstructorAccount instructorAccount= new InstructorAccount(name, email, password);
        instructorAccount=instructorAccountRepository.save(instructorAccount);
        int accountID = instructorAccount.getId();

        InstructorAccount instructorAccountDatabase = instructorAccountRepository.findInstructorAccountById(accountID);

        assertNotNull(instructorAccountDatabase);
        assertEquals(name, instructorAccountDatabase.getName());
        assertEquals(email, instructorAccountDatabase.getEmail());
        assertEquals(password, instructorAccountDatabase.getPassword());
    }

}
