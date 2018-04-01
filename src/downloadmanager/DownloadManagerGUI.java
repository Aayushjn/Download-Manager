package downloadmanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DownloadManagerGUI extends JFrame implements Observer {
	private static final long serialVersionUID = -9009871315599711783L;
	
	private DownloadTableModel tableModel;
	private Downloader selectedDownloader;
	private boolean isClearing;
	
	private JScrollPane scrollPane;
	private JButton addButton;
	private JButton cancelButton;
	private JButton exitButton;
	private JButton pauseButton;
	private JButton removeButton;
	private JButton resumeButton;
	private JTable downloadTable;
	private JTextField textURL;
	
	/*
	 * Constructor
	 */
	public DownloadManagerGUI() {
		initComponents();
		initialize();
	}
	
	/*
	 * Initialize all the members, their relative positions in the GUI, and ActionListener(s) on the buttons
	 */
	private void initComponents() {
		scrollPane = new JScrollPane();
		addButton = new JButton();
		cancelButton = new JButton();
		exitButton = new JButton();
		pauseButton = new JButton();
		removeButton = new JButton();
		resumeButton = new JButton();
		textURL = new JTextField();
		downloadTable = new JTable();
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Download Manager");
		setResizable(false);
		
		downloadTable.setModel(tableModel);				// Set the data model
		scrollPane.setViewportView(downloadTable);		// Allows downloadTable to be scrollable
		
		addButton.setText("Add Download");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event){
				addButtonActionPerformed();				
			}
		});
		
		cancelButton.setText("Cancel");
		cancelButton.setEnabled(false);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event){
				cancelButtonActionPerformed();
			}
		});
		
		exitButton.setText("Exit");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event){
				exitButtonActionPerformed();
			}
		});
		
		pauseButton.setText("Pause");
		pauseButton.setEnabled(false);
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event){
				pauseButtonActionPerformed();
			}
		});
		
		removeButton.setText("Remove");
		removeButton.setEnabled(false);
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event){
				removeButtonActionPerformed();
			}
		});
		
		resumeButton.setText("Resume");
		resumeButton.setEnabled(false);
		resumeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event){
				resumeButtonActionPerformed();
			}
		});
		
		// GUI Layout configuration
		GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pauseButton, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(resumeButton, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(removeButton, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 177, Short.MAX_VALUE)
                        .addComponent(exitButton, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(textURL, GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addButton))
                    .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE))
                .addContainerGap())
        );
        
        layout.linkSize(SwingConstants.HORIZONTAL, cancelButton, exitButton, pauseButton, removeButton,
        		resumeButton);

        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(textURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, 
                    		GroupLayout.PREFERRED_SIZE)
                    .addComponent(addButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(pauseButton)
                    .addComponent(resumeButton)
                    .addComponent(cancelButton)
                    .addComponent(removeButton)
                    .addComponent(exitButton))
                .addContainerGap())
        );

        pack();
	}
	
	/*
	 * Initialize Download Table properties and setup renderer
	 */
	private void initialize() {
		downloadTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event0){
				tableSelectionChanged();
			}
		});
		
		downloadTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		ProgressBar renderer = new ProgressBar(0, 100);
		renderer.setStringPainted(true);
		downloadTable.setDefaultRenderer(JProgressBar.class, renderer);
		
		downloadTable.setRowHeight((int)renderer.getPreferredSize().getHeight());
	}
	
	/*
	 * Add download on button press
	 */
	private void addButtonActionPerformed() {
		URL verifiedURL = DownloadManager.verifyURL(textURL.getText());
		if(verifiedURL == null) {
			selectedDownloader download = DownloadManager.getInstance().createDownload(verifiedURL, 
					DownloadManager.DEFAULT_OUTPUT_FOLDER);
			tableModel.addNewDownload(download);
			textURL.setText("");
		}
		else {
			JOptionPane.showMessageDialog(this, "Invalid Download URL", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/*
	 * Cancel download on button press
	 */
	private void cancelButtonActionPerformed() {
		selectedDownloader.cancel();
		updateButtons();
	}
	
	/*
	 * Exit GUI on button press
	 */
	private void exitButtonActionPerformed() {
		setVisible(false);
	}
	
	/*
	 * Pause download on button press
	 */
	private void pauseButtonActionPerformed() {
		selectedDownloader.pause();
		updateButtons();
	}
	
	/*
	 * Remove download on button press
	 */
	private void removeButtonActionPerformed() {
		isClearing = true;
		int index = downloadTable.getSelectedRow();
		DownloadManager.getInstance().removeDownload(index);
		tableModel.clearDownload(index);
		isClearing = false;
		selectedDownloader = null;
		updateButtons();
	}
	
	/*
	 * Resume download on button press
	 */
	private void resumeButtonActionPerformed() {
		selectedDownloader.resume();
		updateButtons();
	}
	
	/*
	 * Modify observer(s) and update buttons
	 */
	private void tableSelectionChanged() {
		if(selectedDownloader != null) {
			selectedDownloader.deleteObserver(DownloadManagerGUI.this);
		}
		
		if(!isClearing) {
			int index = downloadTable.getSelectedRow();
			if(index != -1) {
				selectedDownloader = DownloadManager.getInstance().getDownload(downloadTable.getSelectedRow());
				selectedDownloader.addObserver(DownloadManagerGUI.this);
			}
			else {
				selectedDownloader = null;
			}
			updateButtons();
		}
	}
	
	/*
	 * Update button visibility
	 */
	private void updateButtons(){
		if(selectedDownloader != null) {
			int state = selectedDownloader.getState();
			switch(state){
				case Downloader.DOWNLOADING:
					pauseButton.setEnabled(true);
					resumeButton.setEnabled(false);
					cancelButton.setEnabled(true);
					removeButton.setEnabled(false);
				break;
				case Downloader.PAUSED:
					pauseButton.setEnabled(false);
					resumeButton.setEnabled(true);
					cancelButton.setEnabled(true);
					removeButton.setEnabled(false);
				break;
				case Downloader.ERROR:
					pauseButton.setEnabled(false);
					resumeButton.setEnabled(true);
					cancelButton.setEnabled(false);
					removeButton.setEnabled(true);
				break;
				default:
					pauseButton.setEnabled(false);
					resumeButton.setEnabled(false);
					cancelButton.setEnabled(false);
					removeButton.setEnabled(true);
			}
		}
		else {
			pauseButton.setEnabled(false);
			resumeButton.setEnabled(false);
			cancelButton.setEnabled(false);
			removeButton.setEnabled(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * @param java.util.Observable o
	 * @param java.lang.Object obj
	 * Update buttons on Observer update
	 */
	@Override
	public void update(Observable o, Object obj){
		if (selectedDownloader != null && selectedDownloader.equals(obj))
            updateButtons();
	}
}
