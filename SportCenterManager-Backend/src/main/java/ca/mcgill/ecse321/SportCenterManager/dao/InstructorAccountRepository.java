package ca.mcgill.ecse321.SportCenterManager.dao;

import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import org.springframework.data.repository.CrudRepository;

public interface InstructorAccountRepository extends CrudRepository<InstructorAccount, Integer> {
  public InstructorAccount findInstructorAccountById(int id);
  public InstructorAccount findCustomerInstructorByEmailAndPassword(String email, String password);
  public boolean existsInstructorAccountByEmail(String email);
  public boolean existsInstructorAccountByEmailAndPassword(String email, String password);
}
