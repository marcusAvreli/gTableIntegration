package gtableIntegration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtableIntegration.cell.CheckCellEditor;
import gtableIntegration.cell.CheckCellRenderer;
import gtableIntegration.cell.CheckHeaderRenderer;
import gtableIntegration.common.utils.Auxiliar;
import gtableIntegration.gdev.gfld.GFormTable;
import gtableIntegration.gen.GConfigView;
import gtableIntegration.gen.GConst;
import gtableIntegration.rowColumn.GIdRow;

import gtableIntegration.rowColumn.GTableRow;
import gtableIntegration.rowColumn.RowItem;
import gtableIntegration.rowColumn.botoneraAccion;



//https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gawt/GTable.java
public class GTable<T> extends GComponent implements ActionListener, MouseListener {
	//final Component c=this;
	private static final long serialVersionUID = 1L;
	private MagTable m_objTable;
	protected DynamicTableModel<T> m_objTableModel;	
	private ArrayList<Integer> m_idProps = new ArrayList<Integer>();
	private HashMap<String, Object> m_moa;
	final static int virtualTypeForAgregation = 0;
	private Object m_control;
	boolean hideHeader = false;
	private Icon iconoAdd, iconoDel, iconoCheck, iconoNotCheck;
	private String m_id;
	
	private int m_action;
	private List m_listaColumnas;
	private List<GTableRow> m_listaFilas;
	private boolean m_cuantitativo;
	private int m_iniVirtColumn;
	private int m_atGroupColum;
	private ArrayList m_totalColumns;
	private ArrayList m_agrupables;
	private boolean m_modoConsulta;
	private String m_label;
	private String m_name;
	private boolean m_topLabel;	

	//private Vector<TableModel> m_formComponents;
	private boolean m_popup;
	private double m_heightRow;
	//private JPopupMenu m_menuBotonera;
	boolean m_nullable;
	boolean m_modoFilter;	
	public static final String BUTTON_ONE_FILE = "buttonTableOneFile";
	private boolean m_creationRow;
	private boolean m_finderRow;
	private boolean m_nullRow;
	//private IDictionaryFinder m_dictionaryFinder;
	public Window window;
	private FocusListener focusListener;
	//private AccessAdapter operations;
	private Set focusListenerExt;
	//private IUserMessageListener m_messageListener;
	
	private boolean manageFocus;
	
	private boolean focusTraversalManagingError=false;
	private static final Logger logger = LoggerFactory.getLogger(GTable.class);
	private boolean processingPasteRows=false;
	private boolean abortPasteRows=false;
	private boolean processingSelectCellFromSystem;
	
	public GTable(
			GFormTable ff,
			
			/* session ses, */
			
		//	IComponentListener controlValue,
		//	IUserMessageListener messageListener,
		//	IDictionaryFinder dictionaryFinder,
			Object control,/* scope myScope, */
			int action/* docDataModel rootDocModel, */
			/*Vector<TableModel> formComponents,*/
	/*Font fuente, boolean modoConsulta, boolean popup, boolean modoFilter, Window window*/)
	{
		super(ff);
		//m_messageListener=messageListener;

		// //////////OBTENCION DE ATRIBUTOS//////////////////
		String id = "gTableId";
		String label = "gTableLabel";
		String name = "gTableName";
		boolean topLabel = true;
		// int sintax=ff.getType();
		boolean cuantitativo = ff.isCuantitativo();
		int iniVirtColumn = 1;
		int atGroupColum = 1;
		boolean nullable = false;
		double height = 15;
		ArrayList<Integer> totalColumns = null;
		ArrayList<Integer> agrupables = null;
		m_listaFilas = ff.getRowList();
		m_listaColumnas = ff.getColumnList();
		
		Iterator iteratorColumnas = m_listaColumnas.iterator();
		while (iteratorColumnas.hasNext()) {
			DynamicTableColumn columna = (DynamicTableColumn) iteratorColumnas.next();
			if (columna.isAgrupable())
				agrupables.add(columna.getColumn());
			if (columna.isTotal())
				totalColumns.add(columna.getColumn());
		}
		
		boolean hideHeader = false;

		//operations=ff.getOperations();
		m_creationRow=true;
		m_finderRow=false;
		m_nullRow=false;/*ff.hasCreationRow()||ff.hasFinderRow();*/
		//m_dictionaryFinder=dictionaryFinder;
		this.hideHeader = hideHeader;
		
		m_control = control;
		m_moa = new HashMap<String, Object>();// moa;
		m_id = id;
		m_action = action;
		m_cuantitativo = cuantitativo;
		
		m_iniVirtColumn = iniVirtColumn;
		m_atGroupColum = atGroupColum;
		m_totalColumns = totalColumns;
		m_agrupables = agrupables;
		m_modoConsulta = true;
		m_label = label;
		m_name = name;
		m_topLabel = topLabel;
		m_popup = false;
		m_heightRow = height;
		m_nullable = nullable;
		m_modoFilter = true;
	//	this.window=window;
		focusListenerExt=new HashSet<FocusListener>();
		
		manageFocus=m_label!=null;//Si tiene label manejamos el foco, eliminando la seleccion cuando lo pierde, ya que se trataria de un formulario con mas campos
	}
	
