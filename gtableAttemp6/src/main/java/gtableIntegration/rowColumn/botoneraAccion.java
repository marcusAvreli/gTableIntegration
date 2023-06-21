package gtableIntegration.rowColumn;


import gtableIntegration.gen.GConst;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;


//https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gawt/utils/botoneraAccion.java
public class botoneraAccion /*extends scopeContainer*/{
	public final static int EDITAR=1;
	public final static int CREAR=4;
	public final static int ELIMINAR=8;
	public final static int UNLINK=9;
	public final static int BUSCAR=16;
	public final static int ABRIR=32;
	public final static int EJECUTAR=64;
	public final static int CANCEL=128;
	public final static int CLOSE=256;
	public final static int EDITAR_FILTRO=512;
	public final static int APLICAR_FILTRO=513;
	public final static int PREV=1024;
	public final static int NEXT=1025;
	public final static int RESET=2048;
	public final static int LAUNCH=4096;
	public final static int CONSULTAR=8192;
	public final static int RESET_ALL=8193;
	public final static int ASIGNAR=8194;
	public final static int PRINT=8195;
	public final static int ACTION=8196;
	public final static int IMPORT=8197;
	public final static int EXPORT=8198;
	public final static int HELP=8199;
	public final static int PRINT_SEARCH=8200;
	public final static int INCREASE_ZOOM=8202;
	public final static int DECREASE_ZOOM=8203;
	public final static int EMAIL = 8204;
	public final static int CONFIG_COLUMNPROPERTIES = 8205;
	//public final static int MAIN_IMAGE=8204;
	public final static int ROWADD=2;
	public final static int ROWDEL=3;
	public final static int REPORT=5;
	public final static int OPERATION_ACTION=6;
	public final static int OPERATION_SCROLL=7;
	
	public final static int RECORD_TYPE=10;
	public final static int VIEW_TYPE=11;
	public final static int SEARCH_TYPE=12;
	public final static int LINKING_TYPE=13;
	public final static int TABLE_TYPE=14;
	
	
	
	public double buttonWidth;
	public double buttonHeight;
	
	
	ArrayList<AbstractButton> botones=new ArrayList<AbstractButton>();
	int numButtons;
	
	JButton m_botonEditar=null;
	JPanel m_comp;
	boolean lockable=true;
	//docServer server;
	//private communicator m_comm;

	public botoneraAccion(/*communicator com,
		     metaData md,*/
			String id,
			String name,
			/*Object tg,*//*Target tg,*//*Integer id,*/
			/*threadActionMenu acciones,*/
			HashMap<Integer, String> idtoReports,
			HashMap<Integer, String> idtoReportsDirectPrint,
			HashMap<Integer, String> idtoNameActions,
			HashMap<Integer, String> idtoNameCreationActions,
			HashMap<Integer, String> idtoImports,
			HashMap<Integer, String> idtoExports,
			HashMap<Integer, String> idoReportFormats,
			boolean email,
			int formType,
			JPanel botoneraExternaInicio,
			JPanel botoneraExternaFin,
			//ITableNavigation tableNavigation,
			ActionListener list,
			/*access myAccess,*//*OperationsObject operations,*//*HashMap<Integer,ArrayList<UserAccess>> accessUserTasks,*/
//			AccessAdapter accessAdapter,
			boolean modoConsultar,
			boolean endStep,
			Graphics graphics,
			
			JComponent componentParentShortCut/*Si es null utiliza toda la ventana para ejecutar el shortcut de los botones. Si tiene valor solo funcionaran cuando ese componente o uno hijo, tengan el foco*/,
			boolean allowChangeColumnProperties){
		
	
		buttonHeight= 10;
		buttonWidth=buttonHeight;
		
		build(	
			
				id,
				name,
				idtoReports,
				idtoReportsDirectPrint,
				idtoNameActions,
				idtoNameCreationActions,
				idtoImports,
				idtoExports,
				idoReportFormats,
				email,
				/*tg,*//*id,*/
				/*myAccess,*//*operations,*//*accessUserTasks,*/
				//accessAdapter,
				/*acciones,*/
				formType,
				botoneraExternaInicio,
				botoneraExternaFin,
			
				list,
				modoConsultar,
				endStep,
				componentParentShortCut,
				allowChangeColumnProperties);
				
		
	}


