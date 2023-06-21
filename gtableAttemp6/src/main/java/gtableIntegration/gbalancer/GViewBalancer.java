package gtableIntegration.gbalancer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.UIManager;

import gtableIntegration.dynagent.common.utils.Utils;
import gtableIntegration.gdev.gfld.GFormField;
import gtableIntegration.gen.GConfigView;
import gtableIntegration.gen.GConst;
import gtableIntegration.gen.IViewBalancer;

//https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gbalancer/GViewBalancer.java
/**
 * Esta es la clase principal donde vamos a obtener las diferentes combinaciones 
 * del formulario leido del XML y devolver la mejor combinación. 
 * En esta clase está el algoritmo principal que se sigue para realizar esto, comenzando 
 * en el método {@link #process(boolean, Insets, Insets)} y a partir de ahí llamando a los 
 * diferentes métodos de cada clase para obtenere los distintos formularios, compararlos y 
 * obtener el mejor, que será el que mostraremos en la aplicación final.
 * <p><h3>Algoritmo Principal de Ordenación</h3>
 * El algoritmo principal de ordenación está prácticamente implementado en los métodos {@link #process(boolean, Insets, Insets)}, {@link #processGroup(GFormGroup)} y {@link #getProcessedGroup(GFormGroup, int)}. Estos métodos llamarán a otros métodos de otras clases para completar el algoritmo de ordenación.
 * El algoritmo (muy a groso modo) que se sigue para conseguir el mejor formulario es:
 * <ol>
 * <li>Lee la información del XML del formulario, con los grupos que contiene y los campos que estos grupos contienen a su vez, y los almacena.</li>
 * <li>Ordeno los grupos que tiene el formulario según el orden establecido en el XML, y para cada grupo generamos todas las posibles combinaciones de colocación de los campos dentro del grupo. 
 * 		<br>Para obtener cada combinación de un grupo hacemos lo siguiente:
 * 		<ol>
 * 		<li>Ordeno sus campos de mayor a menor ancho. En esta ordenación tendremos en cuenta prioridades y ordenes de grupo también.</li>
 * 		<li>Calculo una altura posible para ese grupo e introduzco todos los campos del grupo, que se irán colocando en su posición correspondiente según esta altura establecida.</li>
 * 		</ol>
 * </li>
 * <li>Calculo todas las posibles combinaciones de los grupos anteriormente hallados para formar un formulario, por lo que obtengo un gran número de formularios posibles.</li>
 * <li>Según la lista de formularios obtenida anteriormente, descarta los formularios que tienen una ordenación muy mala La ordenación es mala si se sale de los márgenes máximos que nos han dado previamente, para la creación del formulario (los márgenes se nos pasan desde el núcleo de la aplicación, no los genera este proyecto).</li>
 * <li>Tras descartar estas malas combinaciones y quedarnos con las combinaciones válidas de los formularios, los vamos a ordenar por puntuación, para ver cuáles tienen mejor disposición.
 *		<br>Para puntuar los formularios se siguen los siguiente pasos:
 * 		<ol>
 * 		<li>Calculamos la proporción de los tamaños de los grupos con respecto al tamaño del panel hallado. Después le hacemos la inversa (1-p) y lo multiplicamos por 1300.</li>
 * 		<li>Calculamos la proporción del tamaño del panel hallado con respecto al tamaño máximo permitido (que nos ha pasado la aplicación). Después lo multiplicamos por 300.</li>
 * 		<li>Multiplicamos los dos parámetros mencionados anteriormente, y el que en total de menor resultado ese será el mejor formulario, es decir, el que tiene mejor aprovechado el espacio posible y más se ajusta al tamaño que nos proporciona la aplicación.</li>
 * 		</ol>
 * </li>
 * <li>Una vez que tengo todos los formularios ordenados, me quedo sólo con el mejor resultado, y los demás los descarto.</li>
 * <li>Optimizo el formulario obtenido para que se alinee lo mejor posible. Los pasos para la alineación se explican a continuación.</li>
 * </ol>
 * </p>
 * 
 * <p><h3>Algoritmo de Alineación</h3>
 * Los pasos básicos de la alineación se empiezan a invocar desde el método {@link GGrpColumn#fineTune(IViewBalancer)} de la clase {@link GGrpColumn}, por lo que se puede seguir una traza a partir de este método para comprender mejor el proceso de alineamiento que se sigue. Como se hace desde la clase {@link GGrpColumn}, sólo podemos obtener los campos por columnas, por lo que el alineamiento se va realizar sobre cada columna independientemente.
 * <br>Los pasos a seguir (muy a groso modo también) son los siguientes:
 * <ol>
 * <li>Calculamos el número máximo de subcolumnas que tiene la columna.</li>
 * <li>Por cada subcolumna (excepto la primera), obtenemos todas la filas que tienen al menos esa subcolumna. Por ejemplo, si el máximo de subcolumnas es 3, obtendremos todas las filas que contienen 2 o más subcolumnas y después todas las filas que contienen 3 subcolumnas. (Esto nos permite alinear la subcolumna 2 de una fila de 2 subcolumnas, con la subcolumna 2 de una fila de 3 subcolumnas).</li>
 * <li>Una vez que tenemos todas las filas que tienen más de una subcolumna, voy trazando los alineamientos independientes por subcolumnas. Para ello haremos lo siguiente (método {@link GGrpColumn#processRowsSymmetric(IViewBalancer, Vector, int)}):
 * 		<ol>
 * 		<li>Separaremos campos de la subcolumna con orden preestablecido de los que no tienen orden.</li>
 * 		<li>Normalizaremos primero las posiciones de los campo y después normalizaremos las etiquetas.
 * 			<ul>
 * 			<li>Para normalizar la posición (método {@link GGrpColumn#normalizeX(IViewBalancer, Vector, Vector, GProcessedField)}) calculamos el campo que está más a la derecha y alineamos todos los campos de la subcolumna a esa posición.</li>
 * 			<li>Para normalizar el ancho de la etiqueta (método {@link GGrpColumn#normalizeLabels(IViewBalancer, Vector, Vector, GProcessedField)}) calculamos la etiqueta mayor e incrementamos todas las etiquetas para que tengan ese tamaño.</li>
 * 			<li>Si ha habido incremento (sólo se llevará a cabo si los campos de la fila no se salen de la columna), desplazaremos con el mismo incremento todas las etiquetas de las siguientes subcolumnas.</li>
 * 			</ul>
 * 		</li>
 * 		</ol>
 * </li>
 * <li>Por último, procederemos a alinear los márgenes derechos de los componentes por subcolumnas. Para ello agrandaremos los componentes para que todos terminen en el mismo margen derecho. Como hemos hecho anteriormente, si el incremento no es posible porque algunos campos se salen de la columna, no se llevará a cabo y se dejará desalineado. Todo esto se hará llamando al método {@link GGrpColumn#alineaElementos()} por lo que se recomienda hacer una traza de éste método para entenderlo mejor. 
 * 		Los pasos que seguimos son los siguientes:
 * 		<ol>
 * 		<li>Separaremos la subcolumna de la derecha del todo, del resto de subcolumnas, y las trataremos de forma separada con los métodos {@link GGrpColumn#alineaSubColumna(Vector, int)} (donde Vector es la columna de la derecha) y {@link GGrpColumn#alineaElementosIntermedio(Vector, int)}</li>
 * 		<li>Para la subcolumna de la derecha, calculamos el máximo margen derecho, y para todos los campos (excepto los CheckBox) calculamos el incremento necesario para ajustarse a ese margen, vemos si no se nos sale de la columna y modificamos el borde si es posible.</li>
 * 		<li>Para las subcolumnas intermedias, primero separaremos los elementos de la subcolumna que tienen un orden preestablecido de los que no lo tienen. Alinearemos cada una de estas dos subcolumnas por separado. Para cada una de ellas haremos (método {@link GGrpColumn#alineaSubColumna(Vector, int)}):
 * 			<ul>
 * 			<li>Calculamos el máximo margen derecho de cada subcolumna.</li>
 * 			<li>Intentamos ajustar todos los campos de la subcolumna a ese margen derecho, comprobando si el incremento es posible y desplazando los elementos de las subcolumnas de la derecha si hemos llevado a cabo el desplazamiento.</li>
 * 			</ul>
 * 		</li>
 * 		</ol>
 * </li>
 * </ol>
 * </p>
 */
