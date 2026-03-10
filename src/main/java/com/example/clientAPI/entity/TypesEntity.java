package com.example.clientAPI.entity;

public class TypesEntity {

    private Integer id;
    private String name;

    // ----------------- Getters & Setters -----------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // ----------------- toString -----------------
    @Override
    public String toString() {
        return "TypesEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}