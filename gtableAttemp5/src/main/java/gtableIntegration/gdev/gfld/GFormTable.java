package gtableIntegration.gdev.gfld;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtableIntegration.DynamicTableColumn;
import gtableIntegration.gen.GConfigView;
import gtableIntegration.gen.GConst;

import gtableIntegration.rowColumn.GTableRow;

//https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gfld/GFormTable.java
public class GFormTable extends GFormField
{
	private static final Logger logger = LoggerFactory.getLogger(GFormTable.class);
    /** Número de Filas visibles*/
    protected int m_iVisibleRow;
    /** Nos indica si varios registros de la tabla son resumibles en uno. */
    /** It tells us if several records in the table can be summarized in one */
    protected boolean m_cuantitativo;
    /** */
    protected int m_atGroupColumn;
    /** */
    protected int m_iniVirtualColumn;
    /** Este atributo es el número de lineas que tiene que ocupar la cabecera.*/
    protected int m_headerLine;
    /** Nos indica si la cabecera tiene que mostrarse o no*/
    protected boolean m_hideHeader;
//    /** Es el número de botones que tiene la tabla.*/
//    protected int m_numBotones;
    /** Ancho mínimo de la tabla.*/
    protected int m_widthMin;
    /** Ancho maximo de la tabla.*/
    protected int m_widthMax;
    /** Accesos que tiene el usuario sobre esta tabla (permisos) sabiendo tambien su usertask.*/
   // protected AccessAdapter m_formAccess;
    /** Este vector contiene todas las columnas de la tabla. 
     * Los elementos de este vector son del tipo {@link GTableColumn}*/
    protected List<DynamicTableColumn> m_vColumn = new ArrayList<DynamicTableColumn>();
    protected boolean creationRow;
    protected boolean finderRow;
    /** Este vector contiene todas las filas iniciales de la tabla.
     * This vector contains all the initial rows of the table
     * The elements of this vector are of the type {@link GTableRow}
     * Los elementos de este vector son del tipo {@link GTableRow}*/
    
    protected List<GTableRow> m_vRow = new ArrayList<GTableRow>();
    
    /**
     * Constructor por defecto de una tabla
     *
     */
    public GFormTable()
    {
        super();
        m_bTopLabel = true;
    }
    /**
     * Este método devuelve el código asociado al tipo Tabla.
     * El código será el que se encuentre definido en la clase GConst ({@link GConst#TM_TABLE}).
     * @return int - El código asociado.
     */
    public int getType()
    {
        return GConst.TM_TABLE;
    }
    
