import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

/**
 * 
 */

/**
 * 
 *
 */
public class DataGrid extends JTable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataGrid(DataGridView dataGridView){
		super(dataGridView);
	  JTableHeader header = this.getTableHeader();
	  header.setBackground(Color.GRAY);
	  header.setForeground(Color.WHITE);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
