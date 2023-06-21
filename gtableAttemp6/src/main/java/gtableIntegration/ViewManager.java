package gtableIntegration;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtableIntegration.core.view.AbstractViewContainer;




//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/view/AbstractViewManager.java
public class ViewManager {
	private static final Logger logger = LoggerFactory.getLogger(ViewManager.class);
	private JFrame frame;
	
	public JFrame getFrame() {
		return frame;
	}

	public void addView	(AbstractViewContainer view)  
	{
		Container content = view.getContentPane();
		if(null != content) {
			logger.info("is not null");
		}else {
			logger.info("is null");
		}
		JRootPane pane = view.getRootPane();
		if(null != pane) {
			logger.info("is not null");
		}else {
			logger.info("is null");
		}
		
		Component component = view.getComponent();
	//	Component 	component 		= view.getRootPane();
		//content.add(component);
		//view.getRootPane().getContentPane().add(component);
		//pane.add(component);
		//view.getRootPane().getContentPane().add(component);
		JFrame frame = this.getFrame();
		frame.add(component);
		
	}
	
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
}