    //https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gfld/GFormGroup.java
    public  GFormTable createTable(List<DynamicTableColumn> vColumns,List<GTableRow> vRows) {
    	
    	GFormTable table = new GFormTable();
     
        Iterator en = vColumns.iterator();
        while(en.hasNext())
        {
        	DynamicTableColumn col = (DynamicTableColumn)en.next();
            table.addColumn(col);
        }
        Iterator enRows = vRows.iterator();
        while(enRows.hasNext())
        {
        	
            GTableRow row = (GTableRow)enRows.next();
           
            table.addRow(row);
        }
        return table;
    }
    /**
     * Añade una fila a la tabla.
     * @param row La fila a añadir
     */
    public void addRow(GTableRow row)
    {
        m_vRow.add(row);
    }
    /**
     * Obtiene la lista de filas de la tabla.
     * @return Vector - Devuelve las filas que contiene la tabla.
     */
    public List<GTableRow> getRowList()
    {
        return m_vRow;
    }
    /**
     * Obtiene el número de filas de la tabla.
     * @return int - Devuelve el número de filas de la tabla.
     */
    public int getVisibleRowCount()
    {
        return m_iVisibleRow;
    }
    /**
     * Este método modifica el número de filas visibles.
     * @param nRowCount Nuevo número de filas.
     */
    public void setVisibleRowCount(int nRowCount)
    {
        m_iVisibleRow = nRowCount;
    }
    /**
     * Añade una columna a la tabla.
     * @param col La columna a añadir
     */
    public void addColumn(DynamicTableColumn col)
    {
        m_vColumn.add(col);
    }
    /**
     * Obtiene la lista de columnas de la tabla.
     * @return Vector - Devuelve las columnas que contiene la tabla.
     */
    public List getColumnList()
    {
        return m_vColumn;
    }
    /**
     * Obtiene el número de columnas que tiene la tabla.
     * @return int - Número de columnas de la tabla.
     */
    public int getColumnCount()
    {
        return m_vColumn.size();
    }
    /**
     * Obtiene una determinada columna indexada por el parámetro 'col' (empieza en 0).
     * @param col Índice de la columna a obtener.
     * @return GTableColumn - Columna cuyo índice coincide con 'col'
     */
    public DynamicTableColumn getColumn(int col)
    {
    	logger.info("get_column:"+col);
    	
        if(col<0||col>=m_vColumn.size()) {
        	
        logger.info("returning_null:"+m_vColumn);
            return null;
        }
        return (DynamicTableColumn)m_vColumn.get(col);
    }
    /**
     * Este método devuelve la mínima dimensión del componente
     * @return Dimension - El área mínima del componente obtenida.
     */
    public Dimension getMinimumComponentDimension()
    {
        //This function does not consider all the possibilities, for the time being minimum features
        //are taken care of. Please see old viewBalancer.getTableDim() method
      //  Dimension dimLabel= m_objViewBalancer.getDimString(m_strLabel, true, false);
    	Dimension dimLabel= new Dimension(100,100);
        Dimension dimCompSecundario=getDimComponenteSecundario();
        /*int anchoBotonera= getNumBotones()*20;*/
        // we are going to measure table total widht as column widht sum
        int widthTotal=0;//no incluye width etiqueta
        boolean hasImage=false;
        //if(m_widthMin==-1){
        	int maxHeaderColWid=0;
	        Iterator<DynamicTableColumn> en = m_vColumn.iterator();
	        while( en.hasNext() )
	        {
	        	DynamicTableColumn col=(DynamicTableColumn)en.next();
	                if(!col.isHide()){
		                /*int colWid = getWidthMin(col.getType(),-1,m_objViewBalancer.getAveCharWidth(),false,true);*/
		              //  int colWid = getWidthMin(col.getType(),col.getLength(),m_objViewBalancer.getAveCharWidth(isHighlighted()),false,true);
	                	int colWid = 20;
		               // int headerColWid=m_objViewBalancer.getDimString(col.getLabel(), false, false).width+6/*Margenes*/;
	                	int headerColWid=20;
		                widthTotal+=Math.max(headerColWid,colWid);
		                maxHeaderColWid=Math.max(maxHeaderColWid, headerColWid);
	        		}
	                
	                if(col.getType()==GConst.TM_IMAGE && !col.getId().equalsIgnoreCase("type")/*Las columnas del tipo no nos interesa que afecten en el alto de la fila*/)
	                	hasImage=true;
	        }
	        
	        //Ya que el espacio se distribuye equitativamente por columnas, si la cabecera maxima no se ve completa se aumenta el ancho
	        if(m_vColumn.size()>0){
	        	if(widthTotal/m_vColumn.size()<maxHeaderColWid)
		        	widthTotal=maxHeaderColWid*m_vColumn.size();
	        }
	        
        /*}else*/ widthTotal=Math.max(m_widthMin, widthTotal);

        //System.outprintln("widthTotal GTable:"+widthTotal);
        // if rows > 1, tbale viewPOrt will have a scrallBar. This will increasing total width
        if(m_iVisibleRow>1 )
           // widthTotal += GConfigView.widthScrollBar;
        	 widthTotal += 15;
            
        if(m_bTopLabel){
	        if(m_widthMax!=-1 && m_widthMax<widthTotal)
	    		widthTotal=m_widthMax;
        }else{
        	if(m_widthMax!=-1 && m_widthMax<widthTotal+dimLabel.width+dimCompSecundario.width)
	    		widthTotal=m_widthMax-dimLabel.width-dimCompSecundario.width;
        }
        
        int filaCabecera=m_hideHeader?0:1;
        
        int height;
        /*
        if(hasImage && !m_objViewBalancer.getFilterMode()){
        	Si tiene imagen y no estamos en modo filtro hacemos mas grande la tabla ya que las imagenes haran que veamos muy pocos registros
        	 el usuario no podra tampoco cambiar el tamaño de la tabla, cosa que si ocurre en el modo filtro.
        	En modo filtro no lo hacemos porque el tamaño sale demasiado grande y no se ve el formulario de filtrado, y como el usuario
        	puede mover el tamaño pues no hay problema
        	height=(int)(Math.max(getRowHeight(),GConfigView.smallImageHeight))*(m_iVisibleRow+filaCabecera);
        }else{
        	height=(int)(m_objViewBalancer.getRowHeight()getRowHeight()*(m_iVisibleRow+filaCabecera));
        }
        */
        /*int height= (int)(m_objViewBalancer.getRowHeight()*m_iVisibleRow);*/
    //    if( widthTotal>viewWidth )
    //            widthTotal=viewWidth;
        
        return new Dimension( widthTotal,100) ;
    }
    public boolean isUsualHeight()
    {
        return false;
    }
    /**
     * Este método devuelve el mínimo ancho del campo
     * @return int - El ancho del componente
     */
    protected int getWidthMin( int tm, int longitud, double widthChar, boolean commented, boolean isTableColumn )
    {
        //int mult = m_objViewBalancer.getFilterMode() ? 2 : 1;
        //int suma = m_objViewBalancer.getFilterMode() ? 1 : 0;
        //return widthEdit(widthChar,suma +/*mult * SH*/(longitud == -1 ? GConfigView.minimumLengthTextTable :longitud));*/
        
      //  int mult = m_objViewBalancer.getFilterMode()||isMultivalued() ? 2 : 1;
    	  int mult = 2;
        //int suma = m_objViewBalancer.getFilterMode()||isMultivalued()? 1 : 0;
        int suma = 1;
       // return widthEdit(widthChar,suma +/*mult * SH*/(longitud == -1 ? GConfigView.minimumLengthTextTable :longitud));
        
        switch(tm){
			case GConst.TM_ENUMERATED:
				return widthEdit(widthChar, (longitud==-1 ? 	/*GConfigView.minimumLengthText*/GConfigView.minimumLengthTextTable:
										longitud))+15;
						//Math.max(m_cfg.longMinimoCampoTexto,longitud))*anchoChar);
			case GConst.TM_INTEGER:
				return widthEdit(widthChar,suma+mult*(longitud==-1 ? 	2:
											longitud));
						//Math.max(m_cfg.longMinimoCampoNumerico,longitud))*anchoChar);
			case GConst.TM_IMAGE:
				return 15*3;//ver,borrar,asignar

			case GConst.TM_BOOLEAN:
				return 15;

			case GConst.TM_BOOLEAN_COMMENTED:
				if( !comentado )
					return 25;
				else
					//return GConfigView.minimumWidthBoolField + (int)(GConfigView.minimumLengthCheckExtension*widthChar);
					return 25;
 			case GConst.TM_REAL:
				return widthEdit(widthChar,suma+mult*(longitud==-1 ?	GConfigView.minimumLengthNumericField:
											longitud));
						//Math.max(m_cfg.longMinimoCampoNumerico,longitud))*anchoChar);
 			case GConst.TM_DATE:
				return widthEdit(widthChar,suma+mult*(longitud==-1 ? 	GConfigView.minimumLengthDate:
											longitud));
							//Math.max(longitud,m_cfg.longMinimoCampoFecha))*anchoChar);
 			case GConst.TM_DATE_HOUR:
				return widthEdit(widthChar,suma+mult*(longitud==-1 ? GConfigView.longMinimoCampoFechaHora:
                                    longitud));
			case GConst.TM_TEXT:
				//int longc=isTableColumn ? GConfigView.minimumLengthTextTable:GConfigView.minimumLengthText;
				int longc=15;
				//System.outprintln("GFormTable1: "+isTableColumn+" "+longc+" "+longitud+" ");
				if( longitud==-1 ){
					return (int)(longc*widthChar);
				}
				longc= Math.min( longc, longitud );
				int ancho=widthEdit(widthChar,longc);
				//System.outprintln("GFormTable2: "+longc+" "+ancho);
				return ancho;
		}
		return 0;
    }
    
