package com.example.springbootapi.bonprintextended;

import javax.print.*;
import java.io.ByteArrayInputStream;

/**
 * Handles the printing of POS documents to a specified printer using ESC/POS
 * commands.
 */
public class POSPrinter {

    /**
     * Prints the given POSDocument using the specified PrintService.
     *
     * @param document      The POSDocument containing all receipt components.
     * @param printerService The PrintService representing the target printer.
     */
    public void print(POSDocument document, PrintService printerService) {
        // Convert the POSDocument to byte array for printing
    	
        byte[] emptyBufferSpace = new byte[100];  // Buffer space to ensure proper printing
        for (int i = 0; i < emptyBufferSpace.length; i++) {
            emptyBufferSpace[i] = ' ';  // Fill with spaces or use '\n' for line feeds
        }
        
        byte[] printData = concat(
            new byte[]{POS.Command.ESC, POS.Command.INIT},   // Printer initialization command
            emptyBufferSpace,
            document.toBytes(),                             // The document data
            new byte[]{POS.Command.LINE_FEED},              // Line feed
            POS.Command.ESC_CUT                             // Cut the page at the end
        );

        // Send the data to the printer
        try {
            DocPrintJob job = printerService.createPrintJob();
            Doc doc = new SimpleDoc(new ByteArrayInputStream(printData), DocFlavor.INPUT_STREAM.AUTOSENSE, null);
            job.print(doc, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Concatenates multiple byte arrays into a single byte array.
     *
     * @param arrays The byte arrays to concatenate.
     * @return A single concatenated byte array.
     */
    private byte[] concat(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] arr : arrays) {
            totalLength += arr.length;
        }
        byte[] result = new byte[totalLength];
        int currentPos = 0;
        for (byte[] arr : arrays) {
            System.arraycopy(arr, 0, result, currentPos, arr.length);
            currentPos += arr.length;
        }
        return result;
    }
}