	private void build(	/*communicator com,
			metaData md,*/
			String id,
			String name,
			HashMap<Integer, String> idtoReports,
			HashMap<Integer, String> idtoReportsDirectPrint,
			HashMap<Integer, String> idtoNameActions,
			HashMap<Integer, String> idtoNameCreationActions,
			HashMap<Integer, String> idtoImports,
			HashMap<Integer, String> idtoExports,
			HashMap<Integer, String> idoReportFormats,
			boolean email,
			/*Object tg,*//*Target tg,*//*Integer id,*/
		
			/*threadActionMenu acciones,*/
			int formType,
			JPanel botoneraExternaInicio,
			JPanel botoneraExternaFin,
			
			final ActionListener list,
			boolean modoConsultar,
			boolean endStep,
			JComponent componentParentShortCut,
			boolean allowChangeColumnProperties){
		m_comp= new JPanel(new FlowLayout(FlowLayout.CENTER,formType!=TABLE_TYPE?10:0,0));
		/*access myAccess= getAccess();*/
		
		numButtons=0;
		//this.ContainerIsFinder=ContainerIsFinder;


		m_comp.setBorder(new EmptyBorder(0,0,0,0));
		
		

		int whenShortCut=componentParentShortCut==null?JComponent.WHEN_IN_FOCUSED_WINDOW:JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
		
		if(formType!=TABLE_TYPE){
			if( formType==VIEW_TYPE/*!actualizarButton && modoConsultar && !TargetIsTable && popupContainer*/ ){
				
				
			//	GConst.addShortCut(componentParentShortCut, button, GConst.CANCEL_SHORTCUT_KEY, GConst.CANCEL_SHORTCUT_MODIFIERS, "Cancelar", whenShortCut, null);
				
				numButtons++;
			}
			if( (formType==RECORD_TYPE && endStep)|| formType==SEARCH_TYPE || formType==LINKING_TYPE/*actualizarButton && !modoConsultar*/){
			
				String idString="Aceptar";
				
						
				
				if(formType!=RECORD_TYPE){
				//	GConst.addShortCut(componentParentShortCut, button, GConst.QUERY_SHORTCUT_KEY, GConst.QUERY_SHORTCUT_MODIFIERS, "Buscar", whenShortCut, null);
					
				}
				
				
				numButtons++;
			}
			
		
					//botones.addAll(buttons);
					//botones.add(button);
					//numButtons++;

				}
				
				if(idtoExports!=null && !idtoExports.isEmpty()){   
					final ArrayList<JMenuItem> buttons = new ArrayList<JMenuItem>();
					Iterator<Integer> it = idtoExports.keySet().iterator();
					while(it.hasNext()){
						int idto = it.next();
						JMenuItem b = new JMenuItem(idtoExports.get(idto));
						b.setContentAreaFilled(false);
						b.setBorderPainted(false);
						b.addActionListener(list);

						
						
						//b.setActionCommand(idString);

						buttons.add(b);
					}
					
					
					//String idString=idObjectOperation.getIdString();
					String idString="operation";
					/*
					final JButton button=subBuildBoton(getComponent(),Utils.normalizeLabel("Aceptar y Exportar"),
							null,
						idString,
							Utils.normalizeLabel("Guardar y ejecutar exportación"),
							list,0,(int)buttonHeight,lockable );
					*/
					//button.addActionListener(new ActionListener(){

										
					//});
					botones.addAll(buttons);
					//botones.add(button);
					//numButtons++;

				}
			
			
			if( formType==LINKING_TYPE && endStep/*actualizarButton && TargetIsTable && popupContainer && asignationMode*/){
			
				
				numButtons++;
			}
	
			if( formType==SEARCH_TYPE || formType==LINKING_TYPE ){
				/*idOperation.setButtonType(RESET_ALL);
				String idString=idOperation.getIdString();	
				JButton button=subBuildBoton(getComponent(),Utils.normalizeLabel("RESET TODO"),null,/*"ACTION:"+id+":"+ idtoUserTask +":"+RESET_ALLidString, Utils.normalizeLabel("Quitar filtrado y resultados"), list,0,(int)buttonHeight,lockable );
				
				button.setMnemonic(GConst.RESETALL_SHORTCUT_KEY);
				GConst.addShortCut(componentParentShortCut, button, GConst.RESETALL_SHORTCUT_KEY, GConst.RESETALL_SHORTCUT_MODIFIERS, "Reset Todo", whenShortCut, null);
				*/
				//botones.add(button);
				numButtons++;
			}

			
			
			if(formType==VIEW_TYPE || formType==RECORD_TYPE /*popupContainer && ((!TargetIsTable && !modoConsultar)|| (actualizarButton && TargetIsTable))*/ ){
				//idOperation.setButtonType(HELP);
				//String idString=idOperation.getIdString();
				//botones.add(subBuildBoton(getComponent(),null,"help",/*"ACTION:"+id+":"+ idtoUserTask +":"+CANCEL*/idString, Utils.normalizeLabel("Consultar ayuda"), list,(int)buttonWidth,(int)buttonHeight,lockable ));
				
				numButtons++;
			}	
		}

		/*if( botoneraExterna!=null )
			m_comp.add( botoneraExterna );*/
		
	

		
}