package utils;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for password-related operations.
 * This class provides exclusively static methods accessable accross the application without instantiating objects.
 */
public class PasswordUtils {

    /**
     * Hashes the provided password using the SHA-256 algorithm.
     *
     * @param password the plain text password to be hashed
     * @return the hashed password as a hexadecimal string
     */
    @NotNull
    public static String hashPassword(@NotNull String password) {
        try {
            // Create a SHA-256 message digest instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the hashed bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
