package ca.mcgill.ecse321.SportCenterManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.SportCenterManager.dao.InstructorAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import jakarta.transaction.Transactional;

@Service
public class InstructorAccountService {
    @Autowired
    private InstructorAccountRepository instructorRepo;

    @Transactional
    public Iterable<InstructorAccount> findAllInstructors() {
        return instructorRepo.findAll();
    }

    @Transactional
    public InstructorAccount findInstructorById(int id) {
        return instructorRepo.findInstructorAccountById(id);
    }

    @Transactional
    public InstructorAccount updateInstructorAccount(int id, String name, String email, String password) {
        InstructorAccount instructorToModify = instructorRepo.findInstructorAccountById(id);
        instructorToModify.setName(name);
        instructorToModify.setEmail(email);
        instructorToModify.setPassword(password);
        return instructorRepo.save(instructorToModify);
    }

    @Transactional
    public InstructorAccount createInstructor(String name, String email, String password) {
        InstructorAccount instructorToCreate = new InstructorAccount(name, email, password);
        return instructorRepo.save(instructorToCreate);
    }

    @Transactional
    public void deleteInstructor(int id) {
        instructorRepo.deleteById(id);
    }
}
