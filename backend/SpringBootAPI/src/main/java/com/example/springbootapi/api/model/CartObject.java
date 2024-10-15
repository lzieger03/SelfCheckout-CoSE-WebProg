package com.example.springbootapi.api.model;

public class CartObject extends Product {
    private int quantity;
    public CartObject(String barcode, String name, double price, int quantity) {
        super(barcode, name, price);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public double getPrice() {
        return super.getPrice()*quantity;
    }
}
