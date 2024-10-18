package com.example.springbootapi.service;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

/**
 * Singleton class responsible for managing the printer service.
 * Ensures that only one instance of the printer service is used throughout the application.
 */
public class PrinterSingleton {
    private static PrinterSingleton instance;
    private PrintService printService;

    /**
     * Private constructor to prevent instantiation.
     */
    private PrinterSingleton() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the singleton instance of PrinterSingleton.
     *
     * @return The singleton instance.
     */
    public static synchronized PrinterSingleton getInstance() {
        if (instance == null) {
            instance = new PrinterSingleton();
        }
        return instance;
    }

    /**
     * Retrieves the print service for the specified printer.
     *
     * @return The PrintService object for the printer.
     */
    public PrintService getPrintService() {
        if (printService == null) {
            printService = findPrintService("OLIVETTI PRT80");
        }
        return printService;
    }

    /**
     * Finds a print service by its name.
     *
     * @param printerName The name of the printer to find.
     * @return The PrintService object if found, otherwise null.
     */
    private PrintService findPrintService(String printerName) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }
        return null;
    }
}