public class GViewBalancer implements IViewBalancer
{
	protected static final int INPUT_FORM=1;
	protected int politica=INPUT_FORM;
	
    protected final int DIR_VERTICAL = 1;
    protected final int DIR_HORIZONTAL = 2;

    /**
     * La información introducida para el formulario en el XML, con todos sus grupos y campos.
     */
   // protected GFormFieldList m_objFormFieldList;
   
    /**
     * Vector con todas las combinaciones de los formularios ya procesados
     * Cada elemento del vector es del tipo {@link GProcessedForm}
     */
    protected Vector m_vProcessedFormList;
    /**
     * The font
     */
    protected Font m_fontRegular=null;
    /**
     * The FontRenderContext object.
     */
    protected FontRenderContext m_fontRender=null;

    /**
     * Alto de una línea de texto
     */
    protected double m_dTextHeight;
    /**
     * El ancho de un carácter de texto
     */
    protected double m_dAveCharWidth;
    /**
     * El alto predefinido de una fila
     */
    protected double m_dRowHeight;

    /**
     * Alto de una línea de texto de un campo resaltado
     */
    protected double m_dTextHeightHighlighted;
    /**
     * El ancho de un carácter de texto de un campo resaltado
     */
    protected double m_dAveCharWidthHighlighted;
    /**
     * El alto predefinido de una fila de un campo resaltado
     */
    protected double m_dRowHeightHighlighted;

    /**
     * Modo búsqueda. Si el formulario que se está creando 
     * es un formulario de busqueda algunos campos nos puede interesar mostrarlos de manera
     * distinta a la predefinida. Por ejemplo los {@link GEdit} solo tendrán una fila.
     */
    protected boolean m_bFilterMode;

