package com.example.springbootapi.api.model.receipt.cart;

import java.util.List;

public interface CartInterface {
    String getCartId();
    List<CartObject> getCartObjectList();
    String getPaymentMethod();
    double getSubTotalPrice();
    double getTaxes();
    double getTotalPrice();
    
    void applyDiscount(String discountCode, double discountValue);
    
    String getDiscountCode();
    double getDiscountValue();
}
