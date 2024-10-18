package com.example.springbootapi.bonprintextended;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class POSLogo implements POSComponent {
    private final byte[] logoData;

    public POSLogo(String filePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(filePath));
        BufferedImage binaryImage = convertToBinaryImage(image);
        this.logoData = convertImageToESCPos(binaryImage);
    }

    /**
     * Converts the given image to a binary (black and white) image.
     *
     * @param image The original BufferedImage.
     * @return A monochrome BufferedImage.
     */
    private BufferedImage convertToBinaryImage(BufferedImage image) {
        BufferedImage binaryImage = new BufferedImage(
            image.getWidth(),
            image.getHeight(),
            BufferedImage.TYPE_BYTE_BINARY
        );
        Graphics2D graphics = binaryImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return binaryImage;
    }

    /**
     * Converts a binary image to ESC/POS byte commands.
     *
     * @param image The monochrome BufferedImage.
     * @return A byte array representing the ESC/POS commands for the image.
     * @throws IOException If an I/O error occurs.
     */
    private byte[] convertImageToESCPos(BufferedImage image) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int width = image.getWidth();
        int height = image.getHeight();

        // Ensure width is a multiple of 8
        int adjustedWidth = (width + 7) / 8 * 8;

        // ESC/POS command to start image printing
        // GS v 0
        byte[] init = new byte[] { 
            POS.Command.GS, 0x76, 0x30, 0x00, 
            (byte) (adjustedWidth % 256), 
            (byte) (adjustedWidth / 256), 
            (byte) (height % 256), // yL
            (byte) (height / 256)  // yH
        };
        output.write(init);

        for (int y = 0; y < height; y++) {
            byte[] row = new byte[adjustedWidth / 8];
            for (int x = 0; x < adjustedWidth; x += 8) {
                byte b = 0;
                for (int bit = 0; bit < 8; bit++) {
                    int pixelX = x + bit;
                    if (pixelX < width) {
                        int pixel = image.getRGB(pixelX, y);
                        // In a binary image, pixel is either 0xFFFFFF (white) or 0x000000 (black)
                        if (pixel == 0x000000) { // Black pixel
                            b |= (byte) (1 << (7 - bit));
                        }
                    }
                }
                row[x / 8] = b;
            }
            output.write(row);
        }

        return output.toByteArray();
    }

    @Override
    public byte[] toBytes() {
        return logoData;
    }
}
