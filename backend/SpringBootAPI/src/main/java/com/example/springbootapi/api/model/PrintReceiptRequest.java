package com.example.springbootapi.api.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO for capturing receipt printing requests.
 */
public class PrintReceiptRequest {
    @NotEmpty(message = "Payment method is required")
    private String paymentMethod;

    @NotNull(message = "Cart objects cannot be null")
    @NotEmpty(message = "Cart must contain at least one item")
    private List<CartObject> cartObjects;

    public PrintReceiptRequest() {
    }

    public PrintReceiptRequest(String paymentMethod, List<CartObject> cartObjects) {
        this.paymentMethod = paymentMethod;
        this.cartObjects = cartObjects;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<CartObject> getCartObjects() {
        return cartObjects;
    }

    public void setCartObjects(List<CartObject> cartObjects) {
        this.cartObjects = cartObjects;
    }
}
