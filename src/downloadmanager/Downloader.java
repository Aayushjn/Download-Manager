package downloadmanager;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;

abstract class Downloader extends Observable implements Serializable, Runnable {
	static final int BLOCK_SIZE = 4096;
	static final int BUFFER_SIZE = 4096;
	static final int MIN_DOWNLOAD_SIZE = BLOCK_SIZE * 100;
	static final String[] STATUSES = {"Downloading", "Paused", "Complete", "Cancelled", "Error"};
	static final int DOWNLOADING = 1;
	static final int PAUSED = 2;
	static final int COMPLETE = 3;
	static final int ERROR = 5;
	private static final long serialVersionUID = -2073108842615019018L;
	private static final int CANCELLED = 4;
	URL url;
	String outputFolder;
	int numConnections;
	String fileName;
	int fileSize;
	int state;
	ArrayList<DownloadThread> downloadThreadList;
	private int downloadedSize;
	
	/*
	 * Constructor
	 * @param downloadURL
	 * @param outputFolder
	 * @param numConnections
	 */
    Downloader(URL url, String outputFolder, int numConnections) {
		this.url = url;
		this.outputFolder = outputFolder;
		this.numConnections = numConnections;
		
		String fileURL = url.getFile();
		fileName = fileURL.substring(fileURL.lastIndexOf('/') + 1);
		System.out.println("File name: " + fileName);
		fileSize = -1;
		state = DOWNLOADING;
		downloadedSize = 0;
		
		downloadThreadList = new ArrayList<>();
	}
	
	/*
	 * Pause the downloader
	 */
    void pause() {
		setState(PAUSED);
	}
	
	/*
	 * Resume the downloader
	 */
    void resume() {
		setState(DOWNLOADING);
	}
	
	/*
	 * Cancel the downloader
	 */
    void cancel() {
		setState(CANCELLED);
	}
	
	/*
	 * Getter for url (String)
	 * @return String
	 */
    String getURL() {
		return url.toString();
	}
	
	/*
	 * Getter for fileSize
	 * @return int
	 */
    int getFileSize() {
		return fileSize;
	}
	
	/*
	 * Return download progress
	 * @return float
	 */
    float getProgress() {
		return ((float)downloadedSize / fileSize) * 100;
	}
	
	/*
	 * Getter for state
	 * @return int
	 */
    int getState() {
		return state;
	}
	
	/*
	 * Setter for state
	 * @param value
	 */
    void setState(int value) {
		state = value;
	}
	
	/*
	 * Start or resume download (Downloader)
	 */
    void download() {
		Thread t = new Thread(this);
		t.start();
	}

    /*
	 * Set the state changed and notify observers
	 */
    void stateChanged() {
		setChanged();
		notifyObservers();
	}
	
	/*
	 * Thread to download parts of a file
	 */
	abstract class DownloadThread implements Runnable {
		int threadId;
		URL url;
		String outputFile;
		int startByte;
		int endByte;
		boolean isFinished;
		Thread thread;
		
		/*
		 * Constructor
		 * @param threadID
		 * @param url
		 * @param outputFile
		 * @param startByte
		 * @param endByte
		 */
        DownloadThread(int threadID, URL url, String outputFile, int startByte, int endByte) {
			threadId = threadID;
			this.url = url;
			this.outputFile = outputFile;
			this.startByte = startByte;
			this.endByte = endByte;
			isFinished = false;
			
			download();
		}
		
		/*
		 * Check whether thread has finished downloading
		 * @return boolean
		 */
        boolean isDownloadFinished() {
			return isFinished;
		}
		
		/*
		 * Start or resume download (DownloadThread)
		 */
        void download() {
			thread = new Thread(this);
			thread.start();
		}
		
		/*
		 * Waiting for thread to finish
		 * @throws InterruptedException
		 */
        void waitForFinish() throws InterruptedException {
			thread.join();
		}
	}
}
