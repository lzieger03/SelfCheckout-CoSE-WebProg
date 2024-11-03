package com.example.springbootapi.service;

import com.example.springbootapi.api.model.receipt.PrintReceiptRequest;
import com.example.springbootapi.api.model.receipt.Receipt;
import com.example.springbootapi.api.model.receipt.ReceiptBuilder;
import com.example.springbootapi.api.model.receipt.cart.Cart;
import com.example.springbootapi.api.model.receipt.cart.CartInterface;
import com.example.springbootapi.api.model.receipt.cart.DiscountCartDecorator;
import com.example.springbootapi.api.model.receipt.template.ReceiptTemplate;
import com.example.springbootapi.bonprintextended.POSPrinter;
import com.example.springbootapi.bonprintextended.POSReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.print.*;

/**
 * Service class responsible for handling receipt-related operations, including
 * converting receipt data to POSReceipt objects and finding print services.
 */
@Service
public class ReceiptService {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);

    /**
     * Creates a Receipt object from a PrintReceiptRequest.
     *
     * @param request The PrintReceiptRequest containing the necessary data.
     * @return A constructed Receipt object.
     */
    public Receipt createReceipt(PrintReceiptRequest request) {
        ReceiptBuilder builder = new ReceiptBuilder();
        CartInterface cart = new Cart(request.getCartObjects(), request.getPaymentMethod());

        if (request.getDiscountCode() != null && !request.getDiscountCode().isEmpty() && request.getDiscountValue() > 0) {
            cart = new DiscountCartDecorator(cart, request.getDiscountCode(), request.getDiscountValue());
        }
        //.setLogo("src/main/resources/static/scanMateLogo.png")
        return builder.setTitle("ScanMate")
                      .setAddress("ScanMate-street 1")
                      .setPhone("+49 123 4567890")
                      .addCart(cart)
                      .addDiscount(request.getDiscountCode(), request.getDiscountValue())
                      .setFooter("Thank you for using ScanMate!")
                      .build();
    }

    /**
     * Converts a Receipt model into a POSReceipt object suitable for printing.
     *
     * @param receipt The Receipt object containing all necessary data.
     */
    public void print(Receipt receipt) {
        try {
            PrintService printService = PrinterSingleton.getInstance().getPrintService();

            if (printService == null) {
                logError("Printer not found!");
                return;
            }

            POSPrinter posPrinter = new POSPrinter();
            POSReceipt posReceipt = new POSReceipt();
            ReceiptTemplate.createReceipt(posReceipt, receipt);
            posPrinter.print(posReceipt, printService);
        } catch (Exception e) {
            logError("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Logs an error message.
     *
     * @param message The error message to log.
     */
    private void logError(String message) {
        logger.error(message);
    }
}