    /**
     * El ancho optimizado del panel. Se calcula en el método {@link #calculateMinDimension(boolean)}.
     * El ancho final del panel puede variar, pero esta primera aproximación es la base para todos los cálculos.
     */
    protected int m_iBestPanelWidth;
    /**
     * El alto optimizado del panel. Se calcula en el método {@link #calculateMinDimension(boolean)}.
     * El alto final del panel puede variar, pero esta primera aproximación es la base para todos los demás cálculos.
     */
    protected int m_iBestPanelHeight;
    
    /**
     * Márgenes del panel (se pasan desde el núcleo en el método formFactory)
     */
    protected Insets m_margenesPanel;
    
    /**
     * Márgenes del grupo (se pasan desde el núcleo en el método formFactory)
     */
    protected Insets m_margenesGrupo;

    /**
     * El constructor. El resto de atributos que no se pasan por parámetros se calculan dentro del constructor. 
     * Por ejemplo {@link #m_dTextHeight}, {@link #m_dAveCharWidth}, {@link #m_dRowHeight}.
     * @param endDimension Área máxima que puede ocupar el panel (el formulario).
     * @param font Valor para el atributo {@link #m_fontRegular}.
     * @param render Valor para el atributo {@link #m_fontRender}.
     * @param bFilterMode Valor para el atributo {@link #m_bFilterMode}.
     */
    private Graphics m_graphics;
    
    public GViewBalancer(Dimension endDimension,Graphics graphics,boolean bFilterMode)
    {
    	m_graphics=graphics;
        Graphics2D gr2D = (Graphics2D) graphics;
		gr2D.setFont(UIManager.getFont("Label.font"));
		m_fontRender = gr2D.getFontRenderContext();
		m_fontRegular=gr2D.getFont();
        m_bFilterMode = bFilterMode;

        Dimension rect=getDimString("ABCDE",false,false);
        m_dTextHeight = rect.getHeight();
        m_dAveCharWidth = rect.getWidth()/5.0; // 5 is the number of chars of example string
        m_dRowHeight = m_dTextHeight+GConfigView.V_InternalEditPadd*2;
        
        rect=getDimString("ABCDE",false,true);
        m_dTextHeightHighlighted = rect.getHeight();
        m_dAveCharWidthHighlighted = rect.getWidth()/5.0; // 5 is the number of chars of example string
        m_dRowHeightHighlighted = m_dTextHeightHighlighted+GConfigView.V_InternalEditPadd*2;

        init(0);

     //   m_objFormFieldList.setPanelWidth(endDimension.width);
       // m_objFormFieldList.setPanelHeight(endDimension.height);
    }
    
    public static double getRowHeightS(Graphics gf){
    	
    	//System.out.println("GRAPHICS = "+gf);
    	Graphics2D gr2D = (Graphics2D) gf;
		gr2D.setFont(UIManager.getFont("Label.font"));
		FontRenderContext fontRender = gr2D.getFontRenderContext();
		Font font=gr2D.getFont();
    	Dimension rect=getDimString("ABCDE",false, font, fontRender, false);
    	//System.out.println("HEIGHT="+(rect.getHeight()+GConfigView.V_InternalEditPadd*2));
    	return rect.getHeight()+GConfigView.V_InternalEditPadd*2;
    }

    /**
     * Inicialización del método. Creamos el atributo {@link #m_objFormFieldList}.
     * @param arg No se utiliza.
     * @return int - Devolvemos 0 si todo ha ido bien.
     */
    public int init(int arg)
    {
       // m_objFormFieldList = new GFormFieldList(this);
        return 0;
    }
    /**
     * Usaremos este método para liberar memoria si es que lo necesitamos. 
     * @param arg No se utiliza.
     * @return int - Devolvemos 0.
     */
    public int exit(int arg)
    {
        return 0;
    }
    
    /**
     * Este método obtiene la mínima Dimension (ancho x alto, width x height) que necesitamos
     * para representar la cadena que se nos pasa en el parámetro 'value'.
     * @param value Cadena que queremos representar y calcular su mínima dimensión.
     * @param bold Si la cadena está en negrita o no (true o false).
     * @return Dimension - Devuelve la mínima dimensión para la cadena pasada en el parámetro 'value'.
     */
    public static Dimension getDimString(String value, boolean bold, Font font, FontRenderContext fontRender, boolean highlighted){
    	float multiplySize=1;
    	if(highlighted)
    		multiplySize=GConfigView.multiplySizeHighlightedFont;
    	return Utils.getDimString(value, bold, font, fontRender, multiplySize);
    }
    
