package com.example.springbootapi.api.model;

public class ReceiptBuilder implements Builder {
    private String title;
    private String address;
    private String phone;
    private Cart cart;
    private String footer;

    @Override
    public Builder setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public Builder setAddress(String address) {
        this.address = address;
        return this;
    }

    @Override
    public Builder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    @Override
    public Builder addCart(Cart cart) {
        this.cart = cart;
        return null;
    }

    @Override
    public Builder setFooter(String footer) {
        this.footer = footer;
        return this;
    }

    @Override
    public Receipt build() {
        return new Receipt(title, address, phone, cart, footer);
    }
}
