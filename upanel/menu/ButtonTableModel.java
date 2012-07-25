package upanel.menu;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

//////
public class ButtonTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int COLUMN = 4;
	private static int ROW = 7;

	private static ButtonTableModel instance = new ButtonTableModel();

	public static ButtonTableModel getInstance() {

		return instance;
	}

	private final String header[] = { "", "Command", "Label", "Key" };
	private final String data[][] = { { "Up", "A", "UP", "W" }, { "Left", "L", "LF", "A" }, { "Right", "D", "RG", "D" }, { "Down", "S", "DW", "S" }, { "Dw Left", "X", "DL", "Q" }, { "Dw Middle", "Y", "DM", "Z" }, { "Dw Right", "Z", "DR", "E" } };

	private TableModelListener listener;

	@Override
	public int getColumnCount() {

		// TODO Auto-generated method stub

		return COLUMN;
	}

	@Override
	public String getColumnName(int column) {

		return header[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		// TODO Auto-generated method stub
		return data[0][columnIndex].getClass();
	}

	@Override
	public int getRowCount() {

		// TODO Auto-generated method stub
		return ROW;
	}

	@Override
	public Object getValueAt(int row, int column) {

		return data[row][column];
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

		if (columnIndex == 3) {
			final String s = aValue.toString();
			if (s.length() > 1)
				aValue = new Character(s.charAt(0)).toString();
		}
		listener.tableChanged(new TableModelEvent(this));
		data[rowIndex][columnIndex] = aValue.toString();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {

		if (columnIndex > 0)
			return true;

		return false;
	}

	public String[] getRowData(int row) {

		final String str[] = new String[COLUMN];

		str[0] = data[row][0];
		str[1] = data[row][1];
		str[2] = data[row][2];
		str[3] = data[row][3];

		return str;
	}

	@Override
	public void addTableModelListener(TableModelListener l) {

		// TODO Auto-generated method stub
		this.listener = l;
	}

	private ButtonTableModel() {

	}

}