package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

public class FileClient extends JFrame{
    PrintWriter out;
    private Socket s;
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JPanel controlPanel;
    BufferedReader in;
    JProgressBar jb;
    String file;

    private void prepareGUI(){
        mainFrame = new JFrame("Download Manager");
        mainFrame.setSize(400,400);
        mainFrame.setLayout(new GridLayout(2, 3));

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        headerLabel = new JLabel("", JLabel.CENTER);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);

        mainFrame.setVisible(true);
    }
    private JProgressBar progressBar;
    private JButton startButton;
    private JTextArea outputTextArea;
    private JTextField textField;
    void showProgressBarDemo(){
        textField = new JTextField("",20);
        headerLabel.setText("Download Manager");
        jb = new JProgressBar(0, 100);
        jb.setValue(0);
        jb.setStringPainted(true);
        startButton = new JButton("Start Download");
        outputTextArea = new JTextArea("",5,20);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("FILE"+textField.getText());
                System.out.println("FILE"+textField.getText());
                textField.setText("");
            }
        });
        controlPanel.add(textField);
        controlPanel.add(jb);
        controlPanel.add(startButton);

        controlPanel.add(scrollPane);
        mainFrame.setVisible(true);
    }
    public FileClient(String host, int port) {
        prepareGUI();
        showProgressBarDemo();
        try {
            int size=0;
            s = new Socket(host, port);
            out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    s.getInputStream()));
            String name = in.readLine();
            if(name.startsWith("FILE")){
                size = Integer.parseInt(name.substring(4));
            }
            saveFile(s,size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFile(Socket clientSock,int s) throws IOException {
        DataInputStream dis = new DataInputStream(clientSock.getInputStream());
        FileOutputStream fos = new FileOutputStream("/home/nac/nnasd.pdf");
        byte[] buffer = new byte[4096];
        int read = 0;
        int totalRead = 0;
        int remaining = s;
        int i = 1;
        while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
            totalRead += read;
            remaining -= read;
            fos.write(buffer, 0, read);
            float progress = 100 * ((float) totalRead) / ((float) s);
            jb.setValue((int)progress);
            int j = Math.round(progress);
            if(i!=j){
                i=j;
                outputTextArea.setText(outputTextArea.getText()
                        + String.format("Completed %d %% of task.\n", j));
                System.out.println("read " + totalRead + " bytes."+progress+s);
            }


        }

        fos.close();
        dis.close();
    }


    public static void main(String[] args) {
        FileClient fc = new FileClient("localhost", 1988);
    }

}