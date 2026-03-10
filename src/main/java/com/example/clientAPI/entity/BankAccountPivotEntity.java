package com.example.clientAPI.entity;

public class BankAccountPivotEntity {

    private String bankAccountId;  // FK vers BankAccount
    private String accountId;      // FK vers Account (VARCHAR(50))

    // ----------------- Getters & Setters -----------------

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    // ----------------- toString -----------------
    @Override
    public String toString() {
        return "BankAccountPivotEntity{" +
                "bankAccountId='" + bankAccountId + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}