package gtableIntegration;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtableIntegration.cell.CheckCellEditor;
import gtableIntegration.cell.CheckCellRenderer;
import gtableIntegration.cell.CheckHeaderRenderer;



//https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gawt/GTable.java
public class GTable<T> extends GComponent  implements ActionListener, MouseListener {
	private static final Logger logger = LoggerFactory.getLogger(GTable.class);
	//final Component c=this;
	private FocusListener focusListener;

	private static final long serialVersionUID = 1L;
	private MagTable<T> m_objTable;
	protected DynamicTableModel m_objTableModel;	
	boolean m_modoFilter;	
	//private  Vector<Vector<Object>> customApplications;
	private Set focusListenerExt;
	private boolean manageFocus;

	public GTable(MagTable<T> dynamicTable) {
		this.m_objTable = dynamicTable;
		m_objTableModel=(DynamicTableModel) m_objTable.getModel();
		manageFocus = false;
		m_modoFilter = false;
		focusListenerExt=new HashSet<FocusListener>();
		focusListener = new FocusListener(){
			public void focusGained(FocusEvent ev){
				//System.err.println("FocusGained Table "+m_label+" component:"+(ev.getComponent()!=null?ev.getComponent().getClass()+" "+ev.getComponent().hashCode():null)+" opposite:"+(ev.getOppositeComponent()!=null?ev.getOppositeComponent().getClass()+" "+ev.getOppositeComponent().hashCode():null)+" isTemporary:"+ev.isTemporary());
logger.info("focus_gained");
				// m_objTable.setFocusTraversalPolicyProvider(false);
//				if(m_objTable.getRowCount()>0 && ev.getOppositeComponent()!=null && ev.getOppositeComponent().getParent()!=null/*Esto ocurrira cuando lo gane desde un cellEditor*/ )
				//	m_objTable.setFocusTraversalKeysEnabled(false);
				//	m_objTable.setFocusCycleRoot(true);//Si tiene filas queremos que tenga su propio cambio de foco, gestionado por los actionPerformed
				notifyFocusListener(ev, false);
				if(!ev.isTemporary() && m_objTable.getRowCount()>0 /*&& !isTopLabel()*/ && m_objTable.getSelectedRowCount()==0){
					//System.err.println("Entra en seleccion");
					int row=1;
					int column=0;
				//	
					//	if(getRowCount()>0)//getRowCount descarta las nullRow
							//m_objTable.setRowSelectionInterval(row, row);
					
				}
				
				
				//if(m_objTable.isEditing())
				//	m_objTable.getEditorComponent().requestFocusInWindow();
				
				//if(!thisFinal.isAncestorOf(ev.getOppositeComponent())/* && (old!=null && old.equals(window))*/){
				if(manageFocus){
					
				}

				/*if(m_controlListener!=null && !isEditing){
					m_controlListener.startEditionTable(getId());
					isEditing=true;
				}*/
					
			}
			public void focusLost(FocusEvent ev){
				//System.err.println("FocusLost Table "+m_label+" component:"+(ev.getComponent()!=null?ev.getComponent().getClass()+" "+ev.getComponent().hashCode():null)+" opposite:"+(ev.getOppositeComponent()!=null?ev.getOppositeComponent().getClass()+" "+ev.getOppositeComponent().hashCode():null)+" isTemporary:"+ev.isTemporary());
				if(/*!ev.isTemporary() &&*/ ev.getOppositeComponent()!=null && ev.getSource()!=null){
					//System.err.println("FocusLost gain "+ev.getOppositeComponent().getClass()+" lost "+ev.getComponent().getClass());
					//m_objTableModel.focusLost(ev);
					notifyFocusListener(ev, true);
					
					Window old = SwingUtilities.getWindowAncestor(ev.getOppositeComponent());
					if(manageFocus /* && (old!=null && old.equals(window))*/){
						 /*if(old!=null && old.equals(window) && !ev.isTemporary()){
							//System.err.println("clearSelection "+m_objTable.hashCode());
							m_objTable.removeEditor();
							m_objTable.clearSelection();
						 }*/
						/*if(!m_modoConsulta && !m_modoFilter){
							if(m_objComponent instanceof JScrollPane)
								((JScrollPane)m_objComponent).setBorder(UIManager.getBorder("ScrollPane.border"));
							else m_objComponent.setBorder(UIManager.getBorder("Table.border"));
						}*/
//						if(m_controlListener!=null && rowEditing!=-1){
//							if(!m_controlListener.stopEditionTable(getId(),getDataFromIndex(rowEditing).getIDO()))
//								m_objTable.requestFocusInWindow();
//							else rowEditing=-1;
//						}
					}
					else{
					
					}
				}
				
			}
		};
	//	m_objTable.addFocusListener(focusListener);
	//}
		buildRenders();
	}
	public MagTable getTable() {
		return m_objTable;
	}
	public DynamicTableModel getModel() {
		return m_objTableModel;
	}
	private void buildRenders() {
		logger.info("start_build_render");
		TableColumnModel tcm = m_objTable.getColumnModel();

		int col = 0;

		//MultiLineHeaderRenderer renderer = new MultiLineHeaderRenderer();

		for (int pos = 0; pos < tcm.getColumnCount(); pos++) {
			TableColumn tc = tcm.getColumn(pos);
			//logger.info("Pos "+pos+" hideHeader "+hideHeader+" tfm.dobleHeaderSize "+tfm.dobleHeaderSize+" tfm.m_cuantitativo "+tfm.m_cuantitativo);
			logger.info("hello");
							
			
			
	
		
			
			if (pos == 2 && m_objTableModel.m_cuantitativo) {
				logger.info("m_modoFilter:"+m_modoFilter);
				tc.setCellRenderer(new CheckCellRenderer(tc, this, m_modoFilter));
				tc.setCellEditor(new CheckCellEditor(tc, this, focusListener));
				tc.setHeaderRenderer(new CheckHeaderRenderer(tc, this));
				tc.setHeaderValue(false);
				tc.setWidth(23);
				tc.setMaxWidth(23);
				continue;
			}
		
		}
		
		logger.info("finish_build_render");
		
		
	}	
		
