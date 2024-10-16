package com.example.springbootapi.service;

import com.example.springbootapi.api.model.Cart;
import com.example.springbootapi.api.model.Receipt;
import com.example.springbootapi.bonprintextended.POS;
import com.example.springbootapi.bonprintextended.POSBarcode;
import com.example.springbootapi.bonprintextended.POSPrinter;
import com.example.springbootapi.bonprintextended.POSReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.print.*;


@Service
public class ReceiptService {

    public void print(Receipt receipt) {
        try {
            // Find the printer by name
            PrintService printService = findPrintService("OLIVETTI PRT80");

            if (printService == null) {
                logEvents(
                        this.getClass().getSimpleName(),
                        new Throwable().getStackTrace()[0].getMethodName(),
                        "Printer not found!"
                );
                return;
            }

            // Create a new POSPrinter instance
            POSPrinter posPrinter = new POSPrinter();

            // Convert Receipt to POSReceipt
            POSReceipt posReceipt = convertToPOSReceipt(receipt);

            // Print the receipt using the POSPrinter
            posPrinter.print(posReceipt, printService);
        } catch (Exception err) {
            logEvents(
                    this.getClass().getSimpleName(),
                    new Throwable().getStackTrace()[0].getMethodName(),
                    err.getMessage()
            );
        }
    }

    private POSReceipt convertToPOSReceipt(Receipt receipt) {
        POSReceipt posReceipt = new POSReceipt();
        Cart cart = receipt.getCart();
        posReceipt.setTitle(receipt.getTitle());
        posReceipt.setAddress(receipt.getAddress());
        posReceipt.setPhone(receipt.getPhone());
        receipt.getCart().getCartObjectList().forEach(item ->
                posReceipt.addItem(
                        String.valueOf(item.getName()),
                        item.getPrice(),
                        item.getQuantity()
                ));
        posReceipt.addSubTotal(cart.getSubTotalPrice());
        posReceipt.addTax(cart.getTaxes());
        posReceipt.addTotal(cart.getTotalPrice());
        posReceipt.addPaymentMethod(cart.getPaymentMethod());
        posReceipt.addBarcode(new POSBarcode(cart.getCartId(), POS.BarcodeType.CODE128));
        posReceipt.setFooterLine(receipt.getFooter());
        return posReceipt;
    }

    private PrintService findPrintService(String printerName) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }
        return null;
    }

    private String getName() {
        return "OLIVETTI";
    }

    public void logEvents(String className, String methodName, String eMessage) {
        Logger logger = LoggerFactory.getLogger(ReceiptService.class);
        logger.info("Error occurred in Class {} in Method {}: {}",
                className,
                methodName,
                eMessage
        );
    }
}