    /**
     * Calcula la dimensión que necesita la tabla.
     */
    protected void calculateDimension()
    {
    	if(m_iVisibleRow>1 )
            m_bTopLabel = true;
        else m_bTopLabel = false;
    	
    	super.calculateDimension();
    }
    
    /**
     * Obtiene la dimensión del componente secundario.
     * @return Dimension - El área del componente secundario.
     */
    public Dimension getDimComponenteSecundario(){
    	 Iterator en = m_vColumn.iterator();
        boolean hasImage=false;
    	 while( en.hasNext() )
        {
    		 DynamicTableColumn col=(DynamicTableColumn)en.next();
            if(col.getType()==GConst.TM_IMAGE  && !col.getId().equalsIgnoreCase("type")/*Las columnas del tipo no nos interesa que afecten en el alto de la fila*/)
            	hasImage=true;
        }
    	
    	 int height;
    	 if(hasImage && m_iVisibleRow==1){
         	height=GConfigView.smallImageHeight;
         }else{
         	height=(int)10;
         }
    	return new Dimension(getNumBotones()*(int)10/*GConfigView.buttonWidth*/,/*GConfigView.buttonHeight*/height);
    }
    
    /**
     * Obtiene el atributo {@link #m_cuantitativo}
     * @return boolean - {@link #m_cuantitativo}
     */
    public boolean isCuantitativo(){
    	return m_cuantitativo;
    }

