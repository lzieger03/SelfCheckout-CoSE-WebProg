package com.example.springbootapi.bonprintextended;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a barcode component for POS receipts. Handles the configuration
 * and conversion of barcode data into ESC/POS byte commands.
 */
public class POSBarcode implements POSComponent {
    private final POS.BarcodeType type;
    private final String data;
    private final List<byte[]> commands = new ArrayList<>();

    /**
     * Constructs a POSBarcode with the specified data and barcode type.
     *
     * @param data The data to encode in the barcode.
     * @param type The type of barcode to generate.
     */
    public POSBarcode(String data, POS.BarcodeType type) {
        this.data = data;
        this.type = type;
    }

    /**
     * Sets the height of the barcode.
     *
     * @param height The height value for the barcode.
     */
    public void setHeight(int height) {
        commands.add(new byte[]{POS.Command.GS, POS.Command.SET_BARCODE_HEIGHT, (byte) height});
    }

    /**
     * Sets the width of the barcode.
     *
     * @param width The width value for the barcode.
     */
    public void setWidth(POS.BarWidth width) {
        commands.add(new byte[]{POS.Command.GS, POS.Command.SET_BAR_WIDTH, (byte) width.getValue()});
    }

    /**
     * Sets the label position of the barcode.
     *
     * @param position The position value for the barcode label.
     */
    public void setLabelPosition(POS.BarLabelPosition position) {
        commands.add(new byte[]{POS.Command.GS, POS.Command.SET_BAR_LABEL_POSITION, (byte) position.getValue()});
    }

    /**
     * Sets the font of the barcode label.
     *
     * @param fontB True for font B, false otherwise.
     */
    public void setLabelFont(boolean fontB) {
        commands.add(new byte[]{POS.Command.GS, POS.Command.SET_BAR_LABEL_FONT, (byte) (fontB ? 1 : 0)});
    }

    /**
     * Converts the barcode configurations and data into a byte array for printing.
     *
     * @return A byte array containing ESC/POS commands for the barcode.
     */
    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            // Append all configuration commands
            for (byte[] command : commands) {
                output.write(command);
            }

            // Append barcode print command
            output.write(POS.Command.GS);
            output.write(POS.Command.BARCODE_PRINT);

            // Append barcode type
            output.write(type.getValue());

            // Append length of data
            output.write(data.length());

            // Append barcode data
            output.write(data.getBytes());

            // Append line feed
            output.write(POS.Command.LINE_FEED);

            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toByteArray();
    }

    /**
     * Retrieves the data encoded in the barcode.
     *
     * @return The barcode data as a String.
     */
    public String getData() {
        return data;
    }
}
