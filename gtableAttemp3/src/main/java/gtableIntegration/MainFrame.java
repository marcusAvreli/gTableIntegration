package gtableIntegration;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class MainFrame  extends JFrame{
	private List<Member> memberList;
	private List<DynamicTableColumn> columns;
	private MagTable<Member> dynamicTable;
	private DynamicTableModel<Member> dynamicTableModel;
	private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);
	public MainFrame() {
		initComponents();
	}

	public void initComponents() {
		Dimension dimension = new Dimension(700, 700);
		// setSize(dimension);
		
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//desktop.setOpaque(true);
		memberList = Arrays.asList(
				new Member("Joe","0392jr"),
				new Member("Janet","323rr"),
				new Member("Blackham","3r23r"),
				new Member("Erikka","234f2"),
				new Member("Moira","23d23d"),
				new Member("Ulrich","23f23f"),
				new Member("Blackham","3r23r"),
				new Member("Erikka","234f2"),
				new Member("Moira","23d23d"),
				new Member("Ulrich","23f23f"),
				new Member("Joe","0392jr"),
				new Member("Janet","323rr"),
				new Member("Blackham","3r23r"),
				new Member("Erikka","234f2"),
				new Member("Moira","23d23d"),
				new Member("Ulrich==","23f23f"),
				new Member("Blackham","3r23r"),
				new Member("Erikka","234f2"),
				new Member("Moira","23d23d"),
				new Member("Ulrich","23f23f"),
				new Member("Joe","0392jr"),
				new Member("Janet","323rr"),
				new Member("Blackham","3r23r"),
				new Member("Erikka","234f2"),
				new Member("Moira","23d23d"),
				new Member("Ulrich","23f23f"),
				new Member("Blackham===","3r23r"),
				new Member("Erikka","234f2"),
				new Member("Moira","23d23d"),
				new Member("Ulrich","23f23f"),
				new Member("Joe","0392jr"),
				new Member("Janet","323rr"),
				new Member("Blackham","3r23r"),
				new Member("Erikka","234f2"),
				new Member("Moira","23d23d"),
				new Member("Ulrich","23f23f"),
				new Member("Blackham","3r23r"),
				new Member("Erikka","234f2"),
				new Member("Moira","23d23d"),
				new Member("Ulrich","23f23f")
			);
			columns = Arrays.asList(
				new DynamicTableColumn("name","Display_Name",0,200),
				new DynamicTableColumn("password","Display_Password",1,50),
				new DynamicTableColumn("selection","selection",2,200)
			);
			logger.info("size:"+memberList.size());
			
		
			
			dynamicTableModel = new DynamicTableModel<Member>(columns);
			dynamicTable = new MagTable<Member>(dynamicTableModel);
			GTable<Member> mvp = new GTable<Member>(dynamicTable);
			dynamicTableModel.setTable(dynamicTable);
			
			
			dynamicTableModel.addAll(memberList);
			mvp.add(new JScrollPane(dynamicTable));
			
			
			
	 		mvp.setPreferredSize(dimension);
			//frame.getContentPane().add(new JScrollPane(dynamicTable));
		getContentPane().add(mvp);
	//	frame.add(desktop, BorderLayout.CENTER);
		setSize(dimension);
		setVisible(true);
		pack();

	}
}
