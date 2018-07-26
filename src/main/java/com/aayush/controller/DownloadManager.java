package com.aayush.controller;

import com.aayush.model.http.HttpDownload;
import com.aayush.model.template.Download;

import java.net.URL;
import java.util.List;

/**
 * This class is a controller for all downloads
 *
 * @author Aayush
 */
public class DownloadManager {
    /**
     * Default number of connections to be established
     */
    private static final int DEFAULT_NUM_CONNECTIONS = 8;

    /**
     * Static instance for implementing Singleton pattern
     */
    private static DownloadManager instance;
    /**
     * Number of connections to be established
     */
    private final int numConnections;
    /**
     * List of all downloads
     */
    private List<Download> downloadList;

    private Download selectedDownload;

    /**
     * Constructor
     */
    private DownloadManager() {
        numConnections = DEFAULT_NUM_CONNECTIONS;
//        downloadList = new ArrayList<>();
    }

    /**
     * Creates a new download
     *
     * @param verifiedUrl url to download from
     * @param outputFolder folder to download the file to
     * @return the created download
     */
    public Download createDownload(URL verifiedUrl, String outputFolder) {
        Download download = new HttpDownload(verifiedUrl, outputFolder, numConnections);
//        downloadList.add(download);

        return download;
    }

    /**
     * Returns the download at a given index position in the {@code downloadList}
     *
     * @param index position of the download in the list
     * @return download at the given index position
     */
    public Download getDownload(int index) {
        if (index >= 0 && index < downloadList.size()) {
            return downloadList.get(index);
        }
        else {
            return null;
        }
    }

    /**
     * Removes the download at a given index position in the {@code downloadList}
     *
     * @param index position of the download in the list
     */
    public void removeDownload(int index) {
        if (index >= 0 && index < downloadList.size()) {
            downloadList.remove(index);
        }
    }

    /**
     * Getter for {@code downloadList}
     *
     * @return {@code downloadList}
     */
    public List<Download> getDownloadList() {
        return downloadList;
    }

    public Download getSelectedDownload() {
        return selectedDownload;
    }

    public void setSelectedDownload(Download selectedDownload) {
        this.selectedDownload = selectedDownload;
    }

    /**
     * Returns the static instance
     *
     * @return {@code instance}
     */
    public static DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }
        return instance;
    }
}
