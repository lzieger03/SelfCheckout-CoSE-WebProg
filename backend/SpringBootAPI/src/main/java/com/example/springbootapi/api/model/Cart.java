package com.example.springbootapi.api.model;

import java.util.List;

public class Cart {
    private List<CartObject> cartObjectList;
    private double subTotalPrice;
    private double taxes;
    private double totalPrice;

    public Cart(List<CartObject> cartObjectList) {
        this.cartObjectList = cartObjectList;
        subTotalPrice = calculateSubTotalPrice();
        taxes = subTotalPrice * 0.19;
        totalPrice = subTotalPrice + taxes;
    }

    private double calculateSubTotalPrice() {
        double subTotal = 0;
        for(CartObject cartObject : cartObjectList) {
            subTotal+=cartObject.getPrice();
        }
        return subTotal;
    }

    public List<CartObject> getCartObjectList() {
        return cartObjectList;
    }

    public double getSubTotalPrice() {
        return subTotalPrice;
    }

    public double getTaxes() {
        return taxes;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
