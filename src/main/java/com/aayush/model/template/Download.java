package com.aayush.model.template;

import com.aayush.util.DownloadStatus;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.net.URL;

/**
 * This class represents a generic template of a download.
 *
 * @author Aayush
 * @see java.io.Serializable
 * @see java.lang.Runnable
 */
public abstract class Download implements Serializable, Runnable {
    /**
     * {@code PropertyChangeSupport} is used as the observable pattern
     */
    private final PropertyChangeSupport propertyChangeSupport;

    /**
     * Download {@code url}
     */
    protected final URL url;
    /**
     * Folder to which the file must be downloaded
     */
    private final String outputFolder;
    /**
     * Number of connections that must be established
     */
    private final int numConnections;
    /**
     * File name
     */
    protected final String fileName;
    /**
     * File size in bytes
     */
    protected long fileSize;
    /**
     * Current status of the download
     */
    protected DownloadStatus status;
//    /**
//     * List of threads downloading the current file
//     */
//    protected final List<DownloadThread> downloadThreadList;
    /**
     * Number of bytes downloaded
     */
    protected int downloadedSize;

    protected long initTime;
    protected long startTime;
    protected long readSinceStart;
    protected long elapsedTime;
    protected long prevElapsedTime;
    protected long remainingTime;
    protected float downloadSpeed;

    /**
     * Constructor
     *
     * @param url the download url
     * @param outputFolder the folder to which file must be downloaded
     * @param numConnections the number of connections to be made
     */
    protected Download(URL url, String outputFolder, int numConnections) {
        propertyChangeSupport = new PropertyChangeSupport(this);

        this.url = url;
        this.outputFolder = outputFolder;
        this.numConnections = numConnections;

        String fileUrl = url.getFile();
        fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        fileSize = -1;
        status = DownloadStatus.DOWNLOADING;
        downloadedSize = 0;
        elapsedTime = 0;
        prevElapsedTime = 0;
        remainingTime = -1;
        downloadSpeed = 0;
//        downloadThreadList = new ArrayList<>();
    }

    /**
     * Add a {@code PropertyChangeListener} that will listen to change in the desired property
     *
     * @param property attribute to which listener must listen
     * @param listener listener on the property
     */
    public void addObserver(String property, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(property, listener);
    }

    /**
     * Update the status of the download
     *
     * @param value the new {@code status}
     */
    protected void setDownloadStatusProperty(DownloadStatus value) {
        DownloadStatus old = status;
        status = value;
        propertyChangeSupport.firePropertyChange("status", old, value);
    }

    /**
     * Update the status of the download
     *
     * @param value the new {@code fileSize}
     */
    protected void setFileSizeProperty(long value) {
        long old = fileSize;
        fileSize = value;
        propertyChangeSupport.firePropertyChange("fileSize", old, value);
    }

    /**
     * Remove a {@code PropertyChangeListener} that listens to change in the desired property
     *
     * @param property attribute to which listener must listen
     * @param listener listener on the property
     */
    public void removeObserver(String property, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(property, listener);
    }

    /**
     * Cancel the download
     */
    public void cancel() {
        prevElapsedTime = elapsedTime;
        setDownloadStatusProperty(DownloadStatus.CANCELLED);
    }

    /**
     * Begin the download
     */
    protected void download() {
        Thread downloadThread = new Thread(this);
        setDownloadStatusProperty(DownloadStatus.DOWNLOADING);
        downloadThread.start();
    }

    protected void error() {
        prevElapsedTime = elapsedTime;
        setDownloadStatusProperty(DownloadStatus.ERROR);
    }

    private String formatTime(long time) {
        String timeString = "";
        timeString += String.format("%02d", time/3600) + ":";
        time %= 3600;
        timeString += String.format("%02d", time/60) + ":";
        time %= 60;
        timeString += String.format("%02d", time);
        return timeString;
    }

    /**
     * Pause the download
     */
    public void pause() {
        prevElapsedTime = elapsedTime;
        setDownloadStatusProperty(DownloadStatus.PAUSED);
    }

    /**
     * Resume the download
     */
    public void resume() {
        setDownloadStatusProperty(DownloadStatus.DOWNLOADING);
        download();
    }

    public float getDownloadSpeed() {
        return downloadSpeed;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Getter for {@code fileName}
     *
     * @return {@code fileName}
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Getter for {@code fileSize}
     *
     * @return {@code fileSize}
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * Returns the progress of the download
     *
     * @return the progress of the download
     */
    public float getProgress() {
        return (downloadedSize / fileSize) * 100;
    }

    public String getRemainingTime() {
        if (remainingTime < 0) {
            return "Unknown";
        }
        return formatTime(remainingTime);
    }

    /**
     * Getter for {@code status}
     *
     * @return {@code status}
     */
    public DownloadStatus getStatus() {
        return status;
    }
}
