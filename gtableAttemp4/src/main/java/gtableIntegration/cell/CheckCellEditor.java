package gtableIntegration.cell;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtableIntegration.GTable;

public class CheckCellEditor extends CellEditor{
	private static final Logger logger = LoggerFactory.getLogger(CheckCellEditor.class);
	private static final long serialVersionUID = 1L;
	protected Boolean value;
	protected JCheckBox editorComponent;
	protected Color colorDefaultComponent;
	protected boolean mustStop = true;
	protected Boolean oldValue;
	protected FocusListener listener;
	
	public CheckCellEditor(TableColumn column,GTable tf, final FocusListener listener) {
		super(tf,column);
		logger.info("build check cell editor");
		this.listener=listener;
		create();
		configure();
	}
	
	protected void create(){
		logger.info("create editor");
		editorComponent = new JCheckBox();
		value=false;
	}
	
	protected void configure(){
		logger.info("configure check editor");
		editorComponent.setHorizontalAlignment(JLabel.CENTER);
		editorComponent.setVerticalAlignment(JLabel.CENTER);
		//editor.setBorder(BorderFactory.createEmptyBorder());
	//	editorComponent.setBorder(GConfigView.borderSelected);
		editorComponent.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{
					nextState();
					logger.info("=============action==================");
					editorComponent.setSelected(value);	
					//gTable.getModel().setValueAt(value,row,column);
					fireEditingStopped();
					stopCellEditing();
				}catch(Exception ex){
					ex.printStackTrace();
					//gTable.getServer().logError(SwingUtilities.getWindowAncestor(gTable),ex,"Error al asignar valor");
				}
			} 
		});
			
		editorComponent.addFocusListener(new FocusListener(){
			
			public void focusGained(FocusEvent ev){
				logger.info("1111111111111");
				mustStop=true;
			}

			public void focusLost(FocusEvent ev){
				logger.info("focus_lost");
				/*try{
					if(mustStop && !ev.isTemporary()){					
						if(gTable.getTable().isEditing())
							stopCellEditing();
						listener.focusLost(ev);
					}
				}catch(Exception ex){
					ex.printStackTrace();
				
				}*/
			}			
		});
		colorDefaultComponent=editorComponent.getBackground();
	}
	
	public Component getTableCellEditorComponent(JTable table, Object val, boolean isSelected, int row, int column) {
		System.err.println("Valueeee editor para row:"+row+" y col:"+column+" value:"+val+" selected:"+isSelected);
		logger.info("1111111111111");
		mustStop=true;
		boolean bolVal=false;
		//logger.info("getTableCellEditorComponent check editor");
		if( val instanceof Boolean ){
			bolVal=(Boolean)val;
		}
		
		else if(val instanceof String){
			String[] buf=((String)val).split(":");
			bolVal=(buf[0].equals("null")?false:new Boolean(buf[0]));
			//TODO Hay que pensar que hacer cuando es un boolean con comentario
			//if(buf.length>1)
			//	commentVal=(buf[1].equals("null")?null:buf[1]);
		}
		oldValue=value;
		editorComponent.setSelected( bolVal );
		value=bolVal;
		
	
		
		return editorComponent;
	}	
    
	protected void nextState() {
		logger.info("1111111111111");
		//logger.info("nextState check editor");
		if (value == true) {
			value=false;
		} else if (value==false) {
			value=true;
		}
	}
	
    public Object getCellEditorValue(){
    	logger.info("1111111111111");
    	logger.info("getCellEditorValue check editor");
    	return value;
    }
    
    public boolean stopCellEditing() {
    	logger.info("1111111111111");
        return super.stopCellEditing();        
    }

	@Override
	public void cancelChangeValue() {
		logger.info("1111111111111");
		editorComponent.setSelected( oldValue );
		value=oldValue;
	}

	@Override
	public void setValue(Object value) {
		logger.info("set selected:"+value);
		editorComponent.setSelected( (Boolean)value );
		this.value=(Boolean)value;
	}     
}