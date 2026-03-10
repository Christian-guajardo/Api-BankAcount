package com.example.clientAPI.entity;

import dto.bankapi.State;

public class BankAccountParameterEntity {

    private Integer id;
    private Double overdraftLimit;
    private State state; // active, inactive, bloqued, closed

    // ----------------- Getters & Setters -----------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(Double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    // ----------------- toString -----------------
    @Override
    public String toString() {
        return "BankAccountParameterEntity{" +
                "id=" + id +
                ", overdraftLimit=" + overdraftLimit +
                ", state='" + state + '\'' +
                '}';
    }
}