    public Dimension getDimString( String value, boolean bold, boolean highlighted )
    {
        return getDimString(value, bold, m_fontRegular, m_fontRender, highlighted);
    }
    /**
	 * Obtiene el valor del atributo {@link #m_bFilterMode}.
	 * @return boolean - Devuelve el atributo {@link #m_bFilterMode}.
	 */
	public boolean getFilterMode()
	{
		return m_bFilterMode;
	}
	/**
	 * Obtiene el atributo {@link #m_dTextHeight}.
	 * @return double - Devuelve {@link #m_dTextHeight}.
	 */
	public double getTextHeight(boolean highlighted)
	{
		if(highlighted)
			return m_dTextHeightHighlighted;
		return m_dTextHeight;
	}
	/**
	 * Obtiene el atributo {@link #m_dAveCharWidth}.
	 * @return double - Devuelve {@link #m_dAveCharWidth}.
	 */
	public double getAveCharWidth(boolean highlighted)
	{
		if(highlighted)
			return m_dAveCharWidthHighlighted;
		return m_dAveCharWidth;
	}
	/**
	 * Obtiene el atributo {@link #m_dRowHeight}.
	 * @return double - Devuelve {@link #m_dRowHeight}.
	 */
	public double getRowHeight(boolean highlighted)
	{
		if(highlighted)
			return m_dRowHeightHighlighted;
		return m_dRowHeight;
	}

	/**
	 * Obtiene la separación horizontal predefinida entre dos grupos del mismo panel
	 * @return int - Devuelve la separación horizontal entre dos grupos.
	 */
	public int getPanelHGap()
	{
		return GConfigView.PanelHGap;
	}
	/**
	 * Obtiene la separación vertical predefinida entre dos grupos del mismo panel
	 * @return int - Devuelve la separación vertical entre dos grupos.
	 */
	public int getPanelVGap()
	{
		return GConfigView.PanelVGap;
	}
	/**
	 * Obtiene el margen izquierdo predefinido entre el grupo más a la izquierda 
	 * y el borde del panel.
	 * @return int - Devuelve el margen izquierdo entre el grupo más a la izquierda y el panel.
	 */
	public int getPanelLeftMargin()
	{
		int margen=GConfigView.PanelLeftMargin;
		if(m_margenesPanel!=null)
			margen=m_margenesPanel.left;
		return margen;
	}
	/**
	 * Obtiene el margen superior predefinido entre el grupo superior y el borde del panel.
	 * @return int - Devuelve el margen superior del grupo superior con respecto al panel.
	 */
	public int getPanelTopMargin()
	{
		int margen=GConfigView.PanelTopMargin;
		if(m_margenesPanel!=null)
			margen=m_margenesPanel.top;
		return margen;
	}
	/**
	 * Obtiene el margen derecho predefinido entre el grupo colocado más a la derecha 
	 * y el borde del panel.
	 * @return int - Devuelve el margen derecho entre el último grupo de la derecha y el panel.
	 */
	public int getPanelRightMargin()
	{
		int margen=GConfigView.PanelRightMargin;
		if(m_margenesPanel!=null)
			margen=m_margenesPanel.right;
		return margen;
	}
	/**
	 * Obtiene el margen inferior predefinido entre el grupo inferior y el borde del panel.
	 * @return int - Devuelve el margen inferior del grupo inferior con respecto al panel.
	 */
	public int getPanelBottomMargin()
	{
		int margen=GConfigView.PanelBottomMargin;
		if(m_margenesPanel!=null)
			margen=m_margenesPanel.bottom;
		return margen;
	}
	/**
	 * Este método obtiene la separación predefinida entre la primera fila de un grupo 
	 * y el borde superior del grupo.
	 * @return int - Devuelve la separación entre la primera fila y el margen superior de un grupo.
	 */
	public int getGroupTopMargin()
	{
		int margen=GConfigView.GroupTopMargin;
		if(m_margenesGrupo!=null)
			margen=m_margenesGrupo.top;
		return margen;
	}
	/**
	 * Este método obtiene la separación predefinida entre la última fila de un grupo 
	 * y el borde inferior del grupo.
	 * @return int - Devuelve la separación entre la última fila y el margen inferior de un grupo.
	 */
	public int getGroupBottomMargin()
	{
		int margen=GConfigView.GroupBottomMargin;
		if(m_margenesGrupo!=null)
			margen=m_margenesGrupo.bottom;
		return margen;
	}
	/**
	 * Este método obtiene la separación entre la primera columna de un grupo 
	 * y el borde superior del grupo.
	 * @return int - Devuelve la separación entre la primera fila y el margen superior de un grupo.
	 */
	public int getGroupLeftMargin()
	{
		int margen=GConfigView.GroupLeftMargin;
		if(m_margenesGrupo!=null)
			margen=m_margenesGrupo.left;
		return margen;
	}
	/**
	 * Este método obtiene la separación entre la última columna de un grupo 
	 * y el borde derecho del grupo.
	 * @return int - Devuelve la separación entre la primera fila y el margen superior de un grupo.
	 */
	public int getGroupRightMargin()
	{
		int margen=GConfigView.GroupRightMargin;
		if(m_margenesGrupo!=null)
			margen=m_margenesGrupo.right;
		return margen;
	}
	/**
	 * Obtiene la separación horizontal predefinida entre campos de un grupo.
	 * Esta separación puede aumentar en caso de alineamiento por columnas, pero nunca disminuir.
	 * @return int - Devuelve la separación horizontal entre campos de un grupo
	 */
	public int getGroupHGap()
	{
		return GConfigView.GroupHGap;
	}
	/**
	 * Obtiene la separación vertical predefinida entre campos de una misma columna.
	 * @return int - Devuelve la separación vertical entre campos de una columna.
	 */
	public int getGroupVGap()
	{
		return GConfigView.GroupVGap;
	}
	
