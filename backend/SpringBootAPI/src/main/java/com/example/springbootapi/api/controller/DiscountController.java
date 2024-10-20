package com.example.springbootapi.api.controller;

import com.example.springbootapi.api.model.discount.Discount;
import com.example.springbootapi.service.DiscountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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
     * Endpoint to retrieve all discounts.
     *
     * @return A ResponseEntity containing a list of all Discounts.
     */
    @GetMapping("/allcoupons") // Corrected endpoint name
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        try {
            return ResponseEntity.ok(discountService.getAllDiscounts());
        } catch (Exception err) {
            logEvents(this.getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), err);
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }

    /**
     * Endpoint to create a new discount.
     *
     * @param code  The code of the discount to create.
     * @param value The value of the discount to create.
     * @return A ResponseEntity containing a success message if the discount is created, or an error message if not.
     */
    @PostMapping("/discount")
    public ResponseEntity<String> createDiscount(@RequestParam String code, @RequestParam double value) {
        try {
            boolean isCreated = discountService.createDiscount(code, value);
            if (isCreated) {
                return ResponseEntity.ok("Discount created successfully.");
            } else {
                return ResponseEntity.status(500).body("Failed to create discount.");
            }
        } catch (Exception err) {
            logEvents(
                    this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(),
                    err
            );
            return ResponseEntity.status(500).body("Internal server error.");
        }
    }

    @PutMapping("/discount")
    public ResponseEntity<String> updateDiscount(@RequestParam String code, @RequestParam double value) {
        try {
            boolean isUpdated = discountService.updateDiscount(code, value);
            if (isUpdated) {
                return ResponseEntity.ok("Discount updated successfully.");
            } else {
                return ResponseEntity.status(404).body("Discount not found.");
            }
        } catch (Exception err) {
            logEvents(
                    this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(),
                    err
            );
        }
        return ResponseEntity.status(500).body("Internal server error.");
    }

    /**
     * Endpoint to delete a discount by its code.
     *
     * @param code The code of the discount to delete.
     * @return A ResponseEntity indicating the result of the deletion.
     */
    @DeleteMapping("/discount")
    public ResponseEntity<String> deleteDiscount(@RequestParam String code) {
        try {
            boolean isDeleted = discountService.deleteDiscount(code);
            if (isDeleted) {
                return ResponseEntity.ok("Discount deleted successfully.");
            } else {
                return ResponseEntity.status(404).body("Discount not found.");
            }
        } catch (Exception err) {
            logEvents(
                    this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(),
                    err
            );
            return ResponseEntity.status(500).body("Internal server error.");
        }
    }

    /**
     * Logs error events with class name, method name, and exception details.
     *
     * @param className  The name of the class where the error occurred.
     * @param methodName The name of the method where the error occurred.
     * @param e          The exception that was thrown.
     */
    public void logEvents(String className, String methodName, Exception e) {
        logger.error("Error occurred in Class {} in Method {}: {}",
                className,
                methodName,
                e.getMessage()
        );
    }
}
