package com.example.springbootapi.api.model.receipt;

import com.example.springbootapi.api.model.receipt.cart.CartObject;
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

    // Fields for discount
    private String discountCode;
    private double discountValue; // Percentage discount

    public PrintReceiptRequest() {
    }

    /**
     * Constructs a new PrintReceiptRequest with the specified payment method and cart objects.
     *
     * @param paymentMethod The method of payment used.
     * @param cartObjects A list of CartObject instances representing items in the cart.
     * @param discountCode The discount code applied.
     * @param discountValue The value of the discount as a percentage.
     */
    public PrintReceiptRequest(String paymentMethod, List<CartObject> cartObjects, String discountCode, double discountValue) {
        this.paymentMethod = paymentMethod;
        this.cartObjects = cartObjects;
        this.discountCode = discountCode;
        this.discountValue = discountValue;
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

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }
}