	public int getHCellPad()
	{
		return GConfigView.HCellPad;
	}
	public int getVCellPad()
	{
		return GConfigView.VCellPad;
	}
	/**
	 * Obtiene el alineamiento del formulario (por norma justificado).
	 * @return int - Devuelve la constante (entera) asignada a la alineación correspondiente.
	 */
	public int getAlignment()
	{
		return GConst.ALIGN_JUSTIFY;
	}
    /**
     * Este método añade un grupo al formulario que vamos a mostrar. Lo hace con el atributo {@link #m_objFormFieldList} y el método de su clase {@link GFormFieldList#addGroup(int, String, int)}.
     * @param groupId El ID del grupo que vamos a añadir.
     * @param strLabel El nombre (etiqueta) del grupo que vamos a añadir.
     * @param order El orden del grupo que vamos a añadir.
     * @return int - Devuelve el código asignado según si el método a funcionado correctamente o ha tenido un error.
     */
	/*
    public int addGroup(int groupId,String strLabel,int order)
    {
        return m_objFormFieldList.addGroup(groupId,strLabel,order);
    }
*/
    /**
     * Este método añade un campo a un grupo. Lo hace con el atributo {@link #m_objFormFieldList} y el método de su clase{@link GFormFieldList#addItem(int, int, String, String, int, int, String, boolean, boolean, boolean, String, String, int, boolean, int, boolean, int, Vector)}. 
     * @param groupId El ID del grupo en el que vamos a añadir el campo.
     * @param fieldType El tipo del campo.
     * @param id Es '0@tapos', donde tapos es el verdadero ID del campo leído del XML.
     * @param id2 El ID del formulario entero al que pertence el Item (campo).
     * @param priority La prioridad del campo en el grupo. A mayor prioridad, antes colocaremos el campo en el grupo.
     * @param mask Se usa en GTable y GEdit para aplicar la máscara al valor del campo.
     * @param enable Si se puede editar el campo o no.
     * @param nullable Si el valor del campo puede ser nulo.
     * @param multivalued Si puede tener multiples valores el campo.
     * @param defaultVal Valor por defecto del campo.
     * @param label Etiqueta del campo.
     * @param length Longitud del componente del campo.
     * @param commented Si el campo tiene un componente secundario con un campo de texto. Sólo se utiliza en los CheckBox
     * @param order Orden del campo.
     * @param visible Si se muestra el campo o no.
     * @param numBotones Número de botones.
     * @param vValues Sirve sólo para los enumerados. Contiene los distintos valores posibles.
     * @return int - Devuelve un código según si el método a funcionado correctamente o ha tenido algun error.
     */
   /* public int addItem(int groupId, int fieldType, String id, String id2, int tapos, int priority,
                            String mask, boolean enable, boolean nullable, boolean multivalued, String defaultVal, String label,int length,
                            boolean commented, int order, boolean visible ,int numBotones,Vector vValues)*/
    