	protected JComponent createComponent()  {
		logger.info("create_component_started");
		m_objTable = new MagTable<T>();
		
		m_objTable.setName(m_name);
		m_objTable.getTableHeader().setName("Header."+m_name);
		if(!m_modoFilter)//No dejamos que se puedan reordenar las columnas por el usuario cuando no esta en filterMode ya que da problemas al asignar valores a las columnas por el usuario
			m_objTable.getTableHeader().setReorderingAllowed(false);
		

		
		m_objTable.setBorder(BorderFactory.createEmptyBorder());

		
/*
			GConst.addShortCut(null, m_objTable, GConst.INFO_SHORTCUT_KEY, GConst.INFO_SHORTCUT_MODIFIERS, "Info tabla", JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new AbstractAction(){
	
				private static final long serialVersionUID = 1L;
				
				public void actionPerformed(ActionEvent arg0) {
					
					RowItem rowItem=m_objTableModel.getRowItemFromIndex(m_objTable.getSelectedRow());
					Integer ido=rowItem.getColumnIdo(m_objTable.getSelectedColumn());
					IdObjectForm idObjForm=new IdObjectForm(m_objTableModel.getFieldIDFromColumn(m_objTable.getSelectedColumn()));
					int idProp=idObjForm.getIdProp();
					m_componentListener.showInformation(ido, idProp);
				}
				
			});
			*/
			final GTable thisFinal = this;
			
			focusListener = new FocusListener(){
				public void focusGained(FocusEvent ev){
					//System.err.println("FocusGained Table "+m_label+" component:"+(ev.getComponent()!=null?ev.getComponent().getClass()+" "+ev.getComponent().hashCode():null)+" opposite:"+(ev.getOppositeComponent()!=null?ev.getOppositeComponent().getClass()+" "+ev.getOppositeComponent().hashCode():null)+" isTemporary:"+ev.isTemporary());

				
					notifyFocusListener(ev, false);
					if(!ev.isTemporary() && m_objTable.getRowCount()>0 /*&& !isTopLabel()*/ && m_objTable.getSelectedRowCount()==0){
						//System.err.println("Entra en seleccion");
						int row=0;
						int column=0;
						if(/*ev.getComponent() instanceof JButton || */m_modoFilter || m_modoConsulta || ev.getSource() instanceof JButton/*Para el boton de acciones de una sola fila*/){
							if(getRowCount()>0)//getRowCount descarta las nullRow
								m_objTable.setRowSelectionInterval(row, row);
						}else{
							editFirstCellEditable(row,column,true,false);
						}
						
					}
					
					
				
					if(manageFocus){
						if(!m_modoConsulta && !m_modoFilter){
							if(m_objComponent instanceof JScrollPane)
								((JScrollPane)m_objComponent).setBorder(GConfigView.borderSelected);
							else m_objComponent.setBorder(GConfigView.borderSelected);
						}
					}

					
						
				}
				public void focusLost(FocusEvent ev){
					//System.err.println("FocusLost Table "+m_label+" component:"+(ev.getComponent()!=null?ev.getComponent().getClass()+" "+ev.getComponent().hashCode():null)+" opposite:"+(ev.getOppositeComponent()!=null?ev.getOppositeComponent().getClass()+" "+ev.getOppositeComponent().hashCode():null)+" isTemporary:"+ev.isTemporary());
					if(/*!ev.isTemporary() &&*/ ev.getOppositeComponent()!=null && ev.getSource()!=null){
						//System.err.println("FocusLost gain "+ev.getOppositeComponent().getClass()+" lost "+ev.getComponent().getClass());
						//m_objTableModel.focusLost(ev);
						notifyFocusListener(ev, true);
						
						Window old = SwingUtilities.getWindowAncestor(ev.getOppositeComponent());
						
						if(manageFocus && !thisFinal.isAncestorOf(ev.getOppositeComponent()) && thisFinal!=ev.getOppositeComponent()/* && (old!=null && old.equals(window))*/){
							 if(old!=null && old.equals(window) && !ev.isTemporary()){
								//System.err.println("clearSelection "+m_objTable.hashCode());
								m_objTable.removeEditor();
								m_objTable.clearSelection();
							 }
							if(!m_modoConsulta && !m_modoFilter){
								if(m_objComponent instanceof JScrollPane)
									((JScrollPane)m_objComponent).setBorder(UIManager.getBorder("ScrollPane.border"));
								else m_objComponent.setBorder(UIManager.getBorder("Table.border"));
							}

						}else{
							
							//end of else
						}
					}
					
				}
			};
			m_objTable.addFocusListener(focusListener);
		//}
		
		m_objComponent = new JScrollPane(m_objTable);
		
		if(!m_modoFilter){
			//Si no esta en modo filtro removemos los listener de la rueda del raton para que este aplique sobre el formulario entero en vez de solo sobre la tabla
			//TODO Hacer que aplique sobre la tabla hasta que este en la ultima fila posible y luego aplique sobre el formulario
			MouseWheelListener[] listMouseWheel=m_objComponent.getMouseWheelListeners();
			for(int i=0;i<listMouseWheel.length;i++)
				m_objComponent.removeMouseWheelListener(listMouseWheel[i]);
		}
	//	m_objComponentSec = null;

		m_objTableModel = new DynamicTableModel(m_id,(GFormTable)this.m_objFormField,  m_action,
				m_listaColumnas, m_listaFilas, m_cuantitativo,
				m_iniVirtColumn, m_atGroupColum, m_totalColumns,
				m_agrupables, m_modoConsulta, m_modoFilter, m_creationRow, m_finderRow, m_topLabel);
		m_objTableModel.setTable(thisFinal);
		m_objTable.setModel(m_objTableModel);
		//m_objTableModel.setDirectEdition(m_modoFilter);

		
		m_objTable.setRowHeight((int) m_heightRow);
		m_objTable.getTableHeader().setPreferredSize(
				new Dimension(m_objTable.getTableHeader().getPreferredSize().width,(int) m_heightRow));
		if (hideHeader)
			m_objTable.setTableHeader(null);
		m_objTableModel.setTable(this);

		if (m_control instanceof MouseListener){
			m_objTable.addMouseListener((MouseListener) m_control);
		}
		
		//Listener para gestionar si hay una imagen mostrandose en la tabla, abrirla en grande si se hace doble click sobre ella
		m_objTable.addMouseListener(new MouseAdapter() {
				
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2){
					JTable table=GTable.this.getTable();
					DynamicTableColumn columna = new DynamicTableColumn();//((GFormTable) m_objFormField).getColumn(table.getSelectedColumn());
					if(columna.getType()==GConst.TM_IMAGE){
						Object value=GTable.this.getModel().getValueAt(table.getSelectedRow(), table.getSelectedColumn());
						//System.err.println("value:"+value);
						if(value!=null){
							String filePath=(String)value;
							//filePath=filePath.replaceAll(Constants.smallImage, "");
							
							final JFrame j=new JFrame(m_label);
							j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
							//j.setIconImage(m_server.getIcon(window,"icon",0,0).getImage());
							j.setResizable(true);
							
							ImageIcon originalImage=new ImageIcon(filePath);
							//if(originalImage.getImageLoadStatus()==MediaTracker.ERRORED)//Significaria que es una imagen en base de datos
							//	originalImage=new ImageIcon(((communicator)m_server).serverGetFilesURL(filePath));
					    	
					     	final Image imageAux=originalImage.getImage();
					     	
					     	ImageIcon imageIcon=new ImageIcon(imageAux);
					     	
							final JLabel labelImage=new JLabel(imageIcon);
							labelImage.setOpaque(true);
							labelImage.setVerticalAlignment(JLabel.CENTER);
							labelImage.setHorizontalAlignment(JLabel.CENTER);
							//JPanel panelAux=new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
							//panelAux.add(labelImage);
							
							final JScrollPane scroll=new JScrollPane(labelImage);
							scroll.getVerticalScrollBar().setUnitIncrement(10);
							
							final JPanel buttonsPanel=new JPanel(/*new FlowLayout(FlowLayout.CENTER,0,0)*/);
							buttonsPanel.setBorder(new EmptyBorder(0,0,0,0));
							
							final JPanel panel=new JPanel(new BorderLayout(0,0));
							
							ActionListener listener=new ActionListener(){

								int zoom=0;
								public void actionPerformed(ActionEvent ae) {
									JButton boton = (JButton) ae.getSource();
						            String command = boton.getActionCommand();
									if (Integer.valueOf(command) == botoneraAccion.INCREASE_ZOOM || Integer.valueOf(command) == botoneraAccion.DECREASE_ZOOM) {
										if(Integer.valueOf(command) == botoneraAccion.INCREASE_ZOOM)
											zoom++;
										else zoom--;
																								     	
								     	int width=imageAux.getWidth(panel);
								     	int height=imageAux.getHeight(panel);
								     	
								     	int newWidth;
								     	int newHeight;
								     	if(zoom>0){
								     		newWidth=width*(int)Math.scalb(1, Math.abs(zoom));//(1+Math.abs(zoom));
								     		newHeight=height*(int)Math.scalb(1, Math.abs(zoom));//(1+Math.abs(zoom));
								     	}else{
								     		newWidth=width/(int)Math.scalb(1, Math.abs(zoom));//(1+Math.abs(zoom));
								     		newHeight=height/(int)Math.scalb(1, Math.abs(zoom));//(1+Math.abs(zoom));
								     	}
								     	
								     	if(newWidth>0 && newHeight>0){
								     		ImageIcon imageIcon=new ImageIcon(imageAux.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
								     		
								     		labelImage.setIcon(imageIcon);
											labelImage.repaint();
											//scroll.validate();
											scroll.repaint();
								     	}else{
								     		//Restauramos el campo zoom ya que no hemos hecho nada porque el número es negativo
								     		if(Integer.valueOf(command) == botoneraAccion.INCREASE_ZOOM)
												zoom--;
											else zoom++;
								     	}
					                }
					                
								}
								
							};
							
							int buttonHeight=100;

							int buttonWidth=buttonHeight;
							/*
							JButton botonIncreaseZoom = botoneraAccion.subBuildBoton(buttonsPanel, null, "zoom_in", ""+botoneraAccion.INCREASE_ZOOM,
									"Más zoom", buttonWidth,buttonHeight,true,m_server);
							botonIncreaseZoom.addActionListener(listener);
							JButton botonDecreaseZoom = botoneraAccion.subBuildBoton(buttonsPanel, null, "zoom_out", ""+botoneraAccion.DECREASE_ZOOM,
									"Menos zoom", buttonWidth,buttonHeight,true,m_server);
							*/
			//				botonDecreaseZoom.addActionListener(listener);
							
							panel.add(scroll,BorderLayout.CENTER);
							panel.add(buttonsPanel,BorderLayout.SOUTH);
							
							//Para que escape cierre la ventana
							
							
						}
					}
				}
			}
				
			//}
			
		});

