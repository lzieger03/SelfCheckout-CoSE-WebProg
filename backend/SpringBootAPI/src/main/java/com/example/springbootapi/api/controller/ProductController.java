package com.example.springbootapi.api.controller;

import com.example.springbootapi.api.model.Product;
import com.example.springbootapi.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501") // Change this to the port of the frontend
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product") // Entrypoint of API https://api.url/product
    public ResponseEntity<Product> getProduct(@RequestParam String id) { // @RequestParam is the API Parameter e.g.: .../product?id=123
        try {
            Optional<Product> product = productService.getProduct(id);
            if (product.isPresent()) {
                return ResponseEntity.ok(product.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception err) {
            logEvents(
                    this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(),
                    err
            );
            return ResponseEntity.status(500).body(new Product("1", err.getMessage(), 0.0));
        }
    }

    public void logEvents(String className, String methodName, Exception e) {
        logger.error("Error occurred in Class {} in Method {}: {}",
                className,
                methodName,
                e.getMessage()
        );
    }
}
