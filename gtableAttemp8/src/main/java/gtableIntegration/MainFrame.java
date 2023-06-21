package gtableIntegration;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


import gtableIntegration.table.MyTable;
import gtableIntegration.table.MyTableModel;
import gtableIntegration.table.rowColumn.ColumnDefinition;
import gtableIntegration.table.rowColumn.RowDefinition;


public class MainFrame extends JFrame{


	public MainFrame() {
		
		initComponents();
	}
	public void initComponents() {
		Dimension dimension = new Dimension(700, 700);
	     setExtendedState(MAXIMIZED_BOTH);
		 setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		 JLabel lbl = new JLabel("test");
		 add(lbl);
		 
		 CustomApplication ca = new CustomApplication(1, "name1", "displayName1", "description1");
		 List<CustomApplication> cas = new ArrayList<CustomApplication>();
		 cas.add(ca);
		 ca = new CustomApplication(2, "name2", "displayName2", "description2");
		 cas.add(ca);
		 ColumnDefinition colDef = new ColumnDefinition(0, "name", "Name");
		 List<ColumnDefinition> colDefs = new ArrayList<ColumnDefinition>();
		 colDefs.add(colDef);
		 colDef = new ColumnDefinition(2, "displayName", "Display Name");
		 colDefs.add(colDef);
		 colDef = new ColumnDefinition(1, "description", "Description");
		 colDefs.add(colDef);
		 
		 List<RowDefinition> rowDefs = buildRows( cas);
		 
		 MyTable myTable = new MyTable();
		 myTable.setBorder(BorderFactory.createEmptyBorder());
		 
			MyTableModel m_objTableModel = new MyTableModel(colDefs,rowDefs);
			///final GTable thisFinal = this;
			myTable.setModel(m_objTableModel);
		 JScrollPane scrollPane = new JScrollPane(myTable);
		add(scrollPane);
		pack();
		
	}
	public List<RowDefinition> buildRows(List<CustomApplication> members) {
		List<RowDefinition> rows = new ArrayList<RowDefinition>();
		RowDefinition row = null;
		int counter=0;
		if(null != members) {
			for(CustomApplication ca : members) {
				counter++;
				int databaseId = counter;
	    		String appName = ca.getName();
	    		String displayName = ca.getDisplayName();
	    		String description = ca.getDescription();
	    		row=new RowDefinition();	
	    		row.setDatabaseId("id");
	    		row.setDataColumn("name", appName);
	    		row.setDataColumn("displayName", displayName);
	    		row.setDataColumn("description", description);
	    		rows.add(row);    		
	    	}	
		}
		return rows;
	}
}
