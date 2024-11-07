package com.example.springbootapi.api.model.receipt;

import com.example.springbootapi.api.model.receipt.cart.CartInterface;

/**
 * Concrete implementation of the Builder interface for constructing Receipt objects.
 * This class provides methods to set various components of a receipt and build the final Receipt object.
 */
public class ReceiptBuilder implements Builder {
    private String logoPath;
    private String title;
    private String address;
    private String phone;
    private CartInterface cart;
    private String discountCode;
    private double discountValue;
    private String footer;

    public ReceiptBuilder(String logoPath, String title, String address,
                          String phone, CartInterface cart, String discountCode,
                          double discountValue, String footer) {
        this.logoPath = logoPath;
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.cart = cart;
        this.discountCode = discountCode;
        this.discountValue = discountValue;
        this.footer = footer;
    }

    public Builder setLogo(String logoPath) {
        this.logoPath = logoPath;
        return this;
    }

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

    public Builder addCart(CartInterface cart) {
        this.cart = cart;
        return this;
    }

    public Builder addDiscount(String discountCode, double discountValue) {
        this.discountCode = discountCode;
        this.discountValue = discountValue;
        return this;
    }

    @Override
    public Builder setFooter(String footer) {
        this.footer = footer;
        return this;
    }

    /**
     * Builds and returns a Receipt object with the configured properties.
     * Throws an IllegalStateException if any required fields are not set.
     *
     * @return A fully constructed Receipt object.
     * @throws IllegalStateException if title, address, phone, or cart is not set.
     */
    @Override
    public Receipt build() {
        if (/*logoPath == null ||*/ title == null || address == null || phone == null || cart == null) {
            throw new IllegalStateException("Title, address, phone, and cart must be set");
        }
        return new Receipt(null, title, address, phone,
                cart, discountCode, discountValue, footer);
    }
}
