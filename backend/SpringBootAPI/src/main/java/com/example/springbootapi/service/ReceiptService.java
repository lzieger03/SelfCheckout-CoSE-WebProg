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

    private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);

    public void print(Receipt receipt) {
        try {
            PrintService printService = findPrintService("OLIVETTI PRT80");

            if (printService == null) {
                logError("Printer not found!");
                return;
            }

            POSPrinter posPrinter = new POSPrinter();
            POSReceipt posReceipt = convertToPOSReceipt(receipt);
            posPrinter.print(posReceipt, printService);
        } catch (PrintException e) {
            logError("Printing error: " + e.getMessage());
        } catch (Exception e) {
            logError("Unexpected error: " + e.getMessage());
        }
    }

    private POSReceipt convertToPOSReceipt(Receipt receipt) {
        POSReceipt posReceipt = new POSReceipt();
        Cart cart = receipt.getCart();
        posReceipt.setTitle(receipt.getTitle());
        posReceipt.setAddress(receipt.getAddress());
        posReceipt.setPhone(receipt.getPhone());
        cart.getCartObjectList().forEach(item ->
                posReceipt.addItem(item.getName(), item.getPrice(), item.getQuantity()));
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

    private void logError(String message) {
        logger.error(message);
    }
}
