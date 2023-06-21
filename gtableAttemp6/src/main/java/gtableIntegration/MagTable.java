package gtableIntegration;

import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class MagTable<T> extends JTable {
	private static final Logger logger = LoggerFactory.getLogger(MagTable.class);
	
	public MagTable(TableModel model) {
		
	    this.setModel(model);
	    this.setRowSelectionAllowed(true);
	    this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

	  //  this.setAutoCreateRowSorter(true); //?
        init();

	}
	public MagTable() {
	    
	    this.setAutoCreateRowSorter(true); //?
       

	}
	 

	  public void init() {
		  //logger.info("magTable init");
		 // setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		  /*
		   ClientSettings clientSettings = ClientSettings.getInstance();
			 applyComponentOrientation(ComponentOrientation
		                .getOrientation(clientSettings.getLocale()));
			 
			 */
		//  getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter2");
		//  getActionMap().put("Enter2", saveAction());
		 // setNewHeaderUI();
	  }
	 public void setNewHeaderUI() {
	        //display:
		// logger.info("magTable setNewHeaderUI");
	        for (int i = 0; i < getTableHeader().getColumnModel().getColumnCount(); i++) {
	         //   getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(new TinyTableHeaderRenderer());
	        }
	        //behaviour:
	       // getTableHeader().setUI(new TinyTableHeaderUI());
	    }
	 
	 private AbstractAction saveAction() {
	        AbstractAction save = new AbstractAction() {

	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	//logger.info("action performed");
	            	int selectedRow = getSelectedRow();
	            	//logger.info("select row:"+selectedRow);
	                /*JOptionPane.showMessageDialog(TestTableKeyBinding.this.table, "Action Triggered.");
	                table.editingCanceled(null);
	                table.editingStopped(null);
	                
	                if (selectedRow != -1) {
	                    ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
	                }*/
	            }
	        };
	        return save;
	    }
	 
		@Override
		protected void processKeyEvent(KeyEvent ev) {
			//Con esto evitamos que entre en los cellEditor estando el foco sobre la tabla en vez de sobre el cellEditor. Si entra en textCellEditor tendriamos problemas con el finder
			//Ademas permitimos los eventos de f2 ya que sirve para entrar en edicion en la fila
			if(ev.getKeyCode()==KeyEvent.VK_DOWN || ev.getKeyCode()==KeyEvent.VK_UP || 
					ev.getKeyCode()==KeyEvent.VK_RIGHT || ev.getKeyCode()==KeyEvent.VK_LEFT ||
					ev.getKeyCode()==KeyEvent.VK_TAB || ev.getKeyCode()==KeyEvent.VK_ALT || ev.getModifiersEx()==KeyEvent.CTRL_DOWN_MASK || ev.getKeyCode()==KeyEvent.VK_CONTROL || ev.getKeyCode()==KeyEvent.VK_ENTER || ev.getKeyCode()==KeyEvent.VK_DELETE ||
					/*ev.getKeyCode()==KeyEvent.VK_ESCAPE ||*/ ev.getKeyCode()==KeyEvent.VK_F2/*Se utiliza para entrar en edicion en la celda*/)
			//if(!Character.isLetterOrDigit(ev.getKeyChar()))
				super.processKeyEvent(ev);
			else if(ev.getKeyCode()==KeyEvent.VK_ESCAPE){
			//	if(this.getCellEditor()==null && GTable.this.getModel().isEditing()){//Si no estamos dentro de un campo pero se esta editando esa fila cancelamos los cambios sobre ella
					//if(ev.getID()==KeyEvent.KEY_PRESSED){//Solo nos interesa que entre una vez por lo que ponemos PRESSED
						//try {
							//m_componentListener.cancelEditionTable(m_id, GTable.this.getModel().getIdoRowEditing());//Cancelamos la edición
						//	GTable.this.getModel().setIdoRowEditing(null);
							//m_objTable.setRowSelectionInterval(this.getRowCount()-1,this.getRowCount()-1);
						/*} catch (EditionTableException e) {
							e.printStackTrace();
							m_server.logError(window, e, "No se ha podido cancelar la edicion de la fila");
						} catch (AssignValueException e) {
							e.printStackTrace();
							m_server.logError(window, e, "No se ha podido cancelar la edicion de la fila");
						}*/
					//}
				//}else super.processKeyEvent(ev);
			}
		}

}
