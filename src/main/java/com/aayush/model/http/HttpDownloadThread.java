package com.aayush.model.http;

import com.aayush.model.template.DownloadThread;
import com.aayush.util.Constants;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.aayush.util.Constants.BUFFER_SIZE;

/**
 * Implementation of a HTTP download thread
 *
 * @author Aayush
 * @see DownloadThread
 */
class HttpDownloadThread extends DownloadThread {
    private static final Logger LOGGER = Logger.getLogger(HttpDownloadThread.class.getSimpleName());

    /**
     * Constructor
     *
     * @param threadId Id of this thread
     * @param url URL of the download
     * @param outputFile Name of output file
     * @param startByte Byte at which download must begin
     * @param endByte Byte at which download must end
     */
    HttpDownloadThread(int threadId, URL url, String outputFile, int startByte,
                       int endByte) {
        super(threadId, url, outputFile, startByte, endByte);
    }

    @Override
    public void run() {
        System.out.println("HttpDownloadThread.run: Thread " + threadId + " executing");
        System.out.println("HttpDownloadThread.run: Size = " + (endByte - startByte));

        HttpURLConnection connection;
        int response;
        FileOutputStream fileOutputStream;
        String urlString = url.toString();

        try {
            connection = (HttpURLConnection) url.openConnection();
            response = connection.getResponseCode();
        }
        catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unable to get connection", e);
            return;
        }

        if (response == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = connection.getHeaderField("Content-Disposition");

            if (disposition != null) {
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            }
            else {
                fileName = urlString.substring(urlString.lastIndexOf("/") + 1,
                        urlString.length());
            }

            InputStream inputStream;
            try {
                inputStream = connection.getInputStream();
            }
            catch (IOException e) {
                LOGGER.log(Level.WARNING, "I/O error occurred while creating input stream", e);
                return;
            }

            String savePath = outputFile;

            fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(savePath);
            }
            catch (FileNotFoundException e) {
                LOGGER.log(Level.WARNING, "Invalid file path", e);
                savePath = Constants.DEFAULT_OUTPUT_FOLDER + "\\" + fileName;
                // TODO: Create alert
                LOGGER.log(Level.INFO, "Destination path not found. Saving to default " +
                        "instead. " + savePath);
                try {
                    fileOutputStream = new FileOutputStream(savePath);
                }
                catch (FileNotFoundException e1) {
                    LOGGER.log(Level.WARNING, "Invalid file path", e1);
                }
            }

            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                        if (fileOutputStream != null) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }
                }
            }
            catch (IOException e) {
                LOGGER.log(Level.WARNING, "I/O error occurred", e);
            }
            finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
                catch (IOException e) {
                    LOGGER.log(Level.WARNING, "I/O error occurred while closing resources", e);
                }
            }
            System.out.println("HttpDownloadThread.run: Thread " + threadId + " downloaded");
        }
        else {
            LOGGER.log(Level.INFO, "No file to download. Server replied response code: " +
                    response);
        }
        connection.disconnect();
    }
}
