package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer extends Thread {

    private ServerSocket ss;
    PrintWriter out;
    public FileServer(int port) {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                Socket clientSock = ss.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        clientSock.getInputStream()));
                String name = in.readLine();
                String file = "/home/nac/Downloads/REda.pdf";
                if(name.startsWith("FILE")){
                    file = name.substring(4);
                }

                out = new PrintWriter(clientSock.getOutputStream(), true);
                File f = new File(file);
                out.println("FILE"+Long.toString(f.length()));
                sendFile(clientSock,file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendFile(Socket clientSock,String file) throws IOException {
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

}
