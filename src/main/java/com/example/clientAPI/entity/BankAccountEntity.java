package com.example.clientAPI.entity;

public class BankAccountEntity {

    private String id;
    private Integer parameterId;
    private Integer typeId;
    private Double sold;
    private String iban;

    // ----------------- Getters & Setters -----------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getParameterId() {
        return parameterId;
    }

    public void setParameterId(Integer parameterId) {
        this.parameterId = parameterId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
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

    // ----------------- toString -----------------
    @Override
    public String toString() {
        return "BankAccountEntity{" +
                "id='" + id + '\'' +
                ", parameterId=" + parameterId +
                ", typeId=" + typeId +
                ", sold=" + sold +
                ", iban='" + iban + '\'' +
                '}';
    }
}