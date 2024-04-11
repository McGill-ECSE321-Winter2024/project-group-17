package ca.mcgill.ecse321.SportCenterManager.dao;

import ca.mcgill.ecse321.SportCenterManager.model.OwnerAccount;
import org.springframework.data.repository.CrudRepository;

public interface OwnerAccountRepository extends CrudRepository<OwnerAccount, Integer> {
    public OwnerAccount findOwnerAccountById(int id);
    public OwnerAccount findOwnerAccountByEmail(String email);
    public boolean existsOwnerAccountByEmail(String email);

}
