package ca.mcgill.ecse321.SportCenterManager.dao;

import ca.mcgill.ecse321.SportCenterManager.model.BillingInformation;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import org.springframework.data.repository.CrudRepository;

public interface BillingInformationRepository extends CrudRepository<BillingInformation, CustomerAccount> {
    public BillingInformation findBillingInformationByKeyCustomerAccount(CustomerAccount customerAccount);
    public boolean existsByKeyCustomerAccount(CustomerAccount customerAccount);
}
