package gtableIntegration;

import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MagTable<T> extends JTable {
	private static final Logger logger = LoggerFactory.getLogger(MagTable.class);
	public MagTable(TableModel model) {
		
	    this.setModel(model);
	  //  this.setAutoCreateRowSorter(true); //?
        init();

	}
	public MagTable() {
	    
	    this.setAutoCreateRowSorter(true); //?
       

	}
	  public void init() {
		  //logger.info("magTable init");
		  setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		  /*
		   ClientSettings clientSettings = ClientSettings.getInstance();
			 applyComponentOrientation(ComponentOrientation
		                .getOrientation(clientSettings.getLocale()));
			 
			 */
		//  getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter2");
		//  getActionMap().put("Enter2", saveAction());
		  setNewHeaderUI();
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

}
