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
        CustomerAccount customer = customerRepo.findCustomerAccountById(customer_id);
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
        if (!customerRepo.existsById(customerId)) {
            throw new IllegalArgumentException("There is no customer with ID " + customerId + " in the system.");
        }
        CustomerAccount customer = customerRepo.findCustomerAccountById(customerId);
        if (!billingRepo.existsByKeyCustomerAccount(customer)) {
            throw new IllegalArgumentException("There is no billing information for customer with ID " + customerId + " in the system.");
        }
        if (address == null || address.trim().length() == 0) {
            throw new IllegalArgumentException("Address cannot be empty.");
        }
        if (postalCode == null || postalCode.trim().length() == 0) {
            throw new IllegalArgumentException("Postal code cannot be empty.");
        }
        if (country == null || country.trim().length() == 0) {
            throw new IllegalArgumentException("Country cannot be empty.");
        }
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (cardNumber == null || cardNumber.trim().length() == 0) {
            throw new IllegalArgumentException("Card number cannot be empty.");
        }
        if (cvc < 100 || cvc > 999) {
            throw new IllegalArgumentException("CVC must be a 3-digit number.");
        }
        if (expirationDate == null) {
            throw new IllegalArgumentException("Expiration date cannot be empty.");
        }
        if (expirationDate.before(Date.valueOf(java.time.LocalDate.now()))) {
            throw new IllegalArgumentException("Expiration date cannot be in the past.");
        }
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