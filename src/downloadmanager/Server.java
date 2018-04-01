package downloadmanager;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Server extends Thread {
	private DataInputStream dis = null;
	private FileOutputStream fos = null;
	
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	
	static final Logger logger = Logger.getLogger(Server.class.getName());

	/*
	 * Constructor
	 * @param port
	 */
	public Server(int port) {
		try{
			serverSocket = new ServerSocket(port);
		}
		catch(IOException e){
			logger.log(Level.SEVERE, "Failed to create server socket", e);
		}
	}


	@Override
	public void run(){
		/*
		 * Run GUI and send downloaded file to client
		 */
		try{
			clientSocket = serverSocket.accept();
			logger.log(Level.INFO, "Client connection established");
			
			try{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch(ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e){
				logger.log(Level.SEVERE, "UI failed", e);
			}
			EventQueue.invokeLater(() -> 
					new DownloadManagerGUI().setVisible(true)
					// TODO: Send file to client
				);
			
		}
		catch(IOException e){
			logger.log(Level.SEVERE, "Client socket failed", e);
		}
		finally{
			if(dis != null) {
				try{
					dis.close();
				}
				catch(IOException e){
					logger.log(Level.WARNING, "DataInputStream failed to close", e);
					dis = null;
				}
			}
			if(fos != null) {
				try{
					fos.close();
				}
				catch(IOException e){
					logger.log(Level.WARNING, "FileOutputStream failed to close", e);
					fos = null;
				}
			}
			if(clientSocket != null) {
				try{
					clientSocket.close();
				}
				catch(IOException e){
					logger.log(Level.WARNING, "Client socket failed to close", e);
					clientSocket = null;
				}
			}
			if(serverSocket != null) {
				try{
					serverSocket.close();
				}
				catch(IOException e){
					logger.log(Level.WARNING, "Server socket failed to close", e);
					serverSocket = null;
				}
			}
		}
	}
	
	/*
	 * Receive URL from client
	 */
	public String receiveURL(Socket s) {
		String url = "";
		try{
			dis = new DataInputStream(s.getInputStream());
			url = dis.readUTF();
		}
		catch(IOException e){
			logger.log(Level.SEVERE, "IO Exception occurred while receiving client input stream", e);
			logger.log(Level.WARNING, "DataInputStream produced exception", e);
		}
		return url;
	}

	public static void main(String[] args) {
		Server server = new Server(10000);
		// TODO: Figure out URL receive thing
		server.start();
	}
}