  /*
     public int addItem(int groupId, int fieldType, String id, String idRoot, int priority,
            String mask, boolean enable, boolean nullable, boolean multivalued, Object defaultVal, String label, String name, int length,
            boolean commented, int order, boolean visible ,int numBotones,Vector vValues,boolean highlighted,boolean directoryType, int rows, Integer redondeo)
    
    {
        return m_objFormFieldList.addItem(groupId,fieldType,id,idRoot,priority,mask,enable,nullable,multivalued,defaultVal,label,name,length,commented,order,visible,numBotones,vValues,highlighted,directoryType,rows,redondeo);
    }
*/
    /**
     * Este método añade una tabla a un grupo. Lo hace con el atributo {@link #m_objFormFieldList} y a través del método de su clase {@link GFormFieldList#addTable(int, String, int, boolean, String, int, boolean, int, Vector, Vector, int, int, boolean, int, int, int, boolean, int, int, int, int)}
     * 
     * @param groupId El ID del grupo donde vamos a añadir la tabla.
     * @param id El ID de la tabla que se va a añadir.
     * @param priority La prioridad de la tabla en el grupo. A mayor prioridad, antes colocaremos la tabla en el grupo.
     * @param enable Si se puede editar la tabla o no.
     * @param label Etiqueta de la tabla.
     * @param order Orden de la tabla.
     * @param visible Si se muestra la tabla o no.
     * @param rows Número de filas de la tabla.
     * @param vColumns Vector con las columnas que contiene la tabla.
     * @param vRows Vector con las filas que contiene la tabla.
     * @param cuantitativo Si varios registros de la tabla son resumibles en uno.
     * @param iniVirtualColumn
     * @param atGroupColumn
     * @param headerLine El número de lineas que tiene que ocupar la cabecera.
     * @param hideHeader Si oculta la cabecera de la tabla
     * @param numBotones Número de botones que tiene la tabla.
     * @param widthMin Ancho mínimo de la tabla.
     * @param creationAllowed 
     * @param access Accesos que tiene el usuario sobre esta tabla (permisos).
     * @return int - Devuelve un código según si el método a funcionado correctamente o ha tenido algun error.
     */
	/*
    public int addTable(int groupId, String id, int priority, boolean enable, boolean nullable, String label, String name, int order,boolean visible,int rows, Vector vColumns,Vector vRows, boolean cuantitativo,int iniVirtualColumn, int atGroupColumn, int headerLine,boolean hideHeader,int numBotones,int widthMin,AccessAdapter accessAdapter, boolean creationRow, boolean finderRow)
    {
        int widthMax=m_objFormFieldList.getPanelWidth()-getPanelRightMargin()-getPanelLeftMargin()-getGroupRightMargin()-getGroupLeftMargin()-getGroupHGap();
    	return m_objFormFieldList.addTable(groupId,id,priority,enable,nullable,label,name,order,visible,rows,vColumns,vRows,cuantitativo,iniVirtualColumn,atGroupColumn,headerLine,hideHeader,numBotones,widthMin,widthMax,accessAdapter,creationRow,finderRow);
    }
    */
    /**
     * Este método devuelve la mejor combinación del formulario, es decir, la que está mejor puntuada.
     * @return GProcessedForm - Devuelve el formulario final obtenido.
     */
	/*
    public GProcessedForm getBestResult()
    {
        //return m_objBestProcessedForm;
        if(m_vProcessedFormList!=null){
        	processPositionTablesMemos();
            return (GProcessedForm)m_vProcessedFormList.elementAt(0);
        }
        return null;
    }
    */
    private void processPositionTablesMemos() {
    	/*
    	GProcessedForm gp=(GProcessedForm)m_vProcessedFormList.elementAt(0);
    	Enumeration en = gp.getProcessedGroupList().elements();
        while(en.hasMoreElements())
        {
        	GProcessedGroup grp = (GProcessedGroup)en.nextElement();
        	Enumeration en2 = grp.getProcessedFieldList().elements();
        	GProcessedFieldsSorts gfs= new GProcessedFieldsSorts();
        	while(en2.hasMoreElements()){
        		GProcessedField gfield=(GProcessedField) en2.nextElement();
        		if (gfield.getFormField().isTopLabel())
        			gfs.addField(gfield);
        	}
        	//gfs.toStingListFields();
        	//gfs.toStingListFieldsWidth();
        	gfs.process();
        }
       */ 
	}

	/**
     * Este método calcula una primera aproximación de la 
     * mínima dimensión del panel (ancho y alto). Se basa en los tamaños del panel
     * definidos por la aplicación y las estimaciones de los tamaños de cada grupo.
     * @param bMinimizeHeight No se usa en esta versión.
     */
    private void calculateMinDimension(boolean bMinimizeHeight)
    {
    }
    /**
     * Este método comienza el procesado del formulario leído del XML
     * para obtener la mejor combinación posible de los grupos y los campos.
     * <p>
     * Para ello lo que hace el método es:
     * <ol>
     * <li>Calcula la mínima dimensión que estima que va a ocupar el formulario, llamando al método {@link #calculateMinDimension(boolean)}.</li>
     * <li>Ordena los grupos del formulario (según el orden obtenido del XML)</li>
     * <li>Itera sobre todos los grupos del formulario, y para cada uno de ellos calcula todas las posibles combinaciones del grupo, llamando al método {@link #processGroup(GFormGroup)}.Todas las distintas combinaciones de los grupos se almacenarán en un vector, dónde cada elemento es del tipo {@link GGrpCombination}</li>
     * <li>Una vez que tengo todas las combinaciones de cada grupo, descartaré las malas combinaciones, llamando al método {@link GGrpCombination#discardPoorCombinations(int, int)} con el tamaño del panel calculado anteriormente.</li>
     * <li>Con el vector que contiene las combinaciones de grupos que me han quedado, genero todos los formularios posibles, combinando los grupos del vector (llamando al método {@link #generateAllFormCombinations(Vector)}</li>
     * <li>Tras conseguir todas las combinaciones de los formularios, me voy a quedar con el mejor, llamando al método {@link #findBestFormCombination()}.</li>
     * <li>El formulario que obtengo, lo optimizo para visualizarlo lo mejor posible en la interfaz, llamando al método {@link #fineTuneBestCombination()}.</li>
     * </ol>
     * Una vez que hemos terminado este procedimiento, en el atributo {@link #m_vProcessedFormList} tengo el formulario final alineado y optimizado.
     * </p>
     * @return int - Devuelve 0 si todo ha ido bien.
     */
    public int process(boolean bMinimizeHeight,Insets margenesPanel,Insets margenesGrupo)
    {
		
        return 0;
    }
    /**
     * Este método analiza un grupo y crea las distintas posibilidades para cololar los campos.
     * La lista de campos se ordena por prioridades, órdenes y anchos de mayor a menor.
     * <br>Calcula los distintos altos que puede tener el formulario, 
     * calculando siempre que no sobrepase el ancho máximo, y para las distintas alturas
     * del formulario y el grupo concreto sobre el que estamos iterando (se nos pasa por parámetro), 
     * llamamos al método {@link #getProcessedGroup(GFormGroup, int)}.
     * @param frmGrp El grupo que vamos a procesar.
     * @return GGrpCombination - Devuelve la combinación del grupo obtenida.
     */
    /*
    protected GGrpCombination processGroup(GFormGroup frmGrp)
    {
        frmGrp.orderByWidth();
        GGrpCombination objProcessedGroupList = new GGrpCombination(frmGrp);

        boolean bMoreColumnPossible = true;
        int newHeight = m_iBestPanelHeight;
        int maxWidthAllowed = (int)(m_iBestPanelWidth);
        int rowHeight = (int)getRowHeight(false);
        while(bMoreColumnPossible)
        {
            GProcessedGroup prGroup = getProcessedGroup(frmGrp,newHeight);
            if (prGroup != null )
            {
                objProcessedGroupList.addCombination(prGroup);
            }
            newHeight -= rowHeight;
        	if(newHeight<rowHeight||prGroup.getBounds().width>maxWidthAllowed)
                bMoreColumnPossible = false;
        }
        return objProcessedGroupList;
    }
    */
    /**
     * Este método devuelve un grupo ya procesado ({@link GProcessedGroup}) basándose en un grupo 
     * leído del XML ({@link GFormGroup}). Los campos se van colocando por columnas, a partir 
     * de un vector que contiene los campos del grupo en el orden correcto. Las columnas pueden tener más 
     * de un campo en la misma fila, para optimizar el espacio lo máximo posible.
     * <p>El método básicamente lo que va haciendo es:
     * <ol>
     * <li>Itera sobre todos los campos del grupo y va colocándolos por columnas. Cuando el campo nuevo sobre el que itera sobrepasa el alto máximo, crea una nueva columna e introduce el campo en ella.</li>
     * <li>Para cada campo, si puede colocarlos en la misma fila (método {@link #repositionField(GProcessedField, GProcessedField, int, int, int)}) lo coloca y así ahorra espacio.
     * <li>Antes de crear una nueva columna, si ha sobrepasado el ancho máximo, con el método {@link #reorganizeComponents(GFormGroup, GGrpColumn, int, int, int)} intenta recolocar todos los campos para optimizar la altura lo máximo posible. Si es necesario (no caben todos los campos) se incrementará la altura.</li>
     * <li>Cuando tenga la columna ya procesada para la altura correspondiente, con el método {@link #normalizeLabels(GGrpColumn)} normalizará las etiquetas de la columna que ha procesado}</li>
     * </ol>
     * Para una vista más detallada del método mirar el código o hacer una traza.
     * </p>
     * @param frmGrp El grupo que vamos a procesar, donde está toda la información leída del XML. 
     * @param aveColHeight La altura máxima que debe ocupar el grupo. No es una restricción, 
     * 					sino que se ajusta a esa altura lo máximo posible, pero si no cabe en 
     * 					el ancho establecido, esta altura será sobrepasada.
     * @return GProcessedGroup - Devuelve el grupo ya procesado.
     */
   
