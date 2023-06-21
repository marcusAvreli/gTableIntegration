package gtableIntegration.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import gtableIntegration.cell.CellEditor;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtableIntegration.common.utils.Auxiliar;
import gtableIntegration.common.utils.IdObjectForm;
import gtableIntegration.dynagent.common.Constants;
import gtableIntegration.gawt.tableCellRenderer.TextCellRenderer;
import gtableIntegration.gawt.utils.ItemList;
import gtableIntegration.gdev.gfld.GFormTable;
import gtableIntegration.gdev.gfld.GValue;
import gtableIntegration.gen.GConst;
import gtableIntegration.rowColumn.GIdRow;
import gtableIntegration.rowColumn.GTableRow;
import gtableIntegration.rowColumn.RowItem;




/**
 * @author mgg
 *
 * @param <T>
 */
//https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gawt/GTableModel.java
//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/swing/src/main/java/org/viewaframework/swing/table/DynamicTableModel.java#L22
public class DynamicTableModel <T> extends AbstractTableModel 
	implements ListSelectionListener{
	private static final Logger logger = LoggerFactory.getLogger(DynamicTableModel.class);
	
	private static final long serialVersionUID = 1L;

	ArrayList<String> columnNames = new ArrayList<String>();

	ArrayList<RowItem> m_rowData = new ArrayList<RowItem>();

	ArrayList<Integer> m_visibleRowMap = new ArrayList<Integer>();

	ArrayList<Integer> m_visibleColMap = new ArrayList<Integer>();

	ArrayList<Integer> m_visibleDataColMap = new ArrayList<Integer>();

	public boolean m_cuantitativo = false;

	int m_atGroupTypeColumn = -1;

	int m_iniVirtualColumn;

	// estos dos valores no contabilizan la col de toggleBoton pra desplegar
	ArrayList<Integer> m_groupByColumns = new ArrayList<Integer>();

	ArrayList<String> columnRef = new ArrayList<String>();

	ArrayList<Integer> columnIdProps = new ArrayList<Integer>();

	ArrayList<Integer> columnSintax = new ArrayList<Integer>();

	ArrayList<Class> columnClass = new ArrayList<Class>();

	ArrayList m_idPropModel;// no contabiliza la primera columna de boton
	// desplegable en cuantitativos

	HashMap<Integer, Integer> Map_columnsIdProp = new HashMap<Integer, Integer>();

	HashMap<String, Integer> Map_IDForm_Column = new HashMap<String, Integer>();
	
	HashMap<String, Integer> Map_IDO_Label = new HashMap<String, Integer>();

	ArrayList<String> Map_Column_IDForm = new ArrayList<String>();
	
	HashSet<Integer> columnsFinder = new HashSet<Integer>();
	
	HashSet<Integer> columnsCreation = new HashSet<Integer>();
	
	HashSet<Integer> columnsFinderIfCreation = new HashSet<Integer>();

	public HashMap Map_XMLDom_ListOption = new HashMap();

	ArrayList<Boolean> columnEditable = new ArrayList<Boolean>();//Columnas que, aunque a lo mejor se tiene permiso de edicion, no se le deja al usuario modificarlo en la tabla
	
	HashSet<Integer> columnsEnable = new HashSet<Integer>();//Columnas sin permisos de edicion para el usuario

	HashMap m_condicionantes_List;

	boolean m_filteredState = false;

	/* JTable parent; */
	GTable parent;

	ArrayList m_colTotales;

	ArrayList m_colAgrupables;

	boolean dobleHeaderSize = false;

	/*
	 * domDataModel m_domModel; docDataModel m_rootDoc;
	 */
	/* metaData m_md; */
	HashMap m_moa;

	int m_action = 0;

	//communicator m_com;

	/*session m_session = null;*/

	private boolean m_modoConsulta;
	
	private boolean m_modoFilter;

	//private IComponentListener m_controlListener;
	
	private List<GTableRow> listaFilas;
	
	private boolean m_creationRow;
	
	private boolean m_finderRow;
	
	private String m_id;
	
	private boolean init;
	
	private Integer idoRowEditing;
	
	private boolean rowCreating;
	
	private HashSet<Integer> listIdosEditing;
	
	private boolean directEdition;
	
	private GFormTable m_ff;
	
	private Integer columnSelectionRowTable;//Nos sirve para saber si hay alguna columna de seleccion de fila y cual es

	private boolean lastSetValueSuccess=true;//Nos indica si el ultimo setValueAt ha tenido exito o ha provocado alguna excepcion

	private boolean executingSetValue=false;//Nos indica si se esta ejecutando el setValueAt para que no ejecutar cada valor de la copia masiva en GTable hasta que setValueAt ha terminado con el valor anterior 
	
	private void debugJustInCase(String message) {
		if (logger.isDebugEnabled()) {
			logger.info(message);
		}
	}
	/*
	(m_id,  m_action,
			m_listaColumnas, m_listaFilas, m_cuantitativo,
			m_iniVirtColumn, m_atGroupColum, m_totalColumns,
			m_agrupables, m_modoConsulta, m_modoFilter, m_creationRow, m_finderRow, m_topLabel);
	*/
	public DynamicTableModel(	String id, GFormTable ff /*IComponentListener controlListener*/,
			/*communicator com,*/ int action,List<DynamicTableColumn> listaColumnas,List<GTableRow> listaFilas, boolean cuantitativo,
			int iniVirtColumn,int atGroupColum, ArrayList totalColumns, ArrayList agrupables,
			boolean modoConsulta, boolean modoFilter, boolean creationRow, boolean finderRow, boolean topLabel)  {
		logger.info("dynamic_tabled_model_constructor_started");
		//m_controlListener = controlListener;
		//m_com = com;
		m_ff = ff;
		m_action = action;
		m_modoConsulta = modoConsulta;
		m_modoFilter = modoFilter;
		m_cuantitativo = cuantitativo;
		m_iniVirtualColumn = iniVirtColumn + (m_cuantitativo ? 1 : 0);
		m_atGroupTypeColumn = atGroupColum + (m_cuantitativo ? 1 : 0);
		m_colTotales = totalColumns;
		m_colAgrupables = agrupables;
		/* BuildModel(dataModel); */
		this.listaFilas=listaFilas;
		this.m_creationRow=creationRow;
		this.m_finderRow=finderRow;
		m_id=id;
		//setIdoRowEditing(null);
		if(null != listaColumnas) {
			logger.info("listaColumnas_is_not_null:"+listaColumnas.size());
		}else {
			logger.info("listaColumnas_is_null");
		}
		init=true;
		buildTabla(listaColumnas,topLabel);
		init=false;
		directEdition=false;
		logger.info("dynamic_tabled_model_constructor_finished");
	}
	/*
	public DynamicTableModel(){
		super();
		this.sourceList = new ArrayList<T>();
		this.columns = new ArrayList<DynamicTableColumn>();
		this.selectedObjects = new ArrayList<T>();
	}
	*/
	/**
	 * @param column
	 */
	/*
	public void addColumn(DynamicTableColumn column){
		this.columns.add(column);
	}
*/
	/**
	 * @param srcList
	 */
	/*
	public synchronized void addAll(Collection<T> srcList){
		logger.info("2222222222222222");
		this.sourceList.clear();
		this.sourceList.addAll(srcList);
		debugJustInCase("add_all_called");
		debugJustInCase("add_all_called:"+this.getRowCount() );
		this.fireTableDataChanged();
		if (this.getRowCount() > 0){
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					//table.setRowSelectionInterval(0,0);					
				}
			});
		}
	}
*/
	/**
	 * @param e
	 */
	/*
	public synchronized void addRow(T e){
		logger.info("2222222222222222");
		this.sourceList.add(e);
		this.fireTableDataChanged();
		int modelIndex = this.sourceList.indexOf(e);
		int visualIndex = this.table.convertRowIndexToView(modelIndex);		
		this.table.setRowSelectionInterval(visualIndex,visualIndex);
	}
	*/
	/**
	 * 
	 */
	/*
	public synchronized void clear(){
		this.sourceList.clear();
		this.fireTableDataChanged();
	}
*/
	/**
	 * @param e
	 * @return
	 */
	/*
	public boolean contains(T e){
		return this.sourceList.contains(e);
	}
*/
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Comparable.class;
	}	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	/*
	public int getColumnCount() {
		return columns.size();
	}
	*/
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	/*
	@Override
	public String getColumnName(int column) {
		return columns.get(column).getTitle();
	}
	*/
	/**
	 * @return
	 */
	/*
	public List<DynamicTableColumn> getColumns() {
		return columns;
	}
	*/
	/**
	 * @param rowIndex
	 * @return
	 */
	/*
	public T getRow(Integer rowIndex){
		return this.sourceList.get(rowIndex);
	}
	*/	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	/*
	public int getRowCount() {
		return sourceList!= null ? sourceList.size() : 0;
	}
	*/
	/**
	 * @return
	 */
	/*
	public T getSelectedObject() {
		logger.info("2222222222222222");
		return selectedObject;
	}
	*/
	/**
	 * @return
	 */
	/*
	public List<T> getSelectedObjects() {
		logger.info("2222222222222222");
		return selectedObjects;
	}
	*/
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	/*
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		debugJustInCase("get_value_at_called");
		T currentObject = this.sourceList.get(rowIndex);
		String propertyName = columns.get(columnIndex).getPropertyName();
		Object valueObject = null;
		if(null != propertyName && !propertyName.equals("selection")) {
			try {
				valueObject = new PropertyUtilsBean().
						getProperty(
							currentObject,
							propertyName);
			} catch (Exception e) {
				e.printStackTrace();
				//logger.info("property_name:"+columns.get(columnIndex).getPropertyName());
			}
		}else {
			valueObject =null;
		}
	
		debugJustInCase("get_value_at_finished");
		return valueObject;
	}
*/
	/**
	 * @param e
	 */
	/*
	public synchronized void removeRow(T e){
		int modelIndex = this.sourceList.indexOf(e);
		this.sourceList.remove(e);
		int visualIndex = this.table.convertRowIndexToView(modelIndex);
		this.fireTableRowsDeleted(visualIndex, visualIndex);		
		this.fireTableDataChanged();
	}
	*/
	/**
	 * @param e
	 *
	 */
	/*
	public void setSelectedObject(T e) {
		logger.info("2222222222222222");
		this.selectedObject = e;
	}
*/
	/**
	 * @param selectedObjects
	 */
	/*
	public void setSelectedObjects(List<T> selectedObjects) {
		this.selectedObjects = selectedObjects;
	}
*/
	/**
	 * @param table
	 */
	/*
	public void setTable(GTable table) {
		parent = table;
	}
*?
	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	/*
	 @Override
	public void valueChanged(ListSelectionEvent e) {
		 logger.info("dafdsafdsafsfsdfds");
		if (!e.getValueIsAdjusting()){				
			int minIndex = table.getSelectionModel().getMinSelectionIndex();
			int maxIndex = table.getSelectionModel().getMaxSelectionIndex();			
			try {
				if (minIndex >= 0){					
					this.selectedObjects.clear();
					for (int i = minIndex ; i<=maxIndex;i++){
						int realIndex = table.convertRowIndexToModel(i);
						T value = getRow(realIndex);
                        if (table.getSelectionModel().isSelectedIndex(i)) {
						    this.selectedObjects.add(value);
                         /* Always the first occurrence is the result of calling getSelectedObject() 
                            if (i == minIndex){
                                this.selectedObject = value;
                            }
                            if (logger.isDebugEnabled()){
                                logger.debug(
                                    new StringBuilder().
                                        append("[M:").append(realIndex).
                                        append(" | V:").append(minIndex ++).
                                        append("] ").append(value).toString());
                            }
                        }
					}
				}
			} catch (Exception e1) {
				logger.error(e1.getMessage());
			} 
		}
	}*/
	 /*
	 @Override
	public void setValueAt(Object newVal, int rowIndex, int columnIndex) {
		
logger.info("before calling getValueAt set value at:"+rowIndex);
		// System.err.println("newVal "+newVal+" rowIndex "+rowIndex+" columnIndex
		// "+columnIndex+" rowItem:"+it);

		getValueAt(rowIndex, columnIndex);
		
		if (columnIndex==2) {
			//logger.info("newVal:"+newVal);
			//logger.info("oldVal:"+oldVal);
			
			logger.info("==========SET VALUE AT IN CONDITION=================");
			//logger.info("setValueAt:"+columnIndex);
			//logger.info("New Val:"+newVal);
			
		}
	}
	 */
	 public void setTable(GTable table) {
		 parent = table;
	 }
	 
	 
	 private void buildTabla(List listaColumnas,boolean topLabel){
			/* Iterator iCol= dataModel.iterator(); */
		 logger.info("build_Tabla_started");
			Iterator iCol = listaColumnas.iterator();
			int size = 0;
			
			
			if (m_cuantitativo) {
				columnNames.add(" ");
				//columnClass.add(Class.forName("javax.swing.JToggleButton"));
				/* columnEditable.add(new Boolean("true")); */
				columnEditable.add(new Boolean(!m_modoConsulta && !m_modoFilter));
			}
			int col = 0;
			while (iCol.hasNext()) {
				DynamicTableColumn item = (DynamicTableColumn) iCol.next();
				/* org.jdom.Element item= (org.jdom.Element)iCol.next(); */
				
				String label = item.getLabel();
				
				/*
				 * String label=item.getAttributeValue("LABEL");
				 * System.out.println("COL "+label+","+m_cuantitativo);
				 */
				int posToken = label.indexOf(":");
				
				if (posToken != -1) {
					label = "<HTML><TABLE cellpadding=0 vspace=0 cellspacing=0><TR><TC>"
						+ label.substring(0, posToken)
						+ "</TC></TR>"
						+ "<TR><TC>"
						+ label.substring(posToken + 1)
						+ "</TC></TR>" + "</TABLE></HTML>";
					dobleHeaderSize = true;
				}
				boolean visible = !item.isHide();
				
				/*
				 * boolean visible= !(item.getAttributeValue("HIDE")!=null &&
				 * item.getAttributeValue("HIDE").equals("TRUE"));
				 */

				if (visible)
					columnNames.add(label);

				String ID = item.getId();
				
				/* String ID= item.getAttributeValue("ID"); */

				Map_IDForm_Column.put(ID, new Integer(col));
				Map_Column_IDForm.add(ID);

				Integer idProp = item.getIdProp();
				
				if(item.hasFinder()){
					columnsFinder.add(col);
					if(item.getTypeFinder()==DynamicTableColumn.CREATION_FINDER)
						columnsFinderIfCreation.add(col);
				}
				
				if(item.hasCreation())
					columnsCreation.add(col);
				/*
				 * Integer tapos= item.getAttributeValue("TA_POS")==null ? null:
				 * new Integer(item.getAttributeValue("TA_POS"));
				 */
				/* int tm= tapos==null ? -1:m_md.getID_TM( tapos ); */
				int tm = item.getType();
				
				if (idProp != null) {
					columnSintax.add(new Integer(tm));
					columnIdProps.add(idProp);
					Map_columnsIdProp.put(idProp, col);
					columnRef.add( /* new Integer( */item.getRef()/* ) */);
					/* columnRef.add( new Integer( item.getAttributeValue("REF") ) ); */
				
					if (visible)
						m_visibleDataColMap.add(new Integer(col));
				} else if (visible)
					m_visibleDataColMap.add(null);

				if (visible)
					m_visibleColMap.add(new Integer(col));

				
				
				
				
				if(ID.equals(GConst.ID_COLUMN_TABLE_SELECTION) && tm==GConst.TM_BOOLEAN)
					columnSelectionRowTable=col;
				
				boolean enable = (item.isEnable() && (!m_modoConsulta && !m_modoFilter) || /*clase
						.equals("javax.swing.JButton")*/tm==GConst.TM_BUTTON/*Para favoritos*/ || Auxiliar.equals(col,columnSelectionRowTable));
				
				
				if(enable) {
					columnsEnable.add(col);
				}
				
				//System.err.println("label:"+label+" enable:"+enable);
				boolean editable = enable && item.isBasicEdition();
				columnEditable.add(new Boolean(editable));
				
				/*
				 * String editable= item.getAttributeValue("ENABLE");
				 * if(editable!=null && editable.equals("TRUE"))
				 * columnEditable.add(new Boolean("true")); else
				 * columnEditable.add(new Boolean("false"));
				 */
				size++;
				col++;
			}
			buildRows(listaFilas,false);
			
			if((m_creationRow || m_finderRow) && !m_modoConsulta){
				if(topLabel || !topLabel && listaFilas.isEmpty())//Si es una tabla de una sola fila y ya tiene algun valor no ponemos la nullRow
					addNullRow();
			}
				
			updateColumnWidths();
			logger.info("build_Tabla_finished");
		}

		/**
		 * Añade filas a la tabla pudiendo ser un vector de GTableRow o de RowItem
		 * @param rows
		 * @param replace
		 * @throws AssignValueException
		 */
		public void buildRows(List<?> rows,boolean replace){
			logger.info("build_rows_started");
			
			int numRowsBefore=getRowCount();
			int rowSelection=-1;
			Iterator<?> itr=rows.iterator();
			
			boolean isGTableRow=true;
			if(!rows.isEmpty() && (rows.get(0) instanceof RowItem)){
				isGTableRow=false;
			}
			
			if(isGTableRow){
			
				while(itr.hasNext()){
					GTableRow tableRow=(GTableRow)itr.next();
					boolean rowAdded=setTableRow(tableRow, replace);
					
					if(rowAdded)
						rowSelection=getRowCount()-1;
				}
			}else{
				
				while(itr.hasNext()){
					RowItem rowItem=(RowItem)itr.next();
					boolean rowAdded=setRowItem(rowItem, replace);
					if(rowAdded)
						rowSelection=getRowCount()-1;
				}
			}
			
			//Deseleccionamos el checkbox de seleccion de todo al insertar nuevos registros
			if(replace && columnSelectionRowTable!=null){
				TableColumn tc = getColumnModel().getColumn(columnSelectionRowTable);
				tc.setHeaderValue(false);
				parent.getTable().getTableHeader().repaint();
			}
			
			if((m_creationRow || m_finderRow) && rowSelection!=-1){
				//Hacemos, si existe, que la fila en blanco sea la ultima fila de la tabla
				boolean selection=true;
				if(parent!=null && parent.getTable().getSelectedRow()==-1){
					selection=false;
				}
				
				if(!init && selection){
					parent.getTable().setRowSelectionInterval(getRowCount()-1, getRowCount()-1);//Fila Creada
					//System.err.println("Seleccionaaaaaaaaaaaaaaaa "+parent.getId());
				}
				
				if(removeNullRow()){
//						Component componentFocus=parent.window.getFocusOwner();
					if(this.parent.getFormField().isTopLabel()){//Si es una tabla de una sola fila no hay que volver a crear la nullRow
						
						addNullRow();
					}
					
				}
			}
			
		
		
			if (!replace && !m_cuantitativo && (parent==null || parent.m_modoFilter) )
				fireTableRowsInserted(numRowsBefore, numRowsBefore+rows.size()-1);
			
			//System.err.println("**************** Final buildRows**********"+System.currentTimeMillis());
			//updateColumnWidths();
			
			//System.err.println("m_rowData tras buildRows:"+m_rowData);
			logger.info("build_rows_finished");
		}
		public void updateColumnWidths(){
			//Hacemos que se ejecute en el hilo AWT ya que, si no es asi, a veces se queda bloqueado en las pruebas
			final Runnable update = new Runnable() {
				public void run() {
					updateColumnWidths(0);
				}
			};
			SwingUtilities.invokeLater(update);
		}
		/**
		 * Actualiza el ancho de las columnas de la tabla a partir de su contenido
		 * @param depth Nos sirve para evitar que entre en bucle infinito
		 */
		private void updateColumnWidths(final int depth){
			final Runnable update = new Runnable() {
				public void run() {
					updateColumnWidths(depth+1);
				}
			};
			if(depth<10){
				if(parent!=null && (m_modoFilter || parent.getTable().isValid())/*Esperamos a que sea valido por que si no los tamaños para hacer los calculos no son correctos*/)
					calcColumnWidths(parent.getTable());
				else{
					SwingUtilities.invokeLater(update);
				}
			}
		}
		public void calcColumnWidths(JTable table) {
			table.validate();
			//table.doLayout();
			JTableHeader header = table.getTableHeader();
			TableCellRenderer defaultHeaderRenderer = null;
			if (header != null)
				defaultHeaderRenderer = header.getDefaultRenderer();

			TableColumnModel columns = table.getColumnModel();
			DynamicTableModel data = this;
			int margin = columns.getColumnMargin();
			int rowCount = data.getRowCount()<10?data.getRowCount():10;//Nos basamos en los primeros 10 registros para hacer los calculos ya que si no tarda demasiado
			int totalWidth = 0;
			int totalWidthRows = 0;

			HashMap<TableColumn,Integer> mapTableColumnToResizeNotPrioritary=new HashMap<TableColumn, Integer>();
			for (int i = columns.getColumnCount() - 1; i >= 0; --i) {
				TableColumn column = columns.getColumn(i);
				int columnIndex = column.getModelIndex();
				int width = -1;
				TableCellRenderer h = column.getHeaderRenderer();
				if (h == null)
					h = defaultHeaderRenderer;
				if (h != null){
					Component c = h.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, -1, i);
					width = c.getPreferredSize().width;
				}
				
				//Las columnas prioritarias son las que vamos a respetar el tamaño calculado. Las no prioritarias dependera de si hay espacio suficiente o no. Si no lo hay se les restaran pixeles.
				boolean prioritaryColumn=false;
				if(rowCount>0){
					TableCellRenderer r = table.getCellRenderer(0, i);
					if(r instanceof TextCellRenderer){
						TextCellRenderer textCell=(TextCellRenderer)r;
						int type=textCell.getType();
						if(type==GConst.TM_INTEGER || type==GConst.TM_REAL || type==GConst.TM_BOOLEAN ||
								(textCell.getColumn().getIdProp()==Constants.IdPROP_RDN && textCell.getColumn().getIdParent()==parent.getId()/*rdn principal*/)){
							prioritaryColumn=true;
						}
					}
				}
				
				int widthRows=width;
				for (int row = rowCount - 1; row >= 0; --row) {
					TableCellRenderer r = table.getCellRenderer(row, i);
					Component c = r.getTableCellRendererComponent(table, data.getValueAt(row, columnIndex), false, false, row, i);
					if (c != null){
						//System.err.println("parent.getTable().getName():"+parent.getTable().getName()+" con preferredSize:"+c.getPreferredSize());
						widthRows = Math.max(widthRows, (int)c.getPreferredSize().getWidth());
					}
				}
				if (widthRows >= 0){
					if(prioritaryColumn){
						column.setPreferredWidth(widthRows + margin);//Asignamos el tamaño maximo de la cabecera y de las filas
					}else{
						column.setPreferredWidth(width + margin);//Asignamos el tamaño de la cabecera. Mas abajo se le asignara el tamaño dependiente del contenido y del espacio disponible
						mapTableColumnToResizeNotPrioritary.put(column, widthRows + margin);
					}
				}/*else
					System.err.println("No hay ancho");*/
				totalWidth += column.getPreferredWidth();
				totalWidthRows +=(widthRows+margin);
			}
			
			int tableWidth=(int)parent.m_objComponent.getWidth();//Se trata del scrollPane
			int toRest=0;
			//Vemos si hay espacio de sobra para asignar el tamaño basandonos en el ancho del contenido de las filas tambien para las columnas que no son prioritarias
			if(totalWidthRows>=tableWidth){
				//Si no lo hay, asignamos el tamaño restandole pixeles a las columnas no prioritarias. De esta manera evitamos que la autoredimension de swing haga mas pequeñas tambien las columnas prioritarias
				toRest=(int)Math.ceil(((double)totalWidthRows-tableWidth)/mapTableColumnToResizeNotPrioritary.size());
			}
			for(TableColumn tableColumn:mapTableColumnToResizeNotPrioritary.keySet()){
				tableColumn.setPreferredWidth(mapTableColumnToResizeNotPrioritary.get(tableColumn)-toRest);
			}
			
			table.validate();
			//table.doLayout();
		}
		/*
		public void addNullRow()  {
			int idto=new IdObjectForm(m_id).getValueCls();
			GTableRow tableRow=new GTableRow(new GIdRow(0,idto,null));
			Iterator<String> itrCol=Map_Column_IDForm.iterator();
			while(itrCol.hasNext()){
				String idColumn=itrCol.next();
				//tableRow.setDataColumn(idColumn, null);
				if(IdObjectForm.matchFormat(idColumn))
					tableRow.setIdtoMap(idColumn, new IdObjectForm(idColumn).getIdto());
				else tableRow.setIdtoMap(idColumn, idto);
				String idParent=m_ff.getColumn(getFieldColumn(idColumn)).getIdParent();
				tableRow.setIdParentMap(idColumn, idParent);
			}
			tableRow.setNullRow(true);
			setTableRow(tableRow, false);
		}*/
		public boolean setTableRow(GTableRow tableRow, boolean replace)
				 {

					Iterator<String> itrIdColumns=Map_Column_IDForm.iterator();
					HashMap<String,Object> columnValues=new HashMap<String, Object>();
					
					
					
					while(itrIdColumns.hasNext()){
						String idColumn=itrIdColumns.next();
						
						Object value=tableRow.getDataColumn(idColumn);
						
						columnValues.put(idColumn, value);
					}
					GIdRow idRow=tableRow.getIdRow();
					
					int ido=-1;
					int idto=-1;
					if(idRow!=null){
						ido=idRow.getIdo();
						idto=idRow.getIdto();
					}
					HashMap<String,Integer> columnsIdObjectByIdColumn=tableRow.getIdoMap();
					HashMap<String,Integer> columnsIdtoByIdColumn=tableRow.getIdtoMap();
					HashMap<String,Integer> columnsIdoFilterByIdColumn=tableRow.getIdoFilterMap();
					HashMap<String,Integer> columnsIdtoFilterByIdColumn=tableRow.getIdtoFilterMap();
					
					Color color = null;
					/*
					 * 03/04/06 Si quisiera meter colores debería ñadir otra estructura con
					 * datos gráficos if(eRow.getAttributeValue("COLOR")!=null){
					 * if(eRow.getAttributeValue("COLOR").equals("BLUE")) color= Color.blue;
					 * if(eRow.getAttributeValue("COLOR").equals("RED")) color= Color.red;
					 * if(eRow.getAttributeValue("COLOR").equals("GREEN")) color=
					 * Color.green; }else
					 */
					color = Color.black;

					boolean nullRow=tableRow.isNullRow();
					boolean permanent=tableRow.isPermanent();
						
//					ArrayList<Integer> rowParRow = new ArrayList<Integer>();
			//
//					// añado los parámetros aunque sean nulos por no romper el indice
//					rowParRow.add(ido);
//					rowParRow.add(idto);
//					rowParRow.add(null);// Es el rdn, y en la nueva instance siempre viene a
//					// null

					int row = !permanent?getRowCount():0;

					ArrayList<Object> columnData = new ArrayList<Object>();
					ArrayList<Integer> columnIdo = new ArrayList<Integer>();
					ArrayList<Integer> columnOldIdo = new ArrayList<Integer>();
					ArrayList<Integer> columnIdto = new ArrayList<Integer>();
					ArrayList<Integer> columnOldIdto = new ArrayList<Integer>();
					ArrayList<String> columnIdParent = new ArrayList<String>();
					ArrayList<Integer> columnIdoFilter = new ArrayList<Integer>();
					ArrayList<Integer> columnIdtoFilter = new ArrayList<Integer>();
					int columns=getColumnCount();
					
					for(int i=0;i<columns;i++){
						String id=getFieldIDFromColumn(i);
						Object value=columnValues.get(id);
						columnData.add(value);
						
						Integer idObject=columnsIdObjectByIdColumn.get(id);
						columnIdo.add(idObject);
						columnOldIdo.add(idObject);
						
						Integer idtoObj=columnsIdtoByIdColumn.get(id);
						columnIdto.add(idtoObj);
						columnOldIdto.add(idtoObj);
						
						Integer idoFilter=columnsIdoFilterByIdColumn.get(id);
						columnIdoFilter.add(idoFilter);
						
						Integer idtoFilter=columnsIdtoFilterByIdColumn.get(id);
						columnIdtoFilter.add(idtoFilter);
						
						String idParent=tableRow.getIdParentMap(id);
						//System.err.println("idParent:"+idParent+" col:"+i+ " columnValue:"+value+" columnIdo:"+idObject);
						columnIdParent.add(idParent);
					}

					
					RowItem ritem = buildRowItem(row, columnData, columnIdo, columnOldIdo, columnIdto, columnOldIdto, columnIdParent, columnIdoFilter, columnIdtoFilter, color, idRow, nullRow, permanent);
					
					return setRowItem(ritem, replace);
				}
		
		private boolean setRowItem(RowItem ritem, boolean replace) {
			boolean rowAdded=true;
			
			if (!replace){
				subAddRow(ritem.getIndex(), ritem);
			}else {
				int rowToReplace = findRow(ritem.getIdRow().getIdo(), false, ritem.isPermanent()); // se supone
				// que para
				// filtrar
				// no llamo
				// a
				// buildMap
				if(parent!=null && !parent.isTopLabel() && !parent.newValueAllowed()){
					rowToReplace=0;
				}
				
				if (rowToReplace >= 0) {
					m_rowData.set(rowToReplace,ritem);
									
					rowAdded=false;
					
					if (!m_cuantitativo)
						fireTableRowsUpdated(rowToReplace, rowToReplace);
					
					// Si esta en modo edicion mientras se inserta una fila hacemos que salga de ese modo y vuelva a entrar ya que si cambia el valor de esa celda(al insertar una nueva fila la celda en edicion puede pertenecer a otro registro) no se actualiza
					if(parent!=null){
						//System.err.println("Tiene padre. La tabla "+parent.getLabel()+" isEditing:"+parent.getTable().isEditing());
						Component editor=parent.getTable().getEditorComponent();
						//System.err.println("editor:"+editor);
						if(editor!=null){
							parent.getTable().removeEditor();
							editor.requestFocusInWindow();
							//System.err.println("************************************************subAddRow");
						}/*else{
							System.err.println("Editor es null de la tabla:"+parent.getLabel());
						}*/
					}
				} else{
					subAddRow(ritem.getIndex(), ritem);
				}
			}

			if (m_cuantitativo)
				updateGUI(true);
			// if(row>0) addNullRow();
			
			return rowAdded;
		}
		

	

		public void orderRows(int column){
			
			int[] columns={column};
			int[] sortingDirections={1};
			sortColumns(columns, sortingDirections, parent.getTable());
		}

	

		/*
		 * public void BuildData(ArrayList dataRows, ArrayList parRows, boolean
		 * replace, instance source) throws NoSuchElementException{ int
		 * row=getRowCount(); for( int i=0;i<dataRows.size();i++){ ArrayList
		 * columnData= (ArrayList)dataRows.get(i); ArrayList rowParRow=
		 * (ArrayList)parRows.get(i);
		 * 
		 * if(!replace) subAddRow( row, columnData, rowParRow, null, source ); else{
		 * int rowToReplace=findRow(Integer.parseInt(rowParRow.get(0).toString()),
		 * false); if(rowToReplace>=0) { RowItem ritem= (RowItem)m_rowData.get(
		 * rowToReplace ); ritem.setColumnData( columnData ); if(!m_cuantitativo)
		 * fireTableRowsUpdated(rowToReplace,rowToReplace); }else subAddRow( row,
		 * columnData, rowParRow, null, source ); } row++; } if( m_cuantitativo)
		 * updateGUI(true); //if(row>0) addNullRow(); }
		 */

		private void subAddRow(int row, RowItem ritem)  {
			
			if(parent==null || parent.m_modoFilter || parent.newValueAllowed()){
				
				
				//m_rowData.add(ritem);
				m_rowData.add(row,ritem);
				//Si la insercion no es al final modificamos el index de los siguientes rowItem
				for(int i=row+1;i<m_rowData.size();i++){
					m_rowData.get(i).setIndex(i);
				}
				m_visibleRowMap.add(/*new Integer(row)*/m_rowData.size()-1);
				
				if (!m_cuantitativo && (parent==null || !parent.m_modoFilter || ritem.isPermanent()))
					fireTableRowsInserted(row, row);
				
				//Si esta en modo edicion mientras se inserta una fila hacemos que salga de ese modo y vuelva a entrar ya que si cambia el valor de esa celda(al insertar una nueva fila la celda en edicion puede pertenecer a otro registro) no se actualiza
				if(parent!=null && !parent.m_modoFilter){
					//System.err.println("Tiene padre. La tabla "+parent.getLabel()+" isEditing:"+parent.getTable().isEditing());
					Component editor=parent.getTable().getEditorComponent();
					//System.err.println("editor:"+editor);
					if(editor!=null){
						parent.getTable().removeEditor();
						editor.requestFocusInWindow();
						//System.err.println("************************************************subAddRow");
					}/*else{
						System.err.println("Editor es null de la tabla:"+parent.getLabel());
					}*/
				}
			}else{
				logger.error("exception No se pueden añadir mas valores a la tabla");
				//AssignValueException e2=new AssignValueException("No se pueden añadir mas valores a la tabla "+parent.getLabel());
				//e2.setUserMessage("No se pueden añadir mas valores a la tabla '"+parent.getLabel()+"'");
				//throw e2;
			}
		}

		private RowItem buildRowItem(int row, ArrayList columnData,
				ArrayList<Integer> columnIdo, ArrayList<Integer> columnOldIdo,
				ArrayList<Integer> columnIdto, ArrayList<Integer> columnOldIdto,
				ArrayList<String> columnIdParent,
				ArrayList<Integer> columnIdoFilter,
				ArrayList<Integer> columnIdtoFilter, Color color, GIdRow idRow,
				boolean nullRow, boolean permanent) {
			RowItem ritem = new RowItem(row, /*getColumnCount()
					- (m_cuantitativo ? 1 : 0),*/ m_groupByColumns);
			ritem.setColumnData(columnData);
			ritem.setColumnIdo(columnIdo);
			ritem.setColumnOldIdo(columnOldIdo);
			ritem.setColumnIdto(columnIdto);
			ritem.setColumnOldIdto(columnOldIdto);
			ritem.setColumnIdParent(columnIdParent);
			ritem.setColumnIdoFilter(columnIdoFilter);
			ritem.setColumnIdtoFilter(columnIdtoFilter);
			ritem.setColor(color);
			ritem.setIdRow(idRow);
			ritem.setNullRow(nullRow);
			ritem.setPermanent(permanent);
			if(nullRow){
				if(m_creationRow)//Por defecto una nullRow se le pone estado de creacion. TextCellEditor se encarga de ponerle estado finder cuando sea necesario
					ritem.setState(RowItem.CREATION_STATE);
			}
			return ritem;
		}

		public void replaceColumn(int column, Integer ido, Object newVal, boolean permanent) {
			int rowToReplace = findRow(ido, false, permanent);
			RowItem it = (RowItem) m_rowData.get(rowToReplace);
			// Object valor= buildValue( column, newVal );
			it.setColumnData(column, newVal);
			// System.out.println("ROW TO REPLACE:"+rowToReplace+",new val,
			// buildVal:"+newVal+","+valor);
			if (m_cuantitativo)
				updateGUI(true);
			else
				fireTableRowsUpdated(rowToReplace, rowToReplace);
		}

		

		public int getColumnSintax(int col) {
			return ((Integer) columnSintax.get(col)).intValue();
		}

		public int getColumnIdProps(int col) {
			return ((Integer) columnIdProps.get(col)).intValue();
		}

		public Integer getColumnOfIdProp(int idProp) {
			return Map_columnsIdProp.get(idProp);
		}

		public void printRows() {

			for (int r = 0; r < m_rowData.size(); r++) {
				RowItem row = (RowItem) m_rowData.get(r);
				System.out.println("ROW:" + r + "," + row.isGroup() + ","
						+ row.getGroupSize());
			}
		}

		void updateGUI(boolean reagrupar) {
			if (reagrupar)
				agrupate();
			m_visibleRowMap.clear();
			for (int r = 0; r < m_rowData.size(); r++) {
				RowItem row = (RowItem) m_rowData.get(r);
				if (row.isGroup()) {
//					row.getColumnPar().clear();
//					row.getColumnPar().add(new Integer(r));
//					row.getColumnPar().add(new Integer(GTable.virtualTypeForAgregation));
				}
				if (row.isFiltered() && m_filteredState)
					continue;
				m_visibleRowMap.add(new Integer(r));
				if (row.isGroup() && !row.isGroupExpand())
					r += row.getGroupSize();
			}
			fireTableDataChanged();
			// printRows();
		}

		void inicializaGroupByColumns() {
			if (m_groupByColumns.size() == 0) {
				int lastGroupColumn = m_iniVirtualColumn - 1;
				// System.out.println("LAST COL:"+lastGroupColumn);
				m_groupByColumns.add(new Integer(m_atGroupTypeColumn));
				for (int c = 1; c < getColumnCount() - 1; c++) {// desde el 1 me
					// salto el rdn.
					// Resto 1 para quitar la columna del despliege
					if (c == m_atGroupTypeColumn)
						continue;
					int stx = ((Integer) columnSintax.get(c)).intValue();
					if (c > lastGroupColumn)
						continue;
					Integer iCol = new Integer(c);
					if (m_colTotales.indexOf(iCol) != -1)
						continue;
					if (m_colAgrupables.indexOf(iCol) != -1)
						continue;
					Integer tapos = (Integer) m_idPropModel
					.get(getRealDataColumn(c));
					//if (tapos.intValue() == helperConstant.TAPOS_ESTADO)
					//	continue;
					// lo considero agrupable
					if (c <= lastGroupColumn
							&& (stx == GConst.TM_MEMO
									|| stx == /* helperConstant.TM_FECHA */GConst.TM_DATE
									|| stx == /* helperConstant.TM_FECHAHORA */GConst.TM_DATE_HOUR
									|| stx == GConst.TM_INTEGER || stx == GConst.TM_REAL))
						continue;
					m_groupByColumns.add(new Integer(c));
				}
			}
		}

		int getGroupParent(int rowIndex, boolean visibleIndex) {
			if (visibleIndex)
				rowIndex = getRowIndex(rowIndex);
			int i = rowIndex - 1;
			while (i >= 0) {
				RowItem it = (RowItem) m_rowData.get(i);
				if (it.isGroup())
					if (it.getGroupSize() >= rowIndex - i)
						return i;
					else
						return -1;
				i--;
			}
			return -1;
		}

		@SuppressWarnings("unchecked")
		void agrupate() {
			if (!m_cuantitativo)
				return;
			int tam = m_rowData.size();
			for (int r = tam - 1; r >= 0; r--) {
				RowItem row = (RowItem) m_rowData.get(r);
				if (row.isGroup())
					m_rowData.remove(r);
			}
			Object[] data = m_rowData.toArray();
			Arrays.sort(data);
			m_rowData = new ArrayList(Arrays.asList(data));

			int lastGroupColumn = m_iniVirtualColumn - 1;
			Object[] oldData = new Object[getColumnCount()];
			boolean inGroup = false;
			ArrayList<RowItem> groupHeaders = new ArrayList<RowItem>();

			RowItem group = null;
			int groupSize = 0;
			boolean[] changeInColumn = new boolean[getColumnCount() - 1];
			for (int i = 0; i < getColumnCount() - 1; i++)
				changeInColumn[i] = false;

			for (int r = 0; r < m_rowData.size(); r++) {
				RowItem row = (RowItem) m_rowData.get(r);
				if (row.isFiltered() && m_filteredState)
					continue;
				boolean changeInGroup = false;

				// primero miro los posibles cambios de agrupacion
				for (int c = 1; c < getColumnCount() - 1; c++) {// desde el 1 me
					// salto el rdn
					if (m_groupByColumns.indexOf(new Integer(c)) == -1)
						continue;

					Object val = row.getColumnData(c);
					if (oldData[c] == null && val != null || val == null
							&& oldData[c] != null || val != null
							&& oldData[c] != null && !val.equals(oldData[c])) {

						changeInGroup = true;
						break;
					}
				}

				for (int c = 1; c < getColumnCount() - 1; c++) {// desde el 1 me
					// salto el
					// desplegable
					if (m_groupByColumns.indexOf(new Integer(c)) != -1)
						continue;
					if (m_colTotales.indexOf(new Integer(c)) != -1)
						continue;
					//int stx = ((Integer) columnSintax.get(c)).intValue();
					Object val = row.getColumnData(c);
					boolean change = false;
					if (oldData[c] == null && val != null || val == null
							&& oldData[c] != null || val != null
							&& oldData[c] != null && !val.equals(oldData[c])) {
						change = true;
					}

					if (!changeInGroup && !changeInColumn[c] && change)
						// si ha habido cambio de grupo no quiero perder la
						// informacion
						changeInColumn[c] = true;

				}
				if (!inGroup && !changeInGroup) {
					inGroup = true;
					groupSize = 1;
					int rowIniGroup = r;
					while (rowIniGroup > 0) {
						RowItem eIniGroup = (RowItem) m_rowData.get(--rowIniGroup);
						// decremento por si me ido abajo saltandome rows filtrados.
						if (!(m_filteredState && eIniGroup.isFiltered())) {
							group = new RowItem(rowIniGroup, /*getColumnCount() - 1,*/
									m_groupByColumns);
							groupHeaders.add(group);

							for (int c = 1; c < getColumnCount() - 1; c++) {
								int col = getRealDataColumn(c);
								int stx = ((Integer) columnSintax.get(c))
								.intValue();
								if (c <= lastGroupColumn
										&& (stx == GConst.TM_MEMO
												|| stx == GConst.TM_DATE || stx == GConst.TM_DATE_HOUR))
									continue;
								if (c > lastGroupColumn && c != m_atGroupTypeColumn)
									continue;
								if (m_colTotales.indexOf(new Integer(c)) != -1) {
									if (stx == GConst.TM_INTEGER) {
										Integer currVal = (Integer) eIniGroup
										.getColumnData(col);
										Integer grVal = new Integer(currVal
												.intValue());
										group.setColumnData(col, grVal);
									}
									if (stx == GConst.TM_REAL) {
										Double currVal = (Double) eIniGroup
										.getColumnData(col);
										Double grVal = new Double(currVal
												.doubleValue());
										group.setColumnData(col, grVal);
									}
								}
								if (m_colTotales.indexOf(new Integer(c)) == -1) {
									if (stx == GConst.TM_ENUMERATED) {
										Integer tapos = (Integer) m_idPropModel
										.get(col);
										//if (tapos.intValue() == helperConstant.TAPOS_ESTADO)
										//	continue;
										// lo considero agrupable
										ItemList currVal = (ItemList) eIniGroup
										.getColumnData(col);
										group.setColumnData(col, currVal.clone());
									}
									if (stx == GConst.TM_TEXT) {
										String currVal = (String) eIniGroup
										.getColumnData(col);
										group.setColumnData(col, new String(
												(String) currVal));
									}
								}
							}
							break;
						}
					}
				}

				if (inGroup)
					if (changeInGroup) {
						inGroup = false;
						groupSize = 0;
						for (int c = 1; c < getColumnCount() - 1; c++) {
							int col = getRealDataColumn(c);
							if (m_groupByColumns.indexOf(new Integer(c)) != -1)
								continue;
							if (m_colTotales.indexOf(new Integer(c)) != -1)
								continue;
							if (!changeInColumn[c]) {
								int stx = ((Integer) columnSintax.get(col))
								.intValue();
								Object val = row.getColumnData(col);
								if (val == null)
									continue;
								if (stx == GConst.TM_INTEGER) {
									Integer currVal = (Integer) val;
									Integer grVal = new Integer(currVal.intValue());
									group.setColumnData(col, grVal);
								}
								if (stx == GConst.TM_REAL) {
									Double currVal = (Double) val;
									Double grVal = new Double(currVal.doubleValue());
									group.setColumnData(col, grVal);
								}
								if (stx == GConst.TM_ENUMERATED) {
									ItemList currVal = (ItemList) val;
									group.setColumnData(col, currVal.clone());
								}
								if (stx == GConst.TM_TEXT) {
									String currVal = (String) val;
									group.setColumnData(col, new String(
											(String) currVal));
								}
							}
						}
						for (int i = 0; i < getColumnCount() - 1; i++)
							changeInColumn[i] = false;
					} else {
						group.setGroupSize(++groupSize);
						for (int c = 0; c < getColumnCount() - 1; c++) {
							int col = getRealDataColumn(c);
							if (m_colTotales.indexOf(new Integer(c)) == -1)
								continue;
							int stx = ((Integer) columnSintax.get(c)).intValue();
							if (stx == GConst.TM_INTEGER) {
								Integer currVal = (Integer) row.getColumnData(col);
								Integer grVal = (Integer) group.getColumnData(col);
								grVal = new Integer(currVal.intValue()
										+ grVal.intValue());
								group.setColumnData(col, grVal);
							}
							if (stx == GConst.TM_REAL) {
								Double currVal = (Double) row.getColumnData(col);
								Double grVal = (Double) group.getColumnData(col);
								grVal = new Double(currVal.floatValue()
										+ grVal.floatValue());
								group.setColumnData(col, grVal);
							}
						}
					}

				for (int c = 1; c < getColumnCount() - 1; c++) {
					int col = getRealDataColumn(c);
					int stx = ((Integer) columnSintax.get(c)).intValue();
					if (stx == GConst.TM_MEMO)
						continue;
					oldData[c] = row.getColumnData(col);
				}
			}
			for (int i = groupHeaders.size() - 1; i >= 0; i--) {
				RowItem header = (RowItem) groupHeaders.get(i);
				m_rowData.add(header.getIndex(), header);
			}
		}

		public int findRow(int ido, boolean filteringAware, Boolean permanent) {
			// se supone que el id del row es el parametro 1 (posicion 0)
			for (int r = 0; r < m_rowData.size(); r++) {
				RowItem row = (RowItem) m_rowData.get(r);
				if (filteringAware && row.isFiltered())
					continue;
				if(permanent!=null && row.isPermanent()!=permanent)
					continue;
				GIdRow idRow = row.getIdRow();
				if (!row.isGroup() && (idRow!=null && idRow.getIdo() == ido))
					// if( isVisible( r, filteringAware ) ) return r;
					return r;
			}
			return -1;
		}

