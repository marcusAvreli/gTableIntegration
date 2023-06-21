package gtableIntegration.cell;


import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtableIntegration.DynamicTableColumn;
import gtableIntegration.GTable;

///https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gawt/tableCellEditor/CellEditor.java
public abstract class CellEditor extends AbstractCellEditor implements TableCellEditor{
	private static final Logger logger = LoggerFactory.getLogger(CellEditor.class);
	protected GTable gTable;
	protected DynamicTableColumn column;
	protected Integer lastSelectionCreation;//Mantiene el valor elegido en la ultima creacion
	protected Integer lastSelectionSubCreation;//Mantiene el valor elegido en la ultima subcreacion
	protected boolean creationConfirmation;//Indica si, en una creacion, hay que preguntarle al usuario si lo quiere crear
	private boolean rememberSelectionForCopyRows=false;
	
	public CellEditor(GTable gTable,DynamicTableColumn column){
		this.gTable=gTable;
		this.column=column;
		logger.info("1111111111111");
		//this.creationConfirmation=column!=null?column.hasFinder():false;//Nos interesa preguntar siempre cuando hay finder ya que puede haber ambiguedad para el usuario de si esta creando o buscando
	}
	


	public void cancelCellEditing() {
		//System.err.println("CancellEditing");
		logger.info("1111111111111");
		fireEditingCanceled();
	}

	public boolean shouldSelectCell(EventObject e) {
		logger.info("1111111111111");
		return true;
	}

	public boolean stopCellEditing() {
		logger.info("1111111111111");
		return super.stopCellEditing();
	}
	
	//Devuelve el tipo resuelto automaticamente o preguntando al usuario si es necesario. En el caso de pasarle idParent, se busca que posibilidad
	//de las encontradas con id casan con las posibles posibilidades de idParent. Es decir, se buscan las posibilidades en esa columna para value y valueCls
	//y luego se comprueba si son compatibles con el idParent ya que es donde luego se enganchará. 
	private Integer getTypeForValue(String idParent,String id,Object value,Integer valueCls,Integer defaultPossibility,boolean creationConfirmation){
		logger.info("1111111111111");
		LinkedHashMap<String,Integer> possibleList;
	
		LinkedList<String> sortList = new LinkedList<String>();
		Integer type=null;
		
		String[] possibilities=new String[sortList.size()];
		Iterator<String> itr=sortList.iterator();
		int i=0;
		String defaultPossibilityString=null;
		while(itr.hasNext()){
			possibilities[i]=itr.next();
			if(i==0)
				defaultPossibilityString=possibilities[i];
		
			i++;
		}
		String res=showConfirmation(possibilities,defaultPossibilityString,creationConfirmation);
		if(res!=null){
			//type=possibleList.get(res);
		}
		
		return type;
	}
	
	//Muestra un mensaje de confirmacion o de seleccion de posibilidades, en el caso de que sea necesario. Devuelve el tipo elegido por el usuario o el de por defecto.
	public String showConfirmation(String[] possibilities,String defaultPossibility,boolean creationConfirmation){
		logger.info("1111111111111");
		String messageConfirmation="";
		String res=defaultPossibility;
		
		if(creationConfirmation)
			messageConfirmation="Se va a proceder a la creación del objeto utilizando el valor '"+this.getCellEditorValue()+"' ya que no existe en base de datos.\n";
		
		return res;
	}
	
	public abstract void cancelChangeValue();

	public Integer getLastSelectionCreation() {
		logger.info("1111111111111");
		return lastSelectionCreation;
	}

	public void setLastSelectionCreation(Integer lastSelectionCreation) {
		logger.info("1111111111111");
		this.lastSelectionCreation = lastSelectionCreation;
	}

	public Integer getLastSelectionSubCreation() {
		logger.info("1111111111111");
		return lastSelectionSubCreation;
	}

	public void setLastSelectionSubCreation(Integer lastSelectionSubCreation) {
		logger.info("1111111111111");
		this.lastSelectionSubCreation = lastSelectionSubCreation;
	}


	public DynamicTableColumn getColumn() {
		return column;
	}
	
	
	public abstract void setValue(Object value);

	public void setRememberSelectionForCopyRows(boolean rememberSelectionForCopyRows) {
		this.rememberSelectionForCopyRows = rememberSelectionForCopyRows;
	}
}