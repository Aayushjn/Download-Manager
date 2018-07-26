package com.aayush.util;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper functions that are used by other classes
 *
 * @author Aayush
 */
public class Utils {
    private Utils() {}

    /**
     * Creates an {@code ImageIcon} from the given path
     *
     * @param path path from which resource must be taken
     * @param desc description of the image
     * @return the created {@code ImageIcon}
     */
    public static ImageIcon createIcon(String path, String desc) {
        URL url = Utils.class.getResource(path);

        if (url == null) {
            Logger.getLogger(Utils.class.getSimpleName()).log(Level.WARNING, "Unable to load" +
                    " image: " + path);
            return null;
        }
        return new ImageIcon(url, desc);
    }

    /**
     * Verifies that the URL is valid
     *
     * @param fileUrl URL to be verified
     * @return verified URL
     */
    public static URL verifyURL(String fileUrl) {
        if (!(fileUrl.toLowerCase().startsWith("http://"))
                && !(fileUrl.toLowerCase().startsWith("https://"))) {
            return null;
        }
        URL verifiedUrl;
        try {
            verifiedUrl = new URL(fileUrl);
        }
        catch (MalformedURLException e) {
            Logger.getLogger(Utils.class.getSimpleName()).log(Level.WARNING, "URL syntax" +
                    " error / Invalid protocol", e);
            return null;
        }
        if (verifiedUrl.getFile().length() < 2) {
            return null;
        }

        return verifiedUrl;
    }
}
