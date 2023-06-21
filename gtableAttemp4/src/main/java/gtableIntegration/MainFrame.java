package gtableIntegration;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.Arrays;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class MainFrame  extends JFrame{
	private List<Member> memberList;
	private List<DynamicTableColumn> columns;
	private MagTable<Member> dynamicTable;
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
			
			cmdQuickSearch = new JButton();
			cmdQuickSearch.setText("Search");
			  cmdQuickSearch.setToolTipText("Suchen");
		        cmdQuickSearch.addActionListener(new java.awt.event.ActionListener() {
		            public void actionPerformed(java.awt.event.ActionEvent evt) {
		               logger.info("gggggggggg");
		            }
		        });
			
	 		mvp.setPreferredSize(dimension);
	 		JScrollPane pane = new JScrollPane(dynamicTable);
			//frame.getContentPane().add(new JScrollPane(dynamicTable));
	 		//mvp.add(txtSearchString);
	 		//mvp.add(cmdQuickSearch);
	 		
	 		//LayoutManager layoutManager = mvp.getLayout();
	 	//	JPanel	mainPanel = new JPanel();
			
		//	mainPanel.add(cmdQuickSearch);
			//mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.LINE_AXIS));
			
			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(pane, javax.swing.GroupLayout.DEFAULT_SIZE,300,300)
			.addComponent(cmdQuickSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 100, 100)
			.addComponent(txtSearchString, javax.swing.GroupLayout.DEFAULT_SIZE, 100,150)
			
					);
			
			
			
			layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
						
			.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			.addComponent(pane, javax.swing.GroupLayout.DEFAULT_SIZE, 300,300)
			.addComponent(cmdQuickSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 50,50)
			.addComponent(txtSearchString, javax.swing.GroupLayout.DEFAULT_SIZE, 50, 50)
			
					));
	 		
		//getContentPane().add(mvp);
		
	//	frame.add(desktop, BorderLayout.CENTER);
		setSize(dimension);
		setVisible(true);
		//pack();

	}
}
