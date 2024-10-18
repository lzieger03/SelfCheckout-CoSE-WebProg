package com.example.springbootapi.api.model.receipt.template;

import com.example.springbootapi.api.model.receipt.Receipt;
import com.example.springbootapi.api.model.receipt.cart.CartObject;
import com.example.springbootapi.bonprintextended.POS;
import com.example.springbootapi.bonprintextended.POSBarcode;
import com.example.springbootapi.bonprintextended.POSReceipt;
import com.example.springbootapi.bonprintextended.POSStyle;

/**
 * Utility class for creating a POSReceipt from a Receipt object.
 * This class provides methods to format and add various components
 * of a receipt such as logo, title, items, and payment details.
 */
public class ReceiptTemplate {

    /**
     * Creates a POSReceipt from the given Receipt data.
     *
     * @param receipt The POSReceipt object to populate.
     * @param receiptData The Receipt object containing the data to be added.
     */
    public static void createReceipt(POSReceipt receipt, Receipt receiptData) {
        // Add logo
        receipt.setLogo(receiptData.getLogoPath());

        // Add title (company name)
        receipt.setTitle(receiptData.getTitle());

        // Add address and phone
        receipt.setAddress(receiptData.getAddress());
        receipt.setPhone(receiptData.getPhone());

        // Add separator
        receipt.addSeparator();

        // Add receipt details (date, cashier, etc.)
        receipt.addStyledText("Date: " + getCurrentDate() + "\n", POSStyle.CENTER);
        receipt.addStyledText("Cashier: Self\n", POSStyle.CENTER);
        receipt.addStyledText("Receipt No: " + receiptData.getCart().getCartId() + "\n", POSStyle.CENTER);

        receipt.addSeparator();

        // Add items
        for (CartObject item : receiptData.getCart().getCartObjectList()) {
            receipt.addItem(item.getName(), item.getPrice(), item.getQuantity());
        }

        receipt.addSeparator();

        // Add subtotal, tax, and total
        receipt.addSubTotal(receiptData.getCart().getSubTotalPrice());
        receipt.addTax(receiptData.getCart().getTaxes());
        receipt.addTotal(receiptData.getCart().getTotalPrice());

        // Add payment method
        receipt.addPaymentMethod(receiptData.getCart().getPaymentMethod());

        receipt.addSeparator();

        // Add barcode
        POSBarcode barcode = new POSBarcode(receiptData.getCart().getCartId(), POS.BarcodeType.CODE128);
        receipt.addBarcode(barcode);

        // Add footer
        receipt.setFooterLine(receiptData.getFooter());
    }

    /**
     * Gets the current date formatted as a string.
     *
     * @return The current date as a string.
     */
    private static String getCurrentDate() {
        // Implement date formatting logic here
        return new java.util.Date().toString();
    }
}