    /**
     * Este método posiciona un campo en su lugar correspondiente de la columna y del formulario.
     * Si tiene espacio en la columna para colocarlo en la misma fila que el campo anterior 
     * lo coloca (siempre y cuando el campo quepa en el ancho de la columna).
     * Si no, se coloca el campo debajo del anterior.
     * @param fieldToPosition El campo que va a ser posicionado.
     * @param prevField El campo anterior al posicionado.
     * @param prevBottomMostPt El punto más bajo del campo colocado anteriormente, es decir, el punto a partir del cual queremos colocar el nuevo campo.
     * @param xColPos La posición X de la columna en el grupo correspondiente.
     * @param maxColWidth Máximo ancho de la columna.
     * @return int - Devuelve 0 si el método se ha ejecutado con éxito y no ha sido recolocado el campo en la misma fila. <br>Devuelve 1 si el campo ha sido recolocado, para que posteriormente en GProcessedGroup no se tenga en cuenta el ancho de la etiqueta de ese campo para normalizar.
     */
   

    /**
     * Este método reorganiza los campos de una columna, viendo si es posible introducir varios 
     * campos en la misma fila, recorriendo toda la columna, y desplazando los campos necesarios
     * hacia arriba, si ha habido alguna recolocación.
     * @param frmGrp El grupo sobre el que estamos iterando, a la hora de recolocar los campos para una columna.
     * @param vOneColumn La columna sobre la que estamos colocando los campos, y que queremos optimizar en altura, colocando varios campos en una misma fila.
     * @param colLeftPos Punto más a la izquierda de la columna.
     * @param maxColWidth Ancho máximo de la columna.
     * @param maxLabelWidth Ancho máximo de la etiqueta de los campos que contiene la columna.
     * @return int - Devuelve la altura final del grupo, tras recolocar (si ha sido posible) varios campos en una misma fila.
     */
   
    /**
     * Esta clase se usa internamente para devolver una tupla. 
     * Será usada para la devolución de una tupla 
     * en el método {@link #placeFieldsInOneRow(GGrpColumn, int, int, int, int, int)}.
     */
    class GPosChangeRes
    {
        int nFieldPlaced;
        int rowHeight;
    }
    
