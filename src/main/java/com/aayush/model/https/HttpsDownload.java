package com.aayush.model.https;

import com.aayush.model.template.Download;

import java.net.URL;

class HttpsDownload extends Download {
    public HttpsDownload(URL url, String outputFolder, int numConnections) {
        super(url, outputFolder, numConnections);
    }

    @Override
    public void run() {

    }
}
