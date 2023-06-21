package gtableIntegration.core.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtableIntegration.ViewContainer;


//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/view/AbstractViewContainer.java
public abstract class AbstractViewContainer implements ViewContainer{
	private static final Logger logger = LoggerFactory.getLogger(AbstractViewContainer.class);
	private JRootPane 								rootPane;
	//application
	private String 									id;
	private String									title;
	private Component component;
	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public AbstractViewContainer() {
		super();
		this.getContentPane().setLayout(new BorderLayout());
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public JRootPane getRootPane() {
		logger.info("get_root_pane");
		if (this.rootPane == null){
			logger.info("root_pane_is_null");
			this.rootPane = new JRootPane();
			this.rootPane.setName("ROOTPANE");
		}else {
			logger.info("root_pane_is_not_null");
		}
		return this.rootPane;
		
	}

	@Override
	public void setContentPane(Container contentPane) {
		// TODO Auto-generated method stub
		this.getRootPane().setContentPane(contentPane);
	}

	@Override
	public Container getContentPane() {
		return this.getRootPane().getContentPane(); 
		
	}

	@Override
	public void setLayeredPane(JLayeredPane layeredPane) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JLayeredPane getLayeredPane() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGlassPane(Component glassPane) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getGlassPane() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
