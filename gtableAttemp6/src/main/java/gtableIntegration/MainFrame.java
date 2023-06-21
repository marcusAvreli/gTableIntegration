package gtableIntegration;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import gtableIntegration.core.view.AbstractViewContainer;
import gtableIntegration.core.view.DefaultViewContainer;
import gtableIntegration.gdev.gfld.GFormTable;
import gtableIntegration.gen.GConst;
import gtableIntegration.rowColumn.GTableRow;
import gtableIntegration.swing.table.DynamicTableColumn;
import gtableIntegration.swing.table.GTable;



public class MainFrame  extends JFrame{
	private List<Member> memberList;
	private List<DynamicTableColumn> columns;
	public MainFrame() {
		
		initComponents();
	}
	
	public void initComponents() {
		Dimension dimension = new Dimension(700, 700);
	     setExtendedState(MAXIMIZED_BOTH);
		 setDefaultCloseOperation(EXIT_ON_CLOSE);
		 AbstractViewContainer viewContainer = new DefaultViewContainer();

		 JLabel lbl = new JLabel("test");

		 memberList = Arrays.asList(new Member("Joe", "0392jr"), new Member("Janet", "323rr"),
					new Member("Blackham", "3r23r"), new Member("Erikka", "234f2"), new Member("Moira", "23d23d"),
					new Member("Ulrich", "23f23f"), new Member("Blackham", "3r23r"), new Member("Erikka", "234f2"),
					new Member("Moira", "23d23d"), new Member("Ulrich", "23f23f"), new Member("Joe", "0392jr"),
					new Member("Janet", "323rr"), new Member("Blackham", "3r23r"), new Member("Erikka", "234f2"),
					new Member("Moira", "23d23d"), new Member("Ulrich==", "23f23f"), new Member("Blackham", "3r23r"),
					new Member("Erikka", "234f2"), new Member("Moira", "23d23d"), new Member("Ulrich", "23f23f"),
					new Member("Joe", "0392jr"), new Member("Janet", "323rr"), new Member("Blackham", "3r23r"),
					new Member("Erikka", "234f2"), new Member("Moira", "23d23d"), new Member("Ulrich", "23f23f"),
					new Member("Blackham===", "3r23r"), new Member("Erikka", "234f2"), new Member("Moira", "23d23d"),
					new Member("Ulrich", "23f23f"), new Member("Joe", "0392jr"), new Member("Janet", "323rr"),
					new Member("Blackham", "3r23r"), new Member("Erikka", "234f2"), new Member("Moira", "23d23d"),
					new Member("Ulrich", "23f23f"), new Member("Blackham", "3r23r"), new Member("Erikka", "234f2"),
					new Member("Moira", "23d23d"), new Member("Ulrich", "23f23f"),new Member("Joe", "0392jr"), new Member("Janet", "323rr"),
					new Member("Blackham", "3r23r"), new Member("Erikka", "234f2"), new Member("Moira", "23d23d"),
					new Member("Ulrich", "23f23f"), new Member("Blackham", "3r23r"), new Member("Erikka", "234f2"),
					new Member("Moira", "23d23d"), new Member("Ulrich", "23f23f"), new Member("Joe", "0392jr"),
					new Member("Janet", "323rr"), new Member("Blackham", "3r23r"), new Member("Erikka", "234f2"),
					new Member("Moira", "23d23d"), new Member("Ulrich==", "23f23f"), new Member("Blackham", "3r23r"),
					new Member("Erikka", "234f2"), new Member("Moira", "23d23d"), new Member("Ulrich", "23f23f"),
					new Member("Joe", "0392jr"), new Member("Janet", "323rr"), new Member("Blackham", "3r23r"),
					new Member("Erikka", "234f2"), new Member("Moira", "23d23d"), new Member("Ulrich", "23f23f"),
					new Member("Blackham===", "3r23r"), new Member("Erikka", "234f2"), new Member("Moira", "23d23d"),
					new Member("Ulrich", "23f23f"), new Member("Joe", "0392jr"), new Member("Janet", "323rr"),
					new Member("Blackham", "3r23r"), new Member("Erikka", "234f2"), new Member("Moira", "23d23d"),
					new Member("Ulrich", "23f23f"), new Member("Blackham", "3r23r"), new Member("Erikka", "234f2"),
					new Member("Moira", "23d23d"), new Member("Ulrich", "23f23f"));
			
			columns = Arrays.asList(
					// String id,String idParent,int col,String strLab,int type,String ref/*,String
					// claseCampo*/,int idProp,int width,int height,int length,String mask,boolean
					// enable,boolean hide,boolean total,boolean agrupable,boolean
					// dobleSizeHeader,boolean nullable,boolean creation,int finder,boolean
					// basicEdition,boolean uniqueValue,Integer redondeo
					new DynamicTableColumn(
							"name", "display_name", 0, "strLab_0", GConst.TM_TEXT, "ref", 1, 100, 100, 100, "mask", false, false,
							false/* agrupable */, false, false, false/* creation */, false, 1, false/* basic edition */,
							false, 1),
					new DynamicTableColumn("password", "Display_Password", 1, "strLab_1", GConst.TM_TEXT, "ref", 1, 100, 100, 100, "mask",
							false, false, false/* agrupable */, false, false, false/* creation */, false, 1,
							false/* basic edition */, false, 1),
					new DynamicTableColumn("2", "con", 2, "strLab_2", GConst.TM_TEXT, "ref", 1, 100, 100, 100, "mask", false,
							false, false/* agrupable */, false, false, false/* creation */, false, 1,
							false/* basic edition */, false, 1),
					new DynamicTableColumn(GConst.ID_COLUMN_TABLE_SELECTION, "con", 3, "strLab_2",GConst.TM_BOOLEAN, "ref", 1, 100, 100, 100, "mask", false,
							false, false/* agrupable */, false, false, false/* creation */, false, 1,
							false/* basic edition */, false, 1));
			List<GTableRow> rows = buildRows(memberList);
			GFormTable form = new GFormTable();
			form = form.createTable(columns,rows);
			GTable<Member> mvp = new GTable<Member>(form, new Object(), 1);
			JComponent pane = mvp.createComponent();

			

			mvp.setPreferredSize(dimension);


		 viewContainer.setComponent(pane);
		 ViewManager viewManager = new ViewManager();
		 viewManager.setFrame(this);
		 viewManager.addView(viewContainer);
		 setVisible(true);
	}
	public List<GTableRow> buildRows(List<Member> members) {
		Vector<GTableRow> rows = new Vector<GTableRow>();
		GTableRow row = null;
		int counter=0;
		if(null != members) {
			for(Member member : members) {
				counter++;
				int databaseId = counter;
	    		String appName = member.getName();
	    		String displayName = member.getPassword();

	    		row=new GTableRow();	
	    		row.setDatabaseId(String.valueOf(databaseId));
	    		row.setDataColumn("name", appName);
	    		row.setDataColumn("password", displayName);
	    		rows.add(row);    		
	    	}	
		}
		return rows;
	}
}
