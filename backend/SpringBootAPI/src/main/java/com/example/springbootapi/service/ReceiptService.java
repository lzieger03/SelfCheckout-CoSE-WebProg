package com.example.springbootapi.service;

import com.example.springbootapi.api.model.Receipt;
import com.example.springbootapi.bonprintextended.POSDocument;
import com.example.springbootapi.bonprintextended.POSPrinter;
import com.example.springbootapi.bonprintextended.POSReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

@Service
public class ReceiptService {

    public void print(Receipt receipt) {
        try {
            // Find the printer by name
            ReceiptService receiptService = findPrintService("OLIVETTI PRT80");

            if (receiptService == null) {
                System.out.println("Printer not found");
                return;
            }

            // Create a new POSPrinter instance
            POSPrinter posPrinter = new POSPrinter();

            // Convert Receipt to POSReceipt
            POSReceipt posReceipt = convertToPOSReceipt(receipt);

            // Print the receipt using the POSPrinter
            posPrinter.print((POSDocument) posReceipt, (PrintService) receiptService);
        } catch (Exception err) {
            logEvents(
                    this.getClass().getName(),
                    this.getClass().getEnclosingMethod().getName(),
                    err
            );
        }
    }

    private POSReceipt convertToPOSReceipt(Receipt receipt) {
        POSReceipt posReceipt = new POSReceipt();
        posReceipt.setTitle(receipt.getTitle());
        posReceipt.setAddress(receipt.getAddress());
        posReceipt.setPhone(receipt.getPhone());
        receipt.getCart().getCartObjectList().forEach(item ->
                posReceipt.addItem(
                        item.getName(),
                        item.getPrice(),
                        item.getQuantity()
                ));
        posReceipt.setFooterLine(receipt.getFooter());
        return posReceipt;
    }

    private ReceiptService findPrintService(String printerName) {
        ReceiptService[] services = (ReceiptService[]) PrintServiceLookup.lookupPrintServices(null, null);
        for (ReceiptService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }
        return null;
    }

    private String getName() {
        return "OLIVETTI";
    }

    public void logEvents(String className, String methodName, Exception e) {
        Logger logger = LoggerFactory.getLogger(ReceiptService.class);
        logger.info("Error occurred in Class {} in Method {}: {}",
                className,
                methodName,
                e.getMessage()
        );
    }
}
