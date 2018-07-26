package com.aayush.view;

import com.aayush.model.template.Download;
import com.aayush.view.util.model.DownloadTableModel;
import com.aayush.view.util.renderer.ProgressCellRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Panel that houses the download table
 *
 * @author Aayush
 * @see javax.swing.JPanel
 */
class DownloadTablePanel extends JPanel {
    /**
     * Download table
     */
    private final JTable table;
    /**
     * Download table model
     */
    private final DownloadTableModel tableModel;

    /**
     * Constructor
     */
    DownloadTablePanel() {
        tableModel = new DownloadTableModel();
        table = new JTable(tableModel);

        Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border innerBorder = BorderFactory.createTitledBorder("Downloads");
        setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        ProgressCellRenderer renderer = new ProgressCellRenderer(0, 100);
        renderer.setStringPainted(true);

        table.setDefaultRenderer(JProgressBar.class, renderer);
        table.setRowHeight(renderer.getPreferredSize().height);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Add a download to the table
     *
     * @param download download to be added
     */
    void addDownload(Download download) {
        tableModel.addDownload(download);
    }

    /**
     * Adds a {@code ListSelectionListener} to the table's selection model
     *
     * @param listener listener to be added
     */
    void addListSelectionListener(ListSelectionListener listener) {
        table.getSelectionModel().addListSelectionListener(listener);
    }

    Download getDownload(int row) {
        return tableModel.getDownload(row);
    }

    /**
     * Returns the selected row
     *
     * @return selected row
     */
    int getSelectedRow() {
        return table.getSelectedRow();
    }

    /**
     * Refreshes the table model
     */
    void refresh() {
        tableModel.fireTableDataChanged();
    }

    /**
     * Removes the download from the table model at a given index
     *
     * @param index index of the download
     */
    void removeDownload(int index) {
//        DownloadManager.getInstance().removeDownload(index);
        tableModel.removeDownload(index);
    }

    void reset() {
        table.getSelectionModel().clearSelection();
    }

    /**
     * Set downloads in the table
     *
     * @param downloads downloads data
     */
    void setData(List<Download> downloads) {
        tableModel.setDownloads(downloads);
    }

    List<Download> getDownloads() {
        return tableModel.getDownloads();
    }
}
