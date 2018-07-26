package com.aayush.listener;

/**
 * {@code ToolbarListener} defines the interface for a {@code Toolbar} to respond to button clicks
 *
 * @author Aayush
 * @see com.aayush.view.Toolbar
 */

public interface ToolbarListener {
    /**
     * This method provides a callback for 'add' button press
     */
    void onAddClicked();

    /**
     * This method provides a callback for 'settings' button press
     */
    void onSettingsClicked();
}
