package com.example.spca4.Model;

public class CardDetails {

    private String nameOnCard;
    private String cardNumber;
    private String day;
    private String month;
    private String cvv;

    // Constructor
    public CardDetails(String nameOnCard, String cardNumber, String day, String month, String cvv) {
        this.nameOnCard = nameOnCard;
        this.cardNumber = cardNumber;
        this.day = day;
        this.month = month;
        this.cvv = cvv;
    }

    // Getters
    public String getNameOnCard() {
        return nameOnCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getCvv() {
        return cvv;
    }

    // Setters
    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
