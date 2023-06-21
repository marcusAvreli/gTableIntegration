package gtableIntegration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtableIntegration.core.core.DefaultApplication;
import gtableIntegration.core.core.DefaultApplicationLauncher;

/**
 * Hello world!
 *
 */
public class App extends DefaultApplication{
	private static final Logger logger = LoggerFactory.getLogger(App.class); 

    public static void main( String[] args )
    {
    	try {
			new DefaultApplicationLauncher().execute(App.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
