package com.example.springbootapi.api.model;


import java.util.List;

public class Receipt {
    private String title;
    private String address;
    private String phone;
    private List<Product> products;
    private String footer;

    public Receipt(String title, String address, String phone, List<Product> products, String footer) {
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.products = products;
        this.footer = footer;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getFooter() {
        return footer;
    }
}
