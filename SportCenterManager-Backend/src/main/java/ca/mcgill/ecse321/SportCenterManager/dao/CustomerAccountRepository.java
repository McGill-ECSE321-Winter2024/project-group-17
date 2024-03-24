package ca.mcgill.ecse321.SportCenterManager.dao;

import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import org.springframework.data.repository.CrudRepository;

public interface CustomerAccountRepository extends CrudRepository<CustomerAccount, Integer> {
  public CustomerAccount findCustomerAccountById(int id);
  public CustomerAccount findCustomerAccountByEmail(String email);
  public CustomerAccount findCustomerAccountByEmailAndPassword(String email, String password);
  public boolean existsCustomerAccountByEmail(String email);
  public boolean existsCustomerAccountByEmailAndPassword(String email, String password);
}
