package downloadmanager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadManager{
	private static DownloadManager instance = null;
	
	private static final int DEFAULT_NUM_CONN_PER_DOWNLOAD = 8;
	public static final String DEFAULT_OUTPUT_FOLDER = "C:\\Users\\aayus\\Desktop";
	
	private int numConnPerDownload;
	private List<Downloader> downloadList;
	
	/*
	 * Constructor
	 */
	protected DownloadManager() {
		numConnPerDownload = DEFAULT_NUM_CONN_PER_DOWNLOAD;
		downloadList = new ArrayList<>();
	}
	
	/*
	 * Getter for numConnPerDownload
	 * @return int
	 */
	public int getNumConnPerDownload() {
		return numConnPerDownload;
	}
	
	/*
	 * Setter for numConnPerDownload
	 * @param num
	 */
	public void setNumConnPerDownload(int num) {
		numConnPerDownload = num;
	}
	
	/*
	 * Get download at index
	 * @param index
	 * @return Downloader
	 */
	public Downloader getDownload(int index) {
		return downloadList.get(index);
	}
	
	/*
	 * Remove download at index from download list
	 * @param index
	 */
	public void removeDownload(int index) {
		downloadList.remove(index);
	}
	
	/*
	 * Getter for downloadList
	 * @return List<Downloader>
	 */
	public List<Downloader> getDownloadList(){
		return downloadList;
	}
	
	/*
	 * Create download on the basis of protocol and return it
	 * @param verifiedURL
	 * @param outputFolder
	 * @return Downloader
	 */
	public Downloader createDownload(URL verifiedURL, String outputFolder) {
		Downloader fd = null;
		//if(verifiedURL.toString().toLowerCase().startsWith("http")){
			fd = new HTTPDownloader(verifiedURL, outputFolder, numConnPerDownload);
			downloadList.add(fd);
		//}
		//else if("https".equals(verifiedURL.getProtocol())) {
			// TODO: Integrate HTTPS downloader
		//}
		return fd;
	}
	
	/*
	 * Getter for instance
	 * @return DownloadManager
	 */
	public static DownloadManager getInstance() {
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
	public static URL verifyURL(String fileURL) {
		if(!(fileURL.toLowerCase().startsWith("http://") ||  fileURL.toLowerCase().startsWith("https://"))){
			return null;
		}
		
		URL verifiedURL = null;
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
}
