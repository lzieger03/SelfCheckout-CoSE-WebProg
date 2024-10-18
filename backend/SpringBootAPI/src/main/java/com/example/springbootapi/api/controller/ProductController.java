package com.example.springbootapi.api.controller;

import com.example.springbootapi.api.model.product.Product;
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

/**
 * REST controller for handling product-related operations.
 * Provides endpoints to retrieve product information.
 */
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501") // Change this to the port of the frontend
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    /**
     * Constructs a new ProductController with the specified ProductService.
     *
     * @param productService The ProductService to be used for product operations.
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Endpoint to retrieve a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return A ResponseEntity containing the Product if found, or a not found response.
     */
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

    /**
     * Logs error events with class name, method name, and exception details.
     *
     * @param className The name of the class where the error occurred.
     * @param methodName The name of the method where the error occurred.
     * @param e The exception that was thrown.
     */
    public void logEvents(String className, String methodName, Exception e) {
        logger.error("Error occurred in Class {} in Method {}: {}",
                className,
                methodName,
                e.getMessage()
        );
    }
}
