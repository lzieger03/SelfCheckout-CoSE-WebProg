package com.example.springbootapi.api.controller;

import com.example.springbootapi.api.model.Product;
import com.example.springbootapi.api.model.Receipt;
import com.example.springbootapi.api.model.ReceiptBuilder;
import com.example.springbootapi.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ReceiptController {
    private final ReceiptService receiptService;

    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/print")
    public String printReceipt(@RequestBody Product[] products) {
        try {
            ReceiptBuilder builder = new ReceiptBuilder();
            builder.setTitle("ScanMate")
                   .setAddress("ScanMate-street 1")
                   .setPhone("+49 123 4567890");

            for (Product product : products) {
                builder.addProduct(product);
            }

            builder.setFooter("Thank you for shopping!");
            Receipt receipt = builder.build();

            receiptService.print(receipt);
            return "Print request successful";
        } catch (Exception e) {
            e.printStackTrace();
            return "Print request failed";
        }
    }
}
