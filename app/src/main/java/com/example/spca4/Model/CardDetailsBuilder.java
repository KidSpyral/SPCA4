package com.example.spca4.Model;

public class CardDetailsBuilder {
    private String nameOnCard;
    private String cardNumber;
    private String day;
    private String month;
    private String cvv;

    public CardDetailsBuilder() {}

    public CardDetailsBuilder nameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
        return this;
    }

    public CardDetailsBuilder cardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public CardDetailsBuilder day(String day) {
        this.day = day;
        return this;
    }

    public CardDetailsBuilder month(String month) {
        this.month = month;
        return this;
    }

    public CardDetailsBuilder cvv(String cvv) {
        this.cvv = cvv;
        return this;
    }

    public CardDetails build() {
        return new CardDetails(nameOnCard, cardNumber, day, month, cvv);
    }
}
