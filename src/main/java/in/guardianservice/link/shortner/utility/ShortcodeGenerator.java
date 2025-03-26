package in.guardianservice.link.shortner.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
public class ShortcodeGenerator {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String generateShortCode(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(url.getBytes());

            // Convert hash bytes to a string containing only alphabetic characters
            StringBuilder shortCode = new StringBuilder();
            for (byte b : hash) {
                char c = ALPHABET.charAt((b & 0xFF) % ALPHABET.length());
                shortCode.append(c);
                if (shortCode.length() == 6) break; // Stop when we have 6 characters
            }

            return shortCode.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}
