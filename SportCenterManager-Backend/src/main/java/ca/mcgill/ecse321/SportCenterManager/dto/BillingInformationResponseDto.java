package ca.mcgill.ecse321.SportCenterManager.dto;

import java.sql.Date;

import ca.mcgill.ecse321.SportCenterManager.model.BillingInformation;

public class BillingInformationResponseDto {
    private String address;
    private String postalCode;
    private String country;
    private String name;
    private String cardNumber;
    private int cvc;
    private Date expirationDate;

    @SuppressWarnings("unused")
    private BillingInformationResponseDto() {}

    public BillingInformationResponseDto(BillingInformation model) {
        this.address = model.getAddress();
        this.postalCode = model.getPostalCode();
        this.country = model.getCountry();
        this.name = model.getName();
        this.cardNumber = model.getCardNumber();
        this.cvc = model.getCvc();
        this.expirationDate = model.getExpirationDate();
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getCvc() {
        return cvc;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCvc(int cvc) {
        this.cvc = cvc;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
