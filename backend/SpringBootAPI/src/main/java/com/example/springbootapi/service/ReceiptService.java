package com.example.springbootapi.service;

import com.example.springbootapi.api.model.Receipt;
import com.example.springbootapi.bonprintextended.POSDocument;
import com.example.springbootapi.bonprintextended.POSPrinter;
import com.example.springbootapi.bonprintextended.POSReceipt;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private POSReceipt convertToPOSReceipt(Receipt receipt) {
        POSReceipt posReceipt = new POSReceipt();
        posReceipt.setTitle(receipt.getTitle());
        posReceipt.setAddress(receipt.getAddress());
        posReceipt.setPhone(receipt.getPhone());
        receipt.getCart().getCartObjectList().forEach(item -> posReceipt.addItem(item.getName(), item.getPrice()));
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
}
