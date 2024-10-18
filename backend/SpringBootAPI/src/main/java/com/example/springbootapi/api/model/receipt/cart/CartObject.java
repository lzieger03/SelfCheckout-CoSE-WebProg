package com.example.springbootapi.api.model.receipt.cart;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an object in the shopping cart.
 * This class encapsulates the details of a single item in the cart,
 * including its ID, name, price, and quantity.
 */
public class CartObject {
    private String id;
    private String name;
    private double price;
    private int quantity;

    /**
     * Default constructor for CartObject.
     */
    public CartObject() {
    }

    /**
     * Constructs a CartObject with specified details.
     *
     * @param id       The unique identifier of the cart object.
     * @param name     The name of the item.
     * @param price    The price of the item.
     * @param quantity The quantity of the item in the cart.
     */
    public CartObject(String id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Gets the ID of the cart object.
     *
     * @return The ID of the cart object.
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the cart object.
     *
     * @param id The ID to set.
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the item.
     *
     * @return The name of the item.
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the item.
     *
     * @param name The name to set.
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of the item.
     *
     * @return The price of the item.
     */
    @JsonProperty("price")
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the item.
     *
     * @param price The price to set.
     */
    @JsonProperty("price")
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the quantity of the item in the cart.
     *
     * @return The quantity of the item.
     */
    @JsonProperty("quantity")
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the item in the cart.
     *
     * @param quantity The quantity to set.
     */
    @JsonProperty("quantity")
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
