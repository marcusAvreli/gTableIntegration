package gtableIntegration.core.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtableIntegration.api.core.Application;
import gtableIntegration.api.view.ViewContainer;


/**
 * This is a default abstract implementation of a ViewContainer. The
 * lifecycle implementation is far to be optimal but is pretty closed
 * from the concept.<br/><br/>
 * 
 * The view should execute the following methods in order:
 * 
 * <p>1) <b>injectListeners()</b> This method should be hidden from the user. This method will be
 * declared in the interface in future releases, because it will be moved from the application
 * context to the view context. </p>
 * <p>2) <b>viewInitUIState()</b> This method should be used by the programmer in order
 * to initialize some visual components before the <code>viewInitBackActions()</code> is called. For example
 * disable some components while the  <code>viewInitBackActions()</code> is reading
 * some information from the database.</p>
 * <p>3) <b>viewInitBackActions()</b>: Used to perform some non visual actions
 * before the final state of the view has been set.</p>
 * <p>4) <b>viewFinalUIState()</b>: Once the <code>viewInitBackActions()</code> has finished
 * this method could be used to establish the final state of the view before
 * the UI user will begin to interactwith it. For example enabling the components previously
 * disabled by the <code>viewInitUIState()</code> method.</p>
 * 
 * @author Mario Garcia
 * @since 1.0
 *
 */

//getViewModelMap

///fireViewInit
//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/view/AbstractViewContainer.java
public abstract class AbstractViewContainer implements ViewContainer
{
	private static final Logger logger = LoggerFactory.getLogger(AbstractViewContainer.class);
	
	
	private Application 							application;
	
	private Image									iconImage;
	private String 									id;
	private JToolBar								jToolBar;
	//private Log										logger				 = LogFactory.getLog(AbstractViewContainer.class);
	private ResourceBundle							messageBundle;
	private Map<String,List<Component>> 			namedComponents;
	private JRootPane 								rootPane;
	private String									title;
	
	private Map<String,Object>						viewModelObjects = new HashMap<String, Object>();
	
	/**
	 * 
	 */
	public AbstractViewContainer(){
		super();
		this.getContentPane().setLayout(new BorderLayout());
		
	}
	/**
	 * @param id
	 */
	public AbstractViewContainer(String id){
		this();
		this.setId(id);
	}
	
	public void setLayeredPane(JLayeredPane layeredPane) {
		this.getRootPane().setLayeredPane(layeredPane);
	}
	public Container getContainer() {
		return this.getRootPane();
	}
	/* (non-Javadoc)
	 * @see javax.swing.RootPaneContainer#getContentPane()
	 */
	public Container getContentPane() {
		return this.getRootPane().getContentPane(); 
	}
public Component getGlassPane() {
	return this.getRootPane().getGlassPane();
}
/* (non-Javadoc)
 * @see org.viewaframework.view.ViewContainer#setTitle(java.lang.String)
 */
public void setTitle(String title) {
	this.title = title;
}
/* (non-Javadoc)
 * @see org.viewaframework.view.ViewContainer#getTitle()
 */
public String getTitle() {
	return title;
}
public String getId() {
	return this.id;
}
/* (non-Javadoc)
 * @see org.viewaframework.view.ViewContainer#getJToolBar()
 */
public JToolBar getJToolBar() {
	if (jToolBar == null){
		this.jToolBar = new JToolBar();
		this.jToolBar.setName(TOOLBAR);
	}
	return jToolBar;
}
public void setId(String id) {
	this.id = id;
}
/* (non-Javadoc)
 * @see org.viewaframework.view.ViewContainer#setJToolbar(javax.swing.JToolBar)
 */
public void setJToolbar(JToolBar toolBar) {
	this.jToolBar = toolBar;
}

/* (non-Javadoc)
 * @see org.viewa.view.ViewBase#setApplication(org.viewa.core.Application)
 */
public void setApplication(Application application) {
	
	this.application = application;
}
/* (non-Javadoc)
 * @see javax.swing.RootPaneContainer#setContentPane(java.awt.Container)
 */
public void setContentPane(Container contentPane) {
	this.getRootPane().setContentPane(contentPane);
}

/* (non-Javadoc)
 * @see org.viewa.view.ViewBase#getApplication()
 */
public Application getApplication() {
	
	return this.application;
}
public JRootPane getRootPane() {
	if (this.rootPane == null){
		this.rootPane = new JRootPane();
		this.rootPane.setName(ROOTPANE);
	}
	return this.rootPane;
}
/* (non-Javadoc)
 * @see javax.swing.RootPaneContainer#getLayeredPane()
 */
public JLayeredPane getLayeredPane() {
	return this.getRootPane().getLayeredPane();
}
/* (non-Javadoc)
 * @see javax.swing.RootPaneContainer#setGlassPane(java.awt.Component)
 */
public void setGlassPane(Component glassPane) {
	this.getRootPane().setGlassPane(glassPane);
}
	
}