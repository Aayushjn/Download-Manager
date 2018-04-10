package downloadmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HTTPDownloader extends Downloader {
	private static final long serialVersionUID = 5870653143064831649L;

	public HTTPDownloader(URL url, String outputFolder, int numConnections) {
		super(url, outputFolder, numConnections);
		download();
	}

	private void error() {
		System.out.println("ERROR");
		setState(ERROR);
	}

	@Override
	public void run() {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(10000);

			conn.connect();

			if(conn.getResponseCode() / 100 != 2) {
				error();
			}

			int contentLength = conn.getContentLength();
			if(contentLength < 1) {
				error();
			}

			if (fileSize == -1) {
				fileSize = contentLength;
				stateChanged();
				System.out.println("File size: " + fileSize);
			}

			if (state == DOWNLOADING) {
				if (downloadThreadList.isEmpty()) {  
					if (fileSize > MIN_DOWNLOAD_SIZE) {
						int partSize = Math.round(((float)fileSize / numConnections) / BLOCK_SIZE) * BLOCK_SIZE;
						System.out.println("Part size: " + partSize);

						// start/end Byte for each thread
						int startByte = 0;
						int endByte = partSize - 1;
						HTTPDownloadThread httpThread = new HTTPDownloadThread(1, url, outputFolder + fileName, 
								startByte, endByte);
						downloadThreadList.add(httpThread);
						int i = 2;
						while(endByte < fileSize) {
							startByte = endByte + 1;
							endByte += partSize;
							httpThread = new HTTPDownloadThread(i, url, outputFolder + fileName, startByte, endByte);
							downloadThreadList.add(httpThread);
							++i;
						}
					}
					else {
						HTTPDownloadThread httpThread = new HTTPDownloadThread(1, url, outputFolder + fileName, 0, 
								fileSize);
						downloadThreadList.add(httpThread);
					}
				}
				else { 
					for(int i = 0;i < downloadThreadList.size(); i++) {
						if(!downloadThreadList.get(i).isDownloadFinished())
							downloadThreadList.get(i).download();
					}
				}


				for(int i = 0;i < downloadThreadList.size();i++) {
					downloadThreadList.get(i).waitForFinish();
				}


				if(state == DOWNLOADING) {
					setState(COMPLETE);
				}
			}
		}
		catch(Exception e) {
			error();
		}
		finally {
			if(conn != null)
				conn.disconnect();
		}
	}

	private class HTTPDownloadThread extends DownloadThread {
		public HTTPDownloadThread(int threadID, URL url, String outputFile, int startByte, int endByte) {
			super(threadID, url, outputFile, startByte, endByte);
		}

		@Override
		public void run(){
			HttpURLConnection httpCon;
			int response = HttpURLConnection.HTTP_NOT_FOUND;
			FileOutputStream outputStream;
			
			try{
				httpCon = (HttpURLConnection) url.openConnection();
				response = httpCon.getResponseCode();
			}
			catch(IOException e){
				System.out.println("Error establishing connection. Try again later");
				return;
			}
			
			if(response == HttpURLConnection.HTTP_OK){
				String fileName = "";
	            String disposition = httpCon.getHeaderField("Content-Disposition");
	            String contentType = httpCon.getContentType();
	            int contentLength = httpCon.getContentLength();
	 
	            if (disposition != null) {
	                int index = disposition.indexOf("filename=");
	                if (index > 0) {
	                    fileName = disposition.substring(index + 10, disposition.length() - 1);
	                }
	            } 
	            else {
	                // extracts file name from URL
	                fileName = url.toString().substring(url.toString().lastIndexOf("/") + 1, url.toString().length());
	            }
	 
	            System.out.println("Content-Type = " + contentType);
	            System.out.println("Content-Disposition = " + disposition);
	            System.out.println("Content-Length = " + contentLength);
	            System.out.println("File Name = " + fileName);
	 
	            // opens input stream from the HTTP connection
	            InputStream inputStream;
				try {
					inputStream = httpCon.getInputStream();
				} 
				catch (IOException e) {
					Logger.getLogger(HTTPDownloadThread.class.getName()).log(Level.SEVERE, "Unable to read data", e);
					return;
				}
	            String saveFilePath = outputFile + File.separator + fileName;
	             
	            // opens an output stream to save into file
	            outputStream = null;
	            try {
	            	outputStream = new FileOutputStream(saveFilePath);
	            }
	            catch(FileNotFoundException fofE) {
	            	saveFilePath = DownloadManager.DEFAULT_OUTPUT_FOLDER + File.separator + fileName;
	            	System.out.println("Destination path not found. Saving to Default "+saveFilePath);
	            	try{
						outputStream = new FileOutputStream(saveFilePath);
					}
					catch(FileNotFoundException e){
						Logger.getLogger(HTTPDownloadThread.class.getName()).log(Level.SEVERE, "Output stream failed", 
								e);
					}
	            }
	 
	            int bytesRead = -1;
	            byte[] buffer = new byte[BUFFER_SIZE];
	            try{
					while ((bytesRead = inputStream.read(buffer)) != -1)  {
					    try{
					    	if(outputStream != null) {
					    		outputStream.write(buffer, 0, bytesRead);
					    	}
						}
						catch(IOException e){
							Logger.getLogger(HTTPDownloadThread.class.getName()).log(Level.SEVERE, "Failed to write",
									e);
						}
					}
				}
				catch(IOException e1){
					Logger.getLogger(HTTPDownloadThread.class.getName()).log(Level.SEVERE, "Failed to read", e1);
				}
	 
	            try{
	            	if(outputStream != null) {
	            		outputStream.close();
	            	}
		            inputStream.close();
				}
				catch(IOException e){
					Logger.getLogger(HTTPDownloadThread.class.getName()).log(Level.WARNING, "Unable to close streams", 
							e);
				}
	 
	            System.out.println("File downloaded");
	        } 
			
			else {
	            System.out.println("No file to download. Server replied HTTP code: " + response);
	        }
	        httpCon.disconnect();
	    }
	}
}
