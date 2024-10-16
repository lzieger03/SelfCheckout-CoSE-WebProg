package com.example.springbootapi.api.controller;

import com.example.springbootapi.api.model.*;
import com.example.springbootapi.service.ProductService;
import com.example.springbootapi.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501") //change this to the port of the frontend
public class ReceiptController {
    private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);
    private final ReceiptService receiptService;

    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/print")
    public ResponseEntity<Map<String, String>> printReceipt(@RequestBody List<CartObject> cartObjects) {
        Map<String, String> response = new HashMap<>();
        // Logging the received products
        cartObjects.forEach(cartObject ->  logger.info(
            "Received CartObject: id:{} name:{} price:{} quantity:{}",
                cartObject.getId(),
                cartObject.getName(),
                cartObject.getPrice(),
                cartObject.getQuantity()
        ));

        try {
            ReceiptBuilder builder = new ReceiptBuilder();
            builder.setTitle("ScanMate")
                   .setAddress("ScanMate-street 1")
                   .setPhone("+49 123 4567890")
                    .addCart(new Cart(cartObjects));

            builder.setFooter("Thank you for shopping!");
            Receipt receipt = builder.build();

            receiptService.print(receipt);

            response.put("message", "print successful");
            return ResponseEntity.ok(response);
        } catch (Exception err) {
            logEvents(
                    this.getClass().getName(),
                    this.getClass().getEnclosingMethod().getName(),
                    err
            );
            response.put("error", err.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    public void logEvents(String className, String methodName, Exception e) {
        logger.info("Error occurred in Class {} in Method {}: {}",
                className,
                methodName,
                e.getMessage()
        );
    }
}
