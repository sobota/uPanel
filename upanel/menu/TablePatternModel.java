package upanel.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

public class TablePatternModel extends AbstractTableModel implements Serializable {

	private static final long serialVersionUID = 1;

	private static TablePatternModel instance = new TablePatternModel();
	private String name;

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	private final int COLUMN = 4;

	private int ROW = 1;

	private final String header[] = { "", "Pattern", "Action", "Next" };
	private String[][] data = { { "1", "", "Date Time", "Log To File" } };

	private int id = 1;

	public static TablePatternModel getInstance() {

		return instance;
	}

	@Override
	public int getColumnCount() {

		// TODO Auto-generated method stub

		return COLUMN;
	}

	private final List<String> editableList = new ArrayList<String>();

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

		// TODO Auto-generated method stub

		if (editableList.contains(new Integer(rowIndex).toString() + ";" + new Integer(columnIndex).toString())
			&& !aValue.toString().equals("Delete")) {
			editableList.remove(new Integer(rowIndex).toString() + ";" + new Integer(columnIndex).toString());
		}

		if (aValue.toString().equals("Delete")) {

			setValueAt("", rowIndex, columnIndex + 1);

			editableList.add(new Integer(rowIndex).toString() + ";" + new Integer(columnIndex).toString());
		}

		data[rowIndex][columnIndex] = (String) aValue;
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		// TODO Auto-generated method stub

		return data[0][columnIndex].getClass();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {

		// TODO Auto-generated method stub
		final Iterator<String> iter = editableList.iterator();

		while (iter.hasNext()) {

			final String str[] = iter.next().split(";");

			if (new Integer(str[0]).intValue() == rowIndex && new Integer(str[1]).intValue() + 1 == columnIndex)
				return false;
		}

		if (columnIndex == 0)
			return false;

		return true;
	}

	@Override
	public String getColumnName(int column) {

		// TODO Auto-generated method stub

		return header[column];
	}

	@Override
	public int getRowCount() {

		// TODO Auto-generated method stub
		return data.length;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {

		// TODO Auto-generated method stub
		return data[arg0][arg1];
	}

	public String[] getRowData(int row) {

		final String str[] = new String[COLUMN - 1];

		str[0] = data[row][1];
		str[1] = data[row][2];
		str[2] = data[row][3];

		return str;
	}

	public void addRow(String a[]) {

		final String tempData[][] = new String[data.length + 1][COLUMN];

		for (int i = 0; i < data.length; i++) {

			for (int j = 0; j < COLUMN; j++) {
				tempData[i][j] = data[i][j];
			}

		}

		for (int i = 0; i < COLUMN; i++) {

			tempData[data.length][i] = a[i];
		}
		ROW++;

		this.data = tempData;

		id++;
		data[data.length - 1][0] = new Integer(id).toString();
		fireTableRowsInserted(data.length - 1, data.length - 1);
	}

	@Override
	public String toString() {

		return "TablePatterModel";
	}

	public void addRow() {

		final String tempData[][] = new String[data.length + 1][COLUMN];

		for (int i = 0; i < data.length; i++) {

			for (int j = 0; j < COLUMN; j++) {
				tempData[i][j] = data[i][j];
			}

		}

		for (int i = 0; i < COLUMN; i++) {

			tempData[data.length][i] = "";
		}
		ROW++;

		this.data = tempData;

		id++;
		data[data.length - 1][0] = new Integer(id).toString();

		fireTableRowsInserted(data.length - 1, data.length - 1);

		data[ROW - 1][2] = "Date Time";
		data[ROW - 1][3] = "Log To File";
	}

	public void removeRow() {

		if (ROW == 0)
			return;

		final String tempData[][] = new String[data.length - 1][COLUMN];

		for (int i = 0; i < data.length - 1; i++) {

			for (int j = 0; j < COLUMN; j++) {
				tempData[i][j] = this.data[i][j];
			}
		}
		this.data = tempData;
		ROW--;
		id--;
		fireTableRowsDeleted(data.length + 1, data.length + 1);
	}

	public Set<String[]> getPatternceSet() {

		final Set<String[]> set = new HashSet<String[]>();

		for (int i = 0; i < this.getRowCount(); i++) {

			set.add(this.getRowData(i));
		}
		return set;
	}

	private TablePatternModel() {

	}

}