//		public int findVisibleRow(String key, boolean filteringAware) {
//			// se supone que el id del row es el parametro 1 (posicion 0)
//			for (int r = 0; r < m_visibleRowMap.size(); r++) {
//				RowItem row = (RowItem) m_rowData.get(getRowIndex(r));
//				ArrayList parList = row.getColumnPar();
//				if (filteringAware && row.isFiltered())
//					return -1;
//				if (!row.isGroup() && parList.indexOf(key) == 0)
//					// if( isVisible( r, filteringAware ) ) return r;
//					return r;
//			}
//			return -1;
//		}

		public boolean isVisible(int rowIndex, boolean filteringAware) {
			RowItem row = (RowItem) m_rowData.get(rowIndex);
			if (m_filteredState && row.isFiltered())
				return false;

			if (!m_cuantitativo)
				return true;

			int indexG = getGroupParent(rowIndex, false);
			if (indexG == -1)
				return true;
			RowItem group = (RowItem) m_rowData.get(indexG);
			//int i = rowIndex;
			return group.isGroupExpand();
		}

		
		public void addNullRow() {
			int idto=new IdObjectForm(m_id).getValueCls();
			GTableRow tableRow=new GTableRow(new GIdRow(0,idto,null));
			Iterator<String> itrCol=Map_Column_IDForm.iterator();
			while(itrCol.hasNext()){
				String idColumn=itrCol.next();
				//tableRow.setDataColumn(idColumn, null);
				if(IdObjectForm.matchFormat(idColumn))
					tableRow.setIdtoMap(idColumn, new IdObjectForm(idColumn).getIdto());
				else tableRow.setIdtoMap(idColumn, idto);
				String idParent=m_ff.getColumn(getFieldColumn(idColumn)).getIdParent();
				tableRow.setIdParentMap(idColumn, idParent);
			}
			tableRow.setNullRow(true);
			setTableRow(tableRow, false);
		}
		
		public boolean removeNullRow() {
			int rowToReplace = findRow(0, false, false);
			if(rowToReplace!=-1)
				delRow(rowToReplace);
			
			return rowToReplace!=-1;
		}

		public void delRow(int visRow) {
			Integer row = (Integer) m_visibleRowMap.get(visRow);
			removeRow(row.intValue(), true);
		}
		private void removeRow(int dataRowIndex, boolean refreshGui) {
			Integer key = new Integer(dataRowIndex);
			int visibleRow = m_visibleRowMap.indexOf(key);
			//System.err.println("VISIBLE ROW:"+visibleRow+" getRowCount:"+getRowCount()+" table.getRowCount:"+parent.getTable().getRowCount());
			if (visibleRow != -1) {
				m_visibleRowMap.remove(visibleRow);
				if (m_visibleRowMap.size() > visibleRow) {
					for (int i = visibleRow; i < m_visibleRowMap.size(); i++) {
						Integer indexData = (Integer) m_visibleRowMap.get(i);
						m_visibleRowMap.set(i,
								new Integer(indexData.intValue() - 1));
					}
				}
			}
			RowItem ri=m_rowData.get(dataRowIndex);
			m_rowData.remove(ri);
			if (refreshGui)
				if (m_cuantitativo)
					updateGUI(true);
				else if (visibleRow != -1)
					fireTableRowsDeleted(visibleRow, visibleRow);
			
			//Si esta en modo edicion mientras se borra una fila hacemos que salga de ese modo ya que su row es incorrecta y provoca una excepcion
			if(parent!=null){
				int editingRow=parent.getTable().getEditingRow();
				if(editingRow>=dataRowIndex){//Si la fila es menor que la borrada el row es correcto por lo que no hacemos nada
					parent.getTable().removeEditor();
				}
			
			}
		//	if((m_creationRow || m_finderRow) && !ri.isNullRow() && !m_modoConsulta){
			//	if(!this.parent.getFormField().isTopLabel())//Si es una tabla de una sola fila y se ha borrado pues ponemos la nullRow
			//		addNullRow();
			//}
		}
		public ArrayList getData(int ido) {
			int newId = findRow(ido, true, null);
			//System.outprintln("SPMDBG1 " + ido + "," + newId);
			return ((RowItem) m_rowData.get(newId)).getColumnData();
		}

		public GIdRow getDataFromIndex(int rowIndex) {
			return ((RowItem) m_rowData.get(getRowIndex(rowIndex))).getIdRow();
		}
		
		public RowItem getRowItemFromIndex(int rowIndex) {
			return m_rowData.get(getRowIndex(rowIndex));
		}

		public RowItem getNextRow(int currRowViewIndex) {
			if (currRowViewIndex + 2 > m_visibleRowMap.size())
				return null;
			RowItem it = (RowItem) m_rowData.get(currRowViewIndex + 1);
			if(it.isNullRow())
				return null;
			return it;
		}

		public RowItem getPrevRow(int currRowViewIndex) {
			if (currRowViewIndex <= 0)
				return null;
			RowItem it = (RowItem) m_rowData.get(currRowViewIndex - 1);
			if(it.isNullRow())
				return null;
			return it;
		}

		/*private ArrayList getParameterTable() {
			ArrayList result = new ArrayList();
			for (int i = 0; i < m_rowData.size(); i++) {
				RowItem it = (RowItem) m_rowData.get(i);
				if (it.isGroup())
					continue;
				result.add(it.columnPar);
			}
			return result;
		}

		private ArrayList getDataTable() {
			ArrayList result = new ArrayList();
			for (int i = 0; i < m_rowData.size(); i++) {
				RowItem it = (RowItem) m_rowData.get(i);
				if (it.isGroup())
					continue;
				ArrayList row = new ArrayList();
				for (int c = 0; c < getVisibleColumnDataCount(); c++) {
					int realC = getRealDataColumn(c);
					if (realC == -1)
						continue;
					Object val = it.columnData.get(realC);
					if (val instanceof ItemList)
						val = ((ItemList) val).getInteger();
					row.add(val);
				}
				result.add(row);
			}
			return result;
		}*/

		public ArrayList<GIdRow> getIdRowsData(Boolean permanent) {
			ArrayList<GIdRow> result = new ArrayList<GIdRow>();
			for (int i = 0; i < m_rowData.size(); i++) {
				RowItem it = (RowItem) m_rowData.get(i);
				if (it.isGroup())
					continue;
				if(permanent==null || permanent.equals(it.isPermanent()))
					result.add(it.getIdRow());
			}
			return result;
		}

		public TableColumnModel getColumnModel() {
			return parent.getTable().getColumnModel();
		}

		public boolean isCellEditable(int row, int col) {
			logger.info("is_cell_editable_called:");
			/*
			 * if( m_cuantitativo && col==0 ){ RowItem it=
			 * (RowItem)m_rowData.get(getRowIndex(row)); return it.isGroup(); }
			 * boolean edit= ((Boolean)columnEditable.get(col)).booleanValue();
			 * return edit;
			 */
			
			RowItem it = (RowItem) m_rowData.get(getRowIndex(row));
			
			if(!parent.isProcessingSelectCellFromSystem() && !directEdition /*&& !parent.m_modoFilter*/ && parent.getTable().getSelectedRow()!=row && !it.isNullRow())//Si la fila no estaba ya seleccionada no se deja editar, asi podemos seleccionarla sin querer editarla
				return false;
			
			int colReal = getRealColumn(col);
			if (m_cuantitativo && colReal == 0) {
				return it.isGroup();
			}
			
			boolean edit=((Boolean) columnEditable.get(colReal)).booleanValue();
			
			if(!m_modoConsulta && !m_modoFilter){
				/* Lógica para permitir introducir datos en una columna que PERMITE FINDER
				 * 	NulRow:
				 * 		FinderRow:Editable si es del 1º nivel. En resto de niveles no editable
				 * 		CreationRow:Editable no se modifica
				 * 		FinderRow+CreationRow:Editable si es del 1º nivel. En resto de niveles no se modifica
				 * 	Registro:
				 * 		Se modifica a editable en todos los niveles menos en el 1º
				 */
				//System.err.println("column:"+colReal+" columnEditable:"+columnEditable.contains(colReal)+" columnEnable:"+columnsEnable.contains(colReal)+" columnFinder:"+columnsFinder.contains(colReal)+" finderRow:"+m_finderRow+" creationRow:"+m_creationRow+" nullRow:"+it.isNullRow()+" columnIdto"+it.getColumnIdto(col)+" parIdto:"+it.getColumnPar().get(1).intValue());
				if(columnsFinder.contains(colReal)/* && parent.getDictionaryFinder()!=null*/){
					if(it.isNullRow()){
						if(m_finderRow){
							//System.err.println(it.getColumnPar().get(1).intValue()+" "+it.getColumnIdto(col));
							if(it.getIdRow().getIdto()==it.getColumnIdto(col))
								edit=true;
							else if(!m_creationRow)
								edit=false;
							else edit=true;
						}else{
							if(it.getIdRow().getIdto()!=it.getColumnIdto(col))
								edit=true;
						}
					}else{
						if(columnsFinderIfCreation.contains(colReal)){
							if(isNewIdo(it.getIdRow().getIdo()))
								edit=true;
						}else{
							//if(it.getColumnPar().get(1).intValue()!=it.getColumnIdto(col))
								edit=true;
							/*if(it.getColumnIdo(col)!=null && m_controlListener!=null && parent!=null)
							if(!this.m_controlListener.isEditableInTable(parent.getId(), it.getColumnIdo(col)))
								edit=false;*/
						}
					}
				}else if(columnsEnable.contains(colReal)){
					if(columnsCreation.contains(colReal)){
						if(it.isNullRow()){
							if(m_creationRow){
								if(it.getColumnIdo(col)==null || isNewIdo(it.getColumnIdo(col)))
									edit=true;
							}
						}else{
							if(it.getColumnIdo(col)==null || isNewIdo(it.getColumnIdo(col))){
								edit=true;
							}else{
								int tm=getColumnSintax(col);
								if(tm==GConst.TM_INTEGER || tm==GConst.TM_REAL){//Permitimos que se editen los campos numericos
									edit=true;
								}
							}
						}
						//if(!allowModifyIdoCell(it.getColumnIdo(col)))
						//	edit=false;
					}else if(!it.isNullRow()){
						if(isNewIdo(it.getColumnIdo(col))){
							edit=true;
						}else{
							int tm=getColumnSintax(col);
							if(tm==GConst.TM_INTEGER || tm==GConst.TM_REAL){//Permitimos que se editen los campos numericos
								edit=true;
							}
						}
					}
				}
				
				//Comprobamos que esa columna pertenece realmente al tipo de la fila.
				//Esto ocurre cuando las tablas son abstractas y muestran columnas que tienen unos hijos pero otros no.
				//Se mira que getColumnIdParent sea null ya que eso ocurre cuando la columna no es compatible, pero
				//quizas deberiamos buscar otro mecanismo que nos lo indique mas fehacientemente
				if(edit && it.getColumnIdParent(col)==null){
					//System.err.println("No es columna de ese individuo");
					edit=false;
				}
			}
			
			//System.err.println("Editable:"+edit);
			return edit;
		}
		public int getRealColumn(int visCol) {
			Integer col = (Integer) m_visibleColMap.get(visCol);
			if (col == null)
				return -1;
			return col.intValue();
		}

		public int getColumnCount() {
			// incluye la columna de boton desplegable en cuantitativo
			return columnNames.size();
		}

		public int getRowCount() {
			if (m_visibleRowMap != null)
				return m_visibleRowMap.size();
			return 0;
		}

		public String getColumnName(int col) {
			return (String) columnNames.get(col);
		}

		private int getRowIndex(int visibleRow) {
			//System.err.println("getRowIndex: visibleRow:"+visibleRow+" m_visibleRowMap:"+m_visibleRowMap);
			return ((Integer) m_visibleRowMap.get(visibleRow)).intValue();
		}

		public Object getValueAt(int row, int col) {
			if (m_cuantitativo) {
				RowItem it = (RowItem) m_rowData.get(getRowIndex(row));
				if (col == 0)
					if (it.isGroup())
						return new Boolean(it.isGroupExpand());
					else {
						int grIndex = getGroupParent(row, true);
						if (grIndex == -1)
							return " ";
						else
							return "    >";
					}
				col--;
			}
			int dataCol = getRealDataColumn(col);
			if (dataCol == -1)
				return null;
			
			return getDataValueAt(row, getRealDataColumn(col));
		}
		public int getRealDataColumn(int visCol) {
			Integer col = (Integer) m_visibleDataColMap.get(visCol);
			if (col == null)
				return -1;
			return col.intValue();
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


		int getDataColumn(int columnIndex) {
			return columnIndex - (m_cuantitativo ? 1 : 0);
		}
		
		public void setValueAt(Object newVal, int rowIndex, int columnIndex) {
			lastSetValueSuccess=true;
			executingSetValue=true;
			RowItem it = (RowItem) m_rowData.get(getRowIndex(rowIndex));
			
			//System.err.println("newVal "+newVal+" rowIndex "+rowIndex+" columnIndex "+columnIndex+" rowItem:"+it);
			Object oldVal=getValueAt(rowIndex, columnIndex);
			// DEPURACION
			if(!it.isNullRow() && !Auxiliar.equals(oldVal,newVal)&&!isNewIdo(it.getIdRow().getIdo()) && it.getState()==RowItem.IDLE_STATE && columnsFinder.contains(getRealColumn(columnIndex))){
				/*if(this.parent.getServer()!=null){
					 Exception ex=new Exception("ERROR DE EDICION DE FINDER: Se ha editado una columna que solo permite finder "+getColumnName(getRealColumn(columnIndex))+" valor:"+newVal);
					 this.parent.getServer().logError(null,ex, null);
				 }
				 Auxiliar.printCurrentStackTrace();
				 */
				logger.info("probably_exception");
			}
			
			//Si se trata del click sobre la columna de selección de fila
			if(Auxiliar.equals(columnSelectionRowTable, columnIndex)){
				it.setColumnData(columnIndex, newVal);
				fireTableCellUpdated(rowIndex, columnIndex);
				if(Auxiliar.equals(newVal, false)){
					ListSelectionModel listSelection=parent.getTable().getSelectionModel();
					if(!listSelection.isSelectionEmpty() && listSelection.getMinSelectionIndex()!=listSelection.getMaxSelectionIndex()){
						listSelection.removeSelectionInterval(rowIndex, rowIndex);
					}
					TableColumn tc = getColumnModel().getColumn(columnIndex);
					tc.setHeaderValue(false);
					parent.getTable().getTableHeader().repaint();
				}
			}else if( /*m_controlListener!=null && */!m_modoFilter){
				if( !(it.isNullRow() && newVal==null && !it.matchesState(RowItem.FINDER_STATE)/*Podría ser un finder sin valor en ese campo si este no es obligatorio*/) && parent.window!=null && parent.window.isDisplayable()){
					//Si la fila es nullRow y no se ha añadido ningun valor, no tenemos que crear nada
					//Ademas evitamos que se intente asignar el valor cuando la ventana ya se ha cerrado porque es un problema para las sesiones
					
					
					Object newV=null, oldV=null;
					String newVData=null, oldVData=null;
					int idSource = it.getIdRow().getIdo();
					Integer oldIdoRowEditing=getIdoRowEditing();
					HashSet<Integer> oldListIdosEditing=(HashSet<Integer>)getListIdosEditing().clone();
					try{
						if(!it.matchesState(RowItem.FINDER_STATE) && ((oldVal==null && newVal==null) || (oldVal != null && newVal != null && oldVal.equals(newVal))))//Si es igual al valor que ya teniamos no hacemos nada
							return;
						
						// Lo hacemos aqui y no donde se usa porque starEditionTable y creationRow pueden provocar que se modifique su valor
						Integer idoFinder=it.getColumnIdo(columnIndex);
						Integer idtoFinder=it.getColumnIdto(columnIndex);
						
						if(getIdoRowEditing()==null){
							if(!it.isNullRow() && it.getState()!=RowItem.FINDER_STATE){
								setIdoRowEditing(idSource);
							}
						//	m_controlListener.startEditionTable(parent.getId(),getIdoRowEditing(),parent.isProcessingPasteRows());
						}
							
						IdObjectForm idObjectForm=null;
						 if(it.getState()==RowItem.FINDER_STATE){
							 idObjectForm=new IdObjectForm(it.getColumnIdParent(columnIndex));
							 if(getIdoRowEditing()==null)//Esto ocurre cuando, en una nullRow asignamos una fila buscando un individuo que ya existe, de primer nivel
								 setIdoRowEditing(it.getColumnIdo(columnIndex));
							 else listIdosEditing.add(idObjectForm.getIdo());
						 }else if(it.getState()==RowItem.SUBCREATION_STATE){
							 IdObjectForm idObjectFormNew=new IdObjectForm(it.getColumnIdParent(columnIndex));
							 
							 int idtoSubRow=((CellEditor)parent.getTable().getCellEditor(rowIndex, columnIndex)).getLastSelectionSubCreation();
							 int idoSub=1;
									 
									 //m_controlListener.newSubRowTable(idObjectFormNew.getIdString(),idtoSubRow);
							 //oldVal = getValueAt(rowIndex, columnIndex);
							 
							 idObjectForm=new IdObjectForm((String) Map_Column_IDForm.get(columnIndex));
							 idObjectForm.setIdo(idoSub);
							 
							 listIdosEditing.add(idObjectForm.getIdo());
							 //if(idoRowEditing==null)
							//	 idoRowEditing=it.getColumnIdo(columnIndex);
						 }else if(it.isNullRow()){
							 int idtoRow=((CellEditor)parent.getTable().getCellEditor(rowIndex, columnIndex)).getLastSelectionCreation();
						 	 int ido=1;
						 			 //m_controlListener.newRowTable(m_id,idtoRow);
							 rowIndex = findRow(ido, false, it.isPermanent());
							 
							 //oldVal = getValueAt(rowIndex, columnIndex);
							 if(it.getState()==RowItem.CREATION_STATE){
								 idObjectForm=new IdObjectForm((String) Map_Column_IDForm.get(columnIndex));
								 idObjectForm.setIdo(ido);
								 
								 //idoRowEditing=ido;
							 }else if(it.getState()==RowItem.CREATION_STATE+RowItem.FINDER_STATE){
								 RowItem itNewRow = (RowItem) m_rowData.get(getRowIndex(rowIndex));
								 idObjectForm=new IdObjectForm(itNewRow.getColumnIdParent(columnIndex));
								 
								 //idoRowEditing=itNewRow.getSourceData().getIDO();
							 }else /*State Creation+SubCreation*/{
								 RowItem itNewRow = (RowItem) m_rowData.get(getRowIndex(rowIndex));
								 IdObjectForm idObjectFormNew=new IdObjectForm(itNewRow.getColumnIdParent(columnIndex));
								 
								 int idtoSubRow=((CellEditor)parent.getTable().getCellEditor(rowIndex, columnIndex)).getLastSelectionSubCreation();
								 int idoSub=1;
										 //m_controlListener.newSubRowTable(idObjectFormNew.getIdString(),idtoSubRow);
								 //oldVal = getValueAt(rowIndex, columnIndex);
								 
								 idObjectForm=new IdObjectForm((String) Map_Column_IDForm.get(columnIndex));
								 idObjectForm.setIdo(idoSub);
								 
								 listIdosEditing.add(idoSub);
							 }
							 rowCreating=true;
							 setIdoRowEditing(ido);
							 listIdosEditing.add(ido);
							 
						 }else if(it.getState()==RowItem.REMOVE_STATE){
							 idObjectForm=new IdObjectForm(it.getColumnIdParent(columnIndex));
							 //if(idoRowEditing==null)
							//	 idoRowEditing=idObjectForm.getIdo();//it.getColumnIdo(columnIndex);
							 
							 listIdosEditing.remove(it.getColumnIdo(columnIndex));
							 listIdosEditing.add(idObjectForm.getIdo());
						 }else{
							 idObjectForm=new IdObjectForm((String) Map_Column_IDForm.get(columnIndex));
							 
							 Integer ido = it.getColumnIdo(columnIndex);
							 idObjectForm.setIdo(ido);
							 
							 //idoRowEditing=idSource;
							 
							 listIdosEditing.add(idObjectForm.getIdo());
						 }
						 
						 //Volvemos a pedirlo ya que puede haber cambiado por las operaciones anteriores
						 oldVal=getValueAt(rowIndex, columnIndex);
						
						/*if(it.isColumnLabelVisible(columnIndex)){
							it.setColumnLabelBooleanValue(columnIndex);
							oldVal = null;
						}else{
							oldVal = getValueAt(rowIndex, columnIndex);
						}*/		
						Object oldData = null, newData = null;
						
						//TODO Actualmente nunca llega ni oldVal ni newVal como ItemList ya que enviamos el Integer en ComboBoxEditor.getCellEditorValue
						if (oldVal instanceof ItemList)
							oldData = ((ItemList) oldVal).getInteger()!=0?((ItemList) oldVal).getInteger():null;
						else
							oldData = oldVal;
						
						if (newVal instanceof ItemList)
							newData = ((ItemList) newVal).getInteger()!=0?((ItemList) newVal).getInteger():null;					
						else newData = newVal;
						
						if (!it.matchesState(RowItem.FINDER_STATE)/*Si es finder puede no tener valor pero si tendra ido a asignar*/ && Auxiliar.equals(oldData, newData))
							return;
				
						
						//System.err.println("setValueAt New "+newVal+" oldVal "+oldVal);
						/*int tm = 0;
						if (!(m_cuantitativo && columnIndex == 0))
							tm = getColumnSintax(getDataColumn(columnIndex));*/
				
						 if (m_cuantitativo) {
							 if (columnIndex == 0) {
								 if (newVal instanceof Boolean) {
									 it.setGroupExpand(((Boolean) newVal).booleanValue());
									 updateGUI(false);
								 }
								 return;
							 }
							 columnIndex--;
						 }
						 
						 String id=idObjectForm.getIdString();
						 //Integer idProp=columnIdProps.get(columnIndex);
						 //DataProperty data = (DataProperty)it.getSourceData().getProperty(ido,idProp,null,null,null);
						 //idObjectForm.setValueCls(data.getDataType());
					 
						 int newValueCls=idObjectForm.getValueCls();
						 int oldValueCls=idObjectForm.getValueCls();
						 
						 /*if(newData==null){
							 newV=null;
							 newVData=null;
						 }
						 else*/ if(it.getState()==RowItem.FINDER_STATE || it.getState()==(RowItem.FINDER_STATE+RowItem.CREATION_STATE)){
							 newV = idoFinder;//idoFinder!=null?String.valueOf(idoFinder):null;
							 newVData = newData!=null?String.valueOf(newData):null;
							 newValueCls= idtoFinder;//it.getColumnIdto(columnIndex);
						 }else{
							 newV = newData;//String.valueOf(newData);
							 newVData = String.valueOf(newData);
						 }
						 
						 /*if(oldData==null){
							 oldV=null;
							 if(it.matchesState(RowItem.FINDER_STATE) || it.getState()==RowItem.REMOVE_STATE){
								 Integer oldIdo=it.getColumnOldIdo(columnIndex);
								 oldV = oldIdo;//oldIdo!=null?String.valueOf(oldIdo):null;
							 }
							 oldVData=null;
						 }else*/ if(it.getState()==RowItem.FINDER_STATE || it.getState()==RowItem.REMOVE_STATE){
							 Integer oldIdo=it.getColumnOldIdo(columnIndex);
							 oldV = oldIdo;//oldIdo!=null?String.valueOf(oldIdo):null;
							 oldVData = oldData!=null?String.valueOf(oldData):null;
							 oldValueCls=it.getColumnOldIdto(columnIndex);
						 }else{
							 oldV = oldData;//String.valueOf(oldData);
							 oldVData = String.valueOf(oldData);
						 }
						 
						 //System.err.println("Value:"+newData+" class:"+(newData!=null?newData.getClass():null));			 
						 //TODO replaceColumn no modifica el valor de la property en el instance. Tenerlo en cuenta!
						 if(!it.isNullRow()){
							 replaceColumn(columnIndex, idSource, /*newVData*/newVal, it.isPermanent());
						 }/*else{
							 it.setColumnOldIdo(columnIndex,null);
							 it.setColumnIdo(columnIndex, null);
						 }*/
						// m_controlListener.setValueField(id,newV,oldV,newValueCls,oldValueCls);
						 
						 if(it.matchesState(RowItem.FINDER_STATE) && Auxiliar.equals(newV, oldV)){
							 //Esto solo ocurrira cuando se edita el valor de una columna que tiene finder en la que
							 //las reglas cambian el filtro de la busqueda, cambiandose el valor de uno de los campos
							 //que puede, o no, tener algo que ver con la tabla. Por lo tanto nos interesa dejar la
							 //columna con el valor que tenia antes del cambio del usuario ya que al final tendría que tener el mismo valor.
							 //Esto no esta ocurriendo automaticamente porque se ha enviado una orden de newV=oldV que no cambia el valor del campo, asi que lo hacemos nosotros aqui.
							 replaceColumn(columnIndex, idSource, oldVal, it.isPermanent());
						 }
					 }catch(Exception ex){
						 ex.printStackTrace();
						 //setText(""+oldVal);
						 setIdoRowEditing(oldIdoRowEditing);
						 setListIdosEditing(oldListIdosEditing);
						 /*if(oldIdoRowEditing==null){
							 try{
								 m_controlListener.cancelEditionTable(parent.getId(), getIdoRowEditing());
							 } catch (EditionTableException exc) {
								parent.getServer().logError(SwingUtilities.getWindowAncestor(parent.getTable()),exc,"Error al intentar cancelar la edición de la fila");
							 } catch (AssignValueException e) {
									parent.getServer().logError(SwingUtilities.getWindowAncestor(parent.getTable()),e,"Error al intentar cancelar la edición de la fila");
								}
						 }
						*/
						 if(!it.isNullRow()){
							 replaceColumn(columnIndex, idSource, /*oldV*/oldVal, it.isPermanent());
							 it.setColumnIdo(columnIndex, it.getColumnOldIdo(columnIndex));
							 it.setColumnIdto(columnIndex, it.getColumnOldIdto(columnIndex));
						 }
						 lastSetValueSuccess=false;
						// parent.getServer().logError(SwingUtilities.getWindowAncestor(parent.getTable()),ex,"Error al asignar valor");
					 
					}finally{
						 if(it.isNullRow()){
							// try{
								 //it.setState(RowItem.CREATION_STATE);//Por defecto el estado de una nullRow es creacion, luego TextCellEditor le pone finder si es necesario
								 if(removeNullRow()){
									 addNullRow();
								 }
							// } catch (AssignValueException exc) {
								// parent.getServer().logError(SwingUtilities.getWindowAncestor(parent.getTable()),exc,"Error al intentar restaurar la fila de introducción de datos");
							// }
						 }else it.setState(RowItem.IDLE_STATE);
						 executingSetValue=false;
					 }
				}
			 }
			
		}

		public boolean isColumnSortable(int column) {
			// Note: For TinyTableModel this works fine. You may
			// take another approach depending on your kind of data.
			if(m_rowData.isEmpty()) return false;
			
			Object value=null;
			int i=-1;
			//Buscamos el primer que no sea null ya que si no no podemos saber si los datos de esa columna son Comparable
			while(value==null && i<getRowCount()-1){
				i++;
				value=getValueAt(i, column);
			}
			return (value instanceof Comparable);
		}

		public boolean supportsMultiColumnSort() {
			// We support multi column sort
			return true;
		}

		//TODO m_visibleRowMap no es actualizado con el nuevo orden. Es posible que haya que actualizarlo ya que se usa en el metodo delRow
		@SuppressWarnings("unchecked")
		public void sortColumns(final int[] columns, final int[] sortingDirections, JTable table) {
			
			//Si la primera fila es una fila permanente(favorito) la quitamos para que no moleste al usuario ya que se queda siempre la primera
			if(!m_rowData.isEmpty() && m_rowData.get(0).isPermanent()){
				
					removeRow(0,true);
				
			}
			
			int[] sr = table.getSelectedRows();
			int[] sc = table.getSelectedColumns();
			int rowIndex = 0;

			Iterator ii = m_rowData.iterator();
			while(ii.hasNext()) {
				RowItem ri=(RowItem)ii.next();
				ri.setIndexOld(ri.getIndex());
			}

			// The sorting part...
			if(columns.length == 0) {
				// The natural order of our data depends on first (Integer) column
				Collections.sort(m_rowData, new Comparator<RowItem>() {
					public int compare(RowItem r1, RowItem r2) {
						// For our data we know that arguments are non-null and are of type Record.					
						Comparable val1 = (Comparable)r1.getColumnData(0);
						Comparable val2 = (Comparable)r2.getColumnData(0);
						if(val1 instanceof String)
							return Constants.languageCollator.compare(val1, val2);
						else return val1.compareTo(val2);
					}
				});
			}else {
				final ArrayList<Boolean> stringColumnWithNumber=new ArrayList<Boolean>();
				//Miramos si siendo esa columna String, todos sus valores son Integer, para así sabe si ordenar esa columna numericamente en vez de alfabeticamente
				for(int i = 0; i < columns.length; i++) {
					Iterator<RowItem> itr=m_rowData.iterator();
					boolean isNumericOfString=true;
					while(itr.hasNext() && isNumericOfString){
						RowItem rowItem=itr.next();
						
						Comparable value=(Comparable)rowItem.getColumnData(columns[i]);
						if(value!=null){
							if(value instanceof String){
								isNumericOfString=Auxiliar.hasDoubleValue((String)value);
							}else{
								isNumericOfString=false;
							}
						}
					}
					stringColumnWithNumber.add(isNumericOfString);
				}
				
				//Creamos el mapa de valores en caso de columnas enumerados ya que tenemos que comparar luego con el label y no con el ido
				final HashMap<Integer,HashMap<Integer,String>> mapValuesPossible=new HashMap<Integer, HashMap<Integer,String>>();
				for(int i = 0; i < columns.length; i++) {
					DynamicTableColumn columna = ((GFormTable) parent.m_objFormField).getColumn(columns[i]);
					if(columna.getValuesPossible()!=null){
						//Para el caso de que sea un enumerado, ya que tenemos que comparar con el label y no con el ido. Machacamos los idos por los label. Esto pasa en las tablas de los formularios, no en las tablas de busquedas.
						Iterator<GValue> itrId = columna.getValuesPossible().iterator();
						HashMap<Integer,String> mapValues=new HashMap<Integer, String>();
						while (itrId.hasNext()) {
							GValue parValue = itrId.next();
							mapValues.put(parValue.getId(), parValue.getLabel());
						}
						mapValuesPossible.put(i,mapValues);
					}
				}
				
				// Multi column sort
				Collections.sort(m_rowData, new Comparator<RowItem>() {
					public int compare(RowItem r1, RowItem r2) {
						// For our data we know that arguments are non-null and are of type Record.
						if(r1.isNullRow())
							return 1;
						else if(r2.isNullRow())
							return -1;
						else if(r1.isPermanent() && !r2.isPermanent())
							return -1;
						else if(r2.isPermanent() && !r1.isPermanent())
							return 1;
						else{
							for(int i = 0; i < columns.length; i++) {
								Comparable val1 = (Comparable)r1.getColumnData(columns[i]);
								Comparable val2 = (Comparable)r2.getColumnData(columns[i]);
								
								int result=0;
								if(val1==null){
									if(val2!=null)
										result=-1;
								}else if(val2==null){
									result=1;
								}else if(val1 instanceof String){
									if(stringColumnWithNumber.get(i)){//Miramos si tenemos que ordenar esta columna numerica o alfabeticamente
										result = Double.valueOf((String)val1).compareTo(Double.valueOf((String)val2));
									}else{
										result = Constants.languageCollator.compare(val1, val2);
									}
								}else{
									
									if(mapValuesPossible.containsKey(i)){
										//Para el caso de que sea un enumerado, ya que tenemos que comparar con el label y no con el ido. Machacamos los idos por los label. Esto pasa en las tablas de los formularios, no en las tablas de busquedas.
										if(mapValuesPossible.get(i).containsKey(val1)){
											val1=mapValuesPossible.get(i).get(val1);
										}
										if(mapValuesPossible.get(i).containsKey(val2)){
											val2=mapValuesPossible.get(i).get(val2);
										}
									}
									
									result = val1.compareTo(val2);
								}
		
								if(result != 0) {
									if(sortingDirections[i] == 1)
										return -result;							
									return result;
								}
							}
							return 0;
						}
					}
				});
			}
			// Tell our listeners that data has changed
			fireTableDataChanged();

			// Restore selection
			rowIndex = 0;

			ii = m_rowData.iterator();
			while(ii.hasNext()) {
				RowItem ri=(RowItem)ii.next();
				ri.setIndex(rowIndex++);
			}
			ArrayList temp = (ArrayList)m_rowData.clone();
			Collections.sort(temp, new Comparator<RowItem>() {
				public int compare(RowItem r1, RowItem r2) {
					if(r1.getIndexOld() > r2.getIndexOld()) 
						return 1;
					return -1;
				}
			});
			// Adding one row selection interval after another is probably inefficient.
			for(int i = 0; i < sr.length; i++) {
				int row=((RowItem)temp.get(sr[i])).getIndex();
				table.addRowSelectionInterval(row, row);
			}
			for(int i = 0; i < sc.length; i++) {
				table.addColumnSelectionInterval(sc[i], sc[i]);
			}
		}

		public List<GTableRow> getListaFilas() {
			return listaFilas;
		}

		public ArrayList<RowItem> getRowData() {
			return m_rowData;
		}
		
		public boolean isEditing(){
			return idoRowEditing!=null;
		}

		public Integer getIdoRowEditing() {
			return idoRowEditing;
		}

		public void setIdoRowEditing(Integer idoRowEditing) {
			this.idoRowEditing = idoRowEditing;
			if(idoRowEditing==null){
				rowCreating=false;
				listIdosEditing=new HashSet<Integer>();
			}
		}
		
		public boolean isCreating(){
			return rowCreating;
		}

		public HashSet<Integer> getListIdosEditing() {
			return listIdosEditing;
		}

		public void setListIdosEditing(HashSet<Integer> listIdosEditing) {
			this.listIdosEditing = listIdosEditing;
		}
		
		public boolean isNewIdo(Integer ido){
			boolean newIdo=true;
		//	if(ido!=null && m_controlListener!=null)
			//	newIdo=this.m_controlListener.isNewCreation(ido);
			return newIdo;
		}

		public boolean isDirectEdition() {
			return directEdition;
		}

		public void setDirectEdition(boolean directEdition) {
			this.directEdition = directEdition;
		}
		
		//Indica los posibles tipos de ido que podemos tener a partir de un valor de una columna
		public LinkedHashMap<String,Integer> getPossibleTypeForValue(String idParent,Object value,Integer valueCls){
		//	if(m_controlListener!=null)
			//	return m_controlListener.getPossibleTypeForValue(idParent, value, valueCls);
			return new LinkedHashMap<String, Integer>();
		}
		public String getFieldIDFromColumn(int column) {
			return (String) Map_Column_IDForm.get(column);
		}
		//Comprueba si esa celda admite nulos dependiendo del individuo que tiene.
		//En el caso de no tener individuo asociado se consulta el generico de la columna
		public boolean isNullable(int row,int col){

			RowItem rowItem = (RowItem) getRowData().get(row);
			Boolean nullable=null;
			/*if(m_controlListener!=null){
				String idColumn=getFieldIDFromColumn(col);
				IdObjectForm idObjForm=new IdObjectForm(idColumn);
				Integer ido=rowItem.getColumnIdo(col);
				if(ido!=null || idObjForm.getIdParent()!=null){
					Integer idoParent=null;
					String idParent=rowItem.getColumnIdParent(col);
					if(idParent!=null){
						idObjForm=new IdObjectForm(idParent);
						idoParent=idObjForm.getIdo();
					}
					nullable=m_controlListener.isNullableForRow(idoParent,ido, idColumn);//Sera null si ido=null y el idoParent es un filtro
				}
			}*/
			if(nullable==null){
				CellEditor cell=(CellEditor)parent.getTable().getCellEditor(row,col);
				nullable= cell.getColumn().isNullable();
			}
			
			return nullable;
		}
		
		public void copyInPermanentRow(int row){
			RowItem ritem=getRowItemFromIndex(row);
			
			RowItem permanentRitem=buildRowItem(0/*Queremos que salga el primero*/, ritem.columnData, ritem.columnIdo, ritem.columnOldIdo, ritem.columnIdto, ritem.columnOldIdto, ritem.columnIdParent, ritem.columnIdoFilter, ritem.columnIdtoFilter, Color.black, ritem.getIdRow(), false, true);
			setRowItem(permanentRitem,false);
			//if (!m_cuantitativo)
			//	fireTableRowsInserted(0, 0);//Esto ya se hace en subAddRow
			parent.getTable().removeEditor();//Quitamos el editor ya que si no se queda el editor sobre el boton y no se actualiza su imagen
		}
		
		public Integer getColumnSelectionRowTable() {
			return columnSelectionRowTable;
		}

		public boolean isLastSetValueSuccess() {
			return lastSetValueSuccess;
		}

		public boolean isExecutingSetValue() {
			return executingSetValue;
		}
		public int getFieldColumn(String field) {
			Integer col = (Integer) Map_IDForm_Column.get(field);
			if (col == null)
				return -1;
			return col.intValue();
		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			// TODO Auto-generated method stub
			
		}

	}


	class DefaultTableFieldModel /* implements field */{
		int m_tm;

		String m_idform;

		boolean m_nullable = false;

		String m_label;

		int m_tapos = 0;

		public DefaultTableFieldModel(int tm, String idForm, String label, int tapos, boolean nullable) {
			m_tm = tm;
			m_idform = idForm;
			m_nullable = nullable;
			m_label = label;
			m_tapos = tapos;
		}

		public int getSintax() {
			return m_tm;
		}

		public void setValue(Object value) {
			;
		}

		public Object getValue() {
			return null;
		}

		public int getIntValue() {
			return 0;
		}

		public float getFloatValue() {
			return 0;
		}

		public String getIdForm() {
			return m_idform;
		}

		public int getTAPOS() {
			return m_tapos;
		}

		public String getValueToString() {
			return null;
		}

//		public org.jdom.Element getAva() {
//		return null;
//		}

		public boolean isNull() {
			return false;
		}

		public boolean isNull(Object val) {
			return false;
		}

		public boolean isNullable() {
			return m_nullable;
		}

		public void resetRestriction() {
		}

		public void reset() {
		}

		public boolean hasChanged() {
			return false;
		}

		public String getLabel() {
			return m_label;
		}

		public void commitValorInicial() {
		}

		public void inizialiceRestriction() {
		}
}