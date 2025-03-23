package in.guardianservice.link.shortner.utility;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class QRCodeGenerator {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;

    // Generate QR code for the provided URL
    public static String generateQRCode(String url) throws Exception {
        // Create a ByteArrayOutputStream to store the QR code image as a byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Generate the QR code
        BufferedImage qrCodeImage = generateQRCodeImage(url);

        // Write the image to the output stream as a PNG
        ImageIO.write(qrCodeImage, "PNG", byteArrayOutputStream);

        // Convert the byte array to Base64 String (if needed for storage or transport)
        return encodeToBase64(byteArrayOutputStream.toByteArray());
    }

    // Generate QR code image for the URL
    private static BufferedImage generateQRCodeImage(String url) throws Exception {
        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.MARGIN, 1); // Optional: Adjust margin (increases border size)

        // Create BitMatrix for the QR code
        BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hintMap);

        // Create BufferedImage to hold the QR code
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        // Set the background color to white
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                image.setRGB(x, y, (matrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB()));
            }
        }

        return image;
    }

    // Encode byte array to Base64 String (optional)
    private static String encodeToBase64(byte[] byteArray) {
        return java.util.Base64.getEncoder().encodeToString(byteArray);
    }
}
