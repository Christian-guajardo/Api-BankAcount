package com.example.clientAPI.entity;

public class BankAccountDetailEntity {

    private String id;
    private BankAccountParameterEntity parameter;
    private TypesEntity type;
    private Double sold;
    private String iban;

    // ==================== Constructors ====================
    public BankAccountDetailEntity() {}

    public BankAccountDetailEntity(String id, BankAccountParameterEntity parameter,
                                   TypesEntity type, Double sold, String iban) {
        this.id = id;
        this.parameter = parameter;
        this.type = type;
        this.sold = sold;
        this.iban = iban;
    }

    // ==================== Getters & Setters ====================
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BankAccountParameterEntity getParameter() {
        return parameter;
    }

    public void setParameter(BankAccountParameterEntity parameter) {
        this.parameter = parameter;
    }

    public TypesEntity getType() {
        return type;
    }

    public void setType(TypesEntity type) {
        this.type = type;
    }

    public Double getSold() {
        return sold;
    }

    public void setSold(Double sold) {
        this.sold = sold;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    // ==================== toString ====================
    @Override
    public String toString() {
        return "BankAccountDetailEntity{" +
                "id='" + id + '\'' +
                ", parameter=" + parameter +
                ", type=" + type +
                ", sold=" + sold +
                ", iban='" + iban + '\'' +
                '}';
    }
}

