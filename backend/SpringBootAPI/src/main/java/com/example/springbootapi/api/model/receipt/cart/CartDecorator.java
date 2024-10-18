package com.example.springbootapi.api.model.receipt.cart;

import java.util.List;

/**
 * Abstract decorator class for CartInterface.
 * Allows adding additional responsibilities to Cart without modifying its structure.
 */
public abstract class CartDecorator implements CartInterface {
    protected CartInterface decoratedCart;

    public CartDecorator(CartInterface decoratedCart) {
        this.decoratedCart = decoratedCart;
    }

    @Override
    public String getCartId() {
        return decoratedCart.getCartId();
    }

    @Override
    public List<CartObject> getCartObjectList() {
        return decoratedCart.getCartObjectList();
    }

    @Override
    public String getPaymentMethod() {
        return decoratedCart.getPaymentMethod();
    }

    @Override
    public double getSubTotalPrice() {
        return decoratedCart.getSubTotalPrice();
    }

    @Override
    public double getTaxes() {
        return decoratedCart.getTaxes();
    }

    @Override
    public double getTotalPrice() {
        return decoratedCart.getTotalPrice();
    }

    @Override
    public void applyDiscount(String discountCode, double discountValue) {
        decoratedCart.applyDiscount(discountCode, discountValue);
    }

    @Override
    public String getDiscountCode() {
        return decoratedCart.getDiscountCode();
    }

    @Override
    public double getDiscountValue() {
        return decoratedCart.getDiscountValue();
    }
}
