package com.example.springbootapi.api.controller;

import com.example.springbootapi.api.model.discount.Discount;
import com.example.springbootapi.service.DiscountService;
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
 * REST controller for handling discount-related operations.
 * Provides endpoints to retrieve discount information.
 */
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501") // Change this to the port of the frontend
public class DiscountController {
    private static final Logger logger = LoggerFactory.getLogger(DiscountController.class);
    private final DiscountService discountService;

    /**
     * Constructs a new DiscountController with the specified DiscountService.
     *
     * @param discountService The DiscountService to be used for discount operations.
     */
    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    /**
     * Endpoint to retrieve a discount by its code.
     *
     * @param code The code of the discount to retrieve.
     * @return A ResponseEntity containing the Discount if found, or a not found response.
     */
    @GetMapping("/discount")
    public ResponseEntity<Discount> getDiscount(@RequestParam String code) {
        try {
            Optional<Discount> discount = discountService.getDiscountByCode(code);
            return discount.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception err) {
            logEvents(
                    this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(),
                    err
            );
            return ResponseEntity.status(500).body(new Discount(err.getMessage(), 0.0));
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
