package gtableIntegration.core.core;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtableIntegration.api.core.Application;
import gtableIntegration.api.view.ViewContainer;
import gtableIntegration.api.view.ViewContainerFrame;
import gtableIntegration.api.view.ViewException;
import gtableIntegration.api.view.ViewManager;
import gtableIntegration.api.view.perspective.PerspectiveConstraint;
import gtableIntegration.core.annotation.processor.ViewsProcessorWrapper;
import gtableIntegration.core.view.DefaultViewContainerFrame;
import gtableIntegration.core.view.DefaultViewManager;
import gtableIntegration.core.view.perspective.DefaultPerspective;
import gtableIntegration.widget.swing.builder.util.SwingBuilderView;
import gtableIntegration.widget.swing.builder.util.TableView;


/**
 * This is a default implementation of an Application which has
 * a <code>ViewControllerDispatcher</code> as well as a
 * <code>ViewManager</code>.
 * 
 * @author Mario Garcia
 * @since 1.0
 *
 */

//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/core/AbstractApplication.java
/**
 * This is a default implementation of an Application which has
 * a <code>ViewControllerDispatcher</code> as well as a
 * <code>ViewManager</code>.
 * 
 * @author Mario Garcia
 * @since 1.0
 *
 */

//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/core/AbstractApplication.java
public abstract class AbstractApplication implements Application
{
	private static final Logger logger = LoggerFactory.getLogger(AbstractApplication.class);
	
	private Locale						locale;
	
	private String 						name;
	private ViewContainerFrame			rootView;
	private ViewManager 				viewManager;
	private List<ViewsProcessorWrapper> initViews;
	public AbstractApplication(){
		logger.info("constructor_0");
		this.viewManager 			= new DefaultViewManager(this,new DefaultPerspective());
		ViewContainer vc = new SwingBuilderView(); 
		ViewContainer tableContainer = new TableView();
		//public ViewsProcessorWrapper(ViewContainer view,PerspectiveConstraint constraint,boolean rootView,boolean trayView){
		ViewsProcessorWrapper wrapper = new ViewsProcessorWrapper(vc, PerspectiveConstraint.LEFT, false, false);
		initViews = new ArrayList<ViewsProcessorWrapper>();
		initViews.add(wrapper);
		wrapper = new ViewsProcessorWrapper(tableContainer, PerspectiveConstraint.RIGHT, false, false);
		initViews.add(wrapper);
	}
	
	/**
	 * @param name
	 * @param mainView
	 */
	public AbstractApplication(String name,ViewContainerFrame mainView){
		logger.info("constructor_1");
		this.setName(name);
		this.setRootView(mainView);
	}
	/**
	 * This constructor sets the default root view
	 * 
	 * @param mainView
	 */
	public AbstractApplication(ViewContainerFrame mainView){
		logger.info("constructor_2");
		this.viewManager 			= new DefaultViewManager(this,new DefaultPerspective());
		this.setRootView(mainView);
	}
	

	/* (non-Javadoc)
	 * @see org.viewaframework.core.Application#close()
	 */
	public void close() {
		
		logger.info("Application_closing!");
		this.fireClose();
	}
	public void fireClose() {
		
		
		ViewContainerFrame viewContainerFrame = this.getViewManager().getRootView();
		viewContainerFrame.getFrame().dispose();
		}
	/* (non-Javadoc)
	 * @see org.viewaframework.core.ApplicationListenerAware#fireClose(org.viewaframework.core.ApplicationEvent)
	 */
	
	@SuppressWarnings("unchecked")
	public void prepare(){
		logger.info("Application preparing!");
		
		
	}
	public ViewContainerFrame getRootView() {
		return rootView;
	}
		/* (non-Javadoc)
		 * @see org.viewa.core.ApplicationBase#getName()
		 */
		public String getName() {
			return this.name;
		}
	/* (non-Javadoc)
	 * @see org.viewaframework.core.Application#prepareUI()
	 */
	public void prepareUI() {
		logger.info("Application preparing UI!");
		
		for (ViewsProcessorWrapper w : initViews){
			try {
				
				if (w.isRootView()){
					this.setRootView(ViewContainerFrame.class.cast(w.getView()));
				} else {
					logger.info("=========================");
					this.getViewManager().addView(w.getView(),w.getConstraint());
				}
			} catch (ViewException e) {
				logger.error("ssss=============");
			}
		}
		
	}


	public void initUI() {
		
			logger.info("Application UI initializing!");
			ViewManager viewManager = this.getViewManager();
	
			Component 	view 		= viewManager.arrangeViews();
	
			view.setVisible(true);
	
		
	}
	public ViewManager getViewManager() {
		return viewManager;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
		Locale.setDefault(this.locale);
	}

	/* (non-Javadoc)
	 * @see org.viewa.core.ApplicationBase#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @param rootView
	 */
	public void setRootView(ViewContainerFrame rootView) {
				
		
			if (this.viewManager!=null){
				this.viewManager.setRootView(rootView);
				this.rootView = rootView;
			}else {
				logger.info("viewManager is null");
			}
		
		
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.core.Application#isVisible()
	 */
	public boolean isVisible(){
		
		boolean result = true;
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.viewaframework.core.Application#setVisible(boolean)
	 */
	public void setVisible(boolean visible){
		JFrame frame = this.getRootView().getFrame();
		if (frame != null){
			if (visible){
				frame.setVisible(visible);
				frame.repaint();
			} else {
				frame.setVisible(visible);
			}
		}
	}
}