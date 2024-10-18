package com.example.springbootapi.api.model.receipt;

import com.example.springbootapi.api.model.receipt.cart.Cart;

/**
 * Concrete implementation of the Builder interface for constructing Receipt objects.
 * This class provides methods to set various components of a receipt and build the final Receipt object.
 */
public class ReceiptBuilder implements Builder {
    private String logoPath;
    private String title;
    private String address;
    private String phone;
    private Cart cart;
    private String footer;

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

    @Override
    public Builder addCart(Cart cart) {
        this.cart = cart;
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
        if (logoPath == null || title == null || address == null || phone == null || cart == null) {
            throw new IllegalStateException("Title, address, phone, and cart must be set");
        }
        return new Receipt(logoPath, title, address, phone, cart, footer);
    }
}
