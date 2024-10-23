package com.example.springbootapi.bonprintextended;

import java.io.IOException;

public class POSReceipt extends POSDocument {
    private String title;
    private String address;
    private String phone;

    public void setLogo(String filePath) {
//        try {
//            addLogo(new POSLogo(filePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void setTitle(String title) {
        this.title = title;
        addStyle(POSStyle.BOLD, POSStyle.BIG, POSStyle.CENTER);
        addText(title + "\n\n");
        resetStyle();
    }

    public void setAddress(String address) {
        this.address = address;
        addStyle(POSStyle.CENTER);
        addText(address + "\n");
        resetStyle();
    }

    public void setPhone(String phone) {
        this.phone = phone;
        addStyle(POSStyle.CENTER);
        addText(phone + "\n");
        addFeed(1);
        resetStyle();
    }

    public void addItem(String itemName, double price, int quantity) {
        addStyle(POSStyle.LEFT);
        String itemLine = String.format("%d x %-20s $%5.2f\n", quantity, itemName, price);
        addText(itemLine);
        resetStyle();
    }

    /**
     * Adds a discount line under an item.
     *
     * @param discountDescription Description of the discount.
     * @param discountAmount Amount discounted.
     */
    public void addDiscountToItem(String discountDescription, double discountAmount) {
        addStyle(POSStyle.SMALL, POSStyle.LEFT);
        String discountLine = String.format("%s -$%5.2f\n", discountDescription, discountAmount);
        addText(discountLine);
        resetStyle();
    }

    public void addSubTotal(double subTotal) {
        addFeed(1);
        addStyle(POSStyle.BOLD, POSStyle.RIGHT);
        String subTotalLine = String.format("%-15s $%5.2f\n", "Subtotal:", subTotal);
        addText(subTotalLine);
        resetStyle();
    }

    public void addTax(double tax) {
        addFeed(0);
        addStyle(POSStyle.SMALL, POSStyle.RIGHT);
        String taxLine = String.format("%-15s $%5.2f\n", "Tax:", tax);
        addText(taxLine);
        resetStyle();
    }

    public void addTotal(double total) {
        addFeed(0);
        addStyle(POSStyle.BOLD, POSStyle.RIGHT);
        String totalLine = String.format("%-15s $%5.2f\n", "Total:", total);
        addText(totalLine);
        resetStyle();
    }

    public void addPaymentMethod(String paymentMethod) {
        addFeed(1);
        addStyle(POSStyle.RIGHT);
        addText(paymentMethod + "\n");
        resetStyle();
    }

    public void addBarcode(POSBarcode barcode) {
        addFeed(2);
        addStyle(POSStyle.CENTER);
        barcode.setHeight(162);
        barcode.setWidth(POS.BarWidth.DEFAULT);
        addComponent(barcode);
        addText(barcode.getData() + "\n");
        resetStyle();
    }

    public void setFooterLine(String footer) {
        addFeed(1);
        addStyle(POSStyle.CENTER);
        addText(footer + "\n\n");
        addFeed(2);
        resetStyle();
    }

    public void addSeparator() {
        addText("--------------------------------\n");
    }

    /**
     * Helper method to add styled text.
     * @param text Text to add to the Receipt
     */
    public void addText(String text) {
        addComponent(text::getBytes);
    }

    public void addStyledText(String text, POSStyle... styles) {
        addStyle(styles);
        addComponent(text::getBytes);
    }

    /**
     * Helper method to add multiple styles at once.
     */
    private void addStyle(POSStyle... styles) {
        for (POSStyle style : styles) {
            super.addStyle(style);
        }
    }
}
