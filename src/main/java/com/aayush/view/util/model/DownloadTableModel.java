package com.aayush.view.util.model;

import com.aayush.model.template.Download;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for all downloads
 *
 * @author Aayush
 * @see javax.swing.table.AbstractTableModel
 * @see java.beans.PropertyChangeListener
 */
public class DownloadTableModel extends AbstractTableModel implements PropertyChangeListener {
    /**
     * List of downloads in the table
     */
    private List<Download> downloads = new ArrayList<>();

    /**
     * Column names
     */
    private static final String[] columnNames = {"File Name", "Size (MB)", "Progress",
            "Download Speed (MB/s)", "Elapsed Time", "Remaining Time", "Status"};
    /**
     * Column classes
     */
    private static final Class[] classNames = {String.class, String.class, JProgressBar.class,
            String.class, String.class, String.class, String.class};

    /**
     * Add a download to the table
     *
     * @param download download to be added
     */
    public void addDownload(Download download) {
        download.addObserver("status", this);
        downloads.add(download);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    public Download getDownload(int row) {
        return downloads.get(row);
    }

    public List<Download> getDownloads() {
        return downloads;
    }

    /**
     * Remove a download from the table
     *
     * @param row row from which download must be removed
     */
    public void removeDownload(int row) {
        downloads.remove(row);
        fireTableRowsDeleted(row, row);
    }

    /**
     * Setter for @{code downloads}
     *
     * @param downloads downloads to be set in the table
     */
    public void setDownloads(List<Download> downloads) {
        this.downloads = downloads;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return classNames[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return downloads.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Download download = downloads.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return download.getFileName();
            case 1:
                long size = download.getFileSize();
                return (size == -1) ? "" : (String.valueOf(size / (2^20)));
            case 2:
                return download.getProgress();
            case 3:
                return download.getDownloadSpeed();
            case 4:
                return download.getElapsedTime();
            case 5:
                return download.getRemainingTime();
            case 6:
                return download.getStatus();
            default:
                return "";
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        int index = downloads.indexOf(evt.getSource());
        fireTableRowsUpdated(index, index);
    }
}
