package com.example.springbootapi.api.controller;

import com.example.springbootapi.api.model.Product;
import com.example.springbootapi.api.model.Receipt;
import com.example.springbootapi.api.model.ReceiptBuilder;
import com.example.springbootapi.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501") //change this to the port of the frontend
public class ReceiptController {
    private final ReceiptService receiptService;

    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/print")
    public ResponseEntity<Map<String, String>> printReceipt(@RequestBody List<Product> products) {
        Map<String, String> response = new HashMap<>();
        // Logging the received products
        products.forEach(product -> System.out.println(product.toString()));

        // Returning a JSON response instead of a plain string
        //response.put("message", products.toString());
        return ResponseEntity.ok(response);
        /*try {
            ReceiptBuilder builder = new ReceiptBuilder();
            builder.setTitle("ScanMate")
                   .setAddress("ScanMate-street 1")
                   .setPhone("+49 123 4567890");

            for (String product : products) {
                builder.addProduct(new Product(product, "", 0.0));
            }

            builder.setFooter("Thank you for shopping!");
            Receipt receipt = builder.build();

            receiptService.print(receipt);
            return "Print request successful";
        } catch (Exception e) {
            e.printStackTrace();
            return "Print request failed";
        }*/
    }
}
