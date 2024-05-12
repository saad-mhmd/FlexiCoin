package com.example.flexicoin.Activity;

public class AddCash {
    private String cardNo;
    private String cardName;
    private String cvv;
    private double cashAmount;
    private String date;

    public AddCash(String cardNo, String cardName, String cvv, double cashAmount, String date) {
        this.cardNo = cardNo;
        this.cardName = cardName;
        this.cvv = cvv;
        this.cashAmount = cashAmount;
        this.date = date;
    }

    public String getCardNo() { return cardNo; }

    public String getCardName() {
        return cardName;
    }

    public String getCvv() {
        return cvv;
    }

    public Double getCashAmount() { return cashAmount; }

    public String getDate() { return date; }


}
