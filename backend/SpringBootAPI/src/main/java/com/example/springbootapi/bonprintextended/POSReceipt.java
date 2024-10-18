package com.example.springbootapi.bonprintextended;

import java.io.IOException;

public class POSReceipt extends POSDocument {
    private String title;
    private String address;
    private String phone;

    public void setLogo(String filePath) {
        try {
            addLogo(new POSLogo(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTitle(String title) {
        this.title = title;
        addStyle(POSStyle.BOLD, POSStyle.BIG, POSStyle.CENTER);
        addStyledText(title + "\n\n");
        resetStyle();
    }

    public void setAddress(String address) {
        this.address = address;
        addStyle(POSStyle.CENTER);
        addStyledText(address + "\n");
        resetStyle();
    }

    public void setPhone(String phone) {
        this.phone = phone;
        addStyle(POSStyle.CENTER);
        addStyledText(phone + "\n");
        addFeed(1);
        resetStyle();
    }

    public void addItem(String itemName, double price, int quantity) {
        addStyle(POSStyle.CENTER);
        String itemLine = String.format("%-2d * %-20s $%5.2f\n", quantity, itemName, price);
        addStyledText(itemLine);
        resetStyle();
    }

    public void addSubTotal(double subTotal) {
        addFeed(2);
        addStyle(POSStyle.BIG, POSStyle.RIGHT);
        String subTotalLine = String.format("%-15s $%5.2f\n", "Subtotal:", subTotal);
        addStyledText(subTotalLine);
        resetStyle();
    }

    public void addTax(double tax) {
        addFeed(0);
        addStyle(POSStyle.SMALL, POSStyle.RIGHT);
        String taxLine = String.format("%-15s $%5.2f\n", "Tax:", tax);
        addStyledText(taxLine);
        resetStyle();
    }

    public void addTotal(double total) {
        addFeed(0);
        addStyle(POSStyle.BOLD, POSStyle.RIGHT);
        String totalLine = String.format("%-15s $%5.2f\n", "Total:", total);
        addStyledText(totalLine);
        resetStyle();
    }

    public void addPaymentMethod(String paymentMethod) {
        addFeed(1);
        addStyle(POSStyle.RIGHT);
        addStyledText(paymentMethod + "\n");
        resetStyle();
    }

    public void addBarcode(POSBarcode barcode) {
        addFeed(2);
        addStyle(POSStyle.CENTER);
        barcode.setHeight(162);
        barcode.setWidth(POS.BarWidth.DEFAULT);
        addComponent(barcode);
        addStyledText(barcode.getData() + "\n");
        resetStyle();
    }

    public void setFooterLine(String footer) {
        addFeed(1);
        addStyle(POSStyle.CENTER);
        addStyledText(footer + "\n\n");
        addFeed(2);
        resetStyle();
    }

    /**
     * Helper method to add multiple styles at once.
     */
    private void addStyle(POSStyle... styles) {
        for (POSStyle style : styles) {
            super.addStyle(style);
        }
    }

    /**
     * Helper method to add styled text.
     */
    private void addStyledText(String text) {
        addComponent(() -> text.getBytes());
    }
}
