package com.example.springbootapi.api.model;

import java.util.List;
import java.util.Random;

public class Cart {
    private String cartId;
    private List<CartObject> cartObjectList;
    private String paymentMethod;
    private double subTotalPrice;
    private double taxes;
    private double totalPrice;

    public Cart(List<CartObject> cartObjectList, String paymentMethod) {
        this.cartId = createCartId();
        this.cartObjectList = cartObjectList;
        this.paymentMethod = paymentMethod;
        subTotalPrice = calculateSubTotalPrice();
        taxes = subTotalPrice * 0.19;
        totalPrice = subTotalPrice + taxes;
    }

    public String createCartId() {
        Random random = new Random();
        long randomNumber = 1000000000000L + (long)(random.nextDouble() * 9000000000000L);
        return Long.toString(randomNumber);
    }

    private double calculateSubTotalPrice() {
        double subTotal = 0;
        for(CartObject cartObject : cartObjectList) {
            subTotal+=cartObject.getPrice();
        }
        return subTotal;
    }

    public String getCartId() {
        return cartId;
    }

    public List<CartObject> getCartObjectList() {
        return cartObjectList;
    }

    public String getPaymentMethod() {
        return paymentMethod;
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
