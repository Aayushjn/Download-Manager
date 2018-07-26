package com.aayush.view.util.renderer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Progress bar that renders the progress of a download in a {@code JTable}
 *
 * @author Aayush
 * @see javax.swing.JProgressBar
 * @see javax.swing.table.TableCellRenderer
 */
public class ProgressCellRenderer extends JProgressBar implements TableCellRenderer {
    /**
     * Constructor
     *
     * @param min minimum value of the progress bar
     * @param max maximum value of the progress bar
     */
    public ProgressCellRenderer(int min, int max) {
        super(min, max);
        setStringPainted(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        setValue((int) ((Float) value).floatValue());
        return this;
    }
}
