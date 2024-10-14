package com.example.springbootapi.api.model;

import java.util.ArrayList;
import java.util.List;

public class ReceiptBuilder implements Builder {
    private String title;
    private String address;
    private String phone;
    private List<Product> products = new ArrayList<>();
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
    public Builder addProduct(Product product) {
        this.products.add(product);
        return this;
    }

    @Override
    public Builder setFooter(String footer) {
        this.footer = footer;
        return this;
    }

    @Override
    public Receipt build() {
        return new Receipt(title, address, phone, products, footer);
    }
}
