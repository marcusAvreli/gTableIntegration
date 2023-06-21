package gtableIntegration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
import mySplashThread8.MagTable;
import mySplashThread8.dynagent.common.utils.Auxiliar;
import mySplashThread8.dynagent.common.utils.RowItem;
import mySplashThread8.events.Event;
import mySplashThread8.events.EventAllRowsSelected;
import mySplashThread8.events.EventBroker;
import mySplashThread8.gdev.gawt.GComponent;
import mySplashThread8.gdev.gawt.GTable;
import mySplashThread8.gdev.gawt.GTableModelReduction;
import mySplashThread8.gdev.gawt.tableCellEditor.CheckCellEditor;
import mySplashThread8.gdev.gawt.tableHeaderRenderer.CheckCellRenderer;
import mySplashThread8.gdev.gawt.tableHeaderRenderer.CheckHeaderRenderer;
import mySplashThread8.gen.GConst;
import mySplashThread8.gfld.GFormTable;
import mySplashThread8.gfld.GTableColumn;
import mySplashThread8.gfld.GTableRow;
*/
//https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gawt/GTable.java
public class GTable<T> extends GComponent {
	//final Component c=this;
	private static final Logger logger = LoggerFactory.getLogger(GTable.class);
	private static final long serialVersionUID = 1L;
	private MagTable<T> m_objTable;
	protected DynamicTableModel m_objTableModel;	
	boolean m_modoFilter;	
	//private  Vector<Vector<Object>> customApplications;
	
		
}