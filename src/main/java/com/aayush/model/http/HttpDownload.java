package com.aayush.model.http;

import com.aayush.model.template.Download;
import com.aayush.util.DownloadStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.aayush.util.Constants.BUFFER_SIZE;

/**
 * Implementation of a HTTP download
 *
 * @author Aayush
 * @see Download
 */
public class HttpDownload extends Download {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(HttpDownload.class.getSimpleName());

    /**
     * Id assigned to threads
     */
    private int id = 1;

    /**
     * Constructor
     *
     * @param url the download url
     * @param outputFolder the folder to which file must be downloaded
     * @param numConnections the number of connections to be made
     */
    public HttpDownload(URL url, String outputFolder, int numConnections) {
        super(url, outputFolder, numConnections);
        download();
    }

    @Override
    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Range", "bytes=" + downloadedSize + "-");
            connection.connect();

            if (connection.getResponseCode() / 100 != 2) {
                error();
            }

            long contentLength = connection.getContentLengthLong();
            if (contentLength < 1) {
                error();
            }

            if (fileSize == -1) {
                setFileSizeProperty(contentLength);
            }
            int i = 0;
            // Open file and set seek to end
            file = new RandomAccessFile(fileName, "rw");
            file.seek(downloadedSize);

            stream = connection.getInputStream();
            initTime = System.nanoTime();

            while (status == DownloadStatus.DOWNLOADING) {
                if (i == 0) {
                    startTime = System.nanoTime();
                    readSinceStart = 0;
                }
                byte[] buffer;
                if (fileSize - downloadedSize > BUFFER_SIZE) {
                    buffer = new byte[BUFFER_SIZE];
                }
                else {
                    buffer = new byte[(int) (fileSize - downloadedSize)];
                }
                int read = stream.read(buffer);
                if (read == -1) {
                    break;
                }
                file.write(buffer, 0, read);
                downloadedSize += read;
                readSinceStart += read;

                i++;
                if (i >= 50) {
                    downloadSpeed = (readSinceStart * 976562.5f) / (System.nanoTime() - startTime);
                    if (downloadSpeed > 0) {
                        remainingTime = (long) ((fileSize - downloadedSize) /
                                (downloadSpeed * 1024));
                    }
                    else {
                        remainingTime = -1;
                    }
                    elapsedTime = prevElapsedTime + (System.nanoTime() - initTime);
                    i = 0;
                }
            }

            if (status == DownloadStatus.DOWNLOADING) {
                // TODO: Create alert
                setDownloadStatusProperty(DownloadStatus.COMPLETE);
                connection.disconnect();
            }
        }
        catch (IOException e) {
            LOGGER.log(Level.WARNING, "I/O error occurred while downloading", e);
            // TODO: Create alert
            error();
        }
        finally {
            if (file != null) {
                try {
                    file.close();
                }
                catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Unable to close file", e);
                }
            }

            if (stream != null) {
                try {
                    stream.close();
                }
                catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Unable to close stream", e);
                }
            }
        }

//        // Connection through which download occurs
//        HttpURLConnection connection = null;
//        try {
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(10000);
//            connection.connect();
//
//            if (connection.getResponseCode() / 100 != 2) {
//                LOGGER.log(Level.WARNING, "Unsuccessful response from url '" + url.toString() +
//                        "'");
//                setDownloadStatusProperty(DownloadStatus.ERROR);
//            }
//
//            long contentLength = connection.getContentLengthLong();
//            if (contentLength == -1) {
//                LOGGER.log(Level.WARNING, "Content length invalid for url '" + url.toString() +
//                        "'");
//                setDownloadStatusProperty(DownloadStatus.ERROR);
//            }
//
//            if (fileSize == -1) {
//                setFileSizeProperty(contentLength);
//            }
//
//            if (status == DownloadStatus.DOWNLOADING) {
//                if (downloadThreadList.isEmpty()) {
//                    if (fileSize > Constants.MIN_DOWNLOAD_SIZE) {
//                        long partSize = fileSize / numConnections;
//                        int startByte = 0;
//                        int endByte = Math.toIntExact(partSize - 1);
//                        HttpDownloadThread downloadThread = new HttpDownloadThread(id++, url,
//                                outputFolder + "\\" + fileName, startByte, endByte);
//                        downloadThreadList.add(downloadThread);
//                        while (endByte < fileSize) {
//                            startByte = endByte + 1;
//                            endByte += partSize;
//                            downloadThread = new HttpDownloadThread(id, url,
//                                    outputFolder + "\\" + fileName, startByte, endByte);
//                            downloadThreadList.add(downloadThread);
//                            id++;
//                        }
//                    }
//                    else {
//                        HttpDownloadThread downloadThread = new HttpDownloadThread(1, url,
//                                outputFolder + "\\" + fileName, 0,
//                                Math.toIntExact(fileSize));
//                        downloadThreadList.add(downloadThread);
//                    }
//                }
//                else {
//                    for (DownloadThread thread : downloadThreadList) {
//                        if (!thread.isFinished()) {
//                            thread.download();
//                        }
//                    }
//                }
//
//                for (DownloadThread thread : downloadThreadList) {
//                    thread.waitForFinish();
//                }
//
//                if (status == DownloadStatus.DOWNLOADING) {
//                    setDownloadStatusProperty(DownloadStatus.COMPLETE);
//                    System.out.println("HttpDownload.run: " + fileName + " downloaded");
//                    // TODO: Create alert
//                }
//            }
//        }
//        catch (IOException | InterruptedException e) {
//            if (e instanceof InterruptedException) {
//                LOGGER.log(Level.WARNING, fileName + " download failed mid-way", e);
//            }
//            else {
//                LOGGER.log(Level.WARNING, "I/O error occurred", e);
//            }
//        }
//        finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//        }
    }
}
