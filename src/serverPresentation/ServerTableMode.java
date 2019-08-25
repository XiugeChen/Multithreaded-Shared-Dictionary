package serverPresentation;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public abstract class ServerTableMode extends AbstractTableModel {
	private ArrayList<String> columnNames;
	private ArrayList<ArrayList<String>> tableData;
	
	public ServerTableMode() {
		columnNames = new ArrayList<>();
		tableData = new ArrayList<>();
	}
	
	public void addValue(ArrayList<String> value) {
		boolean add = true;
		for (ArrayList<String> row: tableData) {
			boolean identical = true;
			
			for (int i = 0; i < row.size(); i++) {
				if (!row.get(i).equals(value.get(i))) {
					identical = false;
					break;
				}
			}
			
			if (identical) {
				add = false;
				break;
			}
		}
		
		if (add) {
			tableData.add(value);
			this.fireTableDataChanged();
		}
	}
	
	public void removeValue(ArrayList<String> value) {
		boolean remove = false;
		int i = 0;
		
		for (i = 0; i < tableData.size(); i++) {
			ArrayList<String> row = tableData.get(i);
			boolean identical = true;
			
			for (int j = 0; j < row.size(); j++) {
				if (!value.get(j).equals("SKIP") && !row.get(j).equals(value.get(j))) {
					identical = false;
					break;
				}
			}
			
			if (identical) {
				remove = true;
				break;
			}
		}
		
		if (remove) {
			tableData.remove(i);
			this.fireTableDataChanged();
		}
	}
	
	public ArrayList<String> getColumnNames() {
		return columnNames;
	}
	
	public ArrayList<ArrayList<String>> getTableData() {
		return tableData;
	}
	
	@Override
	public String getColumnName(int col) {
        return columnNames.get(col);
    }
	
	@Override
	public boolean isCellEditable(int row, int col) {
        return false;
    }

	@Override
	public int getRowCount() {
		return tableData.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return tableData.get(rowIndex).get(columnIndex);
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		tableData.get(row).set(col, value.toString());
        fireTableCellUpdated(row, col);
    }
}