    /**
     * Este método devuelve el número de campos que pueden ser colocados en una misma fila
     * para un ancho definido (incluyendo el espaciado entre ellos).
     * Este método será llamado desde {@link #reorganizeComponents(GFormGroup, GGrpColumn, int, int, int)}.
     * @param grpColumn Columna que va a ser procesada (para iterar por todos sus campos)
     * @param startIndex Índice para empezar a iterar
     * @param colWidth Ancho de la columna
     * @param left Borde izquierdo de la columna
     * @param top Borde superior de la columna
     * @param maxLabelWidth Eiqueta más ancha de la columna (se harán todas las etiquetas igual de anchas que ésta).
     * @return GPosChangeRes - Devuelve el número de campos procesados y el alto de la fila.
     */
  
    /**
     * Este método hace todas las longitudes de las etiquetas iguales a la de mayor ancho.
     * This method makes all label length equal to the longest one
     */
  

    /*
    For debug purpose, this should not be there in the final version
    */
    public Vector getProcessedFormList()
    {
        return m_vProcessedFormList;
    }
    /**
     * Este método devuelve el atributo {@link #m_objFormFieldList}.
     * @return GFormFieldList - El atributo {@link #m_objFormFieldList}.
     */
    /*
    public GFormFieldList getFormFieldList()
    {
        return m_objFormFieldList;
    }
    */
    /**
     * Este método genera las diferentes combinaciones de posiciones de los grupos en el formulario.
     * Las diferentes dimensiones de los formularios han sido calculadas previamente 
     * a la llamada de éste método, y almacenadas en la clase {@link GGrpCombination}, y por 
     * tanto en este método se remite a estas dimensiones.
     * 
     * @param vGrpCombinationList  Este vector contiene todas las posibles combinaciones para cada grupo. 
     * Cada elemento de este vector es del tipo {@link GGrpCombination}.
     * @return Vector - El vector devuelto consiste en una combinación concreta de los grupos posibles.
     * Cada elemento de este vector es del tipo {@link GProcessedForm}.
     */
   
  

	/**
     * Este método devuelve una posible posición del grupo en el formulario.
     * @param rcPrevItemPos La posición del grupo anteriormente colocado.
     * @param theGroup El grupo que voy a colocar.
     * @param direction La dirección en la que va a ser colocado (con respecto al anterior).
     * @return Rectangle La posición final donde se va a colocar.
     */
   

    /**
     * Este método calcula la posible posición del grupo en el formulario final.
     * @param vGroupList con la lista de grupos que contiene el formulario.
     * @param rcPrevItemPos Rectángulo (área) del grupo anterior de la lista tratado.
     * @param nItemPos La posición del grupo en el vector de grupos.
     * @param direction Dirección (horizontal o vertical) como se quiere colocar el grupo.
     * @param node Información del nodo del árbol correspondiente a ese grupo.
     */
   
    /**
     * Este método ordenará todos los formularios obtenidos (por puntuación) y
     * se quedará únicamente con el mejor.
     */
    protected void findBestFormCombination()
    {
    	
        if(m_vProcessedFormList==null)
            return;

        Object[] array = m_vProcessedFormList.toArray();
        Arrays.sort(array);
        m_vProcessedFormList = new Vector();
        int arLen = array.length;
        //int maxCombination = 8; Asi estaba antes, pero ahora solo me interesa el mejor
        int maxCombination = 1;
        if(array.length<maxCombination)
            maxCombination = array.length;
        for(int i = 0;i<maxCombination;i++)
        {
            m_vProcessedFormList.addElement(array[i]);
           // System.err.println("TAMAÑO "+((GProcessedForm)array[i]).getBounds());
        }
//        m_vProcessedFormList.addElement(m_objBestProcessedForm);
    }
    /**
     * Este método optimiza el formulario para mejorar el alineamiento y la visión
     * general de éste. 
     */
    protected void fineTuneBestCombination()
    {
      
       
    }

    /**
     * Devuelve el ancho de un campo de texto
     * @param anchoChar Ancho de un carácter (hallado como media). 
     * @param lon Número de caracteres que quiero que quepan en el campo de texto.
     * @return int - 
     */
    int anchoEdit( double anchoChar, int lon ){
    	return (int)(anchoChar*lon + 2*GConfigView.H_InternalEditPadd);//el 3 es el ancho de los bordes
    }

    /**
     * Iterador para los grupos. 
     * @return boolean - Devuelve cierto si hay más grupos que tratar.
     */
    public boolean hasItems(){
    	
    	return false;
    }

    /**
     * Elimina el grupo cuyo Id corresponda con el del parámetro
     * @param groupId Id del grupo que queremos borrar.
     */
    

	public Dimension getPanelDimensionUseful() {
		Dimension dimPanel=new Dimension(200,200);
		//System.err.println("Antess useful "+dimPanel);
		dimPanel.width-=(getPanelRightMargin()+getPanelLeftMargin()+getPanelHGap());
		dimPanel.height-=(getPanelTopMargin()+getPanelBottomMargin()+getPanelVGap());
		//System.err.println("Despues useful "+dimPanel);
		return dimPanel;
	}

	public Graphics getGraphics() {
		return m_graphics;
	}
}