    /**
     * Obtiene el atributo {@link #m_hideHeader}
     * @return boolean - {@link #m_hideHeader}
     */
    public boolean isHideHeader(){
    	return m_hideHeader;
    }
    
    /**
     * Obtiene el atributo {@link #m_iniVirtualColumn}
     * @return boolean - {@link #m_iniVirtualColumn}
     */
    public int getIniVirtualColumn(){
    	return m_iniVirtualColumn;
    }
    
    /**
     * Obtiene el atributo {@link #m_atGroupColumn}
     * @return boolean - {@link #m_atGroupColumn}
     */
    public int getAtGroupColumn(){
    	return m_atGroupColumn;
    }
    
    /**
     * Obtiene el atributo {@link #m_headerLine}
     * @return boolean - {@link #m_headerLine}
     */
    public int getHeaderLine(){
    	return m_headerLine;
    }
    
//    /**
//     * Obtiene el atributo {@link #m_numBotones}
//     * @return boolean - {@link #m_numBotones}
//     */
//    public int getNumBotones(){
//    	return m_numBotones;
//    }
    
    /**
     * Obtiene el ancho de un botón por defecto, definido en la clase GConfigView, {@link GConfigView#buttonWidth}.
     * @return boolean - Devuelve {@link GConfigView#buttonWidth}
     */
    /*public int getAnchoBoton(){
    	return GConfigView.buttonWidth;
    }*/
    
    /**
     * Obtiene el atributo {@link #m_widthMin}, que se refiere al ancho mínimo de la tabla.
     * @return boolean - Devuelve el ancho mínimo almacenado, {@link #m_widthMin}
     */
    public int getWidthMin(){
    	return m_widthMin;
    }
    
