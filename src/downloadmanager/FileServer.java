package downloadmanager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static downloadmanager.DownloadManager.verifyURL;

class FileServer extends Thread {
    private DownloadManagerGUI dmg;
    private static final Logger logger = Logger.getLogger(FileServer.class.getName());
    private static String file = DownloadManager.DEFAULT_OUTPUT_FOLDER + File.separator;
    private static PrintWriter out;
    private static Socket clientSock;

    private ServerSocket ss;

    private FileServer(int port) {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void initSendFile(String fileName){
        file = DownloadManager.DEFAULT_OUTPUT_FOLDER + File.separator + fileName;
        try {
            out = new PrintWriter(clientSock.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File f = new File(file);
        out.println("FILE"+Long.toString(f.length()));
        try {
            sendFile(clientSock,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFile(Socket clientSock, String file) throws IOException {
        DataOutputStream dos = new DataOutputStream(clientSock.getOutputStream());
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4096];

        while (fis.read(buffer) > 0) {
            dos.write(buffer);
        }

        fis.close();
        dos.close();
    }

    public static void main(String[] args) {
        FileServer fs = new FileServer(1988);
        fs.start();
    }

    public void run() {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e){
            logger.log(Level.SEVERE, "UI failed", e);
        }
        EventQueue.invokeLater(() -> {
            dmg = new DownloadManagerGUI();
            dmg.setVisible(true);
        });

        while (true) {
            try {
                clientSock = ss.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        clientSock.getInputStream()));
                String name = in.readLine();
                URL url;

                if(name.startsWith("FILE")){
                    file = name.substring(4);
                }

                if((url = verifyURL(file)) == null){
                    System.out.println("Invalid url");
                    continue;
                }

                dmg.setTextURLText(file);
                file = url.getFile();
                StringBuilder sb = new StringBuilder(), sb1 = new StringBuilder();
                sb.append(file);
                sb.reverse();
                file = sb1.append(sb.substring(0, sb.indexOf("/") + 1)).reverse().toString();
                file = DownloadManager.DEFAULT_OUTPUT_FOLDER + file;
            } catch(SocketException e){
                logger.log(Level.WARNING, "Socket disconnected", e);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Exception while reading stream", e);
            }
        }
    }

}
