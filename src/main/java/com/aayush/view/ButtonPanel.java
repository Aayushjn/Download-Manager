package com.aayush.view;

import com.aayush.model.template.Download;
import com.aayush.util.DownloadStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Panel that contains all the buttons that control download status
 *
 * @author Aayush
 * @see javax.swing.JPanel
 */
class ButtonPanel extends JPanel {
    /**
     * Cancel button
     */
    private final JButton cancelButton;
    /**
     * Pause button
     */
    private final JButton pauseButton;
    /**
     * Resume button
     */
    private final JButton resumeButton;
    private final JButton removeButton;
    /**
     * Exit button
     */
    private final JButton exitButton;

    /**
     * Constructor
     */
    ButtonPanel() {
        cancelButton = new JButton("Cancel");
        pauseButton = new JButton("Pause");
        resumeButton = new JButton("Resume");
        removeButton = new JButton("Remove");
        exitButton = new JButton("Exit");

        Dimension preferredSize = resumeButton.getPreferredSize();
        cancelButton.setPreferredSize(preferredSize);
        pauseButton.setPreferredSize(preferredSize);
        removeButton.setPreferredSize(preferredSize);
        exitButton.setPreferredSize(preferredSize);

        cancelButton.setEnabled(false);
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);

        setLayout(new FlowLayout(FlowLayout.RIGHT));

        add(pauseButton);
        add(resumeButton);
        add(cancelButton);
        add(removeButton);
        add(exitButton);
    }

    /**
     * Add action listener to the desired button
     *
     * @param button name of button to which listener must be added
     * @param listener listener to be added
     */
    void addActionListener(String button, ActionListener listener) {
        switch (button) {
            case "Cancel":
                cancelButton.addActionListener(listener);
                break;
            case "Pause":
                pauseButton.addActionListener(listener);
                break;
            case "Resume":
                resumeButton.addActionListener(listener);
                break;
            case "Remove":
                removeButton.addActionListener(listener);
            case "Exit":
                exitButton.addActionListener(listener);
                break;
            default:
                Logger.getLogger(ButtonPanel.class.getSimpleName()).log(Level.WARNING, "How " +
                        "is this even possible?");
        }
    }

    /**
     * Update button states depending on download status
     *
     * @param download download whose status alters the state of the buttons
     */
    void updateButtons(Download download) {
        if (download == null) {
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(false);
            removeButton.setEnabled(false);
            cancelButton.setEnabled(false);
        }
        else {
            DownloadStatus status = download.getStatus();
            switch (status) {
                case DOWNLOADING:
                    pauseButton.setEnabled(true);
                    resumeButton.setEnabled(false);
                    removeButton.setEnabled(false);
                    cancelButton.setEnabled(true);
                    break;
                case PAUSED:
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(true);
                    removeButton.setEnabled(false);
                    cancelButton.setEnabled(true);
                    break;
                case ERROR:
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(true);
                    removeButton.setEnabled(true);
                    cancelButton.setEnabled(false);
                    break;
                default:
                    // COMPLETE or CANCELLED
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(false);
                    removeButton.setEnabled(true);
                    cancelButton.setEnabled(false);
            }
        }
    }
}
