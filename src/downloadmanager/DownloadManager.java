package downloadmanager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class DownloadManager{
	static final String DEFAULT_OUTPUT_FOLDER = "C:/Users/aayus/Desktop";
	private static final int DEFAULT_NUM_CONN_PER_DOWNLOAD = 8;
	private static DownloadManager instance = null;
	private int numConnPerDownload;
	private List<Downloader> downloadList;
	
	/*
	 * Constructor
	 */
	private DownloadManager() {
		numConnPerDownload = DEFAULT_NUM_CONN_PER_DOWNLOAD;
		downloadList = new ArrayList<>();
	}

	/*
	 * Getter for instance
	 * @return DownloadManager
	 */
	static DownloadManager getInstance() {
		if(instance == null) {
			return new DownloadManager();
		}
		return instance;
	}
	
	/*
	 * Verifies if a given string is a valid URL and returns it
	 * @param fileURL
	 * @return URL URL version of fileURL
	 */
	static URL verifyURL(String fileURL) {
		if(!(fileURL.toLowerCase().startsWith("http://"))){
			return null;
		}

		URL verifiedURL;
		try{
			verifiedURL = new URL(fileURL);
		}
		catch(MalformedURLException e){
			return null;
		}

		if(verifiedURL.getFile().length() < 2) {
			return null;
		}

		return verifiedURL;
	}
	
	/*
	 * Get download at index
	 * @param index
	 * @return Downloader
	 */
	Downloader getDownload(int index) {
		return downloadList.get(index);
	}
	
	/*
	 * Remove download at index from download list
	 * @param index
	 */
	void removeDownload(int index) {
		downloadList.remove(index);
	}
	
	/*
	 * Getter for downloadList
	 * @return List<Downloader>
	 */
	List<Downloader> getDownloadList(){
		return downloadList;
	}
	
	/*
	 * Create download on the basis of protocol and return it
	 * @param verifiedURL
	 * @param outputFolder
	 * @return Downloader
	 */
	Downloader createDownload(URL verifiedURL, String outputFolder) {
		Downloader fd;
		fd = new HTTPDownloader(verifiedURL, outputFolder, numConnPerDownload);
		downloadList.add(fd);

		return fd;
	}
}
