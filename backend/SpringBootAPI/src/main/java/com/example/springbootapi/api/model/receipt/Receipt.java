package com.example.springbootapi.api.model.receipt;

import com.example.springbootapi.api.model.receipt.cart.Cart;
import com.example.springbootapi.api.model.receipt.cart.CartInterface;
import lombok.Setter;

/**
 * Represents a receipt with all necessary information for printing.
 * This class encapsulates details such as logo, title, address, phone number,
 * cart contents, and a footer message.
 */
public class Receipt {
    private final String logoPath;
    private final String title;
    private final String address;
    private final String phone;
    @Setter
    private CartInterface cart;
    private final String footer;

    /**
     * Constructs a new Receipt with all required fields.
     *
     * @param logoPath The path to the logo image file.
     * @param title The title or name of the business.
     * @param address The business address.
     * @param phone The contact phone number.
     * @param cart The Cart object containing items and payment information.
     * @param footer A footer message for the receipt.
     */
    public Receipt(String logoPath, String title, String address, String phone, CartInterface cart, String footer) {
        this.logoPath = logoPath;
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.cart = cart;
        this.footer = footer;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public CartInterface getCart() {
        return cart;
    }

    public String getFooter() {
        return footer;
    }
}
