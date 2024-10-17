package com.example.springbootapi.bonprintextended;

public class POSReceipt extends POSDocument {
    private String title;
    private String address;
    private String phone;

    public void setTitle(String title) {
        this.title = title;
        addStyle(POSStyle.BOLD);
        addStyle(POSStyle.BIG);
        addStyle(POSStyle.CENTER);
        addComponent(() -> (title + "\n\n").getBytes());
        resetStyle();
    }

    public void setAddress(String address) {
        this.address = address;
        addComponent(() -> (address + "\n").getBytes());
    }

    public void setPhone(String phone) {
        this.phone = phone;
        addComponent(() -> (phone + "\n").getBytes());
        addFeed(1);
    }

    public void addItem(String itemName, double price, int quantity) {
        addComponent(() -> String.format("%-2d * %-40s $%5.2f\n", quantity, itemName, price).getBytes());
    }

    public void addSubTotal(double subTotal) {
        // Feed a bit before printing the total
        addFeed(2);
        addStyle(POSStyle.BIG);
        addStyle(POSStyle.RIGHT);
        addComponent(() -> String.format("SubTotal: $%-15.2f\n", subTotal).getBytes());
        resetStyle(); // Reset after total
    }

    public void addTax(double tax) {
        // Feed a bit before printing the total
        addFeed(0);
        addStyle(POSStyle.SMALL);
        addStyle(POSStyle.RIGHT);
        addComponent(() -> String.format("Tax: $%15.2f\n", tax).getBytes());
        resetStyle(); // Reset after total
    }

    public void addTotal(double total) {
        // Feed a bit before printing the total
        addFeed(0);
        addStyle(POSStyle.BOLD);
        addStyle(POSStyle.RIGHT);
        addComponent(() -> String.format("Total: $%15.2f\n", total).getBytes());
        resetStyle(); // Reset after total
    }

    public void addPaymentMethod(String paymentMethod) {
        addFeed(2);
        addComponent((paymentMethod + "\n")::getBytes);
    }

    public void addBarcode(POSBarcode barcode) {
        addFeed(2);
        barcode.setHeight(162);
        barcode.setWidth(POS.BarWidth.DEFAULT);
        addComponent(barcode);
        addComponent(() -> (barcode.getData() + "\n").getBytes()); // print productID directly after barcode
        // addText(barcode.getData()); // <-- same as the above
    }

    public void setFooterLine(String footer) {
        addFeed(1);
        addComponent(() -> (footer + "\n\n").getBytes());
        addFeed(2);
    }
}
