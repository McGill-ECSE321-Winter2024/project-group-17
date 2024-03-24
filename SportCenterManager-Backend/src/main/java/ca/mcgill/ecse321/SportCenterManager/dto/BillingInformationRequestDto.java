package ca.mcgill.ecse321.SportCenterManager.dto;

import java.sql.Date;

public class BillingInformationRequestDto {
    private String address;
    private String postalCode;
    private String country;
    private String name;
    private String cardNumber;
    private int cvc;
    private Date expirationDate;

    public BillingInformationRequestDto(String address, String postalCode, String country, String name, String cardNumber, int cvc, Date expirationDate) {
        this.address = address;
        this.postalCode = postalCode;
        this.country = country;
        this.name = name;
        this.cardNumber = cardNumber;
        this.cvc = cvc;
        this.expirationDate = expirationDate;
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
