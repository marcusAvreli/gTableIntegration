package gtableIntegration.cell;


import java.awt.Component;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtableIntegration.swing.table.DynamicTableColumn;
import gtableIntegration.swing.table.DynamicTableModel;
import gtableIntegration.swing.table.GTable;


public class CheckCellRenderer extends JCheckBox implements TableCellRenderer{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(CheckCellRenderer.class);
	private DynamicTableColumn column;
	private GTable gTable;
	private boolean modeFilter;
	
	public CheckCellRenderer(DynamicTableColumn column, GTable gTable, boolean filter) {
		super();
		this.column=column;
		this.gTable=gTable;
		this.modeFilter=false;
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
	}
	
	public Component getTableCellRendererComponent(JTable table, Object val, boolean isSelected, boolean hasFocus, int row, int col) {
		if(isSelected) {
			//logger.info("here:"+val+" row:"+row + " isSelected:"+isSelected+" hasFocus:"+hasFocus+" col:"+col);
		}
		
		//logger.info("Valueeee renderer para row:"+row+" y col:"+col+" value:"+val+" selected:"+isSelected);
		super.setHorizontalAlignment(SwingConstants.CENTER);
		super.setVerticalAlignment(JLabel.CENTER);
		super.setMargin(new Insets(0,0,0,0));
		//super.setIconTextGap(0);
		
		DynamicTableModel tfm = (DynamicTableModel) table.getModel();
		/*if(tfm.getRowData().size()>row){
			RowItem rowItem = (RowItem) tfm.getRowData().get(row);		
			
		}*/
	
		
		
		super.setSelected(isSelected);
		return this;
	}

}
