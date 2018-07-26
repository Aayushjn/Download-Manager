package com.aayush.util;

/**
 * Helper class that has major constants needed by other classes
 *
 * @author Aayush
 */
public class Constants {
    private static final int BLOCK_SIZE = 4096;
    public static final int BUFFER_SIZE = 1024;
    public static final int MIN_DOWNLOAD_SIZE = BLOCK_SIZE * 100;

    public static final String DEFAULT_OUTPUT_FOLDER = System.getProperty("user.home") +
            "\\Desktop";

    private Constants() {}
}
