package com.example.springbootapi.bonprintextended;

import com.example.springbootapi.bonprintextended.POS;
import com.example.springbootapi.bonprintextended.POS.ErrorCorrection;
import com.example.springbootapi.bonprintextended.POS.QrCodeSize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a QR Code component for POS receipts. Handles the configuration
 * and conversion of QR code data into ESC/POS byte commands.
 */
public class POSQRCode implements POSComponent {
    private final String data;
    private final List<byte[]> commands = new ArrayList<>();

    /**
     * Constructs a POSQRCode with the specified data, error correction level,
     * and size.
     *
     * @param data          The data to encode in the QR code.
     * @param percent15     The error correction level (15%).
     * @param medium        The size of the QR code (e.g., EXTRA_LARGE).
     */
    public POSQRCode(String data, ErrorCorrection percent15, QrCodeSize medium) {
        this.data = data;
        commands.add(new byte[]{POS.Command.GS, 0x28, 0x6B, 0x04, 0x00, 0x31, 0x41, 0x32, 0x00});
        commands.add(new byte[]{POS.Command.GS, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x43, (byte) medium.getValue()});
        commands.add(new byte[]{POS.Command.GS, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x45, (byte) percent15.getValue()});
        commands.add(new byte[]{POS.Command.GS, 0x28, 0x6B, (byte) (data.length() + 3), 0x00, 0x31, 0x50, 0x30});
        commands.add(data.getBytes());
        commands.add(new byte[]{POS.Command.GS, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30});
    }

    /**
     * Converts the QR code configurations and data into a byte array for printing.
     *
     * @return A byte array containing ESC/POS commands for the QR code.
     */
    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            // Append all configuration commands
            for (byte[] command : commands) {
                output.write(command);
            }

            // Append line feed
            output.write(POS.Command.LINE_FEED);

            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toByteArray();
    }
}
