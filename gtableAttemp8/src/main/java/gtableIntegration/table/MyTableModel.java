package gtableIntegration.table;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import gtableIntegration.table.rowColumn.ColumnDefinition;
import gtableIntegration.table.rowColumn.RowDefinition;
import gtableIntegration.table.rowColumn.RowItem;

public class MyTableModel extends  AbstractTableModel{
	private static final Logger logger = LoggerFactory.getLogger(MyTableModel.class); 
	ArrayList<RowItem> m_rowData = new ArrayList<RowItem>();

	ArrayList<Integer> m_visibleRowMap = new ArrayList<Integer>();
	ArrayList<String> Map_Column_IDForm = new ArrayList<String>();
	ArrayList<Integer> m_visibleDataColMap = new ArrayList<Integer>();
	private List<ColumnDefinition> colDefs;
	private List<RowDefinition> rowDefs;
	ArrayList<String> columnNames = new ArrayList<String>();
	public MyTableModel(List<ColumnDefinition> colDefs, List<RowDefinition> rowDefs) {
		this.colDefs=colDefs;
		this.rowDefs=rowDefs;
		buildTabla(colDefs);
	}
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		 	return m_visibleRowMap.size();
	}
	@Override
	public String getColumnName(int col) {
		return (String) columnNames.get(col);
	}
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
		 return getDataValueAt(rowIndex, getRealDataColumn(columnIndex));
	}
	public Object getDataValueAt(int row, int col) {
		// System.out.println("GET VALUE
		// AT:CUANT:"+m_cuantitativo+","+row+","+col+","+getRowCount());
		if (getRowCount() < row + 1) {
			System.err.println("Table Form Model:getValueAt, error, no existe el registro " + row);
			return null;
		}
		
		RowItem it = (RowItem) m_rowData.get(getRowIndex(row));
		if (it.getColumnSize() < col + 1) {
			System.err.println("Table Form Model:getValueAt, error, no existe la col, row " + col + "," + row);
			return null;
		}
		
		return it.getColumnData(col);
	}
	private int getRowIndex(int visibleRow) {
		//System.err.println("getRowIndex: visibleRow:"+visibleRow+" m_visibleRowMap:"+m_visibleRowMap);
		return ((Integer) m_visibleRowMap.get(visibleRow)).intValue();
	}
	public int getRealDataColumn(int visCol) {
		Integer col = (Integer) m_visibleDataColMap.get(visCol);
		if (col == null)
			return -1;
		return col.intValue();
	}
	private void buildTabla(List colDefs){
		Iterator iCol = colDefs.iterator();
		int size = 0;
		
		
		
		
		int col = 0;
		while (iCol.hasNext()) {
			ColumnDefinition item = (ColumnDefinition) iCol.next();
			String label = item.getHeaderName();
			columnNames.add(label);
			String ID = item.getId();
			m_visibleDataColMap.add(new Integer(col));
			/* String ID= item.getAttributeValue("ID"); */
			col++;
		//	Map_IDForm_Column.put(ID, new Integer(col));
			Map_Column_IDForm.add(ID);
		}
		buildRows(rowDefs,false);
	}
	public void buildRows(List<?> rows,boolean replace) {
		Iterator<?> itr=rows.iterator();
		while(itr.hasNext()){
			RowDefinition tableRow=(RowDefinition)itr.next();
			setTableRow(tableRow, replace);
		
		}
		
		//updateGUI(true);
	//	fireTableRowsInserted(0, 2);
	}
	public boolean setTableRow(RowDefinition tableRow, boolean replace) {
		
		Iterator<String> itrIdColumns=Map_Column_IDForm.iterator();
		HashMap<String,Object> columnValues=new HashMap<String, Object>();
		while(itrIdColumns.hasNext()){
			String idColumn=itrIdColumns.next();
			
			Object value=tableRow.getDataColumn(idColumn);
		
			columnValues.put(idColumn, value);
		}
		ArrayList<Object> columnData = new ArrayList<Object>();
		int columns=getColumnCount();
		for(int i=0;i<columns;i++){
			String id=getFieldIDFromColumn(i);
			Object value=columnValues.get(id);
			columnData.add(value);
		}
		
		RowItem ritem = buildRowItem(columnData);
		
		return setRowItem(ritem, replace);
	}
	private boolean setRowItem(RowItem ritem, boolean replace) {
		boolean rowAdded=true;
		
		
			int rowToReplace = 1;
			subAddRow(ritem.getIndex(), ritem);
				//	fireTableRowsUpdated(0, 2);
				
				
		

	
			//updateGUI(true);
			//		fireTableDataChanged();
		// if(row>0) addNullRow();
		
		return rowAdded;
	}
	private void subAddRow(int row, RowItem ritem)  {
		
		
			
			
			//m_rowData.add(ritem);
			m_rowData.add(0,ritem);
			//Si la insercion no es al final modificamos el index de los siguientes rowItem
			for(int i=row+1;i<m_rowData.size();i++){
				m_rowData.get(0).setIndex(i);
			}
			m_visibleRowMap.add(/*new Integer(row)*/m_rowData.size()-1);
		
	}
	void updateGUI(boolean reagrupar) {
		
		
		//fireTableDataChanged();
		// printRows();
	}
	private RowItem buildRowItem(ArrayList columnData) {
		RowItem ritem = new RowItem(1);
		ritem.setColumnData(columnData);
		return ritem;
	}
	public String getFieldIDFromColumn(int column) {
		return (String) Map_Column_IDForm.get(column);
	}
}
