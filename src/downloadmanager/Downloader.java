package downloadmanager;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;

public abstract class Downloader extends Observable implements Serializable, Runnable {
	private static final long serialVersionUID = -2073108842615019018L;
	
	protected URL url;
	protected String outputFolder;
	protected int numConnections;
	protected String fileName;
	protected int fileSize;
	protected int state;
	protected int downloadedSize;
	protected ArrayList<DownloadThread> downloadThreadList;
	
	protected static final int BLOCK_SIZE = 4096;
	protected static final int BUFFER_SIZE = 4096;
	protected static final int MIN_DOWNLOAD_SIZE = BLOCK_SIZE * 100;
	
	public static final String[] STATUSES = {"Downloading", "Paused", "Complete", "Cancelled", "Error"};
	
	public static final int DOWNLOADING = 1;
	public static final int PAUSED = 2;
	public static final int COMPLETE = 3;
	public static final int CANCELLED = 4;
	public static final int ERROR = 5;
	
	/*
	 * Constructor
	 * @param downloadURL
	 * @param outputFolder
	 * @param numConnections
	 */
	protected Downloader(URL url, String outputFolder, int numConnections) {
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
	public void pause() {
		setState(PAUSED);
	}
	
	/*
	 * Resume the downloader
	 */
	public void resume() {
		setState(DOWNLOADING);
	}
	
	/*
	 * Cancel the downloader
	 */
	public void cancel() {
		setState(CANCELLED);
	}
	
	/*
	 * Getter for url (String)
	 * @return String
	 */
	public String getURL() {
		return url.toString();
	}
	
	/*
	 * Getter for fileSize
	 * @return int
	 */
	public int getFileSize() {
		return fileSize;
	}
	
	/*
	 * Return download progress
	 * @return float
	 */
	public float getProgress() {
		return ((float)downloadedSize / fileSize) * 100;
	}
	
	/*
	 * Getter for state
	 * @return int
	 */
	public int getState() {
		return state;
	}
	
	/*
	 * Setter for state
	 * @param value
	 */
	protected void setState(int value) {
		state = value;
	}
	
	/*
	 * Start or resume download (Downloader)
	 */
	protected void download() {
		Thread t = new Thread(this);
		t.start();
	}
	
	/*
	 * Increment download size
	 * @param value
	 */
	protected synchronized void downloaded(int value) {
		downloadedSize += value;
		stateChanged();
	}
	
	/*
	 * Set the state changed and notify observers
	 */
	protected void stateChanged() {
		setChanged();
		notifyObservers();
	}
	
	/*
	 * Thread to download parts of a file
	 */
	protected abstract class DownloadThread implements Runnable {
		protected int threadId;
		protected URL url;
		protected String outputFile;
		protected int startByte;
		protected int endByte;
		protected boolean isFinished;
		protected Thread thread;
		
		/*
		 * Constructor
		 * @param threadID
		 * @param url
		 * @param outputFile
		 * @param startByte
		 * @param endByte
		 */
		public DownloadThread(int threadID, URL url, String outputFile, int startByte, int endByte) {
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
		public boolean isDownloadFinished() {
			return isFinished;
		}
		
		/*
		 * Start or resume download (DownloadThread)
		 */
		public void download() {
			thread = new Thread(this);
			thread.start();
		}
		
		/*
		 * Waiting for thread to finish
		 * @throws InterruptedException
		 */
		public void waitForFinish() throws InterruptedException {
			thread.join();
		}
	}
}
