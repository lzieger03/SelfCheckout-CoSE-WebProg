package com.example.springbootapi.bonprintextended;

import com.example.springbootapi.api.model.Product;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

// execute with:
// java -cp ./sqlite-jdbc-3.46.1.1.jar ./PrintJobExample2.java

public class PrintJobExample2 {

    static Connection connection;
    static ResultSet resultSet;
    static ArrayList<Product> products = new ArrayList<>();

    private static void sqlQuery(){
        try {
            String url = "jdbc:sqlite:item_database.db";
            connection = DriverManager.getConnection(url);

            Statement statement = connection.createStatement();

            resultSet = statement.executeQuery("select * from products");
            while (resultSet.next()){
                products.add(
                        new Product(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getDouble(3)
                        ));
            }
        } catch (Exception err) {
            System.out.println("------------sqlQuery()----------------" + err);
        }
    }

    public static void main(String[] args) {
        try {
            // Find the printer by name
            PrintService printerService = findPrintService("OLIVETTI PRT80");

            if (printerService == null) {
                System.out.println("Printer not found");
                return;
            }
            // Create a new POSPrinter instance
            POSPrinter posPrinter = new POSPrinter();

            // Create a new receipt
            POSReceipt receipt = new POSReceipt();

            receipt.setTitle("CODE128 (THIN) - functional");

            sqlQuery();
            for (Product product:products) {
                receipt.addItem(product.getName(), product.getPrice(),1);

                // Create and add a barcode to the receipt
                POSBarcode barcode = new POSBarcode(product.getId(), POS.BarcodeType.CODE128);
                barcode.setHeight(162);
                barcode.setWidth(POS.BarWidth.THIN);
                receipt.addBarcode(barcode);
            }

            // Print the receipt using the POSPrinter
            posPrinter.print(receipt, printerService);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to find a printer by name
    public static PrintService findPrintService(String printerName) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }
        return null;
    }
}
