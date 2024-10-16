package com.example.springbootapi.api.controller;

import com.example.springbootapi.api.model.receipt.PrintReceiptRequest;
import com.example.springbootapi.api.model.receipt.Receipt;
import com.example.springbootapi.service.ReceiptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for handling receipt-related operations.
 * Provides endpoints to create and print receipts.
 */
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501") // Change this to the port of the frontend
public class ReceiptController {
    private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);
    private final ReceiptService receiptService;

    /**
     * Constructs a new ReceiptController with the specified ReceiptService.
     *
     * @param receiptService The ReceiptService to be used for receipt operations.
     */
    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    /**
     * Endpoint to create and print a receipt based on the provided cart and payment method.
     *
     * @param request The PrintReceiptRequest containing payment method and cart objects.
     * @param bindingResult The result of validation.
     * @return A ResponseEntity containing a success message or an error message.
     */
    @PostMapping("/print")
    public ResponseEntity<Map<String, String>> printReceipt(
            @Valid @RequestBody PrintReceiptRequest request,
            BindingResult bindingResult) {
        Map<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                response.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            Receipt receipt = receiptService.createReceipt(request);
            receiptService.print(receipt);

            response.put("message", "Print successful");
            return ResponseEntity.ok(response);
        } catch (Exception err) {
            logError("Error during receipt printing", err);
            response.put("error", err.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Logs an error message with the provided exception details.
     *
     * @param message The error message to log.
     * @param e The exception that caused the error.
     */
    private void logError(String message, Exception e) {
        logger.error("{}: {}", message, e.getMessage());
    }
}
