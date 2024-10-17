package com.example.springbootapi.api.model;

import java.util.List;

public class Receipt {
    private final String title;
    private final String address;
    private final String phone;
    private final Cart cart;
    private final String footer;

    public Receipt(String title, String address, String phone, Cart cart, String footer) {
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.cart = cart;
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

    public Cart getCart() {
        return cart;
    }

    public String getFooter() {
        return footer;
    }
}
