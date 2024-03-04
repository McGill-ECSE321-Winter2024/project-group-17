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

    @Transactional
    public BillingInformation createBillingInformation(String address, String postalCode, String country, String name, String cardNumber, int cvc, Date expirationDate, int customer_id) {
        if (!customerRepo.existsById(customer_id)) {
            throw new IllegalArgumentException("There is no customer with ID " + customer_id + " in the system.");
        }
        BillingInformation billingToCreate = new BillingInformation(address, postalCode, country, name, cardNumber, cvc, expirationDate, customer);
        return billingRepo.save(billingToCreate);
    }

    @Transactional
    public BillingInformation getBillingInformation(int customerId) {
        if (!customerRepo.existsById(customerId)) {
            throw new IllegalArgumentException("There is no customer with ID " + customerId + " in the system.");
        }
        CustomerAccount customer = customerRepo.findCustomerAccountById(customerId);
        return billingRepo.findBillingInformationByKeyCustomerAccount(customer);
    }

    @Transactional
    public BillingInformation updateBillingInformation(int customerId, String address, String postalCode, String country, String name, String cardNumber, int cvc, Date expirationDate) {
        CustomerAccount customer = customerRepo.findCustomerAccountById(customerId);
        BillingInformation billingToModify = billingRepo.findBillingInformationByKeyCustomerAccount(customer);
        billingToModify.setAddress(address);
        billingToModify.setPostalCode(postalCode);
        billingToModify.setCountry(country);
        billingToModify.setName(name);
        billingToModify.setCardNumber(cardNumber);
        billingToModify.setCvc(cvc);
        billingToModify.setExpirationDate(expirationDate);
        return billingRepo.save(billingToModify);
    }
}