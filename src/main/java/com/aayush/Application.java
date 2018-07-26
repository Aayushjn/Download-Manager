package com.aayush;

import com.aayush.view.MainFrame;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Top level class that drives the application
 */
class Application {
    public static void main(String[] args) {
        String className = UIManager.getCrossPlatformLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(className);
        }
        catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | UnsupportedLookAndFeelException e) {

            if (e instanceof ClassNotFoundException) {
                logWarning("Invalid class name: " +
                        className, e);
            }
            else if (e instanceof InstantiationException) {
                logWarning("Class cannot be instantiated: " +
                        className, e);
            }
            else if (e instanceof IllegalAccessException) {
                logWarning("Illegal reflective access to class: " + className, e);
            }
            else {
                logWarning("Look and feel not installed on system: " + className, e);
            }
        }
        SwingUtilities.invokeLater(MainFrame::new);
    }

    private static void logWarning(String msg, Exception e) {
        Logger.getLogger(Application.class.getSimpleName()).log(Level.WARNING, msg, e);
    }
}
