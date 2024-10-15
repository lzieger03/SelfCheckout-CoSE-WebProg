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
@CrossOrigin(origins = "http://127.0.0.1:5501") //change this to the port of the frontend
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/product") // Entrypoint of API https://api.url/product
    public Product getProduct(@RequestParam String id){ // @RequestParam is the API Parameter e.g.: .../product?id=123
        try {
            Optional product = productService.getProduct(id); // Optional because product could be empty (workaround)
            if (product.isPresent()){
                return (Product) product.get(); // Parse Optional to Product if possible (workaround)
            }
        } catch (Exception err) {
            System.out.println("--------/product---------" + err);
            return new Product("1", err.getMessage(), 0.0);
        }
        return null;
    }
}
