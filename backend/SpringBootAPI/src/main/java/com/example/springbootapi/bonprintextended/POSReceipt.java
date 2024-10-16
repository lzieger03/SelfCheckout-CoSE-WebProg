package com.example.springbootapi.bonprintextended;

public class POSReceipt extends POSDocument {
    private String title;
    private String address;
    private String phone;

    public void setTitle(String title) {
        this.title = title;
        addStyle(POSStyle.BOLD);
        addStyle(POSStyle.BIG);
        addComponent(() -> (title + "\n").getBytes());
        resetStyle();
    }

    public void setAddress(String address) {
        this.address = address;
        addComponent(() -> (address + "\n").getBytes());
    }

    public void setPhone(String phone) {
        this.phone = phone;
        addComponent(() -> (phone + "\n").getBytes());
    }

    public void addItem(String itemName, double price, int quantity) {
        addComponent(() -> String.format("%-5d * %-20s $%10.2f\\n", quantity, itemName, price).getBytes());
    }

    public void addSubTotal(double subTotal) {
        // Feed a bit before printing the total
        addFeed(2);
        addStyle(POSStyle.BIG);
        addComponent(() -> String.format("SubTotal: %10.2f\n", subTotal).getBytes());
        resetStyle(); // Reset after total
    }

    public void addTax(double tax) {
        // Feed a bit before printing the total
        addFeed(0);
        addStyle(POSStyle.SMALL);
        addComponent(() -> String.format("Tax: %10.2f\n", tax).getBytes());
        resetStyle(); // Reset after total
    }

    public void addTotal(double total) {
        // Feed a bit before printing the total
        addFeed(0);
        addStyle(POSStyle.BOLD);
        addComponent(() -> String.format("Total: %10.2f\n", total).getBytes());
        resetStyle(); // Reset after total
    }

    public void addPaymentMethod(String paymentMethod) {
        addComponent(() -> (address + "\n").getBytes());
    }

    public void addBarcode(POSBarcode barcode) {
        addComponent(barcode);
        addComponent(() -> (barcode.getData() + "\n").getBytes()); // print productID directly after barcode
        // addText(barcode.getData()); // <-- same as the above
    }

    public void setFooterLine(String footer) {
        addComponent(() -> (footer + "\n\n").getBytes());
    }
}
