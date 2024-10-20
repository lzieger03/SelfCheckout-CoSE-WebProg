package com.example.springbootapi.api.model.product;

/**
 * Product Class to create a Product-Element to send over API
 */
public class Product {
    private String id;
    private String name;
    private double price;
    private byte[] itemImage; // Image data as byte array

    public Product() {
        // Default constructor
    }

    public Product(String id, String name, double price, byte[] itemImage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.itemImage = itemImage;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public byte[] getItemImage() {
        return itemImage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setItemImage(byte[] itemImage) {
        this.itemImage = itemImage;
    }
}
