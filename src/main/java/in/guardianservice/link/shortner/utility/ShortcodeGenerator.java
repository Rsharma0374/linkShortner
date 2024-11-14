package in.guardianservice.link.shortner.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
public class ShortcodeGenerator {

    public static String generateShortCode(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(url.getBytes());
            // Take the first 6 bytes and encode them to Base64 to get a 6-character code
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash).substring(0, 6);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}
