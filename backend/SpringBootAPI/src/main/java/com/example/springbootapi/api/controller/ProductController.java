package com.example.springbootapi.api.controller;

import com.example.springbootapi.api.model.Product;
import com.example.springbootapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/product")
    public Product getProduct(@RequestParam String id){
        try {
            Optional product = productService.getProduct(id);
            if (product.isPresent()){
                return (Product) product.get();
            }
        } catch (Exception err) {
            System.out.println("--------/product---------" + err);
            return new Product("1", err.getMessage(), 0.0);
        }
        return null;
    }
}
