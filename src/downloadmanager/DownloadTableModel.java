package downloadmanager;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

public class DownloadTableModel extends AbstractTableModel implements Observer {
	private static final long serialVersionUID = 4382612586571250137L;
	
	private static final String[] columnNames = {"URL", "Size (KB)", "Progress", "Status"};
	
	@SuppressWarnings("rawtypes")
	private static final Class[] columnClasses = {String.class, String.class, JProgressBar.class, String.class};
	
	/*
	 * Adds a new download
	 * @param Downloader download
	 */
	public void addNewDownload(Downloader download) {
		download.addObserver(this);
		
		fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
	}
	
	/*
	 * Clears a previous existing download
	 * @param row
	 */
	public void clearDownload(int row) {
		fireTableRowsDeleted(row, row);
	}
	
	/*
	 * (non-Javadoc)
	 * @return int Returns number of columns
	 */
	public int getColumnCount() {
		return columnNames.length;
	}
	
	/*
	 * (non-Javadoc)
	 * @param col
	 * @return String Returns column name of a particular column
	 */
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	/*
	 * (non-Javadoc)
	 * @param col
	 * @return Class Returns class of a particular column
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public Class getColumnClass(int col) {
		return columnClasses[col];
	}
	
	/*
	 * (non-Javadoc)
	 * @return int Returns the number of rows in download list
	 */
	public int getRowCount() {
		return DownloadManager.getInstance().getDownloadList().size();
	}
	
	/*
	 * (non-Javadoc)
	 * @param row
	 * @param col
	 * @return Object Returns the 'object' in the table at (row, col)
	 */
	public Object getValueAt(int row, int col) {
		Downloader download = DownloadManager.getInstance().getDownloadList().get(row);
		
		switch(col) {
			case 0:
				return download.getURL();
			case 1:
				int size = download.getFileSize();
				return (size == -1) ? "" : (Integer.toString(size / 1000));
			case 2:
				return Float.valueOf(download.getProgress());
			case 3:
				return Downloader.STATUSES[download.getState()];
			default:
				return "";
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @param o
	 * @param obj
	 * Update table entries corresponding to o's index
	 */
	public void update(Observable o, Object obj) {
		int index = DownloadManager.getInstance().getDownloadList().indexOf(o);
		
		fireTableRowsUpdated(index, index);
	}
}
