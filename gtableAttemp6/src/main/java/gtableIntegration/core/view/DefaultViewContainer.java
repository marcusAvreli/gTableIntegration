package gtableIntegration.core.view;

import java.awt.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a helper class. It helps programmer not to implement certain methods. Also
 * its constructor helps for setting the id directly.
 * 
 * @author Mario García
 *
 */
public class DefaultViewContainer extends AbstractViewContainer{

	private static final Logger logger = LoggerFactory.getLogger(DefaultViewContainer.class);
	/**
	 * 
	 */
	public DefaultViewContainer(){
		super();
		logger.info("default_constructor");
	}
	
	/**
	 * @param id
	 * @param component
	 */
	public DefaultViewContainer(String id,Component component){
		this();
		this.setId(id);
		this.setTitle(id);
		this.getContentPane().add(component);
		logger.info("constructor_1");
	}
	
	/**
	 * @param id
	 * @param title
	 * @param component
	 */
	public DefaultViewContainer(String id,String title,Component component){
		this();
		this.setId(id);
		this.setTitle(title);
		this.getContentPane().add(component);
		logger.info("constructor_2");
	}

}