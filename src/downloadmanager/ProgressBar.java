package downloadmanager;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class ProgressBar extends JProgressBar implements TableCellRenderer {
	private static final long serialVersionUID = -199728233636903225L;
	
	/*
	 * Constructor
	 * @param min
	 * @param max
	 */
	ProgressBar(int min, int max) {
		super(min, max);
	}

	/*
	 * (non-Javadoc)
	 * @param javax.swing.JTable table
	 * @param java.lang.Object value
	 * @param boolean isSelected
	 * @param boolean hasFocus
	 * @param int row
	 * @param int column
	 * @return Component Returns ProgressBar after updating value
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, 
			int row, int column){
		setValue((int) ((Float) value).floatValue());
		return this;
	}
	
}
