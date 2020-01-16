package javau1;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Ukol1 {
	
	
	private static final Logger LOGGER = Logger.getLogger(Ukol1.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("C:\\dev\\git\\test\\Ukol1\\log.properties");
		LOGGER.debug("******BEGIN******");
		
		SpravceDatabaze spravce = SpravceDatabaze.getInstance();
		spravce.provedZmeny();
		
		LOGGER.debug("******END******");
	}

}
