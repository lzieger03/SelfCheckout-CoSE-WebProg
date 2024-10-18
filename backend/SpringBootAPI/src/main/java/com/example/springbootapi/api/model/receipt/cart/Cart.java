package com.example.springbootapi.api.model.receipt.cart;

import java.util.List;
import java.util.Random;

/**
 * Represents a shopping cart with items and payment information.
 * Handles calculation of subtotal, taxes, and total price, including any discounts.
 */
public class Cart implements CartInterface {
    private String cartId;
    private List<CartObject> cartObjectList;
    private String paymentMethod;
    private double subTotalPrice;
    private double taxes;
    private double totalPrice;

    // Fields for discount
    private String discountCode;
    private double discountValue; // Percentage discount (e.g., 10.0 for 10%)

    public Cart(List<CartObject> cartObjectList, String paymentMethod) {
        this.cartId = createCartId();
        this.cartObjectList = cartObjectList;
        this.paymentMethod = paymentMethod;
        this.discountCode = "";
        this.discountValue = 0.0;
        this.subTotalPrice = calculateSubTotalPrice();
        this.taxes = calculateTaxes();
        this.totalPrice = calculateTotalPrice();
    }

    /**
     * Generates a unique cart ID.
     *
     * @return A string representing the unique cart ID.
     */
    public String createCartId() {
        Random random = new Random();
        long randomNumber = 1000000000000L + (long)(random.nextDouble() * 9000000000000L);
        return Long.toString(randomNumber);
    }

    /**
     * Calculates the subtotal price of all items in the cart, applying any discounts.
     *
     * @return The subtotal price.
     */
    private double calculateSubTotalPrice() {
        double subTotal = 0;
        for(CartObject cartObject : cartObjectList) {
            double itemTotal = cartObject.getPrice() * cartObject.getQuantity();
            double discountAmount = (discountValue > 0) ? (itemTotal * (discountValue / 100)) : 0.0;
            cartObject.setDiscountAmount(discountAmount); // Track discount per item
            subTotal += (itemTotal - discountAmount);
        }
        return subTotal;
    }

    /**
     * Calculates the taxes based on the subtotal price.
     *
     * @return The tax amount.
     */
    private double calculateTaxes() {
        // Assuming a tax rate of 10%
        double taxRate = 0.10;
        return subTotalPrice * taxRate;
    }

    /**
     * Calculates the total price including taxes.
     *
     * @return The total price.
     */
    private double calculateTotalPrice() {
        return subTotalPrice + taxes;
    }

    /**
     * Sets the discount code and value, then recalculates prices.
     *
     * @param discountCode The discount code.
     * @param discountValue The discount value as a percentage.
     */
    @Override
    public void applyDiscount(String discountCode, double discountValue) {
        this.discountCode = discountCode;
        this.discountValue = discountValue;
        this.subTotalPrice = calculateSubTotalPrice();
        this.taxes = calculateTaxes();
        this.totalPrice = calculateTotalPrice();
    }

    // Getters and Setters

    @Override
    public String getCartId() {
        return cartId;
    }

    @Override
    public List<CartObject> getCartObjectList() {
        return cartObjectList;
    }

    @Override
    public String getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public double getSubTotalPrice() {
        return subTotalPrice;
    }

    @Override
    public double getTaxes() {
        return taxes;
    }

    @Override
    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String getDiscountCode() {
        return discountCode;
    }

    @Override
    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }
}
