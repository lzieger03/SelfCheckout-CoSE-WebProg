package com.example.springbootapi.api.model.receipt.cart;

/**
 * Concrete decorator that applies a discount to the cart.
 */
public class DiscountCartDecorator extends CartDecorator {
    private String discountCode;
    private double discountValue;

    public DiscountCartDecorator(CartInterface decoratedCart, String discountCode, double discountValue) {
        super(decoratedCart);
        this.discountCode = discountCode;
        this.discountValue = discountValue;
        applyDiscount(discountCode, discountValue);
    }

    // Additional discount-related methods can be added here if needed
}
