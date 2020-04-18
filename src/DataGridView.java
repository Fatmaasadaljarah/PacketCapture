import javax.swing.table.DefaultTableModel;

/**
 * 
 */

/**
 *
 *
 */
public class DataGridView extends DefaultTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String [] columnNames;

	public DataGridView(String[] columnNames) {
		super(null,columnNames);	
		this.columnIdentifiers = convertToVector(columnNames);
	}
	
	public void addPacket(CapturedPacket capturedPacket){
	//	this.insertRow(0,capturedPacket.getRowRecord());
		this.addRow(capturedPacket.getRowRecord());
	}
	
	
}
