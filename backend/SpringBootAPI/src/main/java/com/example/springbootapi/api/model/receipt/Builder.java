package com.example.springbootapi.api.model.receipt;

import com.example.springbootapi.api.model.receipt.cart.CartInterface;

/**
 * Builder interface for constructing Receipt objects.
 * This interface defines methods for setting various components of a receipt.
 */
public interface Builder {
    /**
     * Sets the logo path for the receipt.
     *
     * @param logoPath The path to the logo image file.
     * @return The Builder instance for method chaining.
     */
    Builder setLogo(String logoPath);

    /**
     * Sets the title for the receipt.
     *
     * @param title The title or name of the business.
     * @return The Builder instance for method chaining.
     */
    Builder setTitle(String title);

    /**
     * Sets the address for the receipt.
     *
     * @param address The business address.
     * @return The Builder instance for method chaining.
     */
    Builder setAddress(String address);

    /**
     * Sets the phone number for the receipt.
     *
     * @param phone The contact phone number.
     * @return The Builder instance for method chaining.
     */
    Builder setPhone(String phone);

    /**
     * Adds a Cart object to the receipt.
     *
     * @param cart The Cart object containing items and payment information.
     * @return The Builder instance for method chaining.
     */
    Builder addCart(CartInterface cart);

    /**
     * Sets the footer message for the receipt.
     *
     * @param footer A footer message for the receipt.
     * @return The Builder instance for method chaining.
     */
    Builder setFooter(String footer);

    /**
     * Builds and returns a Receipt object with the configured properties.
     *
     * @return A fully constructed Receipt object.
     */
    Receipt build();

    Builder addDiscount(String discountCode, double discountValue);
}
