package com.aayush.view;

import com.aayush.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Dialog that allows the user to add a new download
 *
 * @author Aayush
 * @see javax.swing.JDialog
 */
class DownloadDialog extends JDialog {
    /**
     * Ok Button
     */
    private final JButton okButton;
    /**
     * Cancel button
     */
    private final JButton cancelButton;
    /**
     * Button to select directories
     */
    private final JButton selectButton;
    /**
     * Text field to write the download URL
     */
    private final JTextField urlField;
    /**
     * Text field to enter the download directory
     */
    private final JTextField directoryField;

    /**
     * Chooser that allows user to select directories
     */
    private final JFileChooser directoryChooser;

    /**
     * Constructor
     *
     * @param parent parent of the this dialog
     */
    DownloadDialog(JFrame parent) {
        super(parent, "Add new download", true);

        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        selectButton = new JButton("...");
        urlField = new JTextField(20);
        urlField.setVisible(true);
        directoryField = new JTextField(20);

        directoryChooser = new JFileChooser(Constants.DEFAULT_OUTPUT_FOLDER);
        directoryChooser.setDialogTitle("Select a directory");
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        directoryChooser.setAcceptAllFileFilterUsed(false);

        directoryField.setText(Constants.DEFAULT_OUTPUT_FOLDER);

        cancelButton.addActionListener(e -> setVisible(false));
        selectButton.addActionListener(e -> {
            if (directoryChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                directoryField.setText(directoryChooser.getSelectedFile().getPath());
            }
            else {
                directoryField.setText(Constants.DEFAULT_OUTPUT_FOLDER);
            }
        });

        addToView();

        setSize(400, 230);
        setLocationRelativeTo(parent);
    }

    /**
     * Adds an action listener to the {@code okButton}
     *
     * @param listener listener to be added
     */
    void addActionListener(ActionListener listener) {
        okButton.addActionListener(listener);
    }

    /**
     * This method adds all components to view
     */
    private void addToView() {
        setLayout(new BorderLayout());

        // <============ Controls Panel ============>
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        // <============ First row ============>
        gc.gridy = 0;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 15);
        controlsPanel.add(new JLabel("URL:"), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        controlsPanel.add(urlField, gc);

        // <============ Next row ============>
        gc.gridy++;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 15);
        controlsPanel.add(new JLabel("Folder:"), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(0, 0, 0, 15);
        controlsPanel.add(directoryField, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        controlsPanel.add(selectButton, gc);

        // <============ Buttons Panel ============>
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        Dimension buttonSize = cancelButton.getPreferredSize();
        okButton.setPreferredSize(buttonSize);
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);

        add(controlsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    /**
     * Returns the URL entered
     *
     * @return download URL
     */
    String getUrlText() {
        return urlField.getText();
    }

    /**
     * Returns the output folder
     *
     * @return output folder
     */
    String getSelectedFile() {
        return directoryField.getText();
    }
}
