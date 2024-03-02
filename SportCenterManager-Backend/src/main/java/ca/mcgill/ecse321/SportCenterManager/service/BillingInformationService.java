package ca.mcgill.ecse321.SportCenterManager.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.SportCenterManager.dao.BillingInformationRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.model.BillingInformation;
import ca.mcgill.ecse321.SportCenterManager.model.CustomerAccount;
import jakarta.transaction.Transactional;

@Service
public class BillingInformationService {
    @Autowired
    private BillingInformationRepository billingRepo;
    @Autowired
    private CustomerAccountRepository customerRepo;

    /*
     * Only one BillingInformation per Customer
     * Create
     * Read
     * Update
     * Delete?
     */

    @Transactional
    public BillingInformation createBillingInformation(String address, String postalCode, String country, String name, String cardNumber, int cvc, Date expirationDate, CustomerAccount customer) {
        BillingInformation billingToCreate = new BillingInformation(address, postalCode, country, name, cardNumber, cvc, expirationDate, customer);
        return billingRepo.save(billingToCreate);
    }

    // @Transactional
    // public BillingInformation getBillingInformation(int customerId) {
    //     CustomerAccount customer = customerRepo.findCustomerAccountById(customerId);
    //     return billingRepo.findBillingInformationByKeyCustomerAccount(customer);
    // }
}
