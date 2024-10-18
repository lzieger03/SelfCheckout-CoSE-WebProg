package com.example.springbootapi.api.model.receipt.cart;

import java.util.List;
import java.util.Random;

/**
 * Represents a shopping cart containing cart objects, payment information, and price calculations.
 */
public class Cart {
    private String cartId;
    private List<CartObject> cartObjectList;
    private String paymentMethod;
    private double subTotalPrice;
    private double taxes;
    private double totalPrice;

    /**
     * Constructs a new Cart with the given cart objects and payment method.
     *
     * @param cartObjectList The list of CartObject items in the cart.
     * @param paymentMethod The method of payment used for this cart.
     */
    public Cart(List<CartObject> cartObjectList, String paymentMethod) {
        this.cartId = createCartId();
        this.cartObjectList = cartObjectList;
        this.paymentMethod = paymentMethod;
        subTotalPrice = calculateSubTotalPrice();
        taxes = subTotalPrice * 0.19;
        totalPrice = subTotalPrice + taxes;
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
     * Calculates the subtotal price of all items in the cart.
     *
     * @return The subtotal price.
     */
    private double calculateSubTotalPrice() {
        double subTotal = 0;
        for(CartObject cartObject : cartObjectList) {
            subTotal += cartObject.getPrice();
        }
        return subTotal;
    }

    /**
     * Gets the unique cart ID.
     *
     * @return The cart ID.
     */
    public String getCartId() {
        return cartId;
    }

    /**
     * Gets the list of cart objects in this cart.
     *
     * @return The list of CartObject items.
     */
    public List<CartObject> getCartObjectList() {
        return cartObjectList;
    }

    /**
     * Gets the payment method used for this cart.
     *
     * @return The payment method.
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Gets the subtotal price of all items in
     *
     * @return The subtotal price.
     */
    public double getSubTotalPrice() {
        return subTotalPrice;
    }

    /**
     * Gets the calculated taxes for this cart.
     *
     * @return The tax amount.
     */
    public double getTaxes() {
        return taxes;
    }

    /**
     * Gets the total price of all items in the cart.
     *
     * @return The total price.
     */
    public double getTotalPrice() {
        return totalPrice;
    }
}