	@Override
	public synchronized void addFocusListener(FocusListener e) {
		focusListenerExt.add(e);
	}
	
	public void notifyFocusListener(FocusEvent e,boolean lost){
		logger.info("1111111111111");
		Iterator<FocusListener> itr=focusListenerExt.iterator();
		while(itr.hasNext()){
			if(lost)
				itr.next().focusLost(e);
			else itr.next().focusGained(e);
		}
	}
	public int getRowCount(){
		logger.info("1111111111111");
		DynamicTableModel tfm = (DynamicTableModel) m_objTable.getModel();
		int size=0;
		size = tfm.getRowCount();
		
		return size;
	}
	public void editFirstCellEditable(int row,int column,boolean searchNextRow,boolean preferRequiredEmpty){
		int rowAux=row;
		int columnAux=column;
		logger.info("2222222222222222");
		int firstColumnEmptyNotRequired=-1;
	//	processingSelectCellFromSystem=true;
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		try{
			System.err.println("MOUSE EXIT");
			//m_menuBotonera.setVisible(false);
			//clearSeleccion();
		}catch(Exception ex){
			ex.printStackTrace();
			//m_server.logError(SwingUtilities.getWindowAncestor(m_objTable),ex,"Error al intentar ocultar la botonera");
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		logger.info("2222222222222222");
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		logger.info("2222222222222222");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		logger.info("2222222222222222");
	}
	
	public void selectAll(boolean select) {
	
			
		if(!select){
			m_objTable.clearSelection();
		}else{
			m_objTable.selectAll();
		}
	}

}