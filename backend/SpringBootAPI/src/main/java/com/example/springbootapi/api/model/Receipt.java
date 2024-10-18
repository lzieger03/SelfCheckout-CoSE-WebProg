package com.example.springbootapi.api.model;


public class Receipt {
    private final String logoPath;
    private final String title;
    private final String address;
    private final String phone;
    private final Cart cart;
    private final String footer;

    public Receipt(String logoPath, String title, String address, String phone, Cart cart, String footer) {
        this.logoPath = logoPath;
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.cart = cart;
        this.footer = footer;
    }

    public String getLogoPath() {
        return logoPath;
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
