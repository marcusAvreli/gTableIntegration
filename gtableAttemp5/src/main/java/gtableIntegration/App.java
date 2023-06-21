package gtableIntegration;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Hello world!
 *
 */
public class App 
{
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	public static String Log4JPath = null;

	   public static void main(String[] args) {
 
		  // logger.info("start");
		
				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						new MainFrame();
					}
				});
	   }


}
