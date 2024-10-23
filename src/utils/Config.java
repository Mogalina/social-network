package utils;

import java.nio.file.Paths;

/**
 * The {@code Config} class holds configuration constants that can be used across the application.
 */
public class Config {

    // Constant representing the default local storage path
    public static final String DEFAULT_LOCAL_STORAGE_PATH = Paths.get("src", "storage", "local").toString();
}
