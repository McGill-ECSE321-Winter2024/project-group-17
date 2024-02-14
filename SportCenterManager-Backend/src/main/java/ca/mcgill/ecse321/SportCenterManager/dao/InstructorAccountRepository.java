package ca.mcgill.ecse321.SportCenterManager.dao;

import ca.mcgill.ecse321.SportCenterManager.model.InstructorAccount;
import org.springframework.data.repository.CrudRepository;

public interface InstructorAccountRepository extends CrudRepository<InstructorAccount, Integer> {
}
