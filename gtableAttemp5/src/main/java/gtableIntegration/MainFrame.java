package gtableIntegration;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtableIntegration.gdev.gfld.GFormTable;
import gtableIntegration.gen.GConst;
import gtableIntegration.rowColumn.GTableRow;


public class MainFrame extends JFrame {
	private List<Member> memberList;
	private List<DynamicTableColumn> columns;
//	private MagTable<Member> dynamicTable;
	private DynamicTableModel<Member> dynamicTableModel;
	private JButton cmdQuickSearch;
	private JTextField txtSearchString;
	private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);

	public MainFrame() {
		initComponents();
	}

	public void initComponents() {
		Dimension dimension = new Dimension(700, 700);
		// setSize(dimension);
		txtSearchString = new javax.swing.JTextField();
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// desktop.setOpaque(true);
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

		javax.swing.GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(pane, GroupLayout.DEFAULT_SIZE, 300, 300)
				

		);

		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()

						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(pane, GroupLayout.DEFAULT_SIZE, 300, 300)
						

				));

		setSize(dimension);
		setVisible(true);
		// pack();

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
