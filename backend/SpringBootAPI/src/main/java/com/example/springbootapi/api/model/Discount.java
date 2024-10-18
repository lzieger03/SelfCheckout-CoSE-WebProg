package com.example.springbootapi.api.model;

public class Discount {


    private String code;
    private double value;

    public Discount(String code, double value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public double getValue() {
        return value;
    }
}
