package com.aayush.util;

/**
 * Enum that defines the status of downloads
 *
 * @author Aayush
 */
public enum DownloadStatus {
    CANCELLED("Cancelled"),
    COMPLETE("Complete"),
    DOWNLOADING("Downloading"),
    ERROR("Error"),
    PAUSED("Paused");

    final String text;

    DownloadStatus(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
