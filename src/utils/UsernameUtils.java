package utils;

import exceptions.ValidationException;

import java.util.regex.Pattern;

/**
 * Utility class for username-related operations.
 * This class provides exclusively static methods accessable accross the application without instantiating objects.
 */
public class UsernameUtils {

    // Regular expression for validating username format (3 to 20 characters in [a-zA-z0-9._-])
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9._-]{3,20}$";

    /**
     * Checks if the provided username follows the right format.
     *
     * @param username the username to be checked
     * @throws ValidationException if the username is null or fails format criteria
     */
    public static void checkUsernameFormat(String username) {
        if (username == null) {
            throw new ValidationException("Username must not be null");
        }
        if (!Pattern.matches(USERNAME_REGEX, username)) {
            throw new ValidationException("Invalid username format");
        }
    }
}
