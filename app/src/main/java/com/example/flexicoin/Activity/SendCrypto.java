package com.example.flexicoin.Activity;

public class SendCrypto {
    String recEmail;
    String recAccount;
    String cryptoType;

    public SendCrypto(String recEmail, String recAccount, String cryptoType) {
        this.recEmail = recEmail;
        this.recAccount = recAccount;
        this.cryptoType = cryptoType;
    }

    public String getRecEmail() {
        return recEmail;
    }

    public String getRecAccount() {
        return recAccount;
    }

    public String getCryptoType() {
        return cryptoType;
    }
}
