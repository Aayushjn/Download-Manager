package com.aayush.model.template;

import java.net.URL;

/**
 * Template of a generic thread on which download occurs
 *
 * @author Aayush
 * @see java.lang.Runnable
 */
public abstract class DownloadThread implements Runnable {
    /**
     * Id of this thread
     */
    protected final int threadId;
    /**
     * URL of the download
     */
    protected final URL url;
    /**
     * Name of output file
     */
    protected final String outputFile;
    /**
     * Byte at which download must begin
     */
    protected final int startByte;
    /**
     * Byte at which download must end
     */
    protected final int endByte;
    /**
     * State of the thread
     */
    private final boolean finished;
    /**
     * Actual thread that runs this target runnable
     */
    private Thread thread;

    /**
     * Constructor
     *
     * @param threadId Id of this thread
     * @param url URL of the download
     * @param outputFile Name of output file
     * @param startByte Byte at which download must begin
     * @param endByte Byte at which download must end
     */
    public DownloadThread(int threadId, URL url, String outputFile, int startByte, int endByte) {
        this.threadId = threadId;
        this.url = url;
        this.outputFile = outputFile;
        this.startByte = startByte;
        this.endByte = endByte;
        finished = false;

        download();
    }

    /**
     * Begin download
     */
    public void download() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Returns the {@code finished} flag
     *
     * @return {@code finished}
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Waits for the thread to complete execution
     *
     * @throws InterruptedException if the current thread is interrupted
     */
    public void waitForFinish() throws InterruptedException {
        thread.join();
    }
}
