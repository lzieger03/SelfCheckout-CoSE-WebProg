package com.example.springbootapi.api.model.receipt.template;

import com.example.springbootapi.api.model.receipt.Receipt;
import com.example.springbootapi.api.model.receipt.cart.CartInterface;
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
     * @param receipt     The POSReceipt object to populate.
     * @param receiptData The Receipt object containing the data to be added.
     */
    public static void createReceipt(POSReceipt receipt, Receipt receiptData) {
        // Add logo
        receipt.setLogo(receiptData.getLogoPath());

        // Add title (company name)
        receipt.setTitle(receiptData.getTitle());

        // Add address
        receipt.setAddress(receiptData.getAddress());

        // Add phone
        receipt.setPhone(receiptData.getPhone());

        // Add separator
        receipt.addSeparator();

        // Add receipt details (date, cashier, etc.)
        receipt.addStyledText("Date:\n" + getCurrentDate() + "\n", POSStyle.CENTER);
        receipt.addStyledText("Cashier: Self\n", POSStyle.CENTER);
        receipt.addStyledText("Receipt No: " + receiptData.getCart().getCartId() + "\n", POSStyle.CENTER);

        receipt.addSeparator();

        // Add items
        CartInterface cart = receiptData.getCart();
        for (var item : cart.getCartObjectList()) {
            receipt.addItem(item.getName(), item.getPrice(), item.getQuantity());
//            if (item.getDiscountAmount() > 0) {
//                receipt.addDiscountToItem(String.format("Discount on %s:", item.getName()), item.getDiscountAmount());
//            }
        }

        receipt.addSeparator();

        receipt.addDiscount(receiptData.getDiscountCode(), receiptData.getDiscountValue(), cart.getPriceBeforeDiscount());

        // Add subtotal
        receipt.addSubTotal(cart.getSubTotalPrice());

        // Add tax
        receipt.addTax(cart.getTaxes());

        // Add total
        receipt.addTotal(cart.getTotalPrice());

        // Add payment method
        receipt.addPaymentMethod(cart.getPaymentMethod());

        // Add separator
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
