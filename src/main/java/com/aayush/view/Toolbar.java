package com.aayush.view;

import com.aayush.listener.ToolbarListener;
import com.aayush.util.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Toolbar that contains add and settings buttons
 *
 * @author Aayush
 * @see javax.swing.JToolBar
 */
class Toolbar extends JToolBar {
    /**
     * Add button
     */
    private final JButton addButton;
    /**
     * Settings button
     */
    private final JButton settingsButton;
    /**
     * Listener interface for toolbar
     */
    private ToolbarListener toolbarListener;

    /**
     * Constructor
     */
    Toolbar() {
        setBorder(BorderFactory.createEtchedBorder());

        addButton = new JButton(Utils.createIcon("/images/add32.png", "Add Task"));
        addButton.setToolTipText("Add Task");
        addButton.addActionListener(e -> toolbarListener.onAddClicked());

        settingsButton = new JButton(Utils.createIcon("/images/gear32.png", "Settings"));
        settingsButton.setToolTipText("Settings");
        settingsButton.addActionListener(e -> toolbarListener.onSettingsClicked());

        setLayout(new FlowLayout());

        add(addButton);
        addSeparator();
        add(settingsButton);
    }

    /**
     * Setter for {@code toolbarListener}
     *
     * @param listener listener to be set
     */
    void setToolbarListener(ToolbarListener listener) {
        toolbarListener = listener;
    }
}