		buildRenders();

		m_objTableModel.m_moa = m_moa;
		m_objTableModel.m_idPropModel = m_idProps;
		//m_objTableModel.inicializaGroupByColumns();
		// tbm.BuildData(data, false);

		/* int rows = Integer.parseInt(itemView.getAttributeValue("ROWS")); */
		/* int rows=m_ff.getRows(); */
		//int rows = ((DynamicTableModel) m_objFormField).getVisibleRowCount();
		int rows = 10;
		boolean containerDriven = m_label == null; // itemView.getAttributeValue("CONTAINER_DRIVEN")!=null
		// &&
		// itemView.getAttributeValue("CONTAINER_DRIVEN").equals("TRUE");
		JComponent tableView = m_objTable;
		JComponent tablePanel = m_objTable;
		if (rows > 1 || m_topLabel && m_label != null) {
			
			tableView = m_objComponent;
			tablePanel = tableView;
		}

		if (m_topLabel && m_label != null) {
			tablePanel = new JPanel();

			tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
			JPanel botonera = null;
			logger.info("containerDriven:"+containerDriven);
			if (!containerDriven) {
				/*
				 * access myAccess = new
				 * access(((GFormTable)m_objFormField).getAccess());
				 */
				/*
				 * OperationsObject operations =
				 * ((GFormTable)m_objFormField).getOperations();
				 */
				// HashMap<Integer,ArrayList<UserAccess>>
				// accessUserTasks=((GFormTable)m_objFormField).getOperations();
			//	AccessAdapter accessAdapter = ((GFormTable) m_objFormField)
				//.getOperations();
				botonera =new  JPanel();
			
			}
			JLabel title = new JLabel(m_label);
			// title.setFont( m_boldFont );



			if (containerDriven) {
				tablePanel.add(title);
				
			} else {
			
				JPanel cabecera = new JPanel();



				cabecera.setAlignmentX(Component.LEFT_ALIGNMENT);
				tableView.setAlignmentX(Component.LEFT_ALIGNMENT);

				title.setAlignmentX(Component.LEFT_ALIGNMENT);
				botonera.setAlignmentX(Component.LEFT_ALIGNMENT);

				// tableView.setPreferredSize(dimTable);
				// tableView.setMinimumSize(dimTable);
				// tableView.setMaximumSize(dimTable);
				m_objTable.setShowGrid(true);

				// cabecera.setBackground(Color.RED);
				FlowLayout fl = (FlowLayout) cabecera.getLayout();
				fl.setAlignment(FlowLayout.LEFT);
				fl.setHgap(0);
				fl.setVgap(0);
				cabecera.setBorder(new EmptyBorder(0, 0, 0, 0));
				cabecera.add(title);
				cabecera.add(botonera);
				tablePanel.add(cabecera);
				
			}
			tablePanel.add(tableView, BorderLayout.CENTER);

			/** ***************************************************** */
			JPanel panelBotonera = new JPanel();

			panelBotonera.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
			 panelBotonera.setAlignmentX(Component.LEFT_ALIGNMENT);
			 botonera.setAlignmentX(Component.LEFT_ALIGNMENT); 

			panelBotonera.add(botonera);
		//	m_objComponentSec = panelBotonera;
			/** ***************************************************** */

		} else {
			
			if (!containerDriven) { // la etiqueta se la pondria buildComponent
			
				tablePanel = new JPanel();


				FlowLayout fl = (FlowLayout) tablePanel.getLayout();
				fl.setHgap(0);
				fl.setVgap(0);
				
			}
			if (rows == 1) {
				
			}
			if (!containerDriven) {
				JPanel panelBotonera = new JPanel();


				panelBotonera.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

				// TODO Al quitar el boton de buscar no haria falta un IdOperationForm
				//IdOperationForm idOperation = new IdOperationForm();
				//idOperation.setOperationType(botoneraAccion.OPERATION_ACTION);

			//	IdObjectForm idObjForm = new IdObjectForm(m_id);

				//AccessAdapter accessAdapter = ((GFormTable) m_objFormField).getOperations();
			//	Iterator<Integer> itr = accessAdapter.getUserTasksAccess(AccessAdapter.VIEW).iterator();

				//if (itr.hasNext()) {}
					//idObjForm.setIdtoUserTask(itr.next());
				//idOperation.setTarget(idObjForm);
				//idOperation.setButtonType(botoneraAccion.ABRIR);

				//botoneraAccion botonera = new botoneraAccion(  m_id, m_name, null, null, null, null, null, null, null, false, botoneraAccion.TABLE_TYPE, null, null, null,
				//		(ActionListener) m_control, accessAdapter, m_modoConsulta, true, getFormField().getViewBalancer().getGraphics(),m_server,this,m_componentListener.isAllowedConfigTables());

			//	JPanel botoneraPanel = botonera.getComponent();
				//Dimension dimButton=m_objFormField.getDimComponenteSecundario();
				/*
				if(botonera.getNumButtons()==1){//Si solo viene view por ejemplo
					m_objComponentSec = botoneraPanel;
					AbstractButton button=botonera.getBotones().get(0);
					button.setPreferredSize(new Dimension(dimButton.width,dimButton.height));
					
					botonera.addListener(new ActionListener(){

						public void actionPerformed(ActionEvent ae) {
							if (m_objTable.getRowCount() != 0)
								m_objTable.setRowSelectionInterval(0, 0);
						}
					});
				}else{
					final JButton boton = botoneraAccion.subBuildBoton((JPanel) panelBotonera, null, "showButtons", GTable.BUTTON_ONE_FILE,
							"Acciones", "acciones@"+getFormField().getName(), dimButton.width,(int)getHeightRow(),true,m_server);
					boton.setPreferredSize(new Dimension(dimButton.width,dimButton.height));
					boton.addActionListener(this);
					boton.addFocusListener(focusListener);
					if(botonera.getBotones().isEmpty())
						boton.setEnabled(false);

					final botoneraAccion botoneraThis=botonera;
					
					GConst.addShortCut(this, boton, GConst.VIEW_SHORTCUT_KEY, GConst.VIEW_SHORTCUT_MODIFIERS, "Consultar", JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new AbstractAction(){

						public void actionPerformed(ActionEvent arg0) {
							int buttonType=botoneraAccion.CONSULTAR;
							doClickButtonsPopupAction(botoneraThis, buttonType, boton);
						}
						
					});
					GConst.addShortCut(this, boton, GConst.SET_SHORTCUT_KEY, GConst.SET_SHORTCUT_MODIFIERS, "Modificar", JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new AbstractAction(){

						public void actionPerformed(ActionEvent arg0) {
							int buttonType=botoneraAccion.EDITAR;
							doClickButtonsPopupAction(botoneraThis, buttonType, boton);
						}
						
					});
					GConst.addShortCut(this, boton, GConst.NEW_SHORTCUT_KEY, GConst.NEW_SHORTCUT_MODIFIERS, "Crear", JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new AbstractAction(){

						public void actionPerformed(ActionEvent arg0) {
							int buttonType=botoneraAccion.CREAR;
							doClickButtonsPopupAction(botoneraThis, buttonType, boton);
						}
						
					});
					GConst.addShortCut(this, boton, GConst.SEARCH_SHORTCUT_KEY, GConst.SEARCH_SHORTCUT_MODIFIERS, "Buscar", JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new AbstractAction(){

						public void actionPerformed(ActionEvent arg0) {
							int buttonType=botoneraAccion.BUSCAR;
							doClickButtonsPopupAction(botoneraThis, buttonType, boton);
						}
						
					});
					
					
					m_menuBotonera = new JPopupMenu();
					m_menuBotonera.setFocusCycleRoot(true);//El ciclo del foco solo debe moverse por la botonera
					m_menuBotonera.add(botoneraPanel);
					botoneraPanel.addMouseListener(this);
					
					m_menuBotonera.addPopupMenuListener(new PopupMenuListener(){
						public void popupMenuCanceled(PopupMenuEvent ev){
							//m_objTable.clearSelection();							
						}
						public void popupMenuWillBecomeInvisible(PopupMenuEvent ev){}
						public void popupMenuWillBecomeVisible(PopupMenuEvent ev){}						
					});

					botonera.addListener(this);
					m_objComponentSec = panelBotonera;					
				}	*/			
				((JScrollPane) m_objComponent).setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				((JScrollPane) m_objComponent).getVerticalScrollBar().setUnitIncrement(GConfigView.IncrementScrollVertical);
				Color color=UIManager.getColor("TextField.background");
				((JScrollPane)m_objComponent).getViewport().setBackground(color);
				m_objTable.setBackground(color);
			}
		}
		if(!m_nullable && !m_modoFilter){
			((JScrollPane)m_objComponent).getViewport().setBackground(GConfigView.colorBackgroundRequired);
			m_objTable.setBackground(GConfigView.colorBackgroundRequired);
		}
		