    /**
     * Obtiene el atributo {@link #m_widthMax}, que se refiere al ancho máximo de la tabla.
     * @return boolean - Devuelve el ancho máximo almacenado, {@link #m_widthMax}
     */
    public int getWidthMax(){
    	return m_widthMax;
    }
    
    /**
     * Obtiene el atributo {@link #m_operations}
     * @return boolean - {@link #m_operations}
     */
    /*
    public AccessAdapter getOperations(){
    	return m_formAccess;
    }
    */
    /**
     * Modifica el atributo {@link #m_cuantitativo}.
     * @param cuantitativo El nuevo valor del atributo {@link #m_cuantitativo}
     */
    public void setCuantitativo(boolean cuantitativo){
    	m_cuantitativo=cuantitativo;
    }

    
    /**
     * Modifica el atributo {@link #m_iniVirtualColumn}.
     * @param iniVirtualColumn El nuevo valor del atributo {@link #m_iniVirtualColumn}
     */
    public void setIniVirtualColumn(int iniVirtualColumn){
    	m_iniVirtualColumn=iniVirtualColumn;
    }
    
    /**
     * Modifica el atributo {@link #m_cuantitativo}.
     * @param atGroupColumn El nuevo valor del atributo {@link #m_cuantitativo}
     */
    public void setAtGroupColumn(int atGroupColumn){
    	m_atGroupColumn=atGroupColumn;
    }
    
    /**
     * Modifica el atributo {@link #m_headerLine}.
     * @param headerLine El nuevo valor del atributo {@link #m_headerLine}
     */
    public void setHeaderLine(int headerLine){
    	m_headerLine=headerLine;
    }
    
    /**
     * Modifica el atributo {@link #m_hideHeader}.
     * @param hideHeader El nuevo valor del atributo {@link #m_hideHeader}
     */
    public void setHideHeader(boolean hideHeader){
    	m_hideHeader=hideHeader;
    	/*if(hideHeader)
    		m_bTopLabel=false;*/
    }

//    /**
//     * Modifica el atributo {@link #m_numBotones}.
//     * @param numBotones El nuevo valor del atributo {@link #m_numBotones}
//     */
//    public void setNumBotones(int numBotones){
//    	m_numBotones=numBotones;
//    }
    
    /**
     * Modifica el atributo {@link #m_widthMin}.
     * @param widthMin El nuevo valor del atributo {@link #m_widthMin}
     */
    public void setWidthMin(int widthMin){
    	m_widthMin=widthMin;
    }
    
    /**
     * Modifica el atributo {@link #m_widthMax}.
     * @param widthMax El nuevo valor del atributo {@link #m_widthMax}
     */
    public void setWidthMax(int widthMax){
    	m_widthMax=widthMax;
    }
    
    /**
     * Modifica el atributo {@link #m_operations}.
     * @param access El nuevo valor del atributo {@link #m_operations}
     */
    /*
    public void setOperations(AccessAdapter accessAdapter){
    	m_formAccess=accessAdapter;
    }
    */
    /**
     * Devuelve el alto por defecto de una fila de una tabla. Se definde en la clase GConfigView, {@link GConfigView#heightRowTable}.  
     * @return double - El alto de la tabla definido en {@link GConfigView#heightRowTable}
     */
    public double getRowHeight(){
    	/*if(m_iVisibleRow>1)
    		return GConfigView.heightRowTable;
    	else*/// return m_objViewBalancer.getRowHeight(isHighlighted());//Si es de una sola fila nos interesa el alto de un campo normal
    	 return 10;
    }
	public boolean hasCreationRow() {
		return creationRow;
	}
	public void setCreationRow(boolean creationRow) {
		this.creationRow = creationRow;
	}
	public boolean hasFinderRow() {
		return finderRow;
	}
	public void setFinderRow(boolean finderRow) {
		this.finderRow = finderRow;
	}

}
