package com.aayush.listener;

/**
 * {@code DownloadTableListener} defines the interface for an object that listens to changes in a
 * {@code DownloadTableModel}
 *
 * @author Aayush
 * @see com.aayush.view.util.model.DownloadTableModel
 */

public interface DownloadTableListener {
    /**
     * This method notifies listeners of the row that has been deleted
     *
     * @param row a {@code int} to notify listeners which row has been deleted
     */
    void rowDeleted(int row);
}
