//package downloadmanager;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class HTTPSDownloader extends Downloader {
//	private static final long serialVersionUID = 5870653143064831649L;
//
//	public HTTPSDownloader(URL url, String outputFolder, int numConnections) {
//		super(url, outputFolder, numConnections);
//		download();
//	}
//
//	private void error() {
//		System.out.println("ERROR");
//		setState(ERROR);
//	}
//
//	@Override
//	public void run() {
//		SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
//		URL url = new URL("https://www.antennahouse.com/XSLsample/pdf/sample-link_1.pdf");
//		int response = HttpsURLConnection.HTTPS_NOT_FOUND;
//		FileOutputStream outputStream;
//		try{
//			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
//			conn.setSSLSocketFactory(sslsocketfactory);
//		}
//		catch(IOException e){
//				System.out.println("Error establishing connection. Try again later");
//				return;
//			}
//
//			if(response == HttpsURLConnection.HTTPS_OK){
//				String fileName = "";
//	            String disposition = httpsCon.getHeaderField("Content-Disposition");
//	            String contentType = httpsCon.getContentType();
//	            int contentLength = httpsCon.getContentLength();
//
//	            if (disposition != null) {
//	                int index = disposition.indexOf("filename=");
//	                if (index > 0) {
//	                    fileName = disposition.substring(index + 10, disposition.length() - 1);
//	                }
//	            }
//	            else {
//	                // extracts file name from URL
//	                fileName = url.toString().substring(url.toString().lastIndexOf("/") + 1, url.toString().length());
//	            }
//
//	            System.out.println("Content-Type = " + contentType);
//	            System.out.println("Content-Disposition = " + disposition);
//	            System.out.println("Content-Length = " + contentLength);
//	            System.out.println("File Name = " + fileName);
//
//	            // opens input stream from the HTTPS connection
//	            InputStream inputStream;
//				try {
//					inputStream = httpsCon.getInputStream();
//				}
//				catch (IOException e) {
//					Logger.getLogger(HTTPSDownloadThread.class.getName()).log(Level.SEVERE, "Unable to read data", e);
//					return;
//				}
//	            String saveFilePath = outputFile + File.separator + fileName;
//
//	            // opens an output stream to save into file
//	            outputStream = null;
//	            try {
//	            	outputStream = new FileOutputStream(saveFilePath);
//	            }
//	            catch(FileNotFoundException fofE) {
//	            	saveFilePath = DownloadManager.DEFAULT_OUTPUT_FOLDER + File.separator + fileName;
//	            	System.out.println("Destination path not found. Saving to Default "+saveFilePath);
//	            	try{
//						outputStream = new FileOutputStream(saveFilePath);
//					}
//					catch(FileNotFoundException e){
//						Logger.getLogger(HTTPSDownloadThread.class.getName()).log(Level.SEVERE, "Output stream failed",
//								e);
//					}
//	            }
//
//	            int bytesRead = -1;
//	            byte[] buffer = new byte[BUFFER_SIZE];
//	            try{
//					while ((bytesRead = inputStream.read(buffer)) != -1)  {
//					    try{
//					    	if(outputStream != null) {
//					    		outputStream.write(buffer, 0, bytesRead);
//					    	}
//						}
//						catch(IOException e){
//							Logger.getLogger(HTTPSDownloadThread.class.getName()).log(Level.SEVERE, "Failed to write",
//									e);
//						}
//					}
//				}
//				catch(IOException e1){
//					Logger.getLogger(HTTPSDownloadThread.class.getName()).log(Level.SEVERE, "Failed to read", e1);
//				}
//
//	            try{
//	            	if(outputStream != null) {
//	            		outputStream.close();
//	            	}
//		            inputStream.close();
//				}
//				catch(IOException e){
//					Logger.getLogger(HTTPSDownloadThread.class.getName()).log(Level.WARNING, "Unable to close streams",
//							e);
//				}
//
//	            System.out.println("File downloaded");
//	        }
//
//			else {
//	            System.out.println("No file to download. Server replied HTTPS code: " + response);
//	        }
//	        httpsCon.disconnect();
//	    }
//	}
//
//}