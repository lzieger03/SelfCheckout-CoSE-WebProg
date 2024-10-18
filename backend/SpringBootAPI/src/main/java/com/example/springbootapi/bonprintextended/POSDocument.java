package com.example.springbootapi.bonprintextended;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a POS document composed of multiple POSComponents. Manages the
 * assembly of receipt elements such as text, barcodes, QR codes, and images.
 */
public class POSDocument {
    private List<POSComponent> components = new ArrayList<>();
    private List<POSStyle> currentStyles = new ArrayList<>();

    /**
     * Adds a POSComponent to the document.
     *
     * @param component The POSComponent to add.
     */
    public void addComponent(POSComponent component) {
        components.add(component);
    }

    /**
     * Adds a specified number of line feeds to the document.
     *
     * @param lines The number of line feeds to add.
     */
    public void addFeed(int lines) {
        for (int i = 0; i < lines; i++) {
            addComponent(() -> "\n".getBytes());
        }
    }

    /**
     * Adds a barcode component to the document.
     *
     * @param barcode The POSBarcode to add.
     */
    public void addBarcode(POSBarcode barcode) {
        addComponent(barcode);
    }

    /**
     * Adds a QR code component to the document.
     *
     * @param qrcode The POSQRCode to add.
     */
    public void addQRCode(POSQRCode qrcode) {
        addComponent(qrcode);
    }

    /**
     * Adds logo to the document.
     *
     * @param logo The logo to add.
     */

    public void addLogo(POSLogo logo) {
        addComponent(logo);
    }

    /**
     * Adds plain text to the document.
     *
     * @param text The text to add.
     */
    public void addText(String text) {
        addComponent(() -> (text + "\n").getBytes());
    }

    /**
     * Adds a heading with specific styles to the document.
     *
     * @param heading The heading text to add.
     */
    public void addHeading(String heading) {
        addStyle(POSStyle.BOLD);
        addStyle(POSStyle.BIG);
        addComponent(() -> (heading + "\n").getBytes());
        resetStyle();
    }

    /**
     * Sets multiple styles for subsequent components.
     *
     * @param style POSStyle to apply.
     */
    public void addStyle(POSStyle style) {
        currentStyles.add(style);
        // Logic to apply the correct ESC/P command based on the style
        switch (style) {
            case BOLD:
                addComponent(() -> POS.Command.ESC_BOLD_ON);
                break;
            case ITALIC:
                addComponent(() -> POS.Command.ESC_ITALIC_ON);
                break;
            case UNDERLINE:
                addComponent(() -> POS.Command.ESC_UNDERLINE_ON);
                break;
            case BIG:
                addComponent(() -> POS.Command.ESC_DOUBLE_HEIGHT_ON);
                break;
            case SMALL:
                addComponent(() -> POS.Command.ESC_DOUBLE_HEIGHT_OFF); // Small is the default height, we reset the "BIG" style
                break;
            case CENTER:
                addComponent(() -> POS.Command.ESC_ALIGN_CENTER);
                break;
            case RIGHT:
                addComponent(() -> POS.Command.ESC_ALIGN_RIGHT);
                break;
            case LEFT:
                addComponent(() -> POS.Command.ESC_ALIGN_LEFT);
                break;
        }
    }

    /**
     * Resets the current styles to their default state.
     */
    public void resetStyle() {
        // Clear the styles and send commands to reset them
        for (POSStyle style : currentStyles) {
            switch (style) {
                case BOLD:
                    addComponent(() -> POS.Command.ESC_BOLD_OFF);
                    break;
                case ITALIC:
                    addComponent(() -> POS.Command.ESC_ITALIC_OFF);
                    break;
                case UNDERLINE:
                    addComponent(() -> POS.Command.ESC_UNDERLINE_OFF);
                    break;
                case BIG:
                    addComponent(() -> POS.Command.ESC_DOUBLE_HEIGHT_OFF);
                    break;
                case CENTER:
                case RIGHT:
                case LEFT:
                    addComponent(() -> POS.Command.ESC_ALIGN_LEFT); // Reset alignment to left
                    break;
                // No need to reset small; it's the default
            }
        }
        currentStyles.clear();
    }

    // New method to cut the page
    public void cutPage() {
        addComponent(() -> POS.Command.ESC_CUT);  // Add the cut command to the document's components
    }

    /**
     * Retrieves the complete byte array representing the entire document.
     *
     * @return A byte array combining all components of the document.
     */
    public byte[] toBytes() {
        List<Byte> byteList = new ArrayList<>();

        // Iterate through each component and add its byte representation to the byte list
        for (POSComponent component : components) {
            byte[] componentBytes = component.toBytes();
            for (byte b : componentBytes) {
                byteList.add(b);
            }
        }

        // Convert the list to a byte array
        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }

        return byteArray;
    }
}
