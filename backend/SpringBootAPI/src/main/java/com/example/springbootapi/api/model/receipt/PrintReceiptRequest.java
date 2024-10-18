package com.example.springbootapi.api.model.receipt;

import com.example.springbootapi.api.model.receipt.cart.CartObject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Data Transfer Object (DTO) for capturing receipt printing requests.
 * This class encapsulates the payment method and cart objects required for printing a receipt.
 */
public class PrintReceiptRequest {
    @NotEmpty(message = "Payment method is required")
    private String paymentMethod;

    @NotNull(message = "Cart objects cannot be null")
    @NotEmpty(message = "Cart must contain at least one item")
    private List<CartObject> cartObjects;

    public PrintReceiptRequest() {
    }

    /**
     * Constructs a new PrintReceiptRequest with the specified payment method and cart objects.
     *
     * @param paymentMethod The method of payment used.
     * @param cartObjects A list of CartObject instances representing items in the cart.
     */
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