		if(isTopLabel()){
			//Solo permitimos el pegado masivo en tablas de mas de una fila
			/*GConst.addShortCut(this, this.m_objTable, GConst.PASTE_TABLE_ROWS, GConst.PASTE_TABLE_ROWS_MODIFIERS, "CopyRows", JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new AbstractAction(){

				private static final long serialVersionUID = 1L;
				
				public void actionPerformed(ActionEvent arg0) {
					try {
						String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
						String[] rows=data.split("\\n");
						ArrayList<String[]> list=new ArrayList<String[]>();
						for (String row : rows) {
							String[] columns=row.split("\\t");
							list.add(columns);
						}
						int row=0;
						int column=0;
					//	m_componentListener.setProcessingCopyRowTable(true);
						processingPasteRows=true;
						abortPasteRows=false;
						processRowColumn(list, row, column);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				

				private void processRowColumn(final ArrayList<String[]> list, final int row, final int column) {
					final String[] columnsValue=list.get(row);
					final String value=columnsValue[column];
					}
					
					};
					
					

					
					//int tableRow=column==0?m_objTableModel.getRowCount()-1:m_objTableModel.getRowCount()-2;//TODO Esto fallaria para tablas con cardinalidad maxima mayor que 1 pero menor que null. Pero no hay casos de esos ahora mismo
					int tableRow=0;
				

				
				
				
				
			});*/
			
		}
		
		

		
	
	
		logger.info("create_component_finished");
		return m_objComponent;
	//end of createComponent
	}

	
		

	
	
	//Busca en la botonera accion el buttonType y si lo encuentra hace click sobre él
	private void doClickButtonsPopupAction(botoneraAccion botonera, int buttonType, final JButton buttonOneFile){
		/*if(!focusTraversalManagingError){//Si focusTraversal esta gestionando un error no pulsamos el boton ya que seria un problema para la gestion del foco. Actualmente siempre entra en este if
			Iterator<AbstractButton> itr=botonera.getBotones().iterator();
			while(itr.hasNext()){
				final AbstractButton button=itr.next();
				if(new IdOperationForm(button.getActionCommand()).getButtonType()==buttonType){
					buttonOneFile.requestFocus();
					buttonOneFile.requestFocusInWindow();
					
					//Necesitamos hacer click sobre el boton de acciones para que se abra el popup y poder hacer click sobre los botones
					//Lo hacemos en un invokeLater ya que los botones creados con botoneraAccion no hacen click hasta que realmente tienen el foco
					SwingUtilities.invokeLater(new Runnable(){
	
						public void run() {
							// Lo hacemos en otro invokeLater para dar tiempo a que GTable pueda estar gestionando un error de datos
							SwingUtilities.invokeLater(new Runnable(){
	
								public void run() {
										if(!focusTraversalManagingError){//Si focusTraversal esta gestionando un error no pulsamos el boton ya que seria un problema para la gestion del foco
											buttonOneFile.doClick();
											SwingUtilities.invokeLater(new Runnable(){
					
												public void run() {
													if(button.isVisible() && button.isShowing()){
														button.requestFocus();
														button.requestFocusInWindow();
														SwingUtilities.invokeLater(new Runnable(){
						
															public void run() {
																button.doClick();
															}
															
														});
													}
												}
												
											});
										}
										
								}
							});
						}
						
					});
				}
					
			}
		}*/
	}
	
	
	// Reescribimos el metodo para tratar las peculiaridades de este componente
	// ya que si es de una sola fila
	// el tamaño de fila deberia ser igual al tamaño del componente
	public void setComponentBounds(Rectangle rc) {
		if (!m_topLabel)
			m_objTable.setRowHeight(rc.height);
		//super.setComponentBounds(rc);
	}

	public JTable getTable() {
		return m_objTable;
	}
	
	public void editFirstCellEditable(int row,int column,boolean searchNextRow,boolean preferRequiredEmpty){
		int rowAux=row;
		int columnAux=column;
		int firstColumnEmptyNotRequired=-1;
		processingSelectCellFromSystem=true;
		try{
			while(m_objTable.getRowCount()>rowAux && m_objTable.getColumnCount()>columnAux && ((!m_objTable.isCellEditable(rowAux, columnAux) || m_objTable.getValueAt(rowAux, columnAux)!=null) || (preferRequiredEmpty && (((DynamicTableColumn)m_listaColumnas.get(columnAux)).isNullable() || m_objTable.getValueAt(rowAux, columnAux)!=null)))){
				columnAux++;
				if(m_objTable.getColumnCount()<=columnAux){
					if(searchNextRow){
						columnAux=0;
						rowAux++;
					}
				}else if(preferRequiredEmpty){
					//Almacenamos la primera columna vacia aunque no sea requerida para que si no encontramos una vacia requerida darle el foco
					if(firstColumnEmptyNotRequired==-1 && m_objTable.isCellEditable(rowAux, columnAux) && m_objTable.getValueAt(rowAux, columnAux)==null)
						firstColumnEmptyNotRequired=columnAux;
				}
			}
			if (m_objTable.getRowCount()>rowAux && m_objTable.getColumnCount()>columnAux && m_objTable.editCellAt(rowAux, columnAux)){
				m_objTable.setRowSelectionInterval(rowAux, rowAux);
				m_objTable.setColumnSelectionInterval(columnAux, columnAux);
				final Component editor=m_objTable.getEditorComponent();
				if(!editor.requestFocusInWindow()){
					editor.requestFocus();
				}
				//System.err.println("************************************************focusGained la tabla:"+getLabel()+" exito:"+exito);
			}else{
				if(firstColumnEmptyNotRequired!=-1 && m_objTable.editCellAt(row, firstColumnEmptyNotRequired)){
					m_objTable.setRowSelectionInterval(row, row);
					m_objTable.setColumnSelectionInterval(firstColumnEmptyNotRequired, firstColumnEmptyNotRequired);
					final Component editor=m_objTable.getEditorComponent();
					if(!editor.requestFocusInWindow()){
						editor.requestFocus();
					}
				}else{
					m_objTable.setRowSelectionInterval(row, row);
					m_objTable.setColumnSelectionInterval(column, column);
				}
			}
		}finally{
			processingSelectCellFromSystem=false;
		}
	}

	/*
	 * public JPanel getBotonera(){ return m_objComponentSec; }
	 */

	public boolean isHideHeader() {
		return hideHeader;
	}


	void buildRenders(/* communicator com,org.jdom.Element columns */) {
		logger.info("build_renders_started");
		TableColumnModel tcm = m_objTable.getColumnModel();

		int col = 0;

	//	MultiLineHeaderRenderer renderer = new MultiLineHeaderRenderer();

		for (int pos = 0; pos < tcm.getColumnCount(); pos++) {
			TableColumn tc = tcm.getColumn(pos);
			//System.err.println("Pos "+pos+" hideHeader "+hideHeader+" tfm.dobleHeaderSize "+tfm.dobleHeaderSize+" tfm.m_cuantitativo "+tfm.m_cuantitativo);
							
			if (pos == 0 && m_objTableModel.m_cuantitativo) {
				/*tc.setCellRenderer(new GroupButtonCellRenderer(this, true));
				tc.setCellEditor(new GroupButtonCellEditor(this, " ", true));
				tc.setWidth(23);
				tc.setMaxWidth(23);
				*/
				continue;
			}
			if (m_objTableModel.m_cuantitativo)
				col = pos - 1;
			else
				col = pos;

			//col = m_objTableModel.getRealColumn(col);
			//col = m_objTableModel.getRealColumn(col);
			
			/*
			 * org.jdom.Element item= jdomParser.findElementByAt(columns,
			 * "ITEM", "COLUMN", String.valueOf(col), false);
			 */
			
			DynamicTableColumn columna = ((GFormTable) m_objFormField).getColumn(col);
			
			if (!hideHeader){
				//if(m_objTableModel.dobleHeaderSize){
					//tc.setHeaderRenderer(renderer);
				//}else
				if(Auxiliar.equals(m_objTableModel.getColumnSelectionRowTable(),col)){
					tc.setHeaderRenderer(new CheckHeaderRenderer(columna, this));
					tc.setHeaderValue(false);
				}
			}

			// tc.setWidth( Integer.parseInt("WIDTH") );
			//System.outprintln("COL_" + col);
			// En botones tapos es nulo
			Integer idProp = columna.getIdProp();
			
logger.info("idProp:"+idProp);
			m_idProps.add(idProp);

			

			int typeField = columna.getType();
			logger.info("type_field:"+typeField);
			switch (typeField) {

			case GConst.TM_DATE:
				//tc.setCellRenderer(new DateCellRenderer(this, columna, m_modoFilter));
			//	tc.setCellEditor(new DateCellEditor(columna, this, m_modoFilter, focusListener, m_server, typeField));
				//setEditEditor(tc, columna);
				break;
			case GConst.TM_DATE_HOUR:
				//tc.setCellRenderer(new DateCellRenderer(this, columna, m_modoFilter));
				///tc.setCellEditor(new DateCellEditor(columna, this, m_modoFilter, focusListener, m_server, typeField));
				//setEditEditor(tc, columna);
				break;
			case GConst.TM_HOUR:
				//tc.setCellRenderer(new DateCellRenderer(this, columna, m_modoFilter));
				//tc.setCellEditor(new DateCellEditor(columna, this, m_modoFilter, focusListener, m_server, typeField));
				//setEditEditor(tc, columna);
				break;
			case GConst.TM_ENUMERATED:				
				//TableCellRenderer render = buildListRenderer(columna.getValuesPossible(), pos);
				//tc.setCellRenderer(render);
				//setComboEditor(tc, columna);
				break;
			case GConst.TM_INTEGER:
				//tc.setCellRenderer(new TextCellRenderer(this, columna, m_modoFilter,typeField,null));
				//tc.setCellEditor(new NumberCellEditor(columna, this, m_modoFilter, focusListener));
				//setEditEditor(tc, columna);
				break;
			case GConst.TM_REAL:
				//tc.setCellRenderer(new TextCellRenderer(this, columna, m_modoFilter,typeField,columna.getRedondeo()));
				//tc.setCellEditor(new NumberCellEditor(columna, this, m_modoFilter, focusListener));
				//setEditEditor(tc, columna);
				break;
			case GConst.TM_TEXT:
			case GConst.TM_MEMO:
				//tc.setCellRenderer(new TextCellRenderer(this, columna, m_modoFilter,typeField,null));
				//TextCellEditor editor = new TextCellEditor(columna, m_modoFilter, this, focusListener);
				//tc.setCellEditor(editor);
				//m_objTable.addKeyListener(editor.getKeyListener());
				//setEditEditor(tc, columna);
				break;
			case GConst.TM_BOOLEAN:
				logger.info("boolean column");
				logger.info("boolean column:"+columna.getId());
				if(columna.getId().equals(GConst.ID_COLUMN_TABLE_SELECTION)){
					tc.setCellRenderer(new CheckCellRenderer(columna, this, m_modoFilter));
					tc.setCellEditor(new CheckCellEditor(columna, this, focusListener));
					int ancho = columna.getWidth();
					tc.setWidth(ancho);
					tc.setMaxWidth(ancho);
				}
				/*else{
					tc.setCellRenderer(new CheckTristateCellRenderer(columna, this, m_modoFilter));
					tc.setCellEditor(new CheckTristateCellEditor(columna, this, focusListener));
				}*/
				
				//setDefaultField(columna);
				break;
			case GConst.TM_BOOLEAN_COMMENTED:
				//tc.setCellRenderer(new CheckTristateCellRenderer(columna, this, m_modoFilter));
				//tc.setCellEditor(new CheckTristateCellEditor(columna, this, focusListener));
				//setDefaultField(columna);
				break;
			case GConst.TM_BUTTON:
				//setButtonEditor(tc, columna/*, m_server*/);
				break;
			case GConst.TM_FILE:
			case GConst.TM_IMAGE:
				//tc.setCellRenderer(new TextCellRenderer(this, columna, m_modoFilter,typeField,null));
				//tc.setCellEditor(new FileCellEditor(columna, this, m_modoFilter, focusListener, m_server, typeField));
				break;
			}
		}
		logger.info("build_renders_finished");
	}	

	public  DynamicTableModel<T> getModel(){
		return (DynamicTableModel<T>) m_objTableModel;
	}

	/*
	private TableCellRenderer buildListRenderer(ArrayList<GValue> valuesPossible, int col) {
		Collections.sort(valuesPossible);
		if(m_modoFilter){
			ComboBoxRendererLabel render = new ComboBoxRendererLabel(this);

			// Añadimos la opcion de no seleccionar nada
			render.addLine(0,"");

			Iterator<GValue> itrId = valuesPossible.iterator();
			while (itrId.hasNext()) {
				GValue parValue = itrId.next();
				int id = parValue.getId();
				String value = parValue.getLabel();
				render.addLine(id, value);			
			}
			return render;

		}else{
			//ComboBoxRenderer render = new ComboBoxRenderer(this, (GTableColumn)m_listaColumnas.get(col));

			//Añadimos la opcion de no seleccionar nada
			//String label = ((GTableColumn)m_listaColumnas.get(col)).getLabel();
			//render.addLine(0,"<"+((GTableColumn)m_listaColumnas.get(col)).getLabel()+">");

			Iterator<GValue> itrId = valuesPossible.iterator();
			while (itrId.hasNext()) {
				GValue parValue = itrId.next();
				int id = parValue.getId();
				String value = parValue.getLabel();
				render.addLine(id, value);			
			}
			return render;
		}
	}
*/
	private void setButtonEditor(TableColumn tc, DynamicTableColumn columna/*,communicator com*/) {
		/* String tipo= itemView.getAttributeValue("ID"); */
		String tipo = columna.getId();
		/*ButtonCellEditor bce = new ButtonCellEditor((ActionListener) m_control,
				this, iconoAdd, iconoDel);
		
		tc.setCellRenderer(new ButtonCellRenderer(this,iconoAdd,iconoDel));
		tc.setCellEditor(bce);
		
		int ancho = columna.getWidth();
		tc.setWidth(ancho);
		tc.setMaxWidth(ancho);
		*/
	}

	private void setComboEditor(TableColumn tc, DynamicTableColumn columna) {
		/*
		String idForm = columna.getId();

		Vector<ItemList> vItems = new Vector<ItemList>();

		if(columna.isNullable()){
			boolean nullInitialSelection=false;
			vItems.add(new ItemList("0", null, "", nullInitialSelection));
		}

		Vector<GValue> vValues = new Vector<GValue>(columna.getValuesPossible());
		Enumeration en = vValues.elements();
		
		while(en.hasMoreElements())
		{
			GValue val = (GValue)en.nextElement();
			ItemList itl= new ItemList(String.valueOf(val.getId()),
					null,
					val.getLabel(),
					false
		);
			vItems.addElement(itl);
		
		}
*/
		//ComboBoxEditor cellEditor = new ComboBoxEditor(columna, vItems, this, focusListener);//columna.getValuesPossible());
		//tc.setCellEditor(cellEditor);
		//m_moa.put(idForm, cellEditor);
	}



	public void selectRow(int ido,boolean permanent) {
		DynamicTableModel tfm = (DynamicTableModel) m_objTable.getModel();
		//int visRow = tfm.findRow(ido, true, permanent);
		int visRow = 0;
		m_objTable.changeSelection(visRow, 0, false, false);
	}
	
	public void selectAll(boolean select) {
		if(m_objTableModel.getColumnSelectionRowTable()!=null){//Si existe una columna de seleccion de fila modificamos sus valores
			for(RowItem rowItem:m_objTableModel.getRowData()){
				rowItem.setColumnData(m_objTableModel.getColumnSelectionRowTable(),select);
			}
		}
		
		if(!select){
			m_objTable.clearSelection();
		}else{
			m_objTable.selectAll();
		}
		
	}

	public boolean isSelectAll(){
		return m_objTable.getSelectedRowCount()==m_objTable.getRowCount();
	}
	/*
	public RowItem getNextRow() {
		int[] rows = m_objTable.getSelectedRows();
		if (rows.length == 0)
			return null;
		DynamicTableModel tfm = (DynamicTableModel) m_objTable.getModel();
		return tfm.getNextRow(rows[rows.length - 1]);
	}
*/
	/*
	public RowItem getPrevRow() {
		int[] rows = m_objTable.getSelectedRows();
		if (rows.length == 0)
			return null;
		DynamicTableModel tfm = (DynamicTableModel) m_objTable.getModel();
		return tfm.getPrevRow(rows[0]);
	}
	*/
