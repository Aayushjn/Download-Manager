package com.aayush.view;

import com.aayush.controller.DownloadManager;
import com.aayush.listener.ToolbarListener;
import com.aayush.util.DownloadStatus;
import com.aayush.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

/**
 * Main frame that houses all of the GUI components
 *
 * @author Aayush
 * @see javax.swing.JFrame
 * @see java.beans.PropertyChangeListener
 */
public class MainFrame extends JFrame implements PropertyChangeListener {
    /**
     * Top most component: {@code toolbar}
     */
    private Toolbar toolbar;
    /**
     * Central component: {@code downloadTablePanel}
     */
    private DownloadTablePanel downloadTablePanel;
    /**
     * Bottom most component: {@code buttonPanel}
     */
    private ButtonPanel buttonPanel;
    /**
     * Dialog for adding downloads
     */
    private DownloadDialog downloadDialog;

    /**
     * Controller that interacts with the model
     */
    private DownloadManager downloadManager;

    /**
     * Boolean that indicates download delete status
     */
    private boolean clearing;

    /**
     * Constructor
     */
    public MainFrame() {
        super("Downloader");

        setLayout(new BorderLayout());

        initComponents();
        setupComponents();
        addToView();

        setMinimumSize(new Dimension(500, 400));
        setSize(800, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationByPlatform(true);
        setVisible(true);
    }

    /**
     * Add components to view
     */
    private void addToView() {
        add(toolbar, BorderLayout.NORTH);
        add(downloadTablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Initialize components
     */
    private void initComponents() {
        toolbar = new Toolbar();
        downloadTablePanel = new DownloadTablePanel();
        buttonPanel = new ButtonPanel();
        downloadDialog = new DownloadDialog(this);

        downloadManager = DownloadManager.getInstance();
    }

    /**
     * Set up components
     * Adding listeners and configuring data
     */
    private void setupComponents() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                final int[] count = {0};
                downloadTablePanel.getDownloads().forEach(download1 -> {
                    if (download1.getStatus().equals(DownloadStatus.DOWNLOADING)) {
                        count[0]++;
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Downloads are still running. Minimizing window instead",
                                "Running downloads", JOptionPane.INFORMATION_MESSAGE);
                        setExtendedState(JFrame.ICONIFIED);
                    }
                });
                if (count[0] == 0) {
                    dispose();
                    Runtime.getRuntime().gc();
                }
            }
        });

        buttonPanel.addActionListener("Cancel", e -> {
            int row = downloadTablePanel.getSelectedRow();
            downloadTablePanel.getDownload(row).cancel();
            buttonPanel.updateButtons(downloadTablePanel.getDownload(row));
        });

        buttonPanel.addActionListener("Pause", e -> {
            int row = downloadTablePanel.getSelectedRow();
            downloadTablePanel.getDownload(row).pause();
            buttonPanel.updateButtons(downloadTablePanel.getDownload(row));
        });

        buttonPanel.addActionListener("Resume", e -> {
            int row = downloadTablePanel.getSelectedRow();
            downloadTablePanel.getDownload(row).resume();
            buttonPanel.updateButtons(downloadTablePanel.getDownload(row));
        });

        buttonPanel.addActionListener("Remove", e -> {
            clearing = true;
            int index = downloadTablePanel.getSelectedRow();
            downloadTablePanel.removeDownload(index);
//            downloadTablePanel.reset();
            clearing = false;
            downloadManager.setSelectedDownload(null);
            buttonPanel.updateButtons(null);
        });

        buttonPanel.addActionListener("Exit", e -> {
            WindowListener[] windowListeners = getWindowListeners();

            for (WindowListener listener : windowListeners) {
                listener.windowClosing(new WindowEvent(this, 0));
            }
        });

        toolbar.setToolbarListener(new ToolbarListener() {
            @Override
            public void onAddClicked() {
                downloadDialog.setVisible(true);
                downloadDialog.addActionListener(e -> {
                    URL url = Utils.verifyURL(downloadDialog.getUrlText());
                    if (url != null) {
                        downloadTablePanel.addDownload(downloadManager.createDownload(url,
                                downloadDialog.getSelectedFile()));
//                        downloadTablePanel.refresh();

                        downloadDialog.setVisible(false);
                    }
                    else {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Invalid Download URL", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
            }

            @Override
            public void onSettingsClicked() {
                // TODO: Open new dialog for settings page
            }
        });

//        downloadTablePanel.setData(downloadManager.getDownloadList());
        downloadTablePanel.addListSelectionListener(e -> tableSelectionChanged());
    }

    /**
     * Indicates change in table selection
     */
    private void tableSelectionChanged() {
        if (downloadManager.getSelectedDownload() != null) {
            downloadManager.getSelectedDownload().removeObserver("status", this);
        }
        if (!clearing) {
            downloadManager.setSelectedDownload(
                    downloadTablePanel.getDownload(downloadTablePanel.getSelectedRow()));
            downloadManager.getSelectedDownload().addObserver("status", this);
            buttonPanel.updateButtons(downloadManager.getSelectedDownload());
        }
//        int index = downloadTablePanel.getSelectedRow();
//        if (index >= 0 && index < downloadManager.getDownloadList().size()) {
//            if (downloadManager.getDownload(index) != null) {
//                downloadManager.getDownload(index).removeObserver("status", this);
//            }
//        }
//
//        if (!clearing) {
//            index = downloadTablePanel.getSelectedRow();
//            if (index != -1) {
//                downloadManager.getDownload(index).addObserver("status", this);
//            }
//            if (downloadManager.getDownload(index) != null) {
//                buttonPanel.updateButtons(downloadManager.getDownload(index).getStatus());
//            }
//            else {
//                buttonPanel.updateButtons(null);
//            }
//        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (downloadManager.getSelectedDownload() != null &&
                downloadManager.getSelectedDownload().equals(evt.getSource())) {
            buttonPanel.updateButtons(downloadManager.getSelectedDownload());
        }
    }
}
