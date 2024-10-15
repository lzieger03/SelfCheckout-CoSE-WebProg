package com.example.springbootapi.api.model;

public interface Builder {
    Builder setTitle(String title);
    Builder setAddress(String address);
    Builder setPhone(String phone);
    Builder addCart(Cart cart);
    Builder setFooter(String footer);
    Receipt build();
}