/*
	public GIdRow getDataFromIndex(int rowIndex) {
		DynamicTableModel tfm = (DynamicTableModel) m_objTable.getModel();
		return tfm.getDataFromIndex(rowIndex);
	}
	*/
	/*
	public RowItem getCompletedDataFromIndex(int rowIndex) {
		DynamicTableModel tfm = (DynamicTableModel) m_objTable.getModel();
		return tfm.getRowItemFromIndex(rowIndex);
	}
*/
	/*
	public GIdRow getDataFromIdo(int ido,Boolean permanent) {
		DynamicTableModel tfm = (DynamicTableModel) m_objTable.getModel();
		int row=tfm.findRow(ido, true, permanent);
		if(row!=-1)
			return tfm.getDataFromIndex(row);
		else return null;
	}
*/
	/*
	public ArrayList getData(int idRow) {
		DynamicTableModel tfm = (DynamicTableModel) m_objTable.getModel();
		return tfm.getData(idRow);
	}
*/
	public ArrayList<GIdRow> getIdRowsSelectionData() {
		
		return getIdRowsSelectedRows();
	}
/*
	public ArrayList<GIdRow> getIdRowsData(Boolean permanent) {
		DynamicTableModel tfm = (DynamicTableModel) m_objTable.getModel();
		return tfm.getIdRowsData(permanent);
	}
*/
	/*
	public boolean selectionIsGroup() {
		int[] rows = m_objTable.getSelectedRows();
		DynamicTableModel tfm = (DynamicTableModel) m_objTable.getModel();
		if (rows.length == 0)
			return false;
		for (int i = 0; i < rows.length; i++) {
			RowItem rt = (RowItem) tfm.m_rowData.get(rows[i]);
			if (rt.isGroup())
				return true;
		}
		return false;
	}
*/
	/*
	public ArrayList getDataSelectedRows() {
		int[] rows = m_objTable.getSelectedRows();
		if (rows.length == 0)
			return null;
		ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
		DynamicTableModel tfm = (DynamicTableModel) m_objTable.getModel();
		for (int row = 0; row < rows.length; row++) {
			ArrayList<Object> currentRowData = new ArrayList<Object>();
			for (int col = 0; col < tfm.getVisibleColumnDataCount(); col++) {
				Object val = tfm.getDataValueAt(rows[row], col);
				if (val instanceof ItemList)
					val = ((ItemList) val).getInteger();
				currentRowData.add(val);
			}
			result.add(currentRowData);
		}
		return result;
	}
	*/

	public ArrayList<GIdRow> getIdRowsSelectedRows() {
		int[] rows = m_objTable.getSelectedRows();
		ArrayList<GIdRow> result = new ArrayList<GIdRow>();
		if (rows.length == 0)
			return result;
		DynamicTableModel tfm = (DynamicTableModel) m_objTable.getModel();
		for (int row = 0; row < rows.length; row++) {
			/*RowItem rt = (RowItem) tfm.m_rowData.get(rows[row]);
			if (rt.isGroup() || rt.isNullRow())
				continue;
			if(!result.contains(rt.getIdRow()))
				result.add(rt.getIdRow());
				*/
		}
		//System.err.println("Selected "+result);
		return result;
	}


	public void clearSeleccion() {
		m_objTable.clearSelection();
	}

	public void actionPerformed(ActionEvent ae) {
		/*try{
			//System.err.println("BOTON CLICK");
			if (ae.getActionCommand() == GTable.BUTTON_ONE_FILE){
				// selectRow(0);
				//if (m_objTable.getRowCount() != 0)
				//	m_objTable.setRowSelectionInterval(0, 0);
				Component component = (Component) ae.getSource();
				// Para que aparezca a la izquierda del boton y no a la derecha ya
				// que, normalmente, se saldria de la pantalla
				int x = (component.getWidth() / 2)
				- m_menuBotonera.getPreferredSize().width;
				int y = component.getHeight() / 2;
				// if(x<0)//No sirve esta comparacion porque es relativo al boton
				// asi que x sera siempre negativo,hay que comparar con la pantalla
				// principal
				// x=0;
				m_menuBotonera.show(component, x, y);
				//Es interesante que la botonera tenga el foco por si nos queremos mover con el tabulador.
				//m_menuBotonera.requestFocusInWindow(); Comentado ya que provocaba que, al cancelar el popup, la ventana intentaba darle el foco al primer componente de la ventana en vez de dejarlo en la tabla
			} else
				m_menuBotonera.setVisible(false);
		}catch(Exception ex){
			ex.printStackTrace();
			m_server.logError(SwingUtilities.getWindowAncestor(m_objTable),ex,"Error al intentar mostrar/ocultar la botonera");
		}*/
	}
	
	public void mouseClicked(MouseEvent e) {
		try{
			//System.err.println("MOUSE CLICK");
			//m_menuBotonera.setVisible(false);
		}catch(Exception ex){
			ex.printStackTrace();
			//m_server.logError(SwingUtilities.getWindowAncestor(m_objTable),ex,"Error al intentar ocultar la botonera");
		}
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {
		try{
			//System.err.println("MOUSE EXIT");
		//	m_menuBotonera.setVisible(false);
			clearSeleccion();
		}catch(Exception ex){
			ex.printStackTrace();
			//m_server.logError(SwingUtilities.getWindowAncestor(m_objTable),ex,"Error al intentar ocultar la botonera");
		}
	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public String getId() {
		return m_id;
	}
/*
	public IDictionaryFinder getDictionaryFinder() {
		return m_dictionaryFinder;
	}
*/
	public boolean hasCreationRow() {
		return m_creationRow;
	}

	public boolean hasFinderRow() {
		return m_finderRow;
	}

	public boolean hasNullRow() {
		return m_nullRow;
	}
/*
	public docServer getServer() {
		return m_server;
	}
	*/
	//Utilizado cuando no se quiere que cuente la nullRow. Se podria hacer mirando el ultimo registro ya que la nullRow esta al final, pero no es seguro que no funcione bien en algun caso
	public int getRowCount(){
		/*DynamicTableModel tfm = (DynamicTableModel) m_objTable.getModel();
		int size=0;
		Iterator<RowItem> itr=tfm.getRowData().iterator();
		while(itr.hasNext()){
			RowItem ri=itr.next();
			if(!ri.isNullRow())
				size++;
		}*/
		return 0;
	}
	
	public boolean isNullRow(int index){
		return false;
		//return m_objTableModel.getRowData().get(index).isNullRow();
	}

	public boolean isTopLabel() {
		return m_topLabel;
	}

	public double getHeightRow() {
		return m_heightRow;
	}
/*
	public AccessAdapter getOperations() {
		return operations;
	}
*/
	public String getLabel() {
		return m_label;
	}

	//@Override
	public boolean newValueAllowed() {
		//Permitimos si es una tabla de una sola fila vacia, o una de varias filas aunque tenga algun valor
		return m_topLabel || getRowCount()==0;
	}

	@Override
	public synchronized void addFocusListener(FocusListener e) {
		focusListenerExt.add(e);
	}
	
	public void notifyFocusListener(FocusEvent e,boolean lost){
		Iterator<FocusListener> itr=focusListenerExt.iterator();
		while(itr.hasNext()){
			if(lost)
				itr.next().focusLost(e);
			else itr.next().focusGained(e);
		}
	}
/*
	public IUserMessageListener getMessageListener() {
		return m_messageListener;
	}
*/
	/*
	public Object getValues() {
		String values=null;
		int numRow=getRowCount();
		if(numRow>0)
			values=getData(getDataFromIndex(0).getIdo()).toString();
		for(int i=1;i<numRow;i++){
			values+=":"+getData(getDataFromIndex(i).getIdo()).toString();
		}
		return values;
	}
	*/

	public boolean isFocusTraversalManagingError() {
		return focusTraversalManagingError;
	}

	//Solo debe ser llamado por GFocusTraversalPolicy para indicar que esta procesando el error
	//y asi evitar que se requiera el foco por otros componentes
	public void setFocusTraversalManagingError(boolean enabledShortCut) {
		this.focusTraversalManagingError = enabledShortCut;
	}

	public boolean isProcessingPasteRows() {
		return processingPasteRows;
	}

	public void setAbortPasteRows(boolean abortPasteRows) {
		this.abortPasteRows = abortPasteRows;
	}

	public boolean isProcessingSelectCellFromSystem() {
		return processingSelectCellFromSystem;
	}

	public boolean isNullable() {
		return m_nullable;
	}